package com.example.petsitterapp;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;

import java.util.ArrayList;

public class JobActivity extends AppCompatActivity {
    private ArrayList<SittingJob> jobList;
    public SittingJob currentJob;
    private boolean ownerJobs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sitter_page);
        ownerJobs = getIntent().getBooleanExtra("ownerJobs", true);
        if(ownerJobs) {
            Log.w("MA", "OwnerJob");
            this.jobList = Controller.currentUser.openJobsOwner;
            ((TextView) findViewById(R.id.allAcceptedJobsTitle)).setText("Your Open Jobs");
        }
        else {
            Log.w("MA", "SitterJob");
            this.jobList = Controller.model.sittersJobs;
            ((TextView) findViewById(R.id.allAcceptedJobsTitle)).setText("Accepted Jobs");
        }

        allJobsList();
    }

    public void allJobsList() {

        String[] arr = getJobArr();
        if(arr != null && arr.length != 0) {
            ArrayAdapter adapter = new ArrayAdapter(this, R.layout.listview_widget, R.id.listText, arr);

            ListView listView = (ListView) findViewById(R.id.acceptedJobsList);
            listView.setAdapter(adapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
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
            Log.w("MA", ""+jobList.get(i).listString(ownerJobs));
            jobArr[i] = (jobList.get(i)).listString(ownerJobs);
        }
        return jobArr;
    }

    public void goToViewJob(SittingJob job) {
        setContentView(R.layout.activity_view_job);
        currentJob = job;
        populateViewJob();
    }

    public void populateViewJob() {
        try {
            ((TextView) findViewById(R.id.startDate)).setText(currentJob.jobInfo.getString("startDate"));
            Log.w("MA", currentJob.jobInfo.getString("startDate"));
        }
        catch(JSONException je) {
            Log.w("MA", "JSONException JobActivity.populateViewJob()");
        }
    }

    /*
    jobID ownerIDKey petIDKet startDate endDate sleepover jobDetails sitterIDKey
     */
}
