package com.androidstudy.alfoneshub.models;

public class LocationHistory {
    String id, activation_id, location_id, location_name, team_leader_id, team_leader_name, interaction, sales, merchandise, created_at, updated_at;

    public LocationHistory() {
    }

    public LocationHistory(String id, String activation_id, String location_id, String location_name, String team_leader_id, String team_leader_name, String interaction, String sales, String merchandise, String created_at, String updated_at) {
        this.id = id;
        this.activation_id = activation_id;
        this.location_id = location_id;
        this.location_name = location_name;
        this.team_leader_id = team_leader_id;
        this.team_leader_name = team_leader_name;
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

    public String getLocation_id() {
        return location_id;
    }

    public void setLocation_id(String location_id) {
        this.location_id = location_id;
    }

    public String getLocation_name() {
        return location_name;
    }

    public void setLocation_name(String location_name) {
        this.location_name = location_name;
    }

    public String getTeam_leader_id() {
        return team_leader_id;
    }

    public void setTeam_leader_id(String team_leader_id) {
        this.team_leader_id = team_leader_id;
    }

    public String getTeam_leader_name() {
        return team_leader_name;
    }

    public void setTeam_leader_name(String team_leader_name) {
        this.team_leader_name = team_leader_name;
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
