<?php


// open mysqli connection
$mysqli = new mysqli("cs-database.cs.loyola.edu", "damorales", "1762204",
    "damorales");

// create account object
$ownerIDKey = $_POST['ownerIDKey'];
$firstName = $mysqli->real_escape_string($_POST['firstName']);
$lastName = $mysqli->real_escape_string($_POST['lastName']);
$email = $mysqli->real_escape_string($_POST['email']);
$address = $mysqli->real_escape_string($_POST['address']);
$phoneNumber = $mysqli->real_escape_string($_POST['phoneNumber']);
$account = $mysqli->real_escape_string($_POST['account']);
$password = $_POST['password'];
$confirmpassword = $_POST['confirmpassword'];

if ($password != $confirmpassword){
  echo 3;
}

// edit the user account in the database through sql query
//else
//{
    $options = ['cost' => 12];
    $encrypted_password = password_hash($password, PASSWORD_BCRYPT, $options);
    $query = "UPDATE Account SET firstName = '$firstName', lastName = '$lastName', address='$address', phoneNumber='$phoneNumber', email='$email', typeOfAccount='$account', password='$encrypted_password' WHERE ownerIDKey = '$ownerIDKey'";

    if ($mysqli->query($query) == 1)
    {
        echo 0;
        setcookie('email', $email, time() + (86400 * 30), "/");
        setcookie('account', $account, time() + (86400 * 30), "/");

    }
    else
        echo 2;

    echo 0;
//}

$mysqli->close();
