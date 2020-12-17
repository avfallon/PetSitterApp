<?php
  // Assign JSON encoded string to a PHP variable
  include( "constants.php");
  $username = Constants::USERNAME;

  // sql credentials to sign into mysqli
  $mysqli = new mysqli( Constants::HOST, $username, Constants::PASSWORD, Constants::DATABASE);

  $json = $_GET['json'];

  // Decode JSON data to PHP object
  $obj = json_decode($json);
  // Access values from the returned object

  // make the job object from the json
  $jobID = $obj->jobID;
  $ownerIDKey = $obj->ownerIDKey;
  $petIDKey = $obj->petIDKey;
  $startDate = $obj->startDate;
  $endDate = $obj->endDate;
  $sleepover = $obj->sleepover;
  $jobDetails = $obj->jobDetails;
  $sitterIDKey = $obj->sitterIDKey;

  // delete the jobs from the database
  $query = "DELETE from Jobs WHERE jobID = $jobID";
  $result = $mysqli->query($query);
  echo "Job Deleted Sucessfully";
?>
