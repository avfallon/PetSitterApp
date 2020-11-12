package com.example.petsitterapp;

import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;

public class Pet {
		public int petID;
		public JSONObject petInfo;

		public Pet(JSONObject petInfo) {
			this.petInfo = petInfo;
			this.petID = addPet(petInfo);
		}

		public Pet(int petID) {
			this.petID = petID;
			//TODO call to database to get the information, store it in petInfo
		}

		/**
		 * Purpose: add a new row to the Pet table with the input information
		 * Input: a list of values for every field in the Pet table of the DB
		 * Return: the integer pet ID, primary key for the Pet table
		 */
		public int addPet(JSONObject petInfo) {
			int petID;
			//TODO Create a new row in the pet table with the petInfo
			//TODO return the created petID

			//return petID;
			return 0;
		}

		/**
		 * Purpose: return the information of a given pet
		 * Input: the primary key ID of the pet
		 * Return: A list of the pet's information, or null if the pet cannot be found
		 */
		public String[] getPetInfo() {

			String[] s = (null);

			try {

				String[] cmdArr = {"Desktop/Atom1.exe"};
				Process proc = Runtime.getRuntime().exec(cmdArr);

				proc.waitFor();
				Log.w("MA", "AHHHHH");


				String line;

				BufferedReader error = new BufferedReader(new InputStreamReader(proc.getErrorStream()));
				while ((line = error.readLine()) != null) {
					System.out.println(line);
					Log.w("MA", line);
				}
				error.close();

				BufferedReader input = new BufferedReader(new InputStreamReader(proc.getInputStream()));
				while ((line = input.readLine()) != null) {
					System.out.println(line);
					Log.w("MA", line);

				}

				input.close();

				OutputStream outputStream = proc.getOutputStream();
				PrintStream printStream = new PrintStream(outputStream);
				printStream.println();
				printStream.flush();
				printStream.close();
			}
			catch(IOException io) {
				Log.w("MA", "io exception"+io);
			}
			catch(InterruptedException ie) {
				Log.w("MA", "ie exception");
			}

			return s;
		}

		/**
		 * Purpose: edit the information of an existing pet in the DB
		 * Input: a list of the new values to be put in the DB
		 * Return: true if the pet row is successfully found and edited
		 */
		public boolean editPet(JSONObject newInfo) {
			this.petInfo = newInfo;
			//TODO command to change the row of the pet database,
			// returns false if the given pet cannot be found

			return true;
		}

		/**
		 * Purpose: remove an existing pet from a pet owner's account
		 * Input: none
		 * Return: true if the pet was successfully deleted, false if pet is not found
		 */
		public boolean deletePet() {
			//TODO command to delete this pet's row from the database
			// returns false if the pet cannot be found in the database

			return false;
		}




}
