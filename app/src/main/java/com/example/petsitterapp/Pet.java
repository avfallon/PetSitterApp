package com.example.petsitterapp;

public class Pet {
		public int petID;

		public Pet(String[] attributeList, String DBName) {
				this.petID = addPet(attributeList);
				//instantiate SQL database
		}

		/**
		 * Purpose: add a new row to the Pet table with the input information
		 * Input: a list of values for every field in the Pet table of the DB
		 * Return: the integer pet ID, primary key for the Pet table
		 */
		public int addPet(String[] info) {


			return 0;
		}

		/**
		 * Purpose: return the information of a given pet
		 * Input: the primary key ID of the pet
		 * Return: A list of the pet's information, or null if the pet cannot be found
		 */
		public String[] getPetInfo(int petID) {

			String[] s = (null);
			return s;
		}

		/**
		 * Purpose: edit the information of an existing pet in the DB
		 * Input: a list of the new values to be put in the DB
		 * Return: true if the pet row is successfully edited
		 */
		public boolean editPet(String[] newInfo) {


			return false;
		}

		/**
		 * Purpose: remove an existing pet from a pet owner's account
		 * Input: none
		 * Return: true if the pet was successfully deleted, false if pet is not found
		 */
		public boolean deletePet() {


			return false;
		}




}
