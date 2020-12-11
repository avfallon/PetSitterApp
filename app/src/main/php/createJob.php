<?php
  // Assign JSON encoded string to a PHP variable
  include( "constants.php");
  $username = Constants::USERNAME;

  $mysqli = new mysqli( Constants::HOST, $username, Constants::PASSWORD, Constants::DATABASE);

  $json = $_GET['json'];
  //$json = '{"ownerIDKey":"4","petIDKey":"5","name":"Jeff","species":"Unknown","size":"Small","temperament":"Lazy","breed":"german shep","age":"21","diet":"Peanut Butter","healthIssues":"Needs Glasses","extraInfo":"Extras"}';
  //$json = '{"ownerIDKey":"1","firstName":"Derek","lastName":"Morales","address":"123 New York Ave, New York, New York 01234","phoneNumber":"1233456789","email":"derek@derek.com","typeOfAccount":"1","password":"password"}';

  // Decode JSON data to PHP object
  $obj = json_decode($json);
  // Access values from the returned object

  $jobID = $obj->jobID;
  $ownerIDKey = $obj->ownerIDKey;
  $petIDKey = $obj->petIDKey;
  $startDate = $obj->startDate;
  $endDate = $obj->endDate;
  $sleepover = $obj->sleepover;
  $jobDetails = $obj->jobDetails;
  $sitterIDKey = $obj->sitterIDKey;

  $query = "insert into Jobs (ownerIDKey, petIDKey, startDate, endDate, sleepover, jobDetails, sitterIDKey) values('$ownerIDKey', '$petIDKey', '$startDate', '$endDate', '$sleepover', '$jobDetails', '$sitterIDKey')";
  $result = $mysqli->query($query);
  echo "Job Created Sucessfully";
?>
