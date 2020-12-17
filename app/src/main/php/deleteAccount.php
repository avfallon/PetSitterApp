<?php
  // Assign JSON encoded string to a PHP variable
  include( "constants.php");
  $username = Constants::USERNAME;

  // credentials to sign into the databse
  $mysqli = new mysqli( Constants::HOST, $username, Constants::PASSWORD, Constants::DATABASE);

  $json = $_GET['json'];

  // Decode JSON data to PHP object
  $obj = json_decode($json);
  // Access values from the returned object

  // get the account and make the values from the json
  $ownerIDKey = $obj->ownerIDKey;
  $firstName = $obj->firstName;
  $lastName = $obj->lastName;
  $address = $obj->address;
  $phoneNumber = $obj->phoneNumber;
  $email = $obj->email;
  $typeOfAccount = $obj->typeOfAccount;
  $password = $obj->password;

  // sql qeury to delete the account from the database
  $query = "DELETE from Account WHERE ownerIDKey = $ownerIDKey";
  $result = $mysqli->query($query);
  echo "Account Deleted Sucessfully";
?>
