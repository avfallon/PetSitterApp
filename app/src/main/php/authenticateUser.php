<?php
 include( "constants.php");
 $username = Constants::USERNAME;

 // connect to database using credentials
 $mysqli = new mysqli( Constants::HOST, $username, Constants::PASSWORD, Constants::DATABASE);

 // get the username and password from the app
 $usrname = $_POST['username'];
 $password = $_POST['password'];

 // run a sql query using the info the client provided to sign in the user
 $result = $mysqli->query("select * from Account where username = '$username' AND password = '$password'");

// make an array of json object that store the users information
 $array = [];
 while($row = $result->fetch_assoc())
 {
   // make the user object and store it in a json
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

// make it a json and then return that
 $result = json_encode($array);

 echo $result;

 ?>
