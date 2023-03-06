package controller;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.ClientModel;
import model.Email;
import utilities.SocketUtils;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import static utilities.Constants.*;


public class ClientController {

    private ClientModel model;
    private SocketUtils socketUtils;


    @FXML
    private ListView chooseSendReceive;

    @FXML
    private TableView table;

    @FXML
    private Label lblDate;

    @FXML
    private Label lblSender;

    @FXML

    private Label lblReceivers;

    @FXML
    private Label lblObject;

    @FXML
    private TextArea textAreaMsg;

    @FXML
    private Label lblMyEmail;

    private Email currentEmail;


    TableColumn<Email, String> colDate;
    TableColumn<Email, String> ColSender;
    TableColumn<Email, String> ColObject;
    TableColumn<Email, String> ColReceivers;

    private final SimpleDateFormat emailFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm");
    private final SimpleDateFormat displayFormat = new SimpleDateFormat("E dd MMM yyyy, HH:mm");


    public void init(ClientModel model, SocketUtils socketUtils){
        if (this.model != null) {
            throw new IllegalStateException("Model can only be initialized once");
        }
        this.model = model;
        this.socketUtils = socketUtils;
        this.socketUtils.setOnline(true);

        lblMyEmail.setText("Collegato come: "+this.model.getMyEmailAddress());

        chooseSendReceive.getItems().add("Posta in arrivo");
        chooseSendReceive.getItems().add("Posta inviata");
        chooseSendReceive.getSelectionModel().select(0);

        colDate = new TableColumn<Email, String>();
        colDate.setCellValueFactory(new PropertyValueFactory<Email, String>("date"));
        colDate.setText("Data");
        table.getColumns().add(colDate);

        ColSender = new TableColumn<Email, String>();
        ColSender.setCellValueFactory(new PropertyValueFactory<Email, String>("sender"));
        ColSender.setText("Mittente");
        table.getColumns().add(ColSender);


        ColObject = new TableColumn<Email, String>();
        ColObject.setCellValueFactory(new PropertyValueFactory<Email, String>("object"));
        ColObject.setText("Oggetto");
        table.getColumns().add(ColObject);

        ColReceivers = new TableColumn<Email, String>();
        ColReceivers.setCellValueFactory(new PropertyValueFactory<Email, String>("receivers"));
        ColReceivers.setText("Destinatari");


        colDate.prefWidthProperty().bind(table.widthProperty().divide(5).subtract(1)); // w * 1/5
        ColReceivers.prefWidthProperty().bind(table.widthProperty().divide(5).multiply(2).subtract(1)); // w * 2/5
        ColSender.prefWidthProperty().bind(table.widthProperty().divide(5).multiply(2).subtract(1)); // w * 2/5
        ColObject.prefWidthProperty().bind(table.widthProperty().divide(5).multiply(2).subtract(1)); // w * 2/5

        table.setItems(this.model.getEmailListReceived());

        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        table.setPlaceholder(new Label("Non ci sono email"));

        chooseSendReceive.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                setTable(newValue);
            }
        });


        table.getSelectionModel().selectedItemProperty().addListener((obs, oldEmail, newEmail) -> {
            if(oldEmail != null) {
                currentEmail = (Email)oldEmail;
                String reformattedStr = currentEmail.getDate();
                try {
                    reformattedStr = displayFormat.format(emailFormat.parse(currentEmail.getDate()));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                lblDate.setText(reformattedStr);
                lblSender.setText(currentEmail.getSender());
                lblReceivers.setText(currentEmail.getReceivers().toString().replace("[", "").replace("]", "").replace(",", ";"));
                lblObject.setText(currentEmail.getObject());
                textAreaMsg.setText(currentEmail.getBody());
            }
            if(newEmail == null) {
                lblDate.setText("");
                lblSender.setText("");
                lblReceivers.setText("");
                lblObject.setText("");
                textAreaMsg.setText("");
            } else {
                currentEmail = (Email)newEmail;
                String reformattedStr = currentEmail.getDate();
                try {
                    reformattedStr = displayFormat.format(emailFormat.parse(currentEmail.getDate()));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                lblDate.setText(reformattedStr);
                lblSender.setText(currentEmail.getSender());
                lblReceivers.setText(currentEmail.getReceivers().toString().replace("[", "").replace("]", "").replace(",", ";"));
                lblObject.setText(currentEmail.getObject());
                textAreaMsg.setText(currentEmail.getBody());
            }
        });

        setTable("Posta in arrivo");
    }

    private void setTable(String where){
        this.table.getColumns().clear();
        this.table.getColumns().add(colDate);
        if (where.equals("Posta in arrivo")) {
            this.table.getColumns().add(ColSender);
            table.setItems(this.model.getEmailListReceived());
        } else {
            this.table.getColumns().add(ColReceivers);
            table.setItems(this.model.getEmailListSent());
        }
        this.table.getColumns().add(ColObject);
        this.table.getSelectionModel().select(0);

        colDate.prefWidthProperty().bind(table.widthProperty().divide(5).subtract(1)); // w * 1/5
        ColReceivers.prefWidthProperty().bind(table.widthProperty().divide(5).multiply(2).subtract(1)); // w * 2/5
        ColSender.prefWidthProperty().bind(table.widthProperty().divide(5).multiply(2).subtract(1)); // w * 2/5
        ColObject.prefWidthProperty().bind(table.widthProperty().divide(5).multiply(2).subtract(1)); // w * 2/5
    }


    public void newRoughEmail(){
        actionNewEmail(new ArrayList<String>(), "", "");
    }

    public void replyEmail(){
        ArrayList<String> receivers = new ArrayList<String>();
        receivers.add(currentEmail.getSender());

        String reformattedStr = currentEmail.getDate();
        try {
            reformattedStr = displayFormat.format(emailFormat.parse(currentEmail.getDate()));
        } catch (ParseException e) {
            e.printStackTrace();
        }


        actionNewEmail(receivers, "Re: "+currentEmail.getObject(), "Il "+reformattedStr+" "+currentEmail.getSender()+" ha scritto:\n"+currentEmail.getBody());
    }

    public void replyAll(){
        String reformattedStr = currentEmail.getDate();
        try {
            reformattedStr = displayFormat.format(emailFormat.parse(currentEmail.getDate()));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        ArrayList<String> receivers = new ArrayList<String>(currentEmail.getReceivers());
        if (!receivers.contains(currentEmail.getSender())) {    // aggiungo solo se già non c'è (es. di email autoinviata)
            receivers.add(currentEmail.getSender());
        }
        actionNewEmail(receivers, "Re: "+currentEmail.getObject(), "Il "+reformattedStr+" "+currentEmail.getSender()+" ha scritto:\n"+currentEmail.getBody());
    }

    public void forward(){
        String reformattedStr = currentEmail.getDate();
        try {
            reformattedStr = displayFormat.format(emailFormat.parse(currentEmail.getDate()));
        } catch (ParseException e) {
            e.printStackTrace();
        }


        actionNewEmail(new ArrayList<String>(), "Fwd: "+currentEmail.getObject(), "Il "+reformattedStr+" "+currentEmail.getSender()+" ha scritto:\n"+currentEmail.getBody());
    }



    public void actionNewEmail(ArrayList<String> receivers, String object, String text){
        if (socketUtils.isOnline()){
            // open new window
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("newEmailView.fxml"));
                Parent root = (Parent) loader.load();
                NewEmailController newEmailController = loader.getController();

                newEmailController.init(this.model, socketUtils, receivers, object, text);

                Stage stageNewEmail = new Stage();
                stageNewEmail.setScene(new Scene(root));
                stageNewEmail.setTitle("Client email - Nuova email");

                stageNewEmail.setMinHeight(400);
                stageNewEmail.setMinWidth(600);

                stageNewEmail.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Errore");
            alert.setHeaderText("Server offline");
            alert.setContentText("Il server non è online: non puoi scrivere una nuova email");
            alert.showAndWait();
        }
    }

    public void actionDeleteEmail(){
        if (socketUtils.isOnline()) {
            try {
                int where=0;
                if (chooseSendReceive.getFocusModel().getFocusedIndex()==0) {
                    where=RECEIVED_EMAIL;
                } else {
                    where=SENT_EMAIL;
                }
                int conf = socketUtils.deleteEmail(currentEmail.getId(), where);
                if (conf ==OK) {

                    boolean found=false;
                    ArrayList<Email> list=null;
                    if (chooseSendReceive.getFocusModel().getFocusedIndex()==0) {
                        list = new ArrayList<Email>(this.model.getEmailListReceived());
                    } else {
                        list = new ArrayList<Email>(this.model.getEmailListSent());
                    }
                    int i=0;
                    while(list.size()>i && !found){
                        if (list.get(i).getId()==currentEmail.getId()) {
                            found=true;
                        }
                        i++;
                    }

                    i--;
                    if (chooseSendReceive.getFocusModel().getFocusedIndex()==0) {
                        this.model.getEmailListReceived().remove(i);
                    } else {
                        this.model.getEmailListSent().remove(i);
                    }
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("OK");
                    alert.setHeaderText("Email cancellata");
                    alert.setContentText("L'email è stata cancellata con successo dal server");
                    alert.showAndWait();
                } else if(conf==DELETE_ERROR) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Errore");
                    alert.setHeaderText("Problema cancellazione");
                    alert.setContentText("C'è stato un problema con la cancellazione dell'email selezionata");
                    alert.showAndWait();
                }
            } catch (IOException e) {
                socketUtils.setOnline(false);

                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Errore");
                alert.setHeaderText("Server offline");
                alert.setContentText("Il server non è online: non puoi cancellare una email");
                alert.showAndWait();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Errore");
            alert.setHeaderText("Server offline");
            alert.setContentText("Il server non è online: non puoi cancellare una email");
            alert.showAndWait();
        }
    }
}