<?php
 include( "constants.php");
 $username = Constants::USERNAME;

 $mysqli = new mysqli( Constants::HOST, $username, Constants::PASSWORD, Constants::DATABASE);

 $usrname = $_POST['username'];
 $password = $_POST['password'];

 $result = $mysqli->query("select * from Account where username = '$username' AND password = '$password'");

 $array = [];
 while($row = $result->fetch_assoc())
 {
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

 $result = json_encode($array);

 echo $result;

 ?>
