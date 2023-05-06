package com.androidstudy.alfoneshub.models;

public class ExpensesItem {
    String id, expenses_id, item_supplier, item_name, item_description, item_unit_cost, item_quantity, item_days, item_amount, reconciliation_status, created_at, updated_at;

    public ExpensesItem() {
    }

    public ExpensesItem(String id, String expenses_id, String item_supplier, String item_name, String item_description, String item_unit_cost, String item_quantity, String item_days, String item_amount, String reconciliation_status, String created_at, String updated_at) {
        this.id = id;
        this.expenses_id = expenses_id;
        this.item_supplier = item_supplier;
        this.item_name = item_name;
        this.item_description = item_description;
        this.item_unit_cost = item_unit_cost;
        this.item_quantity = item_quantity;
        this.item_days = item_days;
        this.item_amount = item_amount;
        this.reconciliation_status = reconciliation_status;
        this.created_at = created_at;
        this.updated_at = updated_at;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getExpenses_id() {
        return expenses_id;
    }

    public void setExpenses_id(String expenses_id) {
        this.expenses_id = expenses_id;
    }

    public String getItem_supplier() {
        return item_supplier;
    }

    public void setItem_supplier(String item_supplier) {
        this.item_supplier = item_supplier;
    }

    public String getItem_name() {
        return item_name;
    }

    public void setItem_name(String item_name) {
        this.item_name = item_name;
    }

    public String getItem_description() {
        return item_description;
    }

    public void setItem_description(String item_description) {
        this.item_description = item_description;
    }

    public String getItem_unit_cost() {
        return item_unit_cost;
    }

    public void setItem_unit_cost(String item_unit_cost) {
        this.item_unit_cost = item_unit_cost;
    }

    public String getItem_quantity() {
        return item_quantity;
    }

    public void setItem_quantity(String item_quantity) {
        this.item_quantity = item_quantity;
    }

    public String getItem_days() {
        return item_days;
    }

    public void setItem_days(String item_days) {
        this.item_days = item_days;
    }

    public String getItem_amount() {
        return item_amount;
    }

    public void setItem_amount(String item_amount) {
        this.item_amount = item_amount;
    }

    public String getReconciliation_status() {
        return reconciliation_status;
    }

    public void setReconciliation_status(String reconciliation_status) {
        this.reconciliation_status = reconciliation_status;
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
