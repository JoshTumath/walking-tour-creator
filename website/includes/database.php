<?php

$username = "webWalk";
$password = "123";
$hostname = "23.226.133.168";
$db = "walking_tour_database";
$error = 'Sorry we are experiencing connection issues, this should be fixed shortly'; 

//Connection to the database
$dbhandle = mysql_connect($hostname, $username, $password, $db) 
  or die($error);
?>

