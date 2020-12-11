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

        String[] ha = {"asdf"};

        ArrayAdapter adapter = new ArrayAdapter(this, R.layout.listview_widget, R.id.listText, ha);

        ListView listView = (ListView) findViewById(R.id.petList);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Pet selectedPet = currentUser.petList.get(position);
//                String selectedItem = (String) parent.getItemAtPosition(position);
//                Log.w("MA", "\nFrom Model: " + selectedPet.toString() + "\nFrom ListView: " + selectedItem);
//                currentPet = selectedPet;
//                currentPet = model.usersPets.get(position);
//                goToPetActivity(false);
            }
        });
    }
}
