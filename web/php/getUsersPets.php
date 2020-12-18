<?php
 include( "constants.php");
 $username = Constants::USERNAME;

 // open mysqli connection
 $mysqli = new mysqli( Constants::HOST, $username, Constants::PASSWORD, Constants::DATABASE);

 $ownerIDKey = $_POST['ownerIDKey'];
 // testing with hard coding
 //$ownerIDKey = 6;
 //echo $ownerIDKey;

 // get all the current users pets if they have any
 $result = $mysqli->query("select * from Pets where ownerIDKey= '$ownerIDKey'");

 $array = [];
 while($row = $result->fetch_assoc())
 {
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

 $result = json_encode($array);

 echo $result;

 ?>
