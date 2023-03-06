package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.ClientModel;
import model.Email;
import utilities.SocketUtils;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static utilities.Constants.*;

public class NewEmailController {
    private ClientModel model;
    private SocketUtils socketUtils;

    @FXML
    TextField fieldObject;

    @FXML
    TextField fieldReceivers;

    @FXML
    TextArea textAreaMsg;

    public void init(ClientModel model, SocketUtils socketUtils, ArrayList<String> receivers, String object, String text) {
        if (this.model != null) {
            throw new IllegalStateException("Model can only be initialized once");
        }
        this.model = model;
        this.socketUtils = socketUtils;

        fieldReceivers.setText(receivers.toString().replace("[", "").replace("]", "").replace(",", ";").replaceAll("\\s+",""));
        fieldObject.setText(object);
        textAreaMsg.setText(text);
    }

    public void actionSend(){
        String[] receiversArray = fieldReceivers.getText().trim().replaceAll("\\s+","").split(";");

        if (receiversArray.length==1 && receiversArray[0].equals("")){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Errore");
            alert.setHeaderText("Campo destinatari vuoto");
            alert.setContentText("Devi inserire almeno un destinatario per inviare l'email");
            alert.showAndWait();
        } else if (textAreaMsg.getText().length()==0){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Errore");
            alert.setHeaderText("Campo messaggio vuoto");
            alert.setContentText("Devi inserire almeno un carattere per inviare l'email");
            alert.showAndWait();
        }
        else {
            ArrayList<String> receivers = new ArrayList<String>();
            for (String receive: receiversArray) {
                receivers.add(receive);
            }

            String object = fieldObject.getText();
            if (object.length()==0){
                object = "(nessun oggetto)";
            }

            String pattern = "yyyy/MM/dd HH:mm";
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);

            Email email = new Email(simpleDateFormat.format(new Date()), this.model.getMyEmailAddress(), receivers, object, textAreaMsg.getText());

            try {
                int conf = socketUtils.sendNewEmail(email);
                if (conf==OK){
                    int id = socketUtils.getEmailId();
                    email.setId(id);

                    this.model.addEmailSent(email);

                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("OK");
                    alert.setHeaderText("Email inviata correttamente");
                    alert.showAndWait();

                    Stage newEmailStage = (Stage) fieldReceivers.getScene().getWindow();
                    newEmailStage.close();
                } else if (conf==RECEIVER_NOT_EXIST){
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Errore");
                    alert.setHeaderText("Mittenti non corretti");
                    alert.setContentText("Uno o più mittenti inseriti non esistono");
                    alert.showAndWait();
                }
            } catch (IOException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Errore");
                alert.setHeaderText("Il server è stato disconnesso");
                alert.setContentText("Non puoi più inviare email quando il server è offline");
                alert.showAndWait();
                socketUtils.setOnline(false);

                Stage newEmailStage = (Stage) fieldReceivers.getScene().getWindow();
                newEmailStage.close();
            }


        }
    }
}
