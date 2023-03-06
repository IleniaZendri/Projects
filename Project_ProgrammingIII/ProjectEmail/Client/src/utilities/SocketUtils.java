package utilities;

import model.ClientModel;
import model.Email;

import java.io.*;
import java.net.Socket;

import static utilities.Constants.*;

public class SocketUtils {
    private Socket socket;
    private ObjectOutputStream outStream;
    private ObjectInputStream inStream;
    private boolean online;

    private ObjectInputStream pipeIn;
    private ObjectOutputStream pipeOut;

    private ServerListener serverListener = null;


    public SocketUtils (ClientModel model, Socket socket, ObjectOutputStream outStream, ObjectInputStream inStream){
        this.socket = socket;
        this.outStream = outStream;
        this.inStream = inStream;


        try {
            //PIPE
            PipedOutputStream pout = new PipedOutputStream();
            PipedInputStream pin = new PipedInputStream(pout);
            pipeOut = new ObjectOutputStream(pout);
            pipeIn = new ObjectInputStream(pin);
        } catch (IOException e) {
            e.printStackTrace();
        }

        //Thread ASCOLTATORE
        serverListener = new ServerListener(outStream, model, this.inStream, this.pipeOut);
        serverListener.start();
    }

    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    public ObjectOutputStream getOutStream() {
        return outStream;
    }

    public void setOutStream(ObjectOutputStream outStream) {
        this.outStream = outStream;
    }

    public ObjectInputStream getInStream() {
        return inStream;
    }

    public void setInStream(ObjectInputStream inStream) {
        this.inStream = inStream;
    }

    public void closeSocket(){

        serverListener.stopRunning();

        try {
            outStream.writeInt(CLOSE);
            outStream.flush();
        } catch (IOException e) {
        }
        finally {
            try {
                socket.close();
            } catch (IOException e) {
            }
        }
    }

    public boolean isOnline(){
        return online;
    }

    public void setOnline(boolean online){
        this.online = online;
    }

    public int sendNewEmail(Email email) throws IOException{
        outStream.writeInt(SEND_NEW_EMAIL);
        outStream.flush();
        outStream.writeObject(email);
        outStream.flush();
        return pipeIn.readInt();
    }

    public int getEmailId() throws IOException{
        return pipeIn.readInt();
    }

    public int deleteEmail (int id, int where) throws IOException {
        outStream.writeInt(DELETE);
        outStream.flush();
        outStream.writeInt(where);
        outStream.flush();
        outStream.writeInt(id);
        outStream.flush();
        return pipeIn.readInt();
    }
}
