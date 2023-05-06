package com.androidstudy.alfoneshub.models;

/**
 * Created by eugene on 7/15/17.
 */

public class PaymentHistory {
    String id, date, amount, checked_in, confirmed, team_leader, ba_id, campaign_id;

    public PaymentHistory(String id, String date, String amount, String checked_in, String confirmed, String team_leader, String ba_id, String campaign_id) {
        this.id = id;
        this.date = date;
        this.amount = amount;
        this.checked_in = checked_in;
        this.confirmed = confirmed;
        this.team_leader = team_leader;
        this.ba_id = ba_id;
        this.campaign_id = campaign_id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getChecked_in() {
        return checked_in;
    }

    public void setChecked_in(String checked_in) {
        this.checked_in = checked_in;
    }

    public String getConfirmed() {
        return confirmed;
    }

    public void setConfirmed(String confirmed) {
        this.confirmed = confirmed;
    }

    public String getTeam_leader() {
        return team_leader;
    }

    public void setTeam_leader(String team_leader) {
        this.team_leader = team_leader;
    }

    public String getBa_id() {
        return ba_id;
    }

    public void setBa_id(String ba_id) {
        this.ba_id = ba_id;
    }

    public String getCampaign_id() {
        return campaign_id;
    }

    public void setCampaign_id(String campaign_id) {
        this.campaign_id = campaign_id;
    }
}
