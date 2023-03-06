package model;

import java.io.Serializable;
import java.util.ArrayList;

public class Email implements Serializable {
    private int id;
    private String sender, object, body, date;
    private ArrayList<String> receivers;

    public Email (String date, String sender, ArrayList<String> receivers, String object, String body){
        this.sender = sender;
        this.object = object;
        this.body = body;
        this.date = date;
        this.receivers = receivers;
    }

    public Email (Email email){
        this.sender = email.getSender();
        this.object = email.getObject();
        this.body = email.getBody();
        this.date = email.getDate();
        this.receivers = email.getReceivers();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getObject() {
        return object;
    }

    public void setObject(String object) {
        this.object = object;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public ArrayList<String> getReceivers() {
        return receivers;
    }

    public void setReceivers(ArrayList<String> receivers) {
        this.receivers = receivers;
    }

    @Override
    public String toString() {
        return "Email{" + "id=" + id + ", sender=" + sender + ", oggetto=" + object + ", body=" + body + ", data=" + date + ", receivers=" + receivers + '}';
    }
}
