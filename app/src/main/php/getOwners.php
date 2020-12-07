<?php
 include( "constants.php");
 $username = Constants::USERNAME;

 $mysqli = new mysqli( Constants::HOST, $username, Constants::PASSWORD, Constants::DATABASE);

 $result = $mysqli->query("select * from Account");

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
