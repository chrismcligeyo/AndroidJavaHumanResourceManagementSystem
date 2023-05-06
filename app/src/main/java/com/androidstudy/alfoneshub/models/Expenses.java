package com.androidstudy.alfoneshub.models;

public class Expenses {
    String id, expense_type, activation_id, activation_name, admin_id, requisition_title, amount, required_date, requisition_status, reconciliation_status, submitted_status, created_at, updated_at;

    public Expenses() {
    }

    public Expenses(String id, String expense_type, String activation_id, String activation_name, String admin_id, String requisition_title, String amount, String required_date, String requisition_status, String reconciliation_status, String submitted_status, String created_at, String updated_at) {
        this.id = id;
        this.expense_type = expense_type;
        this.activation_id = activation_id;
        this.activation_name = activation_name;
        this.admin_id = admin_id;
        this.requisition_title = requisition_title;
        this.amount = amount;
        this.required_date = required_date;
        this.requisition_status = requisition_status;
        this.reconciliation_status = reconciliation_status;
        this.submitted_status = submitted_status;
        this.created_at = created_at;
        this.updated_at = updated_at;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getExpense_type() {
        return expense_type;
    }

    public void setExpense_type(String expense_type) {
        this.expense_type = expense_type;
    }

    public String getActivation_id() {
        return activation_id;
    }

    public void setActivation_id(String activation_id) {
        this.activation_id = activation_id;
    }

    public String getActivation_name() {
        return activation_name;
    }

    public void setActivation_name(String activation_name) {
        this.activation_name = activation_name;
    }

    public String getAdmin_id() {
        return admin_id;
    }

    public void setAdmin_id(String admin_id) {
        this.admin_id = admin_id;
    }

    public String getRequisition_title() {
        return requisition_title;
    }

    public void setRequisition_title(String requisition_title) {
        this.requisition_title = requisition_title;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getRequired_date() {
        return required_date;
    }

    public void setRequired_date(String required_date) {
        this.required_date = required_date;
    }

    public String getRequisition_status() {
        return requisition_status;
    }

    public void setRequisition_status(String requisition_status) {
        this.requisition_status = requisition_status;
    }

    public String getReconciliation_status() {
        return reconciliation_status;
    }

    public void setReconciliation_status(String reconciliation_status) {
        this.reconciliation_status = reconciliation_status;
    }

    public String getSubmitted_status() {
        return submitted_status;
    }

    public void setSubmitted_status(String submitted_status) {
        this.submitted_status = submitted_status;
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
