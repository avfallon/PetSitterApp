<?php
 include( "constants.php");
 $username = Constants::USERNAME;

 // credentials to sign into the sqli
 $mysqli = new mysqli( Constants::HOST, $username, Constants::PASSWORD, Constants::DATABASE);

 $result = $mysqli->query("select * from Account");

 // make an array of json object for all the users in the database;
 $array = [];
 while($row = $result->fetch_assoc())
 {
   //assign the values of the user into the json object
   array_push($array, [
     'ownerIDKey' => $row['OwnerIdKey'],
     'firstName' => $row['firstName'],
     'lastName' => $row['lastName'],
     'address' => $row['address'],
     'phoneNumber' => $row['phoneNumber'],
     'email' => $row['email'],
     'typeOfAccount' => $row['typeOfAccount'],
     'password' => $row['password'],
   ]);
 }

 // encode the array into json
 $result = json_encode($array);

 // return the full array
 echo $result;

 ?>
