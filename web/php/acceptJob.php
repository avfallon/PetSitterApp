<?php
 include( "constants.php");
 $username = Constants::USERNAME;

 // open an mysqli connection
 $mysqli = new mysqli( Constants::HOST, $username, Constants::PASSWORD, Constants::DATABASE);

 //get the passed data
 $sitterIDKey = $_POST['sitterIDKey'];
 $jobID = $_POST['jobID'];

 // query the database to update the job to have a sitter
 $query = "UPDATE Jobs SET sitterIDKey = '$sitterIDKey' WHERE jobID = '$jobID'";
 $result = $mysqli->query($query);

 echo 0;

?>
