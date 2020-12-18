/**
 * This class is the controller handling the view showing all jobs, and the context switch to
 * display a single job's information. It contains functionality to show open jobs, accepted jobs, and owner jobs
 * @author: Andrew Fallon, Jeff Umanzor, Derek Morales, Nick Pierce-Ptak
 * Date updated: 12/17/20
 */

package com.example.petsitterapp;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;

import java.util.ArrayList;

public class JobActivity extends AppCompatActivity {
    public static final int SLEEPOVER_YES = 1;
    public static final int SLEEPOVER_NO = 0;

    private ArrayList<SittingJob> jobList;
    public SittingJob currentJob;
    private String jobType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sitter_page);
        jobType = getIntent().getStringExtra("jobType");
        switch(jobType) {
            case "owner":
                Log.w("MA", "OwnerJob");
                this.jobList = Controller.model.ownersJobs;
                ((TextView) findViewById(R.id.allAcceptedJobsTitle)).setText("Your Open Jobs");
                break;
            case "sitter":
                Log.w("MA", "SitterJob");
                this.jobList = Controller.model.sittersJobs;
                ((TextView) findViewById(R.id.allAcceptedJobsTitle)).setText("Accepted Jobs");
                break;
            case "open":
                Log.w("MA", "OpenJob");
                this.jobList = Controller.model.openJobs;
                ((TextView) findViewById(R.id.allAcceptedJobsTitle)).setText("Available Jobs");
                break;
            default:
                Log.w("MA", "Intent error creating JobActivity");
                finish();
                break;
        }

        allJobsList();
    }

    /**
     * This method flls the listview on the all jobs page with all jobs objects associated with this activity
     */
    public void allJobsList() {
        Log.w("MA", "allJobsList");
        String[] jobArr = getJobArr();

        if(jobArr != null && jobArr.length != 0) {
            ArrayAdapter adapter = new ArrayAdapter(this, R.layout.listview_widget, R.id.listText, jobArr);
            ListView listView = (ListView) findViewById(R.id.acceptedJobsList);
            listView.setAdapter(adapter);
            Log.w("MA", "test");
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Log.w("MA", "selectedJob");
                    goToViewJob(jobList.get(position));
                }
            });
        }
    }

    /**
     * This returns an array of Strings of each Job object, for the Job ListView in sitter_page
     * @return an array of the strings created from each of the specified jobs
     */
    public String[] getJobArr() {
        String[] jobArr;
        jobArr = new String[jobList.size()];

        for(int i=0;i<jobList.size();i++) {
            Log.w("MA", ""+jobList.get(i).listString(jobType));
            jobArr[i] = (jobList.get(i)).listString(jobType);
        }
        return jobArr;
    }

    /**
     * This method switches the context to the view job screen and populates it with the given info
     * @param job - the job whose info will be shown on the view job page
     */
    public void goToViewJob(SittingJob job) {
        setContentView(R.layout.activity_view_job);
        currentJob = job;
        Log.w("MA", "goToViewJob");
        populateViewJob();
    }

    /**
     * This method fills all the fields in the view job page when the user selects a job from the listview
     */
    public void populateViewJob() {
        try {
            String[] startDate = currentJob.jobInfo.getString("startDate").split("-");
            ((TextView) findViewById(R.id.day_value)).setText(startDate[2]);
            ((TextView) findViewById(R.id.month_value)).setText(startDate[1]);
            ((TextView) findViewById(R.id.year_value)).setText(startDate[0]);
            String[] endDate = currentJob.jobInfo.getString("endDate").split("-");
            ((TextView) findViewById(R.id.end_day_value)).setText(endDate[2]);
            ((TextView) findViewById(R.id.end_month_value)).setText(endDate[1]);
            ((TextView) findViewById(R.id.end_year_value)).setText(endDate[0]);

            String jobDetails = currentJob.jobInfo.get("jobDetails").toString();

            ((TextView) findViewById(R.id.job_details_field)).setText(jobDetails);

            String sleepover = "yes";
            if(currentJob.jobInfo.get("sleepover").toString() == "1") {
                sleepover = "Yes";
            }
            else {
                sleepover = "No";
            }
            ((TextView) findViewById(R.id.sleepover_value)).setText(sleepover);

            ((TextView) findViewById(R.id.address_value)).setText(Controller.currentUser.accountInfo.get("address").toString());
            Log.w("MA", "post-addy");

            if(jobType.equals("open")) {
                findViewById(R.id.accept_job).setVisibility(View.VISIBLE);
                findViewById(R.id.complete_job).setVisibility(View.INVISIBLE);
            }
            else {
                findViewById(R.id.accept_job).setVisibility(View.INVISIBLE);
                findViewById(R.id.complete_job).setVisibility(View.VISIBLE);
            }

            Log.w("MA", "DONE");

        }
        catch(JSONException je) {
            Log.w("MA", "JSONException JobActivity.populateViewJob()");
        }
    }

    /**
     * This method ends the activity
     * @param v - the go back button in the all jobs page
     */
    public void goBackAllJobs(View v) {
        finish();
    }

    /**
     * This method goes back from viewing a job, to the all jobs page
     * @param v - the go back button in the view job page
     */
    public void goBackViewJob(View v) {
        setContentView(R.layout.activity_sitter_page);
        if(jobType.equals("open")) {
            ((TextView) findViewById(R.id.allAcceptedJobsTitle)).setText("Available Jobs");
        }
        allJobsList();
    }

    /**
     * This method accepts the job currently being viewed, adding it to the user account
     * @param v - the accept job button available in the open jobs view
     */
    public void acceptJob(View v) {
        setContentView(R.layout.activity_sitter_page);
        SittingJob job = currentJob;
        try {
            Controller.model.deleteJob(currentJob);
            job.jobInfo.put("sitterIDKey", ""+Controller.currentUser.accountInfo.get("ownerIDKey"));
            Log.w("MA", "Success accepting job");
        }
        catch(JSONException je) {
            Log.w("MA", "JSONException accepting a job");
        }
    }

    /**
     * Mark a job as completed
     * @param v - the "mark job as completed" button on the view job page
     */
    public void completeJob(View v) {
        // FIXME save that it is completed somehow
        Log.w("MA", "completeJob");
        setContentView(R.layout.activity_sitter_page);
        allJobsList();
    }

}
