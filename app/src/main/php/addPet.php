<?php
  // Assign JSON encoded string to a PHP variable
  include( "constants.php");
  $username = Constants::USERNAME;

  // connect to database using credentials
  $mysqli = new mysqli( Constants::HOST, $username, Constants::PASSWORD, Constants::DATABASE);

  $json = $_GET['json'];

  // Decode JSON data to PHP object
  $obj = json_decode($json);
  // Access values from the returned object


  // get each field from the json to create pet
  $ownerIDKey = $obj->ownerIDKey;
  echo $ownerIDKey;
  $petIDKey = $obj->petIDKey;
  echo $petIDKey;
  $name = $obj->name;
  echo $name;
  $species = $obj->species;
  echo $species;
  $size = $obj->size;
  echo $size;
  $temperament = $obj->temperament;
  echo $temperament;
  $breed = $obj->breed;
  echo $breed;
  $age = $obj->age;
  echo $age;
  $diet = $obj->diet;
  echo $diet;
  $healthIssues = $obj->healthIssues;
  echo $healthIssues;
  $extraInfo = $obj->extraInfo;
  echo $extraInfo;

  // sql query to the database to create the pet
  $query = "insert into Pets (ownerIDKey, name, species, size, temperament, breed, age, diet, healthIssues, extraInfo) values('$ownerIDKey', '$name', '$species', '$size', '$temperament', '$breed', '$age', '$diet' , '$healthIssues', '$extraInfo')";

  // run the query
  $result = $mysqli->query($query);
  echo "Pet Added Sucessfully";
?>
