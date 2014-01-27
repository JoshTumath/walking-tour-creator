<!DOCTYPE HTML>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
		<title>Group 14 - Walking Tour</title>
		<link rel="stylesheet" type="text/css" href="css/style.css" />
		<link href="http://fonts.googleapis.com/css?family=Ubuntu:bold" rel="stylesheet" type="text/css">
		<link href="http://fonts.googleapis.com/css?family=Vollkorn" rel="stylesheet" type="text/css">

		<script type="text/javascript"
      			src="https://maps.googleapis.com/maps/api/js?key=AIzaSyBvBB7hlS-aPXieeaTMzZQMbCPtqCqJNz0
				&sensor=false">
		<!-- The script above loads the Google Maps API. The Key is the API key I was given and the sensor 
		and indicates whether this application uses a sensor (such as a GPS locator) to determine the users location. -->
   		</script>
	</head>	
	<body>
		<div id="header">
			<h1><a href="index.php">Walking Tour Creator</a></h1>
		</div>
		<div id="contents">
			<h1> Welcome to walking tour displayer</h1>
			<p> Please select a tour that you wish to view <p>
			<div id="list">
				<form method="post" action='walkingtournew.php'>
				<?php					
					$connection = mysql_connect('23.226.133.168','webWalk','123');
								mysql_select_db('walking_tour_database',$connection) or die("cannont connect");				

					$SQL = "SELECT * FROM listOfWalks";
					$result = mysql_query($SQL);
					echo "<ul class='walkItem'>";
					while ($db_field = mysql_fetch_assoc($result)) {
						echo "<li>";	
							echo "<img src='aber.jpeg' href='#' />";
							echo "<p>" . $db_field['title'] . "</p>";
							echo "<input name='walkID' value='" . $db_field["id"] ."' type='checkbox'>";
						echo "</li>";
					}
					echo "</ul>";
				?>	
					<input type="submit" name="submitWalk" value="Submit">
				</form>		
				<div id='clear'>
				</div>
			</div>
		</div>
	</body>

</html>

