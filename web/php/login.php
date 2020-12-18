<?php
 include( "constants.php");
 //include("./js/cookie.js");
 $username = Constants::USERNAME;

 // open mysqli connection
 $mysqli = new mysqli( Constants::HOST, $username, Constants::PASSWORD, Constants::DATABASE);

 $email = $_POST['email'];
 $password = $_POST['password'];

 // get the info from the database about the current user
 $result = $mysqli->query("select * from Account where email = '$email' ");

 while($user = $result->fetch_assoc())
 {
   $typeAccount = $user['typeOfAccount'];
   $ownerIDKey = $user['OwnerIdKey'];
   $retrieved_password = $user['password'];
 }

 // make sure their password matches
 $check = password_verify($password, $retrieved_password);

 if ($check) {
     setcookie('email', $email, time() + (86400 * 30), "/");
     setcookie('userKey', $ownerIDKey, time() + (86400 * 30), "/");
     setcookie('account', $typeAccount, time() + (86400 * 30), "/");
     echo 0;
 }
 else{
   echo 1;
 }

 $mysqli->close();

 ?>
