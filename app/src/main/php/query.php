<?php
 include( "constants.php");
 $username = Constants::USERNAME;
 echo "username is $username\n";

 $mysqli = new mysqli( Constants::HOST, $username, Constants::PASSWORD, Constants::DATABASE);

 $result = $mysqli->query("select * from Pets");

 while($row = $result->fetch_assoc())
 {
   echo $row['Name'], "\n";
 }

?>
