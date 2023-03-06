package controller;

import javafx.collections.FXCollections;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import model.ClientModel;
import model.Email;
import utilities.SocketUtils;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;

import static utilities.Constants.*;


public class LoginController {

    @FXML
    private TextField fieldEmail;

    @FXML
    private TextField fieldPassword;

    private ClientModel model;

    public void initialize() {

    }

    public void connect() {


        if (fieldEmail.getLength() != 0 && fieldPassword.getLength() != 0) {

            try {

                String hostName = InetAddress.getLocalHost().getHostName();
                Socket socket = new Socket(hostName, PORT);
                ObjectOutputStream outStream = new ObjectOutputStream(socket.getOutputStream());
                ObjectInputStream inStream = new ObjectInputStream(socket.getInputStream());

                outStream.writeInt(LOGIN);
                outStream.flush();
                outStream.writeUTF(fieldEmail.getText());
                outStream.flush();
                outStream.writeUTF(fieldPassword.getText());
                outStream.flush();

                switch (inStream.readInt()) {
                    case OK: {
                        this.model = new ClientModel();
                        this.model.setMyEmailAddress(fieldEmail.getText());

                        this.model.setEmailListReceived(FXCollections.observableArrayList((ArrayList<Email>) inStream.readObject()));   // restore received emails
                        this.model.setEmailListSent(FXCollections.observableArrayList((ArrayList<Email>) inStream.readObject()));    // restore sent emails


                        // open new window
                        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("clientView.fxml"));
                        Parent root = (Parent) loader.load();
                        ClientController clientController = loader.getController();
                        SocketUtils socketUtils = new SocketUtils(this.model, socket, outStream, inStream);

                        clientController.init(this.model, socketUtils);

                        Stage stageClient = new Stage();
                        stageClient.setScene(new Scene(root, 800, 700));
                        stageClient.setTitle("Client email");
                        stageClient.setOnCloseRequest(new EventHandler<WindowEvent>() {
                            public void handle(WindowEvent we) {
                                socketUtils.closeSocket();
                            }
                        });

                        stageClient.setMinHeight(800);
                        stageClient.setMinWidth(800);

                        stageClient.show();

                        // close login window
                        Stage loginStage = (Stage) fieldEmail.getScene().getWindow();
                        loginStage.close();
                        break;
                    }
                    case ALREADY_LOGGED : {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Errore");
                        alert.setHeaderText("Sei già loggato");
                        alert.setContentText("Sei ancora collegato da un altro client, o ti sei scollegato in maniera scorretta nell'ultima sessione");
                        alert.showAndWait();
                        break;
                    }
                    case LOGIN_FAIL: {
                        // email or password incorrect
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Errore");
                        alert.setHeaderText("Credenziali errate");
                        alert.setContentText("Email o password non corrette");
                        alert.showAndWait();
                        break;
                    }
                }

            } catch (IOException e) {
                // server is offline

                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Errore");
                alert.setHeaderText("Server offline");
                alert.setContentText("Il server non è online: non puoi accedere alla tua casella mail");
                alert.showAndWait();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        } else {
            // empty fields
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Errore");
            alert.setHeaderText("Campi vuoti");
            alert.setContentText("Inserisci email e password per fare il login");
            alert.showAndWait();
        }
    }
}
