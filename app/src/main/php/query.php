<?php
 include( "constants.php");
 $username = Constants::USERNAME;

 // credentials to connect to sqli
 $mysqli = new mysqli( Constants::HOST, $username, Constants::PASSWORD, Constants::DATABASE);

 $result = $mysqli->query("select * from Pets");

 // make an array of json pet objects
 $array = [];
 while($row = $result->fetch_assoc())
 {
  // assign the values into the json object
   array_push($array, [
     'ownerIDKey' => $row['OwnerIDKey'],
     'petIDKey' => $row['PetIDKey'],
     'name' => $row['Name'],
     'species' => $row['Species'],
     'size' => $row['Size'],
     'temperament' => $row['Temperament'],
     'breed' => $row['Breed'],
     'age' => $row['Age'],
     'diet' => $row['Diet'],
     'healthIssues' => $row['HealthIssues'],
     'extraInfo' => $row['ExtraInfo']
   ]);
 }

 //encode the array into json
 $result = json_encode($array);

 //reurn the array of pets in the database
 echo $result;

 ?>
