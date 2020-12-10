package com.example.petsitterapp;

import android.content.Intent;
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
import org.json.JSONObject;

import java.io.IOException;


public class Controller extends AppCompatActivity {
    public static Model model;

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
        Log.w("Testing", "Inside PetFormActivity onCreate.");
    }

    /**
     * This method is called when the user presses the Login button
     * It checks if their login credentials are correct,
     * @param v
     */
    public void login(View v) {
        String username = ((EditText)findViewById(R.id.username)).getText().toString();
        String password = ((EditText)findViewById(R.id.password)).getText().toString();

        boolean accountExists = model.authenticateUser(username, password);
        if(accountExists) {
            ((TextView)findViewById(R.id.loginError)).setVisibility(View.INVISIBLE);
            allPetsActivity();
        }
        else {
            ((TextView)findViewById(R.id.loginError)).setVisibility(View.VISIBLE);
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
        allPetsActivity();
    }

    public void savePreferences(View v) {
        setContentView(R.layout.activity_credit_card);
    }


    /**
     * Switches to the activity of that "add pet" screen (PetFormActivity)
     *
     * @param v - the view that this method is triggered from (activity_all_pets)
     */
    public void addPetForm(View v) {
        Intent intent = new Intent(this, PetFormActivity.class);
        intent.putExtra("Json_NewOrEdit", "New");

        startActivity(intent);
    }

    /**
     * Switches to the activity of that "add pet" screen (PetFormActivity)
     *
     * @param v - the view that this method is triggered from (activity_all_pets)
     */
    public void newSittingJob(View v) {
        setContentView(R.layout.activity_new_job);
        String[] pets = {"sdf", "sdfgdfgdf"};


        ArrayAdapter adapter = new ArrayAdapter(this, R.layout.widget_single_pet, R.id.listText, pets);

        ListView listView = (ListView) findViewById(R.id.petListNewJobPage);
        listView.setAdapter(adapter);
    }

    /**
     * Switched to the "add pet" activity, but with an edit flag
     *
     * @param v - the view that this method is triggered from (single pet view)
     */
    public void editPetForm(View v) {
//        Intent intent = new Intent(this, PetFormActivity.class);
//        intent.putExtra("Json_NewOrEdit", "Edit");
//        startActivity(intent);

        Intent intent = new Intent(this, PetFormActivity.class);
        intent.putExtra("Json_NewOrEdit", "Edit");
        startActivity(intent);
    }

    /**
     * This method switches to the All pets activity and fills the listview on that screen with all pets
     */
    public void allPetsActivity() {
        setContentView(R.layout.activity_all_pets);
//        String[] pets = model.user.getPets();
        String[] pets = {"Spot  -  Dog (Chihuahua)", "Rover  -  Iguana", "Teddy  -  Cat (Siamese)"};


        ArrayAdapter adapter = new ArrayAdapter(this, R.layout.widget_single_pet, R.id.listText, pets);

        ListView listView = (ListView) findViewById(R.id.petList);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = (String) parent.getItemAtPosition(position);
                Log.w("MA", ""+position+"");
            }
        });
    }

    /**
     * Method called when a user has entered a new pet's information, this saves it in the model
     *
     * @param petInfo
     */
    public void addPet(JSONObject petInfo) throws JSONException, IOException {
        model.addPet(petInfo);

        setContentView(R.layout.activity_login);
    }

    /**
     * This method edits an existing pet row in the pet database
     *
     * @param newPetInfo - the updated information of an existing pet
     */
    public void editPet(JSONObject newPetInfo) throws JSONException {
        model.editPet(newPetInfo);

        setContentView(R.layout.activity_login);
    }

    /**
     * This method deletes a pet object from the pet database and removes it from an owner's pet map
     *
     * @param petID - the ID of the pet to be deleted
     */
    public void deletePet(int petID) {
        model.deletePet(petID);
    }

    /**
     * This method attempts to log into a user account
     *
     * @param username - the username entered by the user
     * @param password - the password entered by the user
     */
    public void login(String username, String password) {
        model.authenticateUser(username, password);
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