package com.example.petsitterapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.io.IOException;


public class Controller extends AppCompatActivity {

    public static Model model;
    public static Pet currentPet;
    public static User currentUser;
    public static SittingJob currentJob;

    public static final int PET_INTENT_REQUEST_CODE = 1;
    public static final int JOB_INTENT_REQUEST_CODE= 2;

    private boolean justCreated = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            model = new Model();
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        setContentView(R.layout.activity_login);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(justCreated) {
            justCreated = false;
        }
        else {
            currentUser.updatePetList(model.usersPets);
            allPetsActivity(new View(this));
        }
    }

    /**
     * This method is called when the user presses the Login button
     * It checks if their login credentials are correct,
     * @param v
     */
    public void login(View v) {
        Log.w("MA", "Login");
        String username = ((EditText)findViewById(R.id.username)).getText().toString();
        String password = ((EditText)findViewById(R.id.password)).getText().toString();

        try {
            currentUser = model.authenticateUser(username, password);
        }
        catch(JSONException je) {
            Log.w("MA", "Error Logging in");
        }
        System.out.println(currentUser.accountInfo.toString());
        if(currentUser == null) {
            ((TextView)findViewById(R.id.loginError)).setVisibility(View.VISIBLE);
        }
        else {
            ((TextView)findViewById(R.id.loginError)).setVisibility(View.INVISIBLE);
            goToDashBoard(v);
        }
    }

    /**
     * This method is called when the user hits the "create account" button on the login page
     * it switches to the general "create account" activity
     * @param v - the login view that the method is called from
     */
    public void createAccount(View v) {
        setContentView(R.layout.activity_create_account);
    }

    /**
     * This method is called when the user clicks the "Save" button on the account creation page
     * @param v - the Create Account view that the method is called from
     */
    public void saveNewAccount(View v) {
        // Do some stuff in the model to save the account info
        // if sitter, send to sitter preferences first
        setContentView(R.layout.activity_sitter_preferences_form);
    }

    public void saveCreditCard(View v) {
        goToDashBoard(v);
    }

    public void savePreferences(View v) {
        setContentView(R.layout.activity_credit_card);
    }

    /**
     * Switches to the activity of that "add pet" screen (PetFormActivity)
     *
     * @param v - the view that this method is triggered from (activity_all_pets)
     */
    public void newSittingJob(View v) {
        setContentView(R.layout.activity_new_job);
        String[] pets = {"sdf", "sdfgdfgdf"};


        ArrayAdapter adapter = new ArrayAdapter(this, R.layout.listview_widget, R.id.listText, pets);

        ListView listView = (ListView) findViewById(R.id.petListNewJobPage);
        listView.setAdapter(adapter);
    }

    /**
     * This method switches to the All pets activity and fills the listview on that screen with all pets
     */
    public void allPetsActivity(View v) {
        setContentView(R.layout.activity_all_pets);

        ArrayAdapter adapter = new ArrayAdapter(this, R.layout.listview_widget, R.id.listText, currentUser.getPets());

        ListView listView = (ListView) findViewById(R.id.petList);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                currentPet = model.usersPets.get(position);
                goToPetActivity(false);
            }
        });
    }

    /**
     * This method is called by the Add Pet button in activity_all_pets, it opens up a blank pet form
     * @param v - the add pet button on all_pets_view
     */
    public void newPet(View v) {
        goToPetActivity(true);
    }

    /**
     * This method starts PetActivity, and passes a flag for whether its a new pet or editing a pet
     * @param newFlag - true if it is a new pet, or false if you're editing a pet
     */
    public void goToPetActivity(boolean newFlag) {
        Intent intent = new Intent(this, PetActivity.class);
        intent.putExtra("newPet", newFlag);
        startActivity(intent);
    }

    /**
     * This method switches to the All pets activity and fills the listview on that screen with all pets
     */
    public void allJobsActivity(View v) {
        setContentView(R.layout.activity_sitter_page);
    }

    public void goToSettingsActivity(View v) {
        setContentView(R.layout.activity_settings_page);
    }

    public void logOut(View v)
    {
        currentUser = null;
        setContentView(R.layout.activity_login);
    }

    public void goToDashBoard(View v)
    {
        setContentView(R.layout.activity_dashboard);
        String typeAccount = "";
        try {
            typeAccount = currentUser.accountInfo.getString("typeOfAccount");
        }
        catch (JSONException je) {
            Log.w("MA", "Error in GoToDashboard");
        }

        if(typeAccount.equals("OWNER"))
        {
            Button ownerButton = findViewById(R.id.owner_page_button);
            ownerButton.setVisibility(View.VISIBLE);
        }
        else if(typeAccount.equals("SITTER"))
        {
            Button sitterButton = findViewById(R.id.sitter_page_button);
            sitterButton.setVisibility(View.VISIBLE);
        }
        else
        {
            Button ownerButton = findViewById(R.id.owner_page_button);
            ownerButton.setVisibility(View.VISIBLE);
            Button sitterButton = findViewById(R.id.sitter_page_button);
            sitterButton.setVisibility(View.VISIBLE);
        }
    }
}