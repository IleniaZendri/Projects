package model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class ClientModel {
    private ObservableList<Email> emailListReceived = FXCollections.observableArrayList();;
    private ObservableList<Email> emailListSent = FXCollections.observableArrayList();;
    private String myEmailAddress;

    public ObservableList<Email> getEmailListReceived() {
        return emailListReceived;
    }

    public void setEmailListReceived(ObservableList<Email> emailListReceived) {
        for (Email e : emailListReceived) {
            this.emailListReceived.add(e);
        }
    }

    public ObservableList<Email> getEmailListSent() {
        return emailListSent;
    }

    public void setEmailListSent(ObservableList<Email> emailListSended) {
        for (Email e : emailListSended) {
            this.emailListSent.add(e);
        }
    }

    public void addEmailReceived(Email email)
    {
        emailListReceived.add(email);
    }

    public void addEmailSent(Email email)
    {
        emailListSent.add(email);
    }

    public boolean deleteEmail(Email email) {
        // bisogna dire dove!!
        return true;
    }

    public String getMyEmailAddress() {
        return myEmailAddress;
    }

    public void setMyEmailAddress(String myEmailAddress) {
        this.myEmailAddress = myEmailAddress;
    }
}
