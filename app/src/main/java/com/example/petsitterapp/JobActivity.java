package com.example.petsitterapp;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
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
        jobType = getIntent().getStringExtra("ownerJobs");
        switch(jobType) {
            case "owner":
                Log.w("MA", "OwnerJob");
                this.jobList = Controller.model.ownersJobs;
                ((TextView) findViewById(R.id.allAcceptedJobsTitle)).setText("Your Open Jobs");                break;
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

    public void allJobsList() {
        Log.w("MA", "allJobsList");
        String[] jobArr = getJobArr();

        if(jobArr != null && jobArr.length != 0) {
            ArrayAdapter adapter = new ArrayAdapter(this, R.layout.listview_widget, R.id.listText, jobArr);

            ListView listView = (ListView) findViewById(R.id.acceptedJobsList);
            listView.setAdapter(adapter);
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

    public void goToViewJob(SittingJob job) {
        setContentView(R.layout.activity_view_job);
        currentJob = job;
        Log.w("MA", "goToViewJob");
        populateViewJob();
    }

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
            Log.w("MA", "pre-sleepover");

            String sleepover = "yes";
            if(currentJob.jobInfo.get("sleepover").toString() == "1") {
                sleepover = "Yes";
            }
            else {
                sleepover = "No";
            }
            ((TextView) findViewById(R.id.sleepover_value)).setText(sleepover);

            ((TextView) findViewById(R.id.address_value)).setText(Controller.currentUser.accountInfo.get("address").toString());

            if(jobType == "owner") {
                findViewById(R.id.submit_job).setVisibility(View.INVISIBLE);
            }
            else {
                findViewById(R.id.submit_job).setVisibility(View.VISIBLE);
            }

            Log.w("MA", "DONE");

        }
        catch(JSONException je) {
            Log.w("MA", "JSONException JobActivity.populateViewJob()");
        }
    }

    public void goBackAllJobs(View v) {
        finish();
    }

    public void goBackViewJob(View v) {
        setContentView(R.layout.activity_sitter_page);
        allJobsList();
    }

}
