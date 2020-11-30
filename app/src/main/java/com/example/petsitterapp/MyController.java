package com.example.petsitterapp;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MyController extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getJSON("http://damorales.cs.loyola.edu/PetSitterApp/app/src/main/php/query.php");
        setContentView(R.layout.activity_main);
        Log.w("Testing","Inside PetFormActivity onCreate.");
    }

    public void addPetView(View v) {
        Intent intent = new Intent(this, PetFormActivity.class);
        intent.putExtra("Json_NewOrEdit", "New");

        startActivity(intent);
    }

    public void allPetsView(View v){
        setContentView(R.layout.all_pets_view);
        String[] pets = {"Johnny   -   German Shepherd", "Rick        -   Fish", "Benny     -   Siamese"};

        ArrayAdapter adapter = new ArrayAdapter<String>(this, R.layout.simple_pet_view, R.id.listText, pets);

        ListView listView = (ListView) findViewById(R.id.petList);
        listView.setAdapter(adapter);
    }


    public void editPetView(View v) {
        Intent intent = new Intent(this, PetFormActivity.class);
        intent.putExtra("Json_NewOrEdit", "Edit");

        startActivity(intent);
    }

    public void addPet(JSONObject petInfo) {
        Pet newPet = new Pet(petInfo);
        // attach pet to owner account

        setContentView(R.layout.activity_main);
    }

    public void editPet(JSONObject newPetInfo) {
//        int petID = newPetInfo.getInt(petID);
        int petID = 2;
        Pet pet = new Pet(petID);
        pet.editPet(newPetInfo);
    }

    public void deletePet(int petID) {
        Pet pet = new Pet(petID);
        pet.deletePet();
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