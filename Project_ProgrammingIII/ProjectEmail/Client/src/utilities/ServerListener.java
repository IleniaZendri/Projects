package utilities;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import model.ClientModel;
import model.Email;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class ServerListener extends Thread {

    //Socket
    private ObjectInputStream inStream;
    //Pipe
    private ObjectOutputStream outStream;

    private ObjectOutputStream outSocketStream;

    private ClientModel model = null;

    private boolean running = true;


    public ServerListener(ObjectOutputStream outSocketStream, ClientModel model, ObjectInputStream input, ObjectOutputStream out) {

        this.outSocketStream = outSocketStream;
        //socket
        this.inStream = input;
        //pipe
        this.outStream = out;

        this.model = model;
    }

    @Override
    public void run() {

        try {
            while (running) {
                Object serverMessage = inStream.readObject();
                if (serverMessage instanceof Email) {

                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            Alert alert = new Alert(Alert.AlertType.INFORMATION);
                            alert.setTitle("Nuova email");
                            alert.setHeaderText("Ti è arrivata una nuova email!");
                            alert.showAndWait();

                            model.addEmailReceived((Email) serverMessage);
                        }
                    });
                } else {
                    //scrivo su pipe
                    outStream.writeInt((int) serverMessage);
                    outStream.flush();
                }

            }
        } catch (IOException e) {
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void stopRunning() {
        this.running = false;
    }
}
