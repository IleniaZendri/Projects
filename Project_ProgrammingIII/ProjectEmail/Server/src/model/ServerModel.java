package model;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import utilities.Log;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.*;
import java.util.ArrayList;
import java.util.Date;

public class ServerModel {

    private IntegerProperty numClientOnline = new SimpleIntegerProperty();

    private final ObservableList<Log> logList = FXCollections.observableArrayList();
    public ArrayList<User> users;

    public ArrayList<User> getUsers() {
        return users;
    }

    public ObservableList<Log> getLogList() {
        return logList;
    }

    public void addLog(String host, String msg) {
        logList.add(new Log(new Date().toString(), host, msg));
    }

    public User getUser(String email, String password) {
        for (User checkUser : this.users) {
            if (checkUser.equals(email, password)) {
                return checkUser;
            }
        }
        return null;
    }

    public User getUserByEmail(String email) {
        for (User checkUser : this.users) {
            if (checkUser.getEmail().equals(email)) {
                return checkUser;
            }
        }
        return null;
    }


    public ServerModel() {
        this.users = new ArrayList<User>();

        File[] userDirectories = new File("Server/database/").listFiles();
        for (File userDirectory : userDirectories) {
            if (userDirectory.isDirectory()) {
                File userData = new File(userDirectory.getPath() + "/profile.txt");
                if (userData.exists()) {
                    try {
                        BufferedReader br = new BufferedReader(new FileReader(userData));
                        String st;
                        if ((st = br.readLine()) != null) {
                            User u = new User(userDirectory.getName(), st, false);
                            setUpEmails(u);
                            this.users.add(u);
                        }
                    } catch (IOException e) {}
                }
            }
        }
    }


    public IntegerProperty numClientOnlineProperty() {
        return numClientOnline;
    }

    public int getNumClientOnline() {
        return numClientOnline.get();
    }

    public void addNumClientOnline() {
        int n = numClientOnline.getValue();
        n++;
        this.numClientOnline.set(n);
    }

    public void decNumClientOnline() {
        int n = numClientOnline.getValue();
        n--;
        this.numClientOnline.set(n);
    }

    private void setUpEmails (User user) {
        Gson gson = new Gson();
        int i = -1;
        File receivedFile = new File("Server/database/" + user.getEmail() + "/received.txt");
        try {
            if (!receivedFile.exists()) {
                receivedFile.createNewFile();
            }
            JsonReader reader = new JsonReader(new FileReader(receivedFile.getPath()));
            boolean end = false;
            while (!end) {
                Email e = gson.fromJson(reader, Email.class);
                if (e != null) {
                    user.addReceivedEmail(e);
                    i++;
                } else {
                    end = true;
                }
            }
            reader.close();
        } catch (Exception e) {
        }
        if (i > -1) {
            user.setCurrentReceivedId(user.getReceivedList().get(i).getId());
        } else {
            user.setCurrentReceivedId(0);
        }

        int j = -1;
        File sentFile = new File("Server/database/" + user.getEmail() + "/sent.txt");
        try {
            if (!sentFile.exists()) {
                sentFile.createNewFile();
            }
            JsonReader reader = new JsonReader(new FileReader(sentFile.getPath()));
            boolean end = false;
            while (!end) {
                Email e = gson.fromJson(reader, Email.class);
                if (e != null) {
                    user.addSentEmail(e);
                    j++;
                } else {
                    end = true;
                }
            }
            reader.close();

        } catch (Exception e) {}
        if (j > -1) {
            user.setCurrentSentId(user.getSentList().get(j).getId());
        } else {
            user.setCurrentSentId(0);
        }
    }
}
