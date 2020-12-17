<?php
  // Assign JSON encoded string to a PHP variable
  include( "constants.php");
  $username = Constants::USERNAME;

  // set the credentials to call the database
  $mysqli = new mysqli( Constants::HOST, $username, Constants::PASSWORD, Constants::DATABASE);

  $json = $_GET['json'];

  // Decode JSON data to PHP object
  $obj = json_decode($json);
  // Access values from the returned object

  // make the account oject and use that to create the account
  $onwerIDKey = $obj->ownerIDKey;
  $firstName = $obj->firstName;
  $lastName = $obj->lastName;
  $address = $obj->address;
  $phoneNumber = $obj->phoneNumber;
  $email = $obj->email;
  $typeOfAccount = $obj->typeOfAccount;
  $password = $obj->password;

  // sql query to insert the new account into the database
  $query = "insert into Account (firstName, lastName, address, phoneNumber, email, typeOfAccount, password) values('$firstName', '$lastName', '$address', '$phoneNumber', '$email', '$typeOfAccount', '$password')";
  $result = $mysqli->query($query);
  echo "Account Created Sucessfully";
?>
