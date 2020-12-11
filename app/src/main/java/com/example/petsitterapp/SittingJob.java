package com.example.petsitterapp;

import org.json.JSONObject;

public class SittingJob {
    public JSONObject jobInfo;

    public SittingJob(JSONObject info) {
        this.jobInfo = info;
    }

    public String toString() {
        return jobInfo.toString();
    }




}
