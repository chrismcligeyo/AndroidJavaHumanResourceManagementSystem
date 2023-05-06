package com.androidstudy.alfoneshub.models;

/**
 * Created by eugene on 7/14/17.
 */

public class CampaignDetail {
    String id, campaign_name, campaign_description, campaign_start_date, campaign_end_date, campaign_company;

    public CampaignDetail(String id, String campaign_name, String campaign_description, String campaign_start_date, String campaign_end_date, String campaign_company) {
        this.id = id;
        this.campaign_name = campaign_name;
        this.campaign_description = campaign_description;
        this.campaign_start_date = campaign_start_date;
        this.campaign_end_date = campaign_end_date;
        this.campaign_company = campaign_company;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCampaign_name() {
        return campaign_name;
    }

    public void setCampaign_name(String campaign_name) {
        this.campaign_name = campaign_name;
    }

    public String getCampaign_description() {
        return campaign_description;
    }

    public void setCampaign_description(String campaign_description) {
        this.campaign_description = campaign_description;
    }

    public String getCampaign_start_date() {
        return campaign_start_date;
    }

    public void setCampaign_start_date(String campaign_start_date) {
        this.campaign_start_date = campaign_start_date;
    }

    public String getCampaign_end_date() {
        return campaign_end_date;
    }

    public void setCampaign_end_date(String campaign_end_date) {
        this.campaign_end_date = campaign_end_date;
    }

    public String getCampaign_company() {
        return campaign_company;
    }

    public void setCampaign_company(String campaign_company) {
        this.campaign_company = campaign_company;
    }
}
