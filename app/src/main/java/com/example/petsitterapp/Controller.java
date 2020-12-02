package com.example.petsitterapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Controller extends AppCompatActivity {
    Model model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        model = new Model();

        //getJSON("http://damorales.cs.loyola.edu/PetSitterApp/app/src/main/php/query.php");
        setContentView(R.layout.activity_login);
        Log.w("Testing", "Inside PetFormActivity onCreate.");
    }

    public void login(View v) {
        String username = ((EditText)findViewById(R.id.username)).getText().toString();
        String password = ((EditText)findViewById(R.id.password)).getText().toString();

        model.authenticateUser(username, password);
        allPetsView();
    }


    /**
     * Switches to the activity of that "add pet" screen (PetFormActivity)
     *
     * @param v - the view that this method is triggered from (activity_all_pets)
     */
    public void addPetView(View v) {
        Intent intent = new Intent(this, PetFormActivity.class);
        intent.putExtra("Json_NewOrEdit", "New");

        startActivity(intent);
    }

    /**
     * Switched to the "add pet" activity, but with an edit flag
     *
     * @param v - the view that this method is triggered from (single pet view)
     */
    public void editPetView(View v) {
        Intent intent = new Intent(this, PetFormActivity.class);
        intent.putExtra("Json_NewOrEdit", "Edit");

        startActivity(intent);
    }

    /**
     * This method switches to the All pets screen and fills the listview on that screen with all pets
     */
    public void allPetsView() {
        setContentView(R.layout.activity_all_pets);
//        String[] pets = model.user.getPets();
        String[] pets = {"sdf", "sdfgdfgdf"};


        ArrayAdapter adapter = new ArrayAdapter(this, R.layout.widget_single_pet, R.id.listText, pets);

        ListView listView = (ListView) findViewById(R.id.petList);
        listView.setAdapter(adapter);
    }

    /**
     * Method called when a user has entered a new pet's information, this saves it in the model
     *
     * @param petInfo
     */
    public void addPet(JSONObject petInfo) throws JSONException {
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