package com.androidstudy.alfoneshub.models;

public class LocationUserAdd {

    String id, activation_id, name, phone_number, selected;

    public LocationUserAdd() {
    }

    public LocationUserAdd(String id, String activation_id, String name, String phone_number, String selected) {
        this.id = id;
        this.activation_id = activation_id;
        this.name = name;
        this.phone_number = phone_number;
        this.selected = selected;
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

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public String getSelected() {
        return selected;
    }

    public void setSelected(String selected) {
        this.selected = selected;
    }
}
