<!DOCTYPE HTML>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
		<title>Group 14 - Walking Tour</title>
		<link rel="stylesheet" type="text/css" href="css/style.css" />
		<script type="text/javascript"
      			src="https://maps.googleapis.com/maps/api/js?key=AIzaSyBvBB7hlS-aPXieeaTMzZQMbCPtqCqJNz0
				&sensor=false">
		<!-- The script above loads the Google Maps API. The Key is the API key I was given and the sensor 
		and indicates whether this application uses a sensor (such as a GPS locator) to determine the users location. -->
   		</script>
	</head>	
	<body>
		<div id="wrapper">
			<div id="header">
				<div id="headerLeft">
						<h1><a href="index.php">Walking Tour Creator</a></h1>
				</div>
			</div>
			<div id="content">
			<form method="post" action='walkingtournew.php'>
			<?php					
				$connection = mysql_connect('23.226.133.168','webWalk','123');
                        	mysql_select_db('walking_tour_database',$connection) or die("cannont connect");				

				$SQL = "SELECT * FROM listOfWalks";
		   	 	$result = mysql_query($SQL);
			    	echo "<ul class='walkItem'>";
				while ($db_field = mysql_fetch_assoc($result)) {
					echo "<li>";					
						echo "<p>" . $db_field['title'] . "</p>";
						echo "<input name='walkID' value='" . $db_field["id"] ."' type='checkbox'>";
					echo "</li>";
				}
				echo "</ul>";
			?>	
				<input type="submit" name="submitWalk" value="Submit">
			</form>		
			</div>

			<div id="googleMaps">
				<script type="text/javascript">
					function initialize() {
					  var mapOptions = {
					    zoom: 3,
					    center: new google.maps.LatLng(-34.397, 150.644)
					  };

					  var map = new google.maps.Map(document.getElementById('googleMaps'),
					      mapOptions);
					}

					window.onload = initialize;

				</script>
			</div>
			<div id="footer">
				<p>Footer...</p>
			</div>
		</div>
	</body>

</html>

