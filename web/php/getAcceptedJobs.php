<?php
 include( "constants.php");
 $username = Constants::USERNAME;

 // open mysqli connection
 $mysqli = new mysqli( Constants::HOST, $username, Constants::PASSWORD, Constants::DATABASE);

 $ownerIDKey = $_POST['ownerIDKey'];

// get the jobs the user is currently accepted
 $result = $mysqli->query("select * from Jobs where sitterIDKey= '$ownerIDKey'");

 $yesOrNo = "";

 // create entire job object
 $array = [];
 while($row = $result->fetch_assoc())
 {

   $petIDKey = $row['petIDKey'];
   $ownIDKey = $row['ownerIDKey'];
   if($row['sleepover'] == "1"){
     $yesOrNo = "Yes";
   }
   else{
     $yesOrNo = "No";
   }

   // get the pets the user is accepting to fill the info
   $petQ = $mysqli->query("select * from Pets where petIDKey= '$petIDKey'");
   while($pet = $petQ->fetch_assoc())
   {

     //else{
       $ownerQ = $mysqli->query("select * from Account where OwnerIDKey= '$ownIDKey'");
       while($owner = $ownerQ->fetch_assoc())
       {
         array_push($array, [
           'ownerIDKey' => $pet['OwnerIDKey'],
           'petIDKey' => $pet['PetIDKey'],
           'name' => $pet['Name'],
           'species' => $pet['Species'],
           'size' => $pet['Size'],
           'temperament' => $pet['Temperament'],
           'breed' => $pet['Breed'],
           'age' => $pet['Age'],
           'diet' => $pet['Diet'],
           'healthIssues' => $pet['HealthIssues'],
           'extraInfo' => $pet['ExtraInfo'],
           'startDate' => $row['startDate'],
           'endDate' => $row['endDate'],
           'sleepover' => $yesOrNo,
           'jobDetails' => $row['jobDetails'],
           'ownIDKey' => $owner['email']
         ]);
       }
     //}
   }
 }



 $result = json_encode($array);

 echo $result;

 ?>
