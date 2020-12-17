package com.example.petsitterapp;
//        android:layout_centerHorizontal="true"
import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnSuccessListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.io.IOException;
import java.util.List;


public class Controller extends AppCompatActivity {
    public static final int BOTH_ACCOUNT = 0;
    public static final int OWNER_ACCOUNT= 1;
    public static final int SITTER_ACCOUNT= 2;

    public static final int SLEEPOVER_NO = 0;
    public static final int SLEEPOVER_YES = 1;

    public static Model model;
    public static Pet currentPet;
    public static User currentUser;
    public ArrayList<Pet> petsInNewJob;

    public static final int PET_INTENT_REQUEST_CODE = 1;
    public static final int JOB_INTENT_REQUEST_CODE= 2;

    // This is a flag for onResume, so it triggers when activities finish and not when the app starts up
    private boolean justCreated = true;
    private boolean returningPetActivity = true;

    private FusedLocationProviderClient mFusedLocationClient;

    private int locationRequestCode = 1000;
    public static double wayLatitude = 0.0, wayLongitude = 0.0;
    private LocationRequest locationRequest;
    private LocationCallback locationCallback;

    public static LatLng myAddy;

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
        /**
         * Sets the context variable of model
         */
        setContext();

        /**
         * Gets the location data and sets the variables associated with it
         */
        generateLocationData();

