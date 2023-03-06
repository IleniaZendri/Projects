package utilities;

import javafx.beans.property.SimpleStringProperty;

public class Log {
    private final SimpleStringProperty date;
    private final SimpleStringProperty host;
    private final SimpleStringProperty msg;

    public Log(String date, String host, String msg) {
        this.date = new SimpleStringProperty(date);
        this.host = new SimpleStringProperty(host);
        this.msg = new SimpleStringProperty(msg);
    }

    // inutili?
    public String getDate() {
        return this.date.get();
    }

    public void setDate(String date) {
        this.date.set(date);
    }

    public String getMsg() {
        return this.msg.get();
    }

    public void setMsg(String msg) {
        this.msg.set(msg);
    }

    public String getHost() {
        return host.get();
    }

    public SimpleStringProperty hostProperty() {
        return host;
    }
}
