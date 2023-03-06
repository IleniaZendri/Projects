package utilities;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import javafx.application.Platform;
import model.Email;
import model.ServerModel;
import model.User;
import javafx.collections.ListChangeListener;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

import static utilities.Constants.*;

public class ClientHandler extends Thread {
    private Socket socket;
    private ServerModel model;
    private String hostname;
    private User user;

    private boolean deletedSent=false;
    private boolean deletedReceived=false;

    private boolean running;

    public ClientHandler(ServerModel model, Socket socket) {
        this.model = model;
        this.socket = socket;
        hostname = socket.getRemoteSocketAddress().toString();
    }

    @Override
    public void run() {
        this.model.addLog(hostname, "Nuovo utente sta facendo il login");

        try {
            ObjectInputStream inStream = new ObjectInputStream(socket.getInputStream());
            ObjectOutputStream outStream = new ObjectOutputStream(socket.getOutputStream());

            running = true;   // variable of ending connection

            while (running) {
                int request = inStream.readInt();
                switch (request) {
                    case LOGIN: {
                        String email = inStream.readUTF();
                        String password = inStream.readUTF();

                        user = this.model.getUser(email, password);

                        if (user != null) {
                            if (!user.isOnline()) {
                                user.setOnline(true);

                                Platform.runLater(new Runnable() {
                                    @Override
                                    public void run() {
                                        model.addNumClientOnline();
                                    }
                                });

                                outStream.writeInt(OK);
                                outStream.flush();

                                user.getReceivedList().addListener(new ListChangeListener<Email>() {   // listener su aggiornamenti
                                    @Override
                                    public void onChanged(Change<? extends Email> c) {
                                        while (c.next()) {
                                            for (Email addedEmail : c.getAddedSubList()) {
                                                try {
                                                    outStream.writeObject(addedEmail);
                                                    outStream.flush();
                                                } catch (IOException e) {
                                                    System.err.println("Errore ricezione nuova email per " + hostname);
                                                }

                                            }
                                        }
                                    }
                                });

                                outStream.writeObject(new ArrayList<Email>(this.user.getReceivedList()));      // invio lista email ricevute per caricarle al client
                                outStream.flush();
                                outStream.writeObject(new ArrayList<Email>(this.user.getSentList()));      // invio lista email inviate per caricarle al client
                                outStream.flush();

                                this.model.addLog(hostname, "collegato");
                            } else {
                                outStream.writeInt(ALREADY_LOGGED);
                                outStream.flush();
                                this.model.addLog(hostname, "sta tentando di ricollegarsi");
                            }
                        } else {
                            outStream.writeInt(LOGIN_FAIL);
                            outStream.flush();
                            this.model.addLog(hostname, "ha sbagliato credenziali");
                        }

                        break;
                    }


                    case SEND_NEW_EMAIL: {
                        Email newEmail = (Email) inStream.readObject();

                        // check all receivers
                        int i = 0;
                        boolean receiversExist = true;
                        ArrayList<User> u = this.model.getUsers();


                        while (newEmail.getReceivers().size() > i && receiversExist) {
                            int j = 0;
                            boolean receiverExist = false;
                            while (u.size() > j && !receiverExist) {
                                if (u.get(j).getEmail().equals(newEmail.getReceivers().get(i))) {
                                    receiverExist = true;
                                }
                                j++;
                            }
                            i++;
                            receiversExist = receiverExist;
                        }

                        if (!receiversExist) {
                            outStream.writeObject(RECEIVER_NOT_EXIST);
                            outStream.flush();
                        } else {
                            outStream.writeObject(OK);
                            outStream.flush();
                            int newEmailId = user.getCurrentSentId();
                            newEmail.setId(newEmailId);
                            outStream.writeObject(newEmailId);
                            outStream.flush();
                            writeEmail(this.user, SENT_EMAIL, newEmail);

                            for (String receiver : newEmail.getReceivers()) {
                                Email email = new Email(newEmail);
                                email.setId(this.model.getUserByEmail(receiver).getCurrentReceivedId());
                                writeEmail(this.model.getUserByEmail(receiver), RECEIVED_EMAIL, email);
                            }

                            this.model.addLog(hostname, "ha inviato un email");
                        }




                        break;
                    }

                    case DELETE: {
                        int where = inStream.readInt();
                        int id = inStream.readInt();

                        ArrayList<Email> list=null;
                        if (where==RECEIVED_EMAIL) {
                            list = new ArrayList<Email>(user.getReceivedList());
                        } else {
                            list = new ArrayList<Email>(user.getSentList());
                        }
                        boolean found = false;
                        int i = 0;
                        while (list.size() > i && !found) {
                            if (list.get(i).getId() == id) {
                                found = true;
                            }
                            i++;
                        }
                        if (found) {
                            i--;
                            if (where==RECEIVED_EMAIL) {
                                synchronized (this.user.getReceivedList()){
                                    this.user.getReceivedList().remove(i);
                                    deletedReceived=true;
                                }
                            } else if (where==SENT_EMAIL) {
                                synchronized (this.user.getSentList()){
                                    this.user.getSentList().remove(i);
                                    deletedSent=true;
                                }
                            }

                            outStream.writeObject(OK);
                            outStream.flush();
                            this.model.addLog(hostname, "Cancellata email");
                        } else {
                            outStream.writeObject(DELETE_ERROR);
                            outStream.flush();
                            this.model.addLog(hostname, "Errore cancellazione email");
                        }
                        break;
                    }

                    case CLOSE: {
                        running = false;
                        break;
                    }
                }
            }

        } catch (IOException e) {
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            user.setOnline(false);
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    model.decNumClientOnline();
                }
            });

            if (deletedReceived) {
                deleteEmail(RECEIVED_EMAIL);
            }
            if (deletedSent) {
                deleteEmail(SENT_EMAIL);
            }

            this.model.addLog(hostname, "Si è scollegato");
        }
    }


    private void writeEmail(User user, int where, Email email) {
        if (where==RECEIVED_EMAIL){
            synchronized (user.getReceivedList()){
                try {
                    Gson gson = new GsonBuilder().setPrettyPrinting().create();
                    FileWriter file = new FileWriter("Server/database/"+user.getEmail()+"/received.txt", true);
                    file.write(gson.toJson(email) + "\n");
                    file.flush();
                    file.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                user.addReceivedEmail(email);
            }
        } else if (where==SENT_EMAIL) {
            synchronized (user.getSentList()){
                try {
                    Gson gson = new GsonBuilder().setPrettyPrinting().create();
                    FileWriter file = new FileWriter("Server/database/"+user.getEmail()+"/sent.txt", true);
                    file.write(gson.toJson(email) + "\n");
                    file.flush();
                    file.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                user.addSentEmail(email);
            }
        }
    }

    private void deleteEmail (int where) {
        if (where==RECEIVED_EMAIL) {
            synchronized(this.user.getReceivedList()) {
                try {
                    PrintWriter writer = new PrintWriter("Server/database/"+user.getEmail()+"/received.txt");
                    writer.print("");
                    writer.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

                Gson gson = new GsonBuilder().setPrettyPrinting().create();
                try {
                    FileWriter file = new FileWriter("Server/database/"+user.getEmail()+"/received.txt", true);
                    for (Email e : user.getReceivedList()){
                        file.write(gson.toJson(e) + "\n");
                        file.flush();
                        file.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } else if (where==SENT_EMAIL){
            synchronized(this.user.getSentList()){
                try {
                    PrintWriter writer = new PrintWriter("Server/database/"+user.getEmail()+"/sent.txt");
                    writer.print("");
                    writer.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

                Gson gson = new GsonBuilder().setPrettyPrinting().create();
                try {
                    FileWriter file = new FileWriter("Server/database/"+user.getEmail()+"/sent.txt", true);
                    for (Email e : user.getSentList()){
                        file.write(gson.toJson(e) + "\n");
                        file.flush();
                        file.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
