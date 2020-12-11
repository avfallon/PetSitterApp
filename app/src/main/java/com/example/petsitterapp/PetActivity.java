package com.example.petsitterapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class PetActivity extends AppCompatActivity {
    private Pet currentPet;
    private boolean editing;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
         this.currentPet = Controller.currentPet;
         boolean newFlag = getIntent().getBooleanExtra("newPet", true);

        if(newFlag) {
            editing = false;
            populatePetForm(null);
         }
        else {
            editing = true;
            Log.w("MA", "editing pet");
            populateViewPet(currentPet);
        }
    }

    /**
     * This method fills activity_view_pet with the given pet's information
     * @param pet - the pet who's information you will populate the view with
     */
    public void populateViewPet(Pet pet) {
        setContentView(R.layout.activity_view_pet);
        Log.w("MA", "PopulateViewPet: "+pet.petInfo.toString());
        try {
            ((TextView) findViewById(R.id.PetNameValue)).setText(pet.petInfo.getString("name"));
            ((TextView) findViewById(R.id.PetSpeciesValue)).setText(pet.petInfo.getString("species"));
            ((TextView) findViewById(R.id.PetSizeValue)).setText(pet.petInfo.getString("size"));
            ((TextView) findViewById(R.id.PetTemperamentValue)).setText(pet.petInfo.getString("temperament"));
            ((TextView) findViewById(R.id.PetBreedValue)).setText(pet.petInfo.getString("breed"));
            ((TextView) findViewById(R.id.PetAgeValue)).setText(pet.petInfo.getString("age"));
            ((TextView) findViewById(R.id.PetDietValue)).setText(pet.petInfo.getString("diet"));
            ((TextView) findViewById(R.id.PetHealthIssuesValue)).setText(pet.petInfo.getString("healthIssues"));
            ((TextView) findViewById(R.id.PetExtraInfoValue)).setText(pet.petInfo.getString("extraInfo"));

            Log.w("MA", "Successfully populated View Pet");
        } catch (JSONException je) {
            Log.w("MA", "JSONException PetActivity.populatePetForm()");
        }
    }

    /**
     * This method fills PetForm with the given pet's information, or clears all info if pet is null
     * @param pet - the pet who's information you will populate the view with, null if it is a new pet
     */
    public void populatePetForm(Pet pet) {
        setContentView(R.layout.activity_petform);
        Log.w("MA", "PopulatePetForm");
        if(pet != null) {
            try {
                ((EditText) findViewById(R.id.PetNameInput)).setText(pet.petInfo.getString("name"));
                ((EditText) findViewById(R.id.PetBreedInput)).setText(pet.petInfo.getString("breed"));
                ((EditText) findViewById(R.id.PetAgeInput)).setText(pet.petInfo.getString("age"));
                ((EditText) findViewById(R.id.PetDietInput)).setText(pet.petInfo.getString("diet"));
                ((EditText) findViewById(R.id.PetHealthIssuesInput)).setText(pet.petInfo.getString("healthIssues"));
                ((EditText) findViewById(R.id.PetExtraInfoInput)).setText(pet.petInfo.getString("extraInfo"));

                setSpinnerData();
                Spinner speciesSpinner = ((Spinner) findViewById(R.id.PetForm_PetSpeciesSpinner));
                speciesSpinner.setSelection(getIndex(speciesSpinner, pet.petInfo.getString("species")));

                Spinner sizeSpinner = ((Spinner) findViewById(R.id.PetForm_PetSizeSpinner));
                sizeSpinner.setSelection(getIndex(sizeSpinner, pet.petInfo.getString("size")));
                Log.w("MA", "size: "+pet.petInfo.getString("size"));

                Spinner temperamentSpinner = ((Spinner) findViewById(R.id.PetForm_PetTemperamentSpinner));
                temperamentSpinner.setSelection(getIndex(temperamentSpinner, pet.petInfo.getString("temperament")));

                findViewById(R.id.deletePet).setVisibility(View.VISIBLE);

                Log.w("MA", "Successfully populated Pet Form");
            } catch (JSONException je) {
                Log.w("MA", "JSONException PetActivity.populatePetForm()");
            }
        }
        else {
            Log.w("MA", "Filling empty pet");
            ((EditText) findViewById(R.id.PetNameInput)).setText("");
            ((EditText) findViewById(R.id.PetBreedInput)).setText("");
            ((EditText) findViewById(R.id.PetAgeInput)).setText("");
            ((EditText) findViewById(R.id.PetDietInput)).setText("");
            ((EditText) findViewById(R.id.PetHealthIssuesInput)).setText("");
            ((EditText) findViewById(R.id.PetExtraInfoInput)).setText("");

            setSpinnerData();

            findViewById(R.id.deletePet).setVisibility(View.INVISIBLE);
        }
    }

    /**
     * This method creates the spinners in PetForm for species and size
     */
    public void setSpinnerData(  ) {
        //SET PETSPECIES SPINNER
        Spinner spinner_species = (Spinner) findViewById(R.id.PetForm_PetSpeciesSpinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter_species = ArrayAdapter.createFromResource(this,
                R.array.petSpecies_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter_species.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner_species.setAdapter(adapter_species);

        //SET PETSIZE SPINNER
        Spinner spinner_size = (Spinner) findViewById(R.id.PetForm_PetSizeSpinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter_size = ArrayAdapter.createFromResource(this,
                R.array.petSize_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter_size.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner_size.setAdapter(adapter_size);

        //SET PETSIZE SPINNER
        Spinner spinner_temp = (Spinner) findViewById(R.id.PetForm_PetTemperamentSpinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter_temp = ArrayAdapter.createFromResource(this,
                R.array.petTemp_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter_temp.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner_temp.setAdapter(adapter_temp);
    }


    /**
     * This method returns the index in the spinner of the given string
     * @param spinner - the spinner you are searchng
     * @param myString - the string you are looking to match with its index
     * @return the index in the given spinner of the given string
     */
    private int getIndex(Spinner spinner, String myString){
        for (int i=0;i<spinner.getCount();i++){
            String splitter = "()";
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
        editing = true;
        populatePetForm(currentPet);
    }

    /**
     * This method is called by the save button in PetForm, it saves the information entered by the user
     * @param v - the save button in activity_PetForm
     */
    public void savePetInfo(View v) {
        setError(View.INVISIBLE, R.id.ErrorTag_PetName);
        setError(View.INVISIBLE, R.id.ErrorTag_PetSpecies);

        if(((TextView)findViewById(R.id.PetNameInput)).getText().toString() == "") {
            setError(View.VISIBLE, R.id.ErrorTag_PetName);
        }
        else if(((Spinner)findViewById(R.id.PetForm_PetSpeciesSpinner)).getSelectedItemPosition() == 0) {
            setError(View.VISIBLE, R.id.ErrorTag_PetSpecies);
        }
        else {
            try {
                JSONObject petInfo = getPetInfo();
                Log.w("MA", "petInfo: "+petInfo.toString());
                Pet pet = new Pet(petInfo);

                if (petInfo != null) {
                    if(editing) {
                        Log.w("MA", "editPet");
                        Controller.model.editPet(petInfo);
                    }
                    else {
                        Log.w("MA", "addPet");
                        Controller.model.addPet(petInfo);
                    }
                    currentPet = pet;
                    populateViewPet(pet);
                    Log.w("MA", "Finished saving");
                }
                else {
                    Log.w("MA", "Error in PetActivity.savePetInfo(), JSON is null");
                }
            }
            catch(JSONException | IOException je) {
                Log.w("MA", "JSONException PetActivity.savePet()");
            }
        }

    }

    public JSONObject getPetInfo() throws JSONException{
        JSONObject petInfo = new JSONObject();
        petInfo.put("ownerIDKey", Controller.currentUser.accountInfo.get("ownerIDKey"));
        if(editing) {
            petInfo.put("petIDKey", currentPet.petInfo.get("petIDKey"));
        }
        petInfo.put("name", ((EditText) findViewById(R.id.PetNameInput)).getText().toString());


        petInfo.put("species", ((Spinner) findViewById(R.id.PetForm_PetSpeciesSpinner)).getSelectedItem().toString());
        petInfo.put("size", ((Spinner) findViewById(R.id.PetForm_PetSizeSpinner)).getSelectedItem().toString());
        petInfo.put("temperament", ((Spinner) findViewById(R.id.PetForm_PetTemperamentSpinner)).getSelectedItem().toString());

        petInfo.put("breed", ((EditText) findViewById(R.id.PetBreedInput)).getText().toString());
        petInfo.put("age", ((EditText) findViewById(R.id.PetAgeInput)).getText().toString());
        petInfo.put("diet", ((EditText) findViewById(R.id.PetDietInput)).getText().toString());
        petInfo.put("healthIssues", ((EditText) findViewById(R.id.PetHealthIssuesInput)).getText().toString());
        petInfo.put("extraInfo", ((EditText) findViewById(R.id.PetExtraInfoInput)).getText().toString());
        return petInfo;
    }

    public void setError(int visibility, int errorID) {
        TextView ErrorTag = findViewById( errorID );
        ErrorTag.setVisibility(visibility);
    }

    public void goBackPetForm(View v) {
        if(editing) {
            setContentView(R.layout.activity_view_pet);
        }
        else {
            finish();
        }
    }

    public void goBackViewPet(View v) {
        finish();
    }

    public void deletePet(View v) {
        try {
            Controller.model.deletePet(currentPet.petInfo);
            finish();
        }
        catch(JSONException je) {
            Log.w("MA", "JSONException PetActivity.deletePet()");
        }
    }
}
