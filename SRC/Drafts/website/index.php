<?php require_once "includes/functions.php"; ?>

<?php insert_header(); ?>
			<h1>Welcome to walking tour displayer</h1>
			<p>Please select a tour that you wish to view <p>
			<div id="list">
				<?php
					$connection = mysql_connect('23.226.133.168','webWalk','123');
								mysql_select_db('walking_tour_database',$connection) or die("cannot connect");				

					$SQL = "SELECT * FROM listOfWalks";
					$result = mysql_query($SQL);
					echo "<ul class='walkItem'>";
					while ($db_field = mysql_fetch_assoc($result)) {
						echo "<li>";
              echo "<a href='walkingtournew.php?id=" . $db_field["id"] . "'>";
                echo "<img src='aber.jpeg' alt='' />";
                echo "<p>$db_field['title']</p>";
              echo "</a>";
						echo "</li>";
					}
					echo "</ul>";
				?>
<?php insert_footer('
<script src="https://maps.googleapis.com/maps/api/js?key=AIzaSyBvBB7hlS-aPXieeaTMzZQMbCPtqCqJNz0&sensor=false">
// The script above loads the Google Maps API. The Key is the API key I was
// given and the sensor and indicates whether this application uses a sensor
// (such as a GPS locator) to determine the users location.
</script>
'); ?>