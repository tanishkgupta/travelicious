<?php

require_once 'dbconfig.php';

class USER
{ 

 private $conn;
 
 public function __construct()
 {
  $database = new Database();
  $db = $database->dbConnection();
  $this->conn = $db;
    }
 
 public function runQuery($sql)
 {
  $stmt = $this->conn->prepare($sql);
  return $stmt;
 }
 
 public function lasdID()
 {
  $stmt = $this->conn->lastInsertId();
  return $stmt;
 }
 
 public function register($uname,$email,$upass,$code)
 {
  try
  {       
   $password = md5($upass);
   $stmt = $this->conn->prepare("INSERT INTO tbl_users(userName,userEmail,userPass,tokenCode) 
                                                VALUES(:user_name, :user_mail, :user_pass, :active_code)");
   $stmt->bindparam(":user_name",$uname);
   $stmt->bindparam(":user_mail",$email);
   $stmt->bindparam(":user_pass",$password);
   $stmt->bindparam(":active_code",$code);
   $stmt->execute(); 
   return $stmt;
  }
  catch(PDOException $ex)
  {
   echo $ex->getMessage();
  }
 }
 
 public function login($email,$upass)
 {
  try
  {
   $stmt = $this->conn->prepare("SELECT * FROM tbl_users WHERE userEmail=:email_id");
   $stmt->execute(array(":email_id"=>$email));
   $userRow=$stmt->fetch(PDO::FETCH_ASSOC);
   
   if($stmt->rowCount() == 1)
   {
    if($userRow['userStatus']=="Y")
    {
     if($userRow['userPass']==md5($upass))
     {
      $_SESSION['userSession'] = $userRow['userID'];
      return true;
     }
     else
     {
      header("Location: login.php?error");
      exit;
     }
    }
    else
    {
     header("Location: login.php?inactive");
     exit;
    } 
   }
   else
   {
    header("Location: login.php?error");
    exit;
   }  
  }
  catch(PDOException $ex)
  {
   echo $ex->getMessage();
  }
 }
 
 
 public function is_logged_in()
 {
  if(isset($_SESSION['userSession']))
  {
   return true;
  }
 }
 
 public function redirect($url)
 {
  header("Location: $url");
 }
 
 public function logout()
 {
  session_destroy();
  $_SESSION['userSession'] = false;
 }
 
 function send_mail($email,$message,$sbject)
 {      
  require("sendgrid-php.php");
  // require_once('phpmailer/class.phpmailer.php');
  // $mail = new PHPMailer();
  // $mail->IsSMTP(); 
  // $mail->SMTPDebug  = 0;                     
  // $mail->SMTPAuth   = true;                  
  // $mail->SMTPSecure = "ssl";                 
  // $mail->Host       = "smtp.gmail.com";      
  // $mail->Port       = 465;             
  // $mail->AddAddress($email);
  // $mail->Username="chawlaaditya8@gmail.com";  
  // $mail->Password="abcd@1234";            
  // $mail->SetFrom('you@yourdomain.com','Coding Cage');
  // $mail->AddReplyTo("you@yourdomain.com","Coding Cage");
  // $mail->Subject    = $subject;
  // $mail->MsgHTML($message);
  // $mail->Send();
 

$from = new SendGrid\Email(null, "admin@onebhk.com");
$subject = $sbject;
$to = new SendGrid\Email(null, $email);
$content = new SendGrid\Content("text/html", $message);
$mail = new SendGrid\Mail($from, $subject, $to, $content);

$apiKey = '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';
$sg = new \SendGrid($apiKey);

$response = $sg->client->mail()->send()->post($mail);
// echo $response->statusCode();
// echo $response->headers();
// echo $response->body();


 } 
}
