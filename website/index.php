<?//php include_once("includes/database.php"); ?>
<!DOCTYPE HTML>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
		<title>Group 14 - Walking Tour</title>
		<link rel="stylesheet" type="text/css" href="css/style.css" />
		<script type="text/javascript"
      			src="https://maps.googleapis.com/maps/api/js?key=AIzaSyBvBB7hlS-aPXieeaTMzZQMbCPtqCqJNz0
				&sensor=false">
		<!-- The script above loads the Google Maps API. The Key is the API key I was given and the sensor and indicates whether this application 				uses a sensor (such as a GPS locator) to determine the user's location. -->
    		</script>
	</head>	
	<body>
		<div id="wrapper">
			<div id="header">
				<div id="headerLeft">
						<h1><a href="index.php">Walking Tour Creator</a></h1>
				</div>
				<div id="headerRight">
						<input class="searchbox" type="text">
				</div>
			</div>
			
			<div id="content">
				<?php 
					/* connect to the db */
					$connection = mysql_connect('23.226.133.168','webWalk','123');
					mysql_select_db('walking_tour_database',$connection) or die("cannont connect to database");
				
					$SQL = "SELECT * FROM listOfWalks";
					$result2 = mysql_query($SQL);

					while ( $db_field = mysql_fetch_assoc($result2) ) {

						$resultsArray [] = array(
						'field1' => $db_field["id"],
						'field2' => $db_field["title"],
						'field3' => $db_field["shortDesc"]);

						print $db_field['id'] . "<br>";
						print $db_field['title'] . "<br>";
						print $db_field['shortDesc'] . "<br>";
						print $db_field['longDesc'] . "<br>";
						print $db_field['hours'] . "<br>";
						print $db_field['distance'] . "<br>";
					}
				?>
				
				<p>	<span><b>Distance:</b> <?php echo $resultsArray['field1'] ?></span>
					<span><b>Number of Locations:</b> 5</span>
					<span><b>Short Description:</b> Lorem ipsum dolor sit amet, consectetuer adipiscing elit. 
							Aenean commodo ligula eget dolor. Aenean m</span>
					<span><b>Long Description: </b>Lorem ipsum dolor sit amet, consectetuer adipiscing elit. Aenean commodo ligula eget 							dolor. Aenean massa. Cum sociis natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus. 							Donec quam felis, ultricies nec, pellentesque eu, pretium quis, sem. Nulla consequat massa quis enim. Donec 							pede justo, fringilla vel, aliquet nec, vulputate eget, arcu. In enim justo, rhoncus ut, imperdiet a, 							venenatis vitae, justo. Nullam dictum felis eu pede mollis pretium. Integer tincidunt. Cras dapibus. 							Vivamus elementum semper nisi. Aenean vulputate eleifend tellus. Aenean leo ligula, porttitor eu, consequat 							vitae, eleifend ac, enim. Aliquam lorem ante, dapibus in, viverra quis, feugiat a, tellus. Phasellus 							viverra nulla ut metus varius laoreet. Quisque rutrum. Aenean imperdiet. Etiam ultricies nisi vel 							augue. Curabitur ullamcorper ultricies nisi. Nam eget dui. Etiam rhoncus. Maecenas tempus, tellus eget 							condimentum rhoncus, sem quam semper libero, sit amet adipiscing sem neque sed ipsum. N</span>
				</p>
			</div>

			<div id="googleMaps">
				<script>
					function initialize() { //Starts the function
						var mapOptions = {
							zoom: 14, //Sets how far the map is zoomed in (0 = view of the earth)
							center: new google.maps.LatLng(52.41612, -4.083798) //Sets the starting location for the map to look at
						};

						var map = new google.maps.Map(document.getElementById('googleMaps'), //Specifies the div container for the map (e.g, googleMaps)
						mapOptions);
						}
					window.onload = initialize();
				</script>
			</div>
			
			<div id="footer">
				<p>Footer...</p>
			</div>
		</div>
	</body>

</html>

