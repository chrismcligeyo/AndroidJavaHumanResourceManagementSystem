package com.androidstudy.alfoneshub.models;

/**
 * Created by eugene on 7/15/17.
 */

public class CheckInHistory {
    String id, activation_id, user_id, user_name, check_in;

    public CheckInHistory() {
    }

    public CheckInHistory(String id, String activation_id, String user_id, String user_name, String check_in) {
        this.id = id;
        this.activation_id = activation_id;
        this.user_id = user_id;
        this.user_name = user_name;
        this.check_in = check_in;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getActivation_id() {
        return activation_id;
    }

    public void setActivation_id(String activation_id) {
        this.activation_id = activation_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getCheck_in() {
        return check_in;
    }

    public void setCheck_in(String check_in) {
        this.check_in = check_in;
    }
}