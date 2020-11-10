package com.example.petsitterapp;


import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

public class PetFormActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private PetObject petOne = new PetObject();


    @Override
    protected void onCreate( Bundle savedInstanceState ) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_petform);

        setSpinnerData();

        //For Testing petObject
        printPetObject( petOne );

        Spinner spinner = (Spinner) findViewById(R.id.PetForm_PetSpeciesSpinner);
        spinner.setOnItemSelectedListener(this);


    }



    //For Testing petObject
    public void printPetObject( PetObject petOne ) {
        //Log.w("Testing", "PetObject info, String format: " + petOne.toString());

        try {
            JSONObject petJSON = petOne.getJSON();
            Log.w("Testing", "PetObject info, JSON format: " + petJSON);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void setSpinnerData(  ) {
        Spinner spinner = (Spinner) findViewById(R.id.PetForm_PetSpeciesSpinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.petSpecies_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);
    }

    public void onItemSelected(AdapterView<?> parent, View view,
                               int pos, long id) {
        Log.w("Testing", "Inside Method: onItemSelected");
        // An item was selected. You can retrieve the selected item using
        String speciesSelected = ( String ) parent.getItemAtPosition(pos);
        Log.w("Testing", "SpeciesSelected: " + speciesSelected);


        Log.w("Testing", "Before if statement: " + speciesSelected.equals("Click me to select species."));

        if(!(speciesSelected.equals("Click me to select species.")) ) {
            Log.w("Testing", "Inside if statement & Before adding string to PetOBject " );
            printPetObject(petOne);

            petOne.setPetSpecies( speciesSelected );

            printPetObject(petOne);
            Log.w("Testing", "Inside if statement & After adding string to PetOBject " );
        }

    }

    public void onNothingSelected(AdapterView<?> parent) {
        Log.w("Testing", "Inside Method: onNothingSelected");
        // Another interface callback
        // Set Error Notification

    }

    //Get the view, go one by one down the form, checking each input for new info, then setting it into the petObject
    public void submitPetFormInfo( View v ) throws JSONException {
        Log.w("Testing", "Inside Method: submitPetFormInfo");
        printPetObject(petOne);



        if(!(SetFormInfo( v ))) { //if !false then there are no errors
            //continue on, no errors found
            Log.w("Testing", "No Errors");
            //call jeffs code

            // send the petObject.JSONObject to code
                JSONObject petInfoForReturn = petOne.getJSON();
                //JEFFS CODE FOR GOING BACKWARDS GOES HERE
                //JEFFS CODE FOR GOING BACKWARDS GOES HERE
                //JEFFS CODE FOR GOING BACKWARDS GOES HERE
            //close activity
            finish();
        }


    }


    public boolean SetFormInfo( View v ) {
        boolean errorCheck = false; //false means no errors.
        boolean name = setName( v );
        boolean species = setPetSpecies( v );

        return errorCheck;
    }

    //The area below here is for grabbing the info from the view, or saying there is no new info.
    public boolean setName(View v) {
        //get the info from name input
        EditText nameInput = ( EditText ) findViewById( R.id.PetNameInput );
        String textInput = nameInput.getText().toString();
        if(textInput.equals("")) {//This is an errorCheck, Checks if input is empty.
            if(textInput.equals("null") || textInput.equals("Null") || textInput.equals("NULL"))
                return true; //Just in case null/Null/NULL could mess up program.
            //Grab the ErrorTag for PetName and set the visibility to visible
            TextView ErrorTag = findViewById( R.id.ErrorTag_PetName );
            ErrorTag.setVisibility(View.VISIBLE);
            textInput = "null";
            Log.w("Testing", "Name grabbed from view is: " + textInput);
            return true; //Errors Found, Error is: Name is empty.
        }

        else { //This means no errors, set errorTag to Invisible.
            TextView mainLabel = findViewById( R.id.ErrorTag_PetName );
            mainLabel.setVisibility(View.INVISIBLE);
            petOne.setPetName( textInput );//This sets name into the petObject.
            Log.w("Testing", "Name grabbed from view is: " + textInput);
            return false; //No Errors Found.
        }
    }

    public boolean setPetSpecies( View v ) {
        String petSpecies = petOne.getPetSpecies();
        if(petSpecies.equals("Click me to select species.")) {
            //Set PetSpecies ErrorTag visibility to true.
            TextView ErrorTag = findViewById( R.id.ErrorTag_PetSpecies );
            ErrorTag.setVisibility(View.VISIBLE);
            return true; //This means Error Found, Species not selected.
        }
        else {
            return false;
        }
    }

    public void setErrorFlag_Name( View v ) { //Shows the error flag for name

    }

/*
    public String getPetName() {


    }

 */
}