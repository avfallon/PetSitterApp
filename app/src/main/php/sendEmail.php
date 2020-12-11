<?php

use PHPMailer\PHPMailer\PHPMailer;
use PHPMailer\PHPMailer\SMTP;
use PHPMailer\PHPMailer\Exception;


require './PHPMailer/src/Exception.php';
require './PHPMailer/src/PHPMailer.php';
require './PHPMailer/src/SMTP.php';

$emailAddress = $_GET['email'];

$subject = "New Pet Sitting Job!";
$message = "There is a new Pet Sitting Job available in your area! Grab it now before it's too late!";

$mail = new PHPMailer(true);

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
