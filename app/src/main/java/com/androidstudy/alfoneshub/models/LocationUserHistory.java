package com.androidstudy.alfoneshub.models;

public class LocationUserHistory {
    String id, activation_id, user_id, user_name, interaction, sales, merchandise, created_at, updated_at;

    public LocationUserHistory() {
    }

    public LocationUserHistory(String id, String activation_id, String user_id, String user_name, String interaction, String sales, String merchandise, String created_at, String updated_at) {
        this.id = id;
        this.activation_id = activation_id;
        this.user_id = user_id;
        this.user_name = user_name;
        this.interaction = interaction;
        this.sales = sales;
        this.merchandise = merchandise;
        this.created_at = created_at;
        this.updated_at = updated_at;
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

    public String getInteraction() {
        return interaction;
    }

    public void setInteraction(String interaction) {
        this.interaction = interaction;
    }

    public String getSales() {
        return sales;
    }

    public void setSales(String sales) {
        this.sales = sales;
    }

    public String getMerchandise() {
        return merchandise;
    }

    public void setMerchandise(String merchandise) {
        this.merchandise = merchandise;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }
}
