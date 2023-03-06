package model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class User {
    private String email, password;
    private boolean online;
    private ObservableList<Email> receivedList = FXCollections.observableArrayList();
    private ObservableList<Email> sentList = FXCollections.observableArrayList();
    private int currentReceivedId;
    private int currentSentId;

    public User (String email, String password, boolean online){
        this.email = email;
        this.password = password;
        this.online = online;
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isOnline() {
        return online;
    }

    public void setOnline(boolean online) {
        this.online = online;
    }

    public boolean equals(String email, String password){
        return (this.email.equals(email) && this.password.equals(password));
    }


    public int getCurrentReceivedId() {
        int id = currentReceivedId;
        currentReceivedId++;
        return id;
    }
    public void setCurrentReceivedId(int currentReceivedId) {
        this.currentReceivedId = currentReceivedId;
    }


    public void setCurrentSentId(int currentSentId) {
        this.currentSentId = currentSentId;
    }
    public int getCurrentSentId() {
        int id = currentSentId;
        currentSentId++;
        return id;
    }


    public ObservableList<Email> getReceivedList() {
        return receivedList;
    }

    public void addReceivedEmail(Email email) {
        this.receivedList.add(email);
    }

    public ObservableList<Email> getSentList() {
        return sentList;
    }

    public void addSentEmail(Email email) {
        this.sentList.add(email);
    }
}
