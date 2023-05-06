package com.androidstudy.alfoneshub.models;

//import org.greenrobot.greendao.annotation.Entity;
//import org.greenrobot.greendao.annotation.Id;
//import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by eugene on 7/19/17.
 */
//@Entity
public class Merchandise {
    //@Id
    Long id;
    String merchandise_id;
    String merchandise_name;
    String merchandise_quantity;
    String campaign_id;
    //@Generated(hash = 990569317)
    public Merchandise(Long id, String merchandise_id, String merchandise_name,
            String merchandise_quantity, String campaign_id) {
        this.id = id;
        this.merchandise_id = merchandise_id;
        this.merchandise_name = merchandise_name;
        this.merchandise_quantity = merchandise_quantity;
        this.campaign_id = campaign_id;
    }
    //@Generated(hash = 1013774933)
    public Merchandise() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getMerchandise_id() {
        return this.merchandise_id;
    }
    public void setMerchandise_id(String merchandise_id) {
        this.merchandise_id = merchandise_id;
    }
    public String getMerchandise_name() {
        return this.merchandise_name;
    }
    public void setMerchandise_name(String merchandise_name) {
        this.merchandise_name = merchandise_name;
    }
    public String getMerchandise_quantity() {
        return this.merchandise_quantity;
    }
    public void setMerchandise_quantity(String merchandise_quantity) {
        this.merchandise_quantity = merchandise_quantity;
    }
    public String getCampaign_id() {
        return this.campaign_id;
    }
    public void setCampaign_id(String campaign_id) {
        this.campaign_id = campaign_id;
    }
}
