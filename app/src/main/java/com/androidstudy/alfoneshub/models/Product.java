package com.androidstudy.alfoneshub.models;

//import org.greenrobot.greendao.annotation.Entity;
//import org.greenrobot.greendao.annotation.Id;
//import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by eugene on 7/19/17.
 */
//@Entity
public class Product {
    //@Id
    Long id;
    String product_id;
    String product_name;
    String product_quantity;
    String campaign_id;
    //@Generated(hash = 413101153)
    public Product(Long id, String product_id, String product_name,
            String product_quantity, String campaign_id) {
        this.id = id;
        this.product_id = product_id;
        this.product_name = product_name;
        this.product_quantity = product_quantity;
        this.campaign_id = campaign_id;
    }
    //@Generated(hash = 1890278724)
    public Product() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getProduct_id() {
        return this.product_id;
    }
    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }
    public String getProduct_name() {
        return this.product_name;
    }
    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }
    public String getProduct_quantity() {
        return this.product_quantity;
    }
    public void setProduct_quantity(String product_quantity) {
        this.product_quantity = product_quantity;
    }
    public String getCampaign_id() {
        return this.campaign_id;
    }
    public void setCampaign_id(String campaign_id) {
        this.campaign_id = campaign_id;
    }
}
