package com.example.petsitterapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

public class PetActivity extends AppCompatActivity {
    private Pet currentPet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.w("MA", "Inside PetActivity");
         currentPet = Controller.currentPet;
         boolean newFlag = getIntent().getBooleanExtra("newFlag", true);

        if(newFlag) {
             populatePetForm(null);
             setContentView(R.layout.activity_petform);
         }
        else {
             setContentView(R.layout.activity_view_pet);
             populateViewPet(currentPet);
        }
    }

    public void populateViewPet(Pet pet) {

        //still needs to be implemented
    }

    public void populatePetForm(Pet pet) {
        try {
            ((EditText) findViewById(R.id.PetNameInput)).setText(currentPet.petInfo.getString("Name"));
            Spinner speciesSpinner = ((Spinner) findViewById(R.id.PetForm_PetSpeciesSpinner));
            speciesSpinner.setSelection(getIndex(speciesSpinner, currentPet.petInfo.getString("Species")));

            Spinner sizeSpinner = ((Spinner) findViewById(R.id.PetForm_PetSizeSpinner));
            sizeSpinner.setSelection(getIndex(sizeSpinner, currentPet.petInfo.getString("Size")));
            Log.w("MA", "Selected");

            ((EditText) findViewById(R.id.PetTemperamentInput)).setText(currentPet.petInfo.getString("Temperament"));
            ((EditText) findViewById(R.id.PetBreedInput)).setText(currentPet.petInfo.getString("Breed"));
            ((EditText) findViewById(R.id.PetAgeInput)).setText(currentPet.petInfo.getString("Age"));
            ((EditText) findViewById(R.id.PetDietInput)).setText(currentPet.petInfo.getString("Diet"));
            ((EditText) findViewById(R.id.PetHealthIssuesInput)).setText(currentPet.petInfo.getString("HealthIssues"));
            ((EditText) findViewById(R.id.PetExtraInfoInput)).setText(currentPet.petInfo.getString("ExtraInfo"));

        }
        catch(JSONException je) {
            Log.w("MA", "JSONException PetActivity.populatePetForm()");

            ((EditText) findViewById(R.id.PetNameInput)).setText("");
//            Spinner speciesSpinner = ((Spinner) findViewById(R.id.PetForm_PetSpeciesSpinner));
//            speciesSpinner.setSelection(getIndex(speciesSpinner, currentPet.petInfo.getString("Species")));
//
//            Spinner sizeSpinner = ((Spinner) findViewById(R.id.PetForm_PetSizeSpinner));
//            sizeSpinner.setSelection(getIndex(sizeSpinner, currentPet.petInfo.getString("Size")));

            ((EditText) findViewById(R.id.PetTemperamentInput)).setText("");
            ((EditText) findViewById(R.id.PetBreedInput)).setText("");
            ((EditText) findViewById(R.id.PetAgeInput)).setText("");
            ((EditText) findViewById(R.id.PetDietInput)).setText("");
            ((EditText) findViewById(R.id.PetHealthIssuesInput)).setText("");
            ((EditText) findViewById(R.id.PetExtraInfoInput)).setText("");
        }
        //still needs to be implemented
    }


    //private method of your class
    private int getIndex(Spinner spinner, String myString){
        for (int i=0;i<spinner.getCount();i++){
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(myString)){
                return i;
            }
        }

        return 0;
    }

    /**
     * This method is called from activity_view_pet, it opens petForm for editing the selected pet
     */
    public void editPet(View v) {
        populatePetForm(currentPet);
        setContentView(R.layout.activity_petform);
    }


    //Method slowly for taking the petData that was transferred through intent.putExtra,
    //Takes that data which is received as a String and turns it back into a JSONObject.
    public JSONObject getTransferredPetData() {
        Intent intent = this.getIntent();
        JSONObject transferData = null;
        try {
            transferData = new JSONObject(intent.getStringExtra("Json_NewOrEdit"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.w("Testing", "The data collected is: " + transferData);

        return transferData;
    }
}
