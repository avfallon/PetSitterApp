<?php

// open mysqli connection
$mysqli = new mysqli("cs-database.cs.loyola.edu", "damorales", "1762204",
    "damorales");

// make the pet object
$ownerIDKey = $_POST['ownerIDKey'];
$name = $_POST['name'];
$species = $_POST['species'];
$size = $_POST['size'];
$temper = $_POST['temper'];
$breed = $_POST['breed'];
$age = $_POST['age'];
$diet = $_POST['diet'];
$health = $_POST['health'];
$extra = $_POST['extra'];

// query the database to add a pet
$query = "insert into Pets (ownerIDKey, name, species, size, temperament, breed, age, diet, healthIssues, extraInfo) values('$ownerIDKey', '$name', '$species', '$size', '$temper', '$breed', '$age', '$diet' , '$health', '$extra')";
$result = $mysqli->query($query);
echo 0;

?>
