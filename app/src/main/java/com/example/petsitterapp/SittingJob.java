package com.example.petsitterapp;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

public class SittingJob {
    public JSONObject jobInfo;

    public SittingJob(JSONObject info) {
        this.jobInfo = info;
    }

    public String toString(boolean ownerJob) {
        String returnStr = "";
        try {
//            returnStr += jobInfo.get("startDate").toString() + " -> " + jobInfo.get("endDate").toString();
            Log.w("MA", jobInfo.get("startDate").toString());

            int userID = 0;
            if(ownerJob) {
                userID = (Integer) jobInfo.get("sitterIDKey");
            }
            else {
                userID = (Integer) jobInfo.get("ownerIDKey");
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




}
