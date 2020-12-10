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

import java.util.ArrayList;
import java.io.IOException;


public class Controller extends AppCompatActivity {
    public static final int BOTHFLAG = 0;
    public static final int OWNERFLAG = 1;
    public static final int SITTERFLAG = 2;

    public static Model model;
    public static Pet currentPet;
    public static User currentUser;
    public SittingJob currentJob;

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

        makeTestUser();
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
            accountInfo.put("typeOfAccount", ""+BOTHFLAG);
            accountInfo.put("password", "password");

            Pet newPet = new Pet(petInfo);
            ArrayList<Pet> petList = new ArrayList<Pet>();
            petList.add(newPet);
            currentUser = new User(petInfo.getInt("OwnerIDKey"), accountInfo, petList);
            Log.w("MA", currentUser.getPets()[0]);

        }
        catch(JSONException je) {
            Log.w("MA", "makeTestUser JSONException");
        }

    }

    /**
     * This method is called when the user presses the Login button
     * It checks if their login credentials are correct,
     * @param v
     */
    public void login(View v) throws JSONException {
        Log.w("MA", "Login");
        String username = ((EditText)findViewById(R.id.username)).getText().toString();
        String password = ((EditText)findViewById(R.id.password)).getText().toString();

        currentUser = model.authenticateUser(username, password);
        if(currentUser == null) {
            ((TextView)findViewById(R.id.loginError)).setVisibility(View.VISIBLE);
        }
        else {
            ((TextView)findViewById(R.id.loginError)).setVisibility(View.INVISIBLE);
            allPetsActivity();
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


        ArrayAdapter adapter = new ArrayAdapter(this, R.layout.listview_widget, R.id.listText, pets);

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

        ArrayAdapter adapter = new ArrayAdapter(this, R.layout.listview_widget, R.id.listText, currentUser.getPets());

        ListView listView = (ListView) findViewById(R.id.petList);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Pet selectedPet = currentUser.petList.get(position);
                String selectedItem = (String) parent.getItemAtPosition(position);
                Log.w("MA", "\nFrom Model: "+selectedPet.toString()+"\nFrom ListView: "+selectedItem);
                currentPet = selectedPet;
                goToPetActivity(false);
            }
        });
    }

    public void goToPetActivity(boolean newFlag) {
        Intent intent = new Intent(this, PetActivity.class);
        intent.putExtra("newPet", newFlag);
        startActivity(intent);
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
        //model.deletePet(petID);
    }

    /**
     * This method attempts to log into a user account
     *
     * @param username - the username entered by the user
     * @param password - the password entered by the user
     */
    public void login(String username, String password) throws JSONException {
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