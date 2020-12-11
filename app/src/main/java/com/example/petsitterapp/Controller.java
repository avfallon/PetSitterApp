package com.example.petsitterapp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
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
import com.google.android.gms.tasks.OnSuccessListener;

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

    private FusedLocationProviderClient mFusedLocationClient;

    private int locationRequestCode = 1000;
    private double wayLatitude = 0.0, wayLongitude = 0.0;
    private LocationRequest locationRequest;
    private LocationCallback locationCallback;

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

        //makeTestUser();
        setContentView(R.layout.activity_login);
    }

    public void makeTestUser() {
        try {
            JSONObject petInfo = new JSONObject();
            petInfo.put("OwnerIDKey", "3333");
            petInfo.put("PetIDKey", "4444");
            petInfo.put("Name", "Devin");
            petInfo.put("Species", "Dog");
            petInfo.put("Size", "Small");
            petInfo.put("Temperament", "Calm");
            petInfo.put("Breed", "Australian Shepherd");
            petInfo.put("Age", "4");
            petInfo.put("Diet", "2 scoops of dry food");
            petInfo.put("HealthIssues", "Bum hip");
            petInfo.put("ExtraInfo", "Nada");

            JSONObject accountInfo = new JSONObject();
            accountInfo.put("UserIDKey", "3333");
            accountInfo.put("firstName", "Andrew");
            accountInfo.put("lastName", "Fallon");
            accountInfo.put("address", "3336 Gilman");
            accountInfo.put("phoneNumber", "666-666-6666");
            accountInfo.put("email", "username");
            accountInfo.put("typeOfAccount", "" + 0);
            accountInfo.put("password", "password");

            Pet newPet = new Pet(petInfo);
            ArrayList<Pet> petList = new ArrayList<Pet>();
            petList.add(newPet);
            // currentUser = new User(petInfo.getInt("OwnerIDKey"), accountInfo, petList);
            //Log.w("MA", currentUser.getPets()[0]);

        } catch (JSONException je) {
            Log.w("MA", "makeTestUser JSONException");
            setContentView(R.layout.activity_login);
        }
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
        String username = ((EditText) findViewById(R.id.username)).getText().toString();
        String password = ((EditText) findViewById(R.id.password)).getText().toString();

        try {
            currentUser = model.authenticateUser(username, password);
        } catch (JSONException je) {
            Log.w("MA", "Error Logging in");
        }
        System.out.println(currentUser.accountInfo.toString());
        if (currentUser == null) {
            ((TextView) findViewById(R.id.loginError)).setVisibility(View.VISIBLE);
        } else {
            ((TextView) findViewById(R.id.loginError)).setVisibility(View.INVISIBLE);
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
                Pet selectedPet = currentUser.petList.get(position);
                String selectedItem = (String) parent.getItemAtPosition(position);
                Log.w("MA", "\nFrom Model: " + selectedPet.toString() + "\nFrom ListView: " + selectedItem);
                currentPet = selectedPet;
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

    public void logOut(View v) {
        currentUser = null;
        setContentView(R.layout.activity_login);
    }

    public void goToDashBoard(View v) {
        setContentView(R.layout.activity_dashboard);
        String typeAccount = "";
        try {
            typeAccount = currentUser.accountInfo.getString("typeOfAccount");
        } catch (JSONException je) {
            Log.w("MA", "Error in GoToDashboard");
        }

        if (typeAccount.equals("OWNER")) {
            Button ownerButton = findViewById(R.id.owner_page_button);
            ownerButton.setVisibility(View.VISIBLE);
        } else if (typeAccount.equals("SITTER")) {
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










    // I moved everything below to Model, im just not deleting it in case I broke something while moving it


    /*
    private void getJSON(final String urlWebService) {

        class GetJSON extends AsyncTask<Void, Void, String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
                try {
                    loadIntoListView(s);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            protected String doInBackground(Void... voids) {
                try {
                    URL url = new URL(urlWebService);

                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    StringBuilder sb = new StringBuilder();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));
                    String json;
                    while ((json = bufferedReader.readLine()) != null) {
                        sb.append(json + "\n");
                    }
                    System.out.println(sb.toString().trim());
                    return sb.toString().trim();
                } catch (Exception e) {
                    return null;
                }

            }
        }
        GetJSON getJSON = new GetJSON();
        getJSON.execute();
    }

    private void loadIntoListView(String json) throws JSONException {
        JSONArray jsonArray = new JSONArray(json);
        System.out.println(jsonArray.toString());
        String[] pets = new String[jsonArray.length()];
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject obj = jsonArray.getJSONObject(i);
            pets[i] = obj.getString("name");
            System.out.println("name");
        }

    }


 */

}