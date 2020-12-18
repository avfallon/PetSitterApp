<?php

// open mysqli connection
$mysqli = new mysqli("cs-database.cs.loyola.edu", "damorales", "1762204",
    "damorales");

// create job object
$startDate = $mysqli->real_escape_string($_POST['startDate']);
$endDate = $mysqli->real_escape_string($_POST['endDate']);
$jobDetails = $mysqli->real_escape_string($_POST['jobDetails']);
$sleepover = $mysqli->real_escape_string($_POST['sleepover']);
$petIDKey = $mysqli->real_escape_string($_POST['petIDKey']);
$ownerIDKey = $_POST['ownerIDKey'];

// query the database to add job object
$query = "insert into Jobs (ownerIDKey, petIDKey, startDate, endDate, sleepover, jobDetails, sitterIDKey) values('$ownerIDKey', '$petIDKey', '$startDate', '$endDate', '$sleepover', '$jobDetails', '0')";
$result = $mysqli->query($query);

echo 0;


?>
