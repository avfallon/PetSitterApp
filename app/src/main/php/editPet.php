<?php
  include( "constants.php");
  $username = Constants::USERNAME;

  $mysqli = new mysqli( Constants::HOST, $username, Constants::PASSWORD, Constants::DATABASE);

  $json = $_GET['json'];
  //$json = '{"ownerIDKey":"4","petIDKey":"5","name":"Derek","species":"Werewolf","size":"Small","temperament":"Lazy","breed":"german shep","age":"21","diet":"Mcdonalds Butter","healthIssues":"Needs Glasses","extraInfo":"Extras"}';

  $obj = json_decode($json);
  // Access values from the returned object

  // $onwerIDKey = $obj->ownerIDKey
  // $firstName = $obj->firstName
  // $lastName = $obj->lastName
  // $address = $obj->address
  // $phoneNumber = $obj->phoneNumber
  // $email = $obj->email
  // $typeOfAccout = $obj->typeOfAccount
  // $password = $obj->password

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

  $query = "UPDATE Pets SET ownerIDKey = '$ownerIDKey', name = '$name', species = '$species', size='$size', temperament='$temperament', breed='$breed', age='$age', diet='$diet', healthIssues='$healthIssues', extraInfo='$extraInfo' WHERE petIDKey = $petIDKey";
  $result = $mysqli->query($query);
  echo "Pet Edited Sucessfully";
?>
