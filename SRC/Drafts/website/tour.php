<?php require_once "includes/functions.php"; ?>

<?php
// TODO: What happens if ID is not set?
//if (isset($_GET['id'])) {
  $walkID = $_GET['id'];
//}

//---------- Connect to Database ----------//			
$connection = mysql_connect('jakemaguire.co.uk','webWalk','123');
mysql_select_db('walking_tour_database', $connection) or die("cannot connect to database");

//---------- Select Walk Information Based on Walk ID ----------//
$SQL = "SELECT * FROM listOfWalks WHERE id = " . $walkID;
$result = mysql_query($SQL);

while ($db_field = mysql_fetch_assoc($result)) {
  $title = $db_field['title'];
  $shortDesc = $db_field['shortDesc'];
  $longDesc = $db_field['longDesc'];
  //$distance = $db_field['distance'];
  $minutes = floor($db_field['time'] / 60);
  $seconds = $db_field['time'] % 60;
}

//---------- Get Markers Query ----------//
$SQL = ("SELECT placeDescription.description, location.latitude, location.longitude, placeDescription.title FROM placeDescription, location WHERE placeDescription.locationID = location.id AND location.walkID = " . $walkID) or mysql_error();
$result = mysql_query($SQL);
while ($row = mysql_fetch_assoc($result)) {
  $markers[] = '["' . $row['title'] . '",' . $row['latitude'] . ',' . $row['longitude'] . ', "' . $row['description'] . '"]';
}

//---------- Get Lines Query ----------//
$SQL = ("SELECT latitude,longitude FROM location WHERE walkID = " .$walkID. " ORDER BY location.timestamp") or mysql_error();
$result = mysql_query($SQL);
while ($row = mysql_fetch_assoc($result)) {
  $coordinates[] = 'new google.maps.LatLng(' . $row['latitude'] . ', ' . $row['longitude'] . ')';
}
?>

<?php insert_header("tour"); ?>
<aside>
	<h1><span><b><?php echo $title;?></b></span></h1>
	<p><span><b>Short Description: </b><?php echo $shortDesc;?></span></p>
	<p><span><b>Long Description: </b><?php echo $longDesc;?></span></p>
	<p><span><b>Distance: </b><span id="distance"></span> miles</span></p>
	<p><span><b>Hours: </b><?php echo $minutes;?></span> minutes and <?php echo $seconds;?> seconds</p>
</aside>

<?php insert_footer_top(); ?>

<div id="map">
	<script>
		var infowindow = null;
		function initialize() {
			var centerMap = new google.maps.LatLng(52.4151, -4.08352);
			var myOptions = {
			zoom: 13,
			center: centerMap,
			mapTypeId: google.maps.MapTypeId.ROADMAP
			}

			var map = new google.maps.Map(document.getElementById("map"), myOptions);
			map.setTilt(45);
			var sites = [
				<?php echo implode(',', $markers) ;?>
			];
			var coordinates = [
				<?php echo implode(',', $coordinates) ?>
			];
			
			setMarkers(map, sites);
			
			infowindow = new google.maps.InfoWindow({
				content: "loading..."
			});
				
			var lineBetweenPoints = new google.maps.Polyline({
				path: coordinates,
				geodesic: true,
				strokeColor: '#FF0000',
				strokeOpacity: 1.0,
				strokeWeight: 2
			});
			lineBetweenPoints.setMap(map);
			
			var boundsListener = google.maps.event.addListener((map), 'bounds_changed', function(event) {
				this.setZoom(14);
				google.maps.event.removeListener(boundsListener);
			});
			var walkDistance = calcDistance(coordinates).toFixed(3);
			document.getElementById("distance").innerHTML = walkDistance;
			
			function calcDistance(coords){
				var distance = 0;
				for (var i = 0; i < coords.length - 1; i++) {
					p1 = coords[i];
					p2 = coords[i+1];
					distance += (google.maps.geometry.spherical.computeDistanceBetween(p1, p2) / 1000);
				}
				return distance / 1.6;
			}		
		}	

		function setMarkers(map, markers) {
			for (var i = 0; i < markers.length; i++) {
				var bounds = new google.maps.LatLngBounds();
				var sites = markers[i];
				var siteLatLng = new google.maps.LatLng(sites[1], sites[2]);
				bounds.extend(siteLatLng);
				var marker = new google.maps.Marker({
					position: siteLatLng,
					map: map,
					title: sites[0],
					html: sites[3]
				});
				google.maps.event.addListener(marker, "click", function () {
					infowindow.setContent("<h1>"+ this.title +"</h1><p>"+ this.html +"</p>");
					infowindow.open(map, this);
				});
				map.fitBounds(bounds);
			}   
		}
		google.maps.event.addDomListener(window, 'load', initialize);
	</script>
</div>
<?php insert_footer_bottom(); ?>
