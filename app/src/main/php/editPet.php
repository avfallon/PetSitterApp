<?php
  include( "constants.php");
  $username = Constants::USERNAME;

  // credentials to sign into the sqli databse
  $mysqli = new mysqli( Constants::HOST, $username, Constants::PASSWORD, Constants::DATABASE);

  $json = $_GET['json'];

  $obj = json_decode($json);
  // Access values from the returned object


  // make the pet object from the given json
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

  // update the pet based on the information given by the user
  $query = "UPDATE Pets SET ownerIDKey = '$ownerIDKey', name = '$name', species = '$species', size='$size', temperament='$temperament', breed='$breed', age='$age', diet='$diet', healthIssues='$healthIssues', extraInfo='$extraInfo' WHERE petIDKey = $petIDKey";
  $result = $mysqli->query($query);
  echo "Pet Edited Sucessfully";
?>
