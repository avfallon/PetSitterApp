<?php

//import necessary files to send the email
use PHPMailer\PHPMailer\PHPMailer;
use PHPMailer\PHPMailer\SMTP;
use PHPMailer\PHPMailer\Exception;


require './PHPMailer/src/Exception.php';
require './PHPMailer/src/PHPMailer.php';
require './PHPMailer/src/SMTP.php';

// get the email from the app
$emailAddress = $_GET['email'];

// set subject and message
$subject = "New Pet Sitting Job!";
$message = "There is a new Pet Sitting Job available in your area! Grab it now before it's too late!";

//create phpmailer object
$mail = new PHPMailer(true);

//send the email
try
{
    # server settings
    $mail->SMTPDebug = SMTP::DEBUG_SERVER;
    $mail->isSMTP();
    $mail->Host = "smtp.gmail.com";
    $mail->SMTPAuth = true;
    $mail->Username = "danj.pet.sitter@gmail.com";
    $mail->Password = "herve1234";
    $mail->SMTPSecure = PHPMailer::ENCRYPTION_STARTTLS;
    $mail->Port = 587;

    # recipient settings
    $mail->setFrom("danj.pet.sitter@gmail.com", "DANJ");
    $mail->addAddress($emailAddress);
    $mail->addReplyTo("danj.pet.sitter@gmail.com");
    $mail->Subject = $subject;
    $mail->Body = $message;
    $mail->send();
    $mail->smtpClose();
} catch (Exception $e)
{

}

?>
