<?php
  // Assign JSON encoded string to a PHP variable
  include( "constants.php");
  $username = Constants::USERNAME;

  // credentials to sign into the sqli databse
  $mysqli = new mysqli( Constants::HOST, $username, Constants::PASSWORD, Constants::DATABASE);

  $json = $_GET['json'];

  // Decode JSON data to PHP object
  $obj = json_decode($json);
  // Access values from the returned object

  // make the account object
  $ownerIDKey = $obj->ownerIDKey;
  $firstName = $obj->firstName;
  $lastName = $obj->lastName;
  $address = $obj->address;
  $phoneNumber = $obj->phoneNumber;
  $email = $obj->email;
  $typeOfAccount = $obj->typeOfAccount;
  $password = $obj->password;

  // update the account based on the new information that was inputted by the user
  $query = "UPDATE Account SET firstName = '$firstName', lastName = '$lastName', address='$address', phoneNumber='$phoneNumber', email='$email', typeOfAccount='$typeOfAccount', password='$password' WHERE ownerIDKey = $ownerIDKey";
  $result = $mysqli->query($query);
  echo "Account Edited Sucessfully";
?>
