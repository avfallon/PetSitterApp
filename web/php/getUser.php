<?php
 include( "constants.php");
 $username = Constants::USERNAME;

 // open mysqli connection
 $mysqli = new mysqli( Constants::HOST, $username, Constants::PASSWORD, Constants::DATABASE);

 $email = $_POST['email'];
 $password = $_POST['password'];

 // get the user information
 $result = $mysqli->query("select * from Account where email = '$email'");

 $array = [];
 while($row = $result->fetch_assoc())
 {
   setCookie("userKey", $row['OwnerIdKey'], time() + (86400 * 30), "/");
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

 //setcookie('account', $typeAccount, time() + (86400 * 30), "/");

 $result = json_encode($array);

 echo $result;

 ?>
