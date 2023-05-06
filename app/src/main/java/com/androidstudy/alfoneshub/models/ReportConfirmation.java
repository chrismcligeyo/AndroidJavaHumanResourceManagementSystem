package com.androidstudy.alfoneshub.models;

/**
 * Created by eugene on 7/15/17.
 */

public class ReportConfirmation {
    String id, date, team_leader, ba_id, campaign_id;

    public ReportConfirmation(String id, String date, String team_leader, String ba_id, String campaign_id) {
        this.id = id;
        this.date = date;
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
