package com.androidstudy.alfoneshub.models;

/**
 * Created by eugene on 7/17/17.
 */

public class Notification {
    String id, date, ba_id, message, read;

    public Notification(String id, String date, String ba_id, String message, String read) {
        this.id = id;
        this.date = date;
        this.ba_id = ba_id;
        this.message = message;
        this.read = read;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getBa_id() {
        return ba_id;
    }

    public void setBa_id(String ba_id) {
        this.ba_id = ba_id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getRead() {
        return read;
    }

    public void setRead(String read) {
        this.read = read;
    }
}
