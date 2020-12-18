<?php
 include( "constants.php");
 $username = Constants::USERNAME;

 // open mysqli connection
 $mysqli = new mysqli( Constants::HOST, $username, Constants::PASSWORD, Constants::DATABASE);

 $ownerIDKey = $_POST['ownerIDKey'];

 // get all the jobs that the owner has posted
 $result = $mysqli->query("select * from Jobs where ownerIDKey= '$ownerIDKey'");

 $yesOrNo = "";

 // creat an array of json job objects of all the postings the user has made
 $array = [];
 while($row = $result->fetch_assoc())
 {

   $petIDKey = $row['petIDKey'];
   $sitterIDKey = $row['sitterIDKey'];
   if($row['sleepover'] == "1"){
     $yesOrNo = "Yes";
   }
   else{
     $yesOrNo = "No";
   }

   $petQ = $mysqli->query("select * from Pets where petIDKey= '$petIDKey'");
   while($pet = $petQ->fetch_assoc())
   {
     if($sitterIDKey == 0){
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
         'sitterIDKey' => "Listed..."
       ]);
     }
     else{
       $sitterQ = $mysqli->query("select * from Account where OwnerIDKey= '$sitterIDKey'");
       while($sitter = $sitterQ->fetch_assoc())
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
           'sitterIDKey' => $sitter['firstName']
         ]);
       }
     }
   }
 }



 $result = json_encode($array);

 echo $result;

 ?>
