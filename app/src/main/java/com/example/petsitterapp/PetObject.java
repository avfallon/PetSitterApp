package com.example.petsitterapp;

import org.json.JSONException;
import org.json.JSONObject;

public class PetObject {
    private String petName;
    private String petSpecies;


    public PetObject() {
        setPetName(null);
        setPetSpecies(null);
    }

    public void setPetName(String petNameInput) {
        this.petName = petNameInput;
    }

    public String getPetName() {
        return this.petName;
    }

    public void setPetSpecies(String petSpeciesInput) {
        this.petSpecies = petSpeciesInput;
    }

    public String getPetSpecies() {
        return this.petSpecies;
    }

    public String toString(  ) {
        String petInfo =
                "PetInfo :{ PetName: " + this.petName +
                        ", PetSpecies: " + this.petSpecies;

        return petInfo;
    }

    public JSONObject getJSON(  ) throws JSONException {
        JSONObject Petjson = new JSONObject();

        Petjson.put("petName", this.petName);
        Petjson.put("petSpecies", this.petSpecies);


        return Petjson;
    }
}
