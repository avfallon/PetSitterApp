<?php
  // Assign JSON encoded string to a PHP variable
  include( "constants.php");
  $username = Constants::USERNAME;

  $mysqli = new mysqli( Constants::HOST, $username, Constants::PASSWORD, Constants::DATABASE);

  $json = $_GET['json'];
  //$json = '{"ownerIDKey":"4","petIDKey":"5","name":"Jeff","species":"Unknown","size":"Small","temperament":"Lazy","breed":"german shep","age":"21","diet":"Peanut Butter","healthIssues":"Needs Glasses","extraInfo":"Extras"}';
  //$json = '{"ownerIDKey":"1","firstName":"Julio","lastName":"Morales","address":"123 New York Ave, New York, New York 01234","phoneNumber":"1233456789","email":"derek@derek.com","typeOfAccount":"1","password":"password"}';

  // // Decode JSON data to PHP associative array
  // $arr = json_decode($json, true);
  // // Access values from the associative array
  // echo $arr["Peter"];  // Output: 65
  // echo $arr["Harry"];  // Output: 80
  // echo $arr["John"];   // Output: 78
  // echo $arr["Clark"];  // Output: 90

  // Decode JSON data to PHP object
  $obj = json_decode($json);
  // Access values from the returned object

  $ownerIDKey = $obj->ownerIDKey;
  $firstName = $obj->firstName;
  $lastName = $obj->lastName;
  $address = $obj->address;
  $phoneNumber = $obj->phoneNumber;
  $email = $obj->email;
  $typeOfAccount = $obj->typeOfAccount;
  $password = $obj->password;

  // $ownerIDKey = $obj->ownerIDKey;
  // echo $ownerIDKey;
  // $petIDKey = $obj->petIDKey;
  // echo $petIDKey;
  // $name = $obj->name;
  // echo $name;
  // $species = $obj->species;
  // echo $species;
  // $size = $obj->size;
  // echo $size;
  // $temperament = $obj->temperament;
  // echo $temperament;
  // $breed = $obj->breed;
  // echo $breed;
  // $age = $obj->age;
  // echo $age;
  // $diet = $obj->diet;
  // echo $diet;
  // $healthIssues = $obj->healthIssues;
  // echo $healthIssues;
  // $extraInfo = $obj->extraInfo;
  // echo $extraInfo;

  $query = "DELETE from Account WHERE ownerIDKey = $ownerIDKey";
  $result = $mysqli->query($query);
  echo "Account Deleted Sucessfully";
?>
