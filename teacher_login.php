<?php

require "init.php";

$email = $_POST["email"];
$password = $_POST["password"];

$sql = "select username, first_name from teachers where email like '".$email."' and password like '".$password."';";

$result = mysqli_query($con, $sql);
$response = array();

if(mysqli_num_rows($result) > 0) {
	$row = mysqli_fetch_row($result);
	$username = $row[0];
	$first_name = $row[1];
	$code = "login_success";
	array_push($response, array("code"=>$code, "first_name"=>$first_name, "username"=>$username));
	echo json_encode($response);
} else {
	$code = "login_failed";
	$message = "User not found. Please try again.";
	array_push($response, array("code"=>$code, "message"=>$message));
	echo json_encode($response);
}

mysqli_close($con);

?>
