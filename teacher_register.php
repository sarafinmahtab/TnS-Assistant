<?php

require "init.php";

$username = $_POST["username"];
$first_name = $_POST["first_name"];
$last_name = $_POST["last_name"];
$email = $_POST["email"];
$phone = $_POST["phone"];
$password = $_POST["password"];

$sql = "select * from teachers where username like '".$username."' and email like '".$email."';";

$result = mysqli_query($con, $sql);
$response = array();

if(mysqli_num_rows($result) > 0) {
	$code = "reg_failed";
	$message = "User already exists!!";
	array_push($response, array("code"=>$code, "message"=>$message));
	echo json_encode($response);
} else {
	$sql = "insert into teachers values('".$username."', '".$first_name."', '".$last_name."', '".$email."', '".$phone."', '".$password."');";
	$result = mysqli_query($con, $sql);
	$code = "reg_success";
	$message = "Thanks for your registratrion. Now you can login.";
	array_push($response, array("code"=>$code, "message"=>$message));
	echo json_encode($response);
}

mysqli_close($con);

?>
