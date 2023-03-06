package utilities;

import model.ServerModel;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static utilities.Constants.*;

public class Accepter extends Thread {
    private ExecutorService exec;
    private ServerSocket serverSocket;
    private Socket socket;
    private ServerModel model;


    public Accepter (ServerModel model) {
        exec = Executors.newFixedThreadPool(NUM_THREAD_POOL);
        this.model = model;
    }

    @Override
    public void run() {
        try {
            serverSocket = new ServerSocket(PORT);
            this.model.addLog(serverSocket.getLocalSocketAddress().toString(), "Server online");

            while(true) {
                socket = serverSocket.accept(); // waiting a new connection
                ClientHandler clientHandler = new ClientHandler(model, socket);
                exec.execute(clientHandler);
            }
        } catch (IOException e) {
            if (serverSocket != null && !serverSocket.isClosed()) {
                try {
                    serverSocket.close();
                } catch (IOException ex) {
                    System.err.println("Error closing socket: " + ex.getMessage());
                }
            }
            exec.shutdown();
        }
    }

    public void stopServer(){
        try {
            if (socket!=null) { // if closing operation occurs between accept and handler start
                socket.close();
            }
            serverSocket.close();
        } catch (Exception e) {
            System.err.println("Error closing socket: " + e.getMessage());
        }
    }
}