        setContentView(R.layout.activity_login);
    }

    /**
     *  Gets the location data and sets the variables associated with it
     */
    public void generateLocationData() {
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(20 * 1000);
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    return;
                }
                for (Location location : locationResult.getLocations()) {
                    if (location != null) {
                        wayLatitude = location.getLatitude();
                        wayLongitude = location.getLongitude();
                        Log.w("MA", "Lat is " + wayLatitude + " long is " + wayLongitude);
                    }
                }
            }
        };
        getLocation();

        myAddy = getLocationFromAddress(this, "1601 Amphitheatre Pkwy, Mountain View, CA 94043");
        System.out.println(myAddy.toString());

        float[] distance = new float[1];
        Location.distanceBetween(wayLatitude, wayLongitude,
                myAddy.latitude, myAddy.longitude, distance);

        double radiusInMeters = 48.2803*1000.0; //1 KM = 1000 Meter

        if( distance[0]/1000 > radiusInMeters ){
            System.out.println("Outside, distance from center: " + distance[0]/1000 + " radius: " + radiusInMeters);
        } else {
            System.out.println("Inside, distance from center: " + distance[0]/1000 + " radius: " + radiusInMeters);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Runs when onCreate runs
        if(justCreated) {
            justCreated = false;
        }
        else {
            // runs when returning from PetActivity
            if(returningPetActivity) {
                currentUser.updatePetList(model.usersPets);
                allPetsActivity(new View(this));
            }
            // Runs when returning from JobActivity
            else {
                currentUser.updateOwnerJobs(model.ownersJobs);
                currentUser.updateSitterJobs(model.sittersJobs);
            }
        }
    }

    /**
     * This method is called when the user presses the Login button
     * It checks if their login credentials are correct,
     * @param v
     */
    public void login(View v) throws JSONException {
        Log.w("MA", "Login");
        String username = ((EditText) findViewById(R.id.username)).getText().toString();
        String password = ((EditText) findViewById(R.id.password)).getText().toString();

        try {
            currentUser = model.authenticateUser(username, password);
        } catch (JSONException je) {
            Log.w("MA", "Error Logging in");
        }

        if (currentUser == null) {
            ((TextView) findViewById(R.id.loginError)).setVisibility(View.VISIBLE);
        } else {
            ((TextView) findViewById(R.id.loginError)).setVisibility(View.INVISIBLE);
            Model.assignJobs();
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
        // FIXME Save the info into a new jsonObject, then save that in newAccount
        // if sitter, send to sitter preferences first
        setContentView(R.layout.activity_sitter_preferences_form);
        setPrefSpinners();
    }

    public void saveCreditCard(View v) {
        // FIXME Get all of the info in the fields, then save that in newAccount
        //  and save newAccount as a new account in the model, then
        goToDashBoard(v);
    }

    public void savePreferences(View v) {
        // FIXME Get all of the info in the fields, add it to newAccount
        // If newAccount is not an owner, then use it to save a new account in the model and go to dashboard
        // otherwise, go to activity_credit_card
        setContentView(R.layout.activity_credit_card);

    }

    /**
     * Switches the new job screen, fills the pet options listview
     * @param v - the view that this method is triggered from (activity_all_pets)
     */
    public void newSittingJob(View v) {
        setContentView(R.layout.activity_new_job);
        petsInNewJob = new ArrayList<Pet>();

        for(String pet:currentUser.getPets()) {
            Log.w("MA", "currentUserPets: "+pet);
        }

        ArrayAdapter adapter = new ArrayAdapter(this, R.layout.listview_widget, R.id.listText, currentUser.getPets());

        ListView listView = (ListView) findViewById(R.id.petListNewJobPage);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Pet selectedPet = model.usersPets.get(position);
                Log.w("MA", "Selected Pet: "+selectedPet.toString());

                if(petsInNewJob.contains(selectedPet)) {
                    petsInNewJob.remove(selectedPet);
                    //((TextView) view).setTextColor(Color.BLACK);
                }
                else {
                    petsInNewJob.add(selectedPet);
                    Log.w("MA", ""+view.toString());
                }
            }
        });

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        int listTextHeight = 100;
        Log.w("MA", ""+currentUser.getPets().length);
        params.height = listTextHeight * currentUser.getPets().length;
        listView.setLayoutParams(params);
    }
    /*
    jobID ownerIDKey petIDKey startDate endDate sleepover jobDetails sitterIDKey
     */
    public void saveSittingJob(View v) {
        Log.w("MA", "Saving Sitting Job");
        try {
            JSONObject jobInfo = new JSONObject();

            jobInfo.put("ownerIDKey", currentUser.userID);

            String startDate = ((EditText) findViewById(R.id.year_input)).getText().toString() + "-" +
                    ((EditText) findViewById(R.id.month_input)).getText().toString() + "-" +
                    ((EditText) findViewById(R.id.day_input)).getText().toString();
            Log.w("MA", "startDate: "+startDate);

            String endDate = ((EditText) findViewById(R.id.end_year_input)).getText().toString() + "-" +
                    ((EditText) findViewById(R.id.end_month_input)).getText().toString() + "-" +
                    ((EditText) findViewById(R.id.end_day_input)).getText().toString();
            Log.w("MA", "endDate: "+endDate);
            jobInfo.put("startDate", startDate);
            jobInfo.put("endDate", endDate);

            jobInfo.put("jobDetails", ((EditText) findViewById(R.id.job_details_field)).getText().toString());

            int groupID = ((RadioGroup) findViewById(R.id.sleepoverGroup)).getCheckedRadioButtonId();
            Log.w("MA", "sleepover button: "+((RadioButton)findViewById(groupID)).getText().toString());

            String sleepover_result = ((RadioButton)findViewById(groupID)).getText().toString();
            if(sleepover_result.equals("YES")) {
                jobInfo.put("sleepover", ""+SLEEPOVER_YES);
                Log.w("MA", "sleepover selection: "+SLEEPOVER_YES);
            }
            else {
                jobInfo.put("sleepover", ""+SLEEPOVER_NO);
                Log.w("MA", "sleepover selection: "+SLEEPOVER_YES);

            }


            String petIDs = "";
            for(int i=0;i<petsInNewJob.size();i++) {
                Log.w("MA", "forID: "+ petsInNewJob.get(i).toString());
                petIDs += petsInNewJob.get(i).petID + ",";
            }

            if(petIDs.length() > 1) {
                jobInfo.put("petIDKey", petIDs.substring(0, petIDs.length()-1));
                Log.w("MA", "Pet IDs: " + petIDs.substring(0, petIDs.length()-1));
            }
            else {
                jobInfo.put("petIDKey", petIDs);
                Log.w("MA", "short: " + petIDs);
            }

            SittingJob job = new SittingJob(jobInfo);
            model.createJob(job);
        }
        catch(JSONException je) {
            Log.w("MA", "JSONException Controller.saveSittingJob()");
        }
        allPetsActivity(v);
    }

    /**
     * This method switches to the All pets activity and fills the listview on that screen with all pets
     * @param v - the owner view button in activity_dashboard
     */
    public void allPetsActivity(View v) {
        setContentView(R.layout.activity_all_pets);
        currentUser.updatePetList(model.usersPets);

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
        returningPetActivity = true;
        Intent intent = new Intent(this, PetActivity.class);
        intent.putExtra("newPet", newFlag);
        startActivity(intent);
    }

    /**
     * This method creates a JobActivity for the list of jobs that a sitter has accepted
     * @param v - the sitter page button in activity_dashboard
     */
    public void sitterJobsActivity(View v) {
        returningPetActivity = false;
        Intent intent = new Intent(this, JobActivity.class);
        intent.putExtra("jobType", "sitter");
        startActivity(intent);
    }

    /**
     * This method creates a new JobActivity to show available jobs that the sitter is within range of
     * @param v - the "Available Jobs" button on the dashboard
     */
    public void openJobsActivity(View v) {
        returningPetActivity = false;
        Intent intent = new Intent(this, JobActivity.class);
        intent.putExtra("jobType", "open");
        startActivity(intent);
    }

    /**
     * This method creates a JobActivity for a list of sitting jobs created by the user
     * @param v - the view jobs button in activity_all_pets
     */
    public void ownerJobsActivity(View v) {
        Log.w("MA", "ownerJobsActivity");
        returningPetActivity = false;
        Intent intent = new Intent(this, JobActivity.class);
        intent.putExtra("jobType", "owner");
        startActivity(intent);
    }

    public void goToSettingsActivity(View v) {
        setContentView(R.layout.activity_settings_page);
        EditText et = (EditText) findViewById(R.id.FirstNameInput);

//        et1.setText(object.toString()));

        EditText et1 = (EditText) findViewById(R.id.LastNameInput);
        //        et2.setText(object.toString()));

        EditText et2 = (EditText) findViewById(R.id.AddressInput);
//        et3.setText(object.toString()));

        EditText et3 = (EditText) findViewById(R.id.EmailInput);
//        et3.setText(object.toString()));

        EditText et4 = (EditText) findViewById(R.id.PasswordInput);
//        et4.setText(object.toString()));
    }

    public void logOut(View v) {
        currentUser = null;
        //resets the pet list etc.
        model.logout();
        setContentView(R.layout.activity_login);
    }

    public void goToDashBoard(View v) {
        setContentView(R.layout.activity_dashboard);
        int typeAccount = 0;
        try {
            typeAccount = Integer.parseInt(currentUser.accountInfo.getString("typeOfAccount"));
        } catch (JSONException je) {
            Log.w("MA", "Error in GoToDashboard");
        }

        if (typeAccount == OWNER_ACCOUNT) {
            Button ownerButton = findViewById(R.id.owner_page_button);
            ownerButton.setVisibility(View.VISIBLE);
        } else if (typeAccount == SITTER_ACCOUNT) {
            Button sitterButton = findViewById(R.id.sitter_page_button);
            sitterButton.setVisibility(View.VISIBLE);
        } else {
            Button ownerButton = findViewById(R.id.owner_page_button);
            ownerButton.setVisibility(View.VISIBLE);
            Button sitterButton = findViewById(R.id.sitter_page_button);
            sitterButton.setVisibility(View.VISIBLE);
        }
    }

    public void getLocation() {


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, locationRequestCode);
        } else {
             //already permission granted
            // get location here
            mFusedLocationClient.getLastLocation().addOnSuccessListener(this, location -> {
                if (location != null) {
                    wayLatitude = location.getLatitude();
                    wayLongitude = location.getLongitude();
                    Log.w("MA", "Lat is " + wayLatitude + " long is " + wayLongitude);
                }
                else {
                    mFusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null);
                }

            });
        }

        //TODO Get user location


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1000: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        return;
                    }
                    mFusedLocationClient.getLastLocation().addOnSuccessListener(this, location -> {
                        if (location != null) {
                            wayLatitude = location.getLatitude();
                            wayLongitude = location.getLongitude();
                            Log.w("MA", "Lat is " + wayLatitude + " long is " + wayLongitude);
                        }
                    });
                } else {
                    Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
                }
                break;
            }
        }
    }

    public static LatLng getLocationFromAddress(Context context, String strAddress) {

        Geocoder coder = new Geocoder(context);
        List<Address> address;
        LatLng p1 = null;

        try {
            // May throw an IOException
            address = coder.getFromLocationName(strAddress, 5);
            if (address == null) {
                return null;
            }

            Address location = address.get(0);
            p1 = new LatLng(location.getLatitude(), location.getLongitude() );

        } catch (IOException ex) {

            ex.printStackTrace();
        }

        return p1;
    }

    public void setContext(){
        Model.context = this;
    }

    public void goBackAllPets(View v) {
        goToDashBoard(v);
    }

    public void setPrefSpinners() {
        //SET PETSPECIES SPINNER
        Spinner spinner_species = (Spinner) findViewById(R.id.PetSpeciesSpinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter_species = ArrayAdapter.createFromResource(this,
                R.array.petSpecies_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter_species.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner_species.setAdapter(adapter_species);

        //SET PETSIZE SPINNER
        Spinner spinner_size = (Spinner) findViewById(R.id.PetSizeSpinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter_size = ArrayAdapter.createFromResource(this,
                R.array.petSize_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter_size.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner_size.setAdapter(adapter_size);

        //SET PETSIZE SPINNER
        Spinner spinner_temp = (Spinner) findViewById(R.id.PetTemperamentSpinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter_temp = ArrayAdapter.createFromResource(this,
                R.array.petTemp_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter_temp.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner_temp.setAdapter(adapter_temp);
    }

    public void settingsGoBack(View v) {
        setContentView(R.layout.activity_dashboard);
    }



    public void saveAccountSettings(View v)
    {
        EditText et = (EditText) findViewById(R.id.FirstNameInput);

//        et1.setText(object.toString()));

        EditText et1 = (EditText) findViewById(R.id.LastNameInput);
        //        et2.setText(object.toString()));

        EditText et2 = (EditText) findViewById(R.id.AddressInput);
//        et3.setText(object.toString()));

        EditText et3 = (EditText) findViewById(R.id.EmailInput);
//        et3.setText(object.toString()));

        EditText et4 = (EditText) findViewById(R.id.PasswordInput);
//        et4.setText(object.toString()));










    }
}