package com.androidstudy.alfoneshub.models;

/**
 * Created by orimbo on 27/04/17.
 */

public class ReportHistory {
    String id, activation_id, location_id, user_id, user_name, customer_name, customer_phone, customer_id, product
            ,product_id, product_name, product_quantity, product_value, extra_field_1, extra_field_2, merchandise, merchandise_id, merchandise_name,
            merchandise_quantity, customer_feedback, image, image_caption, latitude, longitude, created_at, updated_at;

    public ReportHistory() {
    }

    public ReportHistory(String id, String activation_id, String location_id, String user_id, String user_name, String customer_name, String customer_phone, String customer_id, String product, String product_id, String product_name, String product_quantity, String product_value, String extra_field_1, String extra_field_2, String merchandise, String merchandise_id, String merchandise_name, String merchandise_quantity, String customer_feedback, String image, String image_caption, String latitude, String longitude, String created_at, String updated_at) {
        this.id = id;
        this.activation_id = activation_id;
        this.location_id = location_id;
        this.user_id = user_id;
        this.user_name = user_name;
        this.customer_name = customer_name;
        this.customer_phone = customer_phone;
        this.customer_id = customer_id;
        this.product = product;
        this.product_id = product_id;
        this.product_name = product_name;
        this.product_quantity = product_quantity;
        this.product_value = product_value;
        this.extra_field_1 = extra_field_1;
        this.extra_field_2 = extra_field_2;
        this.merchandise = merchandise;
        this.merchandise_id = merchandise_id;
        this.merchandise_name = merchandise_name;
        this.merchandise_quantity = merchandise_quantity;
        this.customer_feedback = customer_feedback;
        this.image = image;
        this.image_caption = image_caption;
        this.latitude = latitude;
        this.longitude = longitude;
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

    public String getCustomer_name() {
        return customer_name;
    }

    public void setCustomer_name(String customer_name) {
        this.customer_name = customer_name;
    }

    public String getCustomer_phone() {
        return customer_phone;
    }

    public void setCustomer_phone(String customer_phone) {
        this.customer_phone = customer_phone;
    }

    public String getCustomer_id() {
        return customer_id;
    }

    public void setCustomer_id(String customer_id) {
        this.customer_id = customer_id;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getProduct_quantity() {
        return product_quantity;
    }

    public void setProduct_quantity(String product_quantity) {
        this.product_quantity = product_quantity;
    }

    public String getProduct_value() {
        return product_value;
    }

    public void setProduct_value(String product_value) {
        this.product_value = product_value;
    }

    public String getExtra_field_1() {
        return extra_field_1;
    }

    public void setExtra_field_1(String extra_field_1) {
        this.extra_field_1 = extra_field_1;
    }

    public String getExtra_field_2() {
        return extra_field_2;
    }

    public void setExtra_field_2(String extra_field_2) {
        this.extra_field_2 = extra_field_2;
    }

    public String getMerchandise() {
        return merchandise;
    }

    public void setMerchandise(String merchandise) {
        this.merchandise = merchandise;
    }

    public String getMerchandise_id() {
        return merchandise_id;
    }

    public void setMerchandise_id(String merchandise_id) {
        this.merchandise_id = merchandise_id;
    }

    public String getMerchandise_name() {
        return merchandise_name;
    }

    public void setMerchandise_name(String merchandise_name) {
        this.merchandise_name = merchandise_name;
    }

    public String getMerchandise_quantity() {
        return merchandise_quantity;
    }

    public void setMerchandise_quantity(String merchandise_quantity) {
        this.merchandise_quantity = merchandise_quantity;
    }

    public String getCustomer_feedback() {
        return customer_feedback;
    }

    public void setCustomer_feedback(String customer_feedback) {
        this.customer_feedback = customer_feedback;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getImage_caption() {
        return image_caption;
    }

    public void setImage_caption(String image_caption) {
        this.image_caption = image_caption;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
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
