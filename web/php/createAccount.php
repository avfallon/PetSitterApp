<?php

// open my sqli connection
$mysqli = new mysqli("cs-database.cs.loyola.edu", "damorales", "1762204",
    "damorales");

// make account object
$firstName = $mysqli->real_escape_string($_POST['firstName']);
$lastName = $mysqli->real_escape_string($_POST['lastName']);
$email = $mysqli->real_escape_string($_POST['email']);
$address = $mysqli->real_escape_string($_POST['address']);
$phoneNumber = $mysqli->real_escape_string($_POST['phoneNumber']);
$account = $mysqli->real_escape_string($_POST['account']);
$password = $_POST['password'];
$confirmpassword = $_POST['confirmpassword'];

// check if the password recieved matches the confirm password
if ($password != $confirmpassword){
  echo 3;
}

// checking if email already exists in db
$query = "select * from Account where email = '$email'";
$result = $mysqli->query($query);

if ($result->num_rows > 0)
    echo 1;

// user does not yet exist in db
else
{
    $options = ['cost' => 12];
    $encrypted_password = password_hash($password, PASSWORD_BCRYPT, $options);
    $query = "insert into Account (firstName, lastName, address, phoneNumber, email, typeOfAccount, password) values('$firstName', '$lastName', '$address', '$phoneNumber', '$email', '$account', '$encrypted_password')";

    if ($mysqli->query($query) == 1)
    {
        echo 0;
        setcookie('email', $email, time() + (86400 * 30), "/");
        setcookie('account', $account, time() + (86400 * 30), "/");
    }
    else
        echo 2;
}

$mysqli->close();
