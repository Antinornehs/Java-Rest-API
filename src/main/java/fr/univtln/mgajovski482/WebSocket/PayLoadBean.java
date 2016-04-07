package fr.univtln.mgajovski482.WebSocket;

import fr.univtln.mgajovski482.JPA.Entities.Player.IPlayer;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Maxime on 05/04/2016.
 */

public class PayLoadBean implements Serializable {
    public final static class PayloadBeanCode extends
            JSONCoder<PayLoadBean> {
    }

    private Date date;
    private IPlayer sender;
    private String message;

    public PayLoadBean() {
    }

    public PayLoadBean(Date date,IPlayer sender,String message) {
        this.date = date;
        this.sender = sender;
        this.message = message;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setSender(IPlayer sender) {
        this.sender = sender;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public Date getDate() {
        return date;
    }

    public IPlayer getSender() {
        return sender;
    }

    @Override
    public String toString() {
        return "PayloadBean{" +
                "date=" + date +
                ", sender=" + sender +
                ", message='" + message + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PayLoadBean that = (PayLoadBean ) o;

        if (!date.equals(that.date)) return false;
        if (!message.equals(that.message)) return false;
        if (!sender.equals(that.sender)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = date.hashCode();
        result = 31 * result + sender.hashCode();
        result = 31 * result + message.hashCode();
        return result;
    }
}