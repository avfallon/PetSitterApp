<?php
  include( "constants.php");
  $username = Constants::USERNAME;

  // credentials to sign into the database
  $mysqli = new mysqli( Constants::HOST, $username, Constants::PASSWORD, Constants::DATABASE);

  $json = $_GET['json'];

  $obj = json_decode($json);

  // make the pet object
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

  // sql query to delete the pet from the databse
  $query = "DELETE from Pets WHERE petIDKey=$petIDKey";
  $result = $mysqli->query($query);
  echo "Pet Deleted Sucessfully";
?>
