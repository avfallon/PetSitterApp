package com.example.petsitterapp;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class JobActivity extends AppCompatActivity {
    private ArrayList<SittingJob> jobList;
    private SittingJob currentJob;
    private boolean ownerJobs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ownerJobs = getIntent().getBooleanExtra("ownerJobs", true);
        if(ownerJobs) {
            this.jobList = Controller.currentUser.openJobsOwner;
        }
        else {
            this.jobList = Controller.currentUser.openJobsSitter;
        }

        allJobsList();
    }

    public void allJobsList() {
        setContentView(R.layout.activity_sitter_page);

        ArrayAdapter adapter = new ArrayAdapter(this, R.layout.listview_widget, R.id.listText, getJobArr());

        ListView listView = (ListView) findViewById(R.id.acceptedJobsList);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });
    }

    /**
     * This returns an array of Strings of each Job object, for the Job ListView in sitter_page
     * @return an array of the strings created from each of the specified jobs
     */
    public String[] getJobArr() {
        String[] jobArr;
        jobArr = new String[jobList.size()];

        for(int i=0;i<jobList.size();i++) {
            jobArr[i] = jobList.get(i).toString();
        }
        return jobArr;
    }
}
