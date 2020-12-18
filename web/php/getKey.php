<?php

// get the userIDKey of the current user
$mysqli = new mysqli("cs-database.cs.loyola.edu", "damorales", "1762204",
    "damorales");

$email = $mysqli->real_escape_string($_POST['email']);

$query = "select * from Account where email = '$email'";
$result = $mysqli->query($query);

while($row = $result->fetch_assoc())
{
  $ownerIDKey = $user['OwnerIdKey'];
}

setcookie('userKey', $ownerIDKey, time() + (86400 * 30), "/");

echo 0;

?>
