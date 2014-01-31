<?php
$TEST_JSON = file_get_contents("log/test_json.txt");
//Recieves data from java application and decodes it into php readable form

if ( !isset($_POST["tourdata"])) {
	if ($TEST_JSON) {
		$_POST["tourdata"] = $TEST_JSON;
	} else {
		header("HTTP/1.1 418 I'm a teapot");
		exit;
	}
}
 
$json_data = $_POST["tourdata"];
$walk_data = json_decode($json_data, true);

$LOGFILE_NAME = date("U");
file_put_contents("log/" . $LOGFILE_NAME . ".txt" , $json_data);

// Flags for successfull querys to allow transaction to work
$q1r = TRUE;
$q2r = TRUE;
$q3r = TRUE;
//Creates variables for the listOfWalks database table from json data
$walk_name 	= $walk_data["data"]["walkname"];
$short_desc = $walk_data["data"]['shortdescription'];
$long_desc	= $walk_data["data"]['longdescription'];
$time 		= $walk_data["data"]['time'];

echo $walk_data["data"]["walkname"];
echo $walk_data["data"]['shortdescription'];
echo $walk_data["data"]['longdescription'];
echo $walk_data["data"]['time'];
$db = new PDO("mysql:host=jakemaguire.co.uk;dbname=walking_tour_database", "webWalk", "123");
$db->beginTransaction();


// Prepared statement to insert a walk from parsed JSON data
$insert_walk_query = $db->prepare("INSERT INTO listOfWalks (title, shortDesc, longDesc, time) VALUES (:walkname, :shortdesc, :longdesc, :time)");
$insert_walk_query->bindParam(":walkname", 	$walk_name);
$insert_walk_query->bindParam(":shortdesc", $short_desc);
$insert_walk_query->bindParam(":longdesc", 	$long_desc);
$insert_walk_query->bindParam(":time", 		$time);

$insert_location_query = $db->prepare("INSERT INTO location (walkID, latitude, longitude, poi, timestamp) VALUES (:walkid, :latitude, :longitude, :poiflag, :time)");
$insert_location_query->bindParam(":walkid", 	 $walk_id);
$insert_location_query->bindParam(":latitude", 	 $latitude);
$insert_location_query->bindParam(":longitude",  $longitude);
$insert_location_query->bindParam("poiflag", 	 $poi_flag);
$insert_location_query->bindParam(":time",		 $timestamp);

$insert_poi_query = $db->prepare("INSERT INTO placeDescription (locationID, title, description) VALUES (:locationid, :locationname, :description)");
$insert_poi_query->bindParam(":locationid", 	$location_id);
$insert_poi_query->bindParam(":locationname", 	$location_name);
$insert_poi_query->bindParam(":description",	$description);

$insert_photo_query = $db->prepare("INSERT INTO photoUsage (placeID, photoName) VALUES (:placeid, :photoname)");
$insert_photo_query->bindParam(":placeid",		$place_id);
$insert_photo_query->bindParam(":photoname", 	$photo_name);

$q1r = $insert_walk_query->execute();
$walk_id = $db->lastInsertId(); //selects last id of added walk using the auto increment option in the database
$number_of_points = count($walk_data["points"]); //counts the number of 'points'(locations on walk)


//for loop assigns variables to the json data
for ($x = 1; $x <= $number_of_points; $x++) {
	$latitude	= $walk_data["points"][$x]["latitude"];
	$longitude	= $walk_data["points"][$x]["longitude"];
	$timestamp	= $walk_data["points"][$x]["timestamp"];
	$poi_flag	= $walk_data["points"][$x]["poiflag"];
	
	if($poi_flag){ //if the point is a point of interest then assign other variables to it
		$location_name	= $walk_data["points"][$x]["poidata"]["locationname"];
		$description	= $walk_data["points"][$x]["poidata"]["description"];
		$image_data 	= $walk_data["points"][$x]["poidata"]["photos"]; // CHANGE TO PHOTO IF USING ONE PHOTO SOLUTION
	}
	
	$q2r = $insert_location_query->execute();
	$location_id = $db->lastInsertId();
	
	if($poi_flag){
		$q3r = $insert_poi_query->execute();
		$place_id = $db->lastInsertId();
		
		
		$number_of_images = count($image_data);
		
		for ($y = 0; $y <= $number_of_images; $y++) {
			$image_data_replaced = strtr($image_data[$y], '-_', '+/');
			$image = base64_decode($image_data_replaced);
			$photo_name = md5($image);
			file_put_contents("images/walkimages/{$photo_name}.jpeg", $image); //name of saved png image becomes md5 key
			
			$insert_photo_query->execute();
		}
		
		// if ($image_data != null) {
			// $image_data_replaced = strtr($image_data, '-_', '+/');
			// //$image_data_replaced_again = str_pad($image_data_replaced, strlen($image_data_replaced) % 4, '=', STR_PAD_RIGHT);
			// $image = base64_decode($image_data_replaced);
			// $photo_name = md5($image);
			// file_put_contents("images/walkimages/{$photo_name}.jpeg", $image); //name of saved png image becomes md5 key
			
			// $insert_photo_query->execute();
		// }
	}
}

if ($q1r && $q2r && $q3r) {
	echo "Success";
	$db->commit();
} else {
	
	echo "Failure - {$q1r} | {$q2r} | {$q3r}";		
	$db->rollBack();
}

?>
