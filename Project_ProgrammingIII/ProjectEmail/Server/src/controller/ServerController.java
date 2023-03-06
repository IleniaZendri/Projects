package controller;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import model.ServerModel;
import utilities.Accepter;
import utilities.Log;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.Date;

public class ServerController {
    @FXML
    private Label lblNuserOnline;

    @FXML
    private Label lblStatus;

    @FXML
    private TableView<Log> table;

    @FXML
    private TableColumn<Log, String> colDate;

    @FXML
    private TableColumn<Log, String> colHost;

    @FXML
    private TableColumn<Log, String> colMsg;

    @FXML
    private Button btnConnect;

    @FXML
    private Button btnDisconnect;

    private ServerModel model;

    private Accepter accepter = null;

    public void initialize() {

        table.setEditable(true);

        colDate.setMinWidth(100);
        colDate.setCellValueFactory(new PropertyValueFactory<Log, String>("date"));

        colHost.setMinWidth(100);
        colHost.setCellValueFactory(new PropertyValueFactory<Log, String>("host"));

        colMsg.setMinWidth(100);
        colMsg.setCellValueFactory(new PropertyValueFactory<Log, String>("msg"));

        colDate.prefWidthProperty().bind(table.widthProperty().divide(4).subtract(1)); // w * 1/4
        colHost.prefWidthProperty().bind(table.widthProperty().divide(4).subtract(1)); // w * 1/4
        colMsg.prefWidthProperty().bind(table.widthProperty().divide(2).subtract(1)); // w * 1/2

        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        btnConnect.setDisable(false);
        btnDisconnect.setDisable(true);

        lblNuserOnline.setText("0");

        lblStatus.setText("Offline");
    }

    @FXML
    private void serverStart() {
        lblStatus.setText("Online");
        this.model = new ServerModel();

        table.getItems().clear();
        table.setItems(this.model.getLogList());
        lblNuserOnline.textProperty().bind(this.model.numClientOnlineProperty().asString());

        accepter = new Accepter(this.model);
        accepter.start();
        btnConnect.setDisable(true);
        btnDisconnect.setDisable(false);
    }

    @FXML
    public void serverStop() {
        lblStatus.setText("Offline");
        if (btnConnect.disableProperty().getValue()) {
            accepter.stopServer();
            btnConnect.setDisable(false);
            btnDisconnect.setDisable(true);
            table.getItems().add(table.getItems().size(), new Log(new Date().toString(), "Server", "Server offline"));
        }
    }

}
