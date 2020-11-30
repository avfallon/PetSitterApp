package com.example.petsitterapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Controller extends Activity {

//    Pet pet;
    public int view;
    public static String dataBase;
//    Context context;

    public Controller( int v , Context context )
    {
//        context = context;
//        pet = new Pet();
//        String dataBase = dataB;
        Log.w("MA", "Controller");
        view = v;
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences( context );
        getJSON("http://damorales.cs.loyola.edu/PetSitterApp/app/src/main/php/query.php");
  //      startFirstScreen();
    }

    /**
     * Makes the initial screen when the app is first opened
     */
    public static void startFirstScreen(Context context)
    {

        Intent intent = new Intent(context, PetFormActivity.class);

        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        intent.putExtra("context" , Controller.class);

        context.startActivity(intent);
//        intent.put
//        intent.putExtra("emptyPet" , new Pet())
//        startActivity(intent);
    }

    /**
     * Retrieve a message from the View in order to do specified work
     * @param msg The message being sent to the Controller
     * @return A string to let the View know it got the message
     */
//    public String getMessage(String msg)
//    {
//        if (msg == "EDIT")
//        {
//
//        }
//        else if( msg == "NEW")
//        {
//
//        }
//    }

    /**
     * Send a message to the view to update
     * @param v The current view
     */
    public void notifyView(View v)
    {

    }

    /**
     * Update a pet's information
     * @param v
     */
    public void updatePet(View v)
    {
        //Get information from view


        //get the model being referenced

        //update the
    }

    /**
     * Changes the current screen
     * @param newV The view to change to
     */
    public void changeScreen(View newV) {



//        Intent intent = new Intent( this,  );
//        startActivity( intent );
    }

    /**
     * Tells the Pet model to create a new pet to be inserted into the database
     * @param newPet The information about the new pet to be added
     */
    public void createNewPet(JSONObject newPet)
    {
        Log.w("Testing", "Inside createNewPet " );

//        Pet newPet = new Pet(newPet , dataBase);

    }

    /**
     * Get the information about a specific pet
     * @param petID The petID to search for
     */
    public void retrievePetInfo( String petID ) throws JSONException
    {


//        pet.getPetInfo(petID);

    }

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



}
