<?php
  // Assign JSON encoded string to a PHP variable
  include( "constants.php");
  $username = Constants::USERNAME;

  // credentials to sign into the database
  $mysqli = new mysqli( Constants::HOST, $username, Constants::PASSWORD, Constants::DATABASE);

  $json = $_GET['json'];

  // Decode JSON data to PHP object
  $obj = json_decode($json);
  // Access values from the returned object

  // make the job object
  $jobID = $obj->jobID;
  $ownerIDKey = $obj->ownerIDKey;
  $petIDKey = $obj->petIDKey;
  $startDate = $obj->startDate;
  $endDate = $obj->endDate;
  $sleepover = $obj->sleepover;
  $jobDetails = $obj->jobDetails;
  $sitterIDKey = $obj->sitterIDKey;

  // sql query to make the job and inset it into the database
  $query = "insert into Jobs (ownerIDKey, petIDKey, startDate, endDate, sleepover, jobDetails, sitterIDKey) values('$ownerIDKey', '$petIDKey', '$startDate', '$endDate', '$sleepover', '$jobDetails', '$sitterIDKey')";
  $result = $mysqli->query($query);
  echo "Job Created Sucessfully";
?>
