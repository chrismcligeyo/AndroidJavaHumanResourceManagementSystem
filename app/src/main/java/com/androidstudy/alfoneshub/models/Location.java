package com.androidstudy.alfoneshub.models;

public class Location {
    String id, activation_id, name, description, team_leader_id, created_at, updated_at;

    public Location() {
    }

    public Location(String id, String activation_id, String name, String description, String team_leader_id, String created_at, String updated_at) {
        this.id = id;
        this.activation_id = activation_id;
        this.name = name;
        this.description = description;
        this.team_leader_id = team_leader_id;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTeam_leader_id() {
        return team_leader_id;
    }

    public void setTeam_leader_id(String team_leader_id) {
        this.team_leader_id = team_leader_id;
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
