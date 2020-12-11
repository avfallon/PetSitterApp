package com.example.petsitterapp;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

public class SittingJob {
    public JSONObject jobInfo;

    public SittingJob(JSONObject info) {
        this.jobInfo = info;
    }

    public String listString(boolean ownerJob) {
        String returnStr = "";
        try {
            returnStr += formatDate(jobInfo.get("startDate").toString()) + " -> " + formatDate(jobInfo.get("endDate").toString());

            int userID = 0;
            if(ownerJob) {
                userID = jobInfo.getInt("sitterIDKey");
            }
            else {
                userID = jobInfo.getInt("ownerIDKey");
            }

            for (int i = 0; i < Controller.model.allOwners.length(); i++) {
                JSONObject obj = Controller.model.allOwners.getJSONObject(i);
                if(userID == obj.getInt("ownerIDKey")) {
                    returnStr += "     " + obj.getString("firstName") + " " + obj.getString("lastName");
                }
            }
        }
        catch(JSONException je) {
            Log.w("MA", "JSONException SittingJob.toString()");
        }

        return returnStr;
    }

    public String formatDate(String date) {
        String[] splitList = date.split("-");
        return splitList[1]+"/"+splitList[2];
    }
}
