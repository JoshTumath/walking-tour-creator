<?php require_once "includes/functions.php"; ?>

<?php
// TODO: What happens if ID is not set?
//if (isset($_GET['id'])) {
  $walkID = $_GET['id'];
//} //Create an else that tells a user to go back to index and select a walk

//---------- Connect to Database ----------//			
$connection = mysql_connect('jakemaguire.co.uk','webWalk','123');
mysql_select_db('walking_tour_database',$connection) or die("cannot connect to database");

//---------- Select Walk Information Based on Walk ID ----------//
$SQL = "SELECT * FROM listOfWalks WHERE id = " . $walkID;
$result = mysql_query($SQL);

while ($db_field = mysql_fetch_assoc($result)) {
  $title = $db_field['title'];
  $shortDesc = $db_field['shortDesc'];
  $longDesc = $db_field['longDesc'];
  $distance = $db_field['distance'];
  $hours = $db_field['time'];
}

//---------- Get Markers Query ----------//
$SQL = ("SELECT placeDescription.description, location.latitude, location.longitude, placeDescription.title FROM placeDescription, location WHERE placeDescription.locationID = location.id AND location.walkID = " . $walkID) or mysql_error();
$result = mysql_query($SQL);
while ($row = mysql_fetch_assoc($result)) {
  $markers[] = '["' . $row['title'] . '",' . $row['latitude'] . ',' . $row['longitude'] . ', "' . $row['description'] . '"]';
}

//---------- Get Lines Query ----------//
$SQL = ("SELECT latitude,longitude FROM location WHERE walkID =" .$walkID) or mysql_error();
$result = mysql_query($SQL);
while ($row = mysql_fetch_assoc($result)) {
  $coordinates[] = 'new google.maps.LatLng(' . $row['latitude'] . ', ' . $row['longitude'] . ')';
}
?>

<?php insert_header("tour"); ?>
<aside>
	<p><span><b><?php echo $title;?></b></span></p>
	<p><span><b>Short Description: </b><?php echo $shortDesc;?></span></p>
	<p><span><b>Long Description: </b><?php echo $longDesc;?></span></p>
	<p><span><b>Distance: </b><?php echo $distance;?></span></p>
	<p><span><b>Hours: </b><?php echo $hours;?></span></p>
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

		  setMarkers(map, sites);
			infowindow = new google.maps.InfoWindow({
			content: "loading..."
			});

		  var coordinates = [
			<?php echo implode(',', $coordinates) ?>
		  ];
		  var lineBetweenPoints = new google.maps.Polyline({
			path: coordinates,
			geodesic: true,
			strokeColor: '#FF0000',
			strokeOpacity: 1.0,
			strokeWeight: 2
		  });
		  lineBetweenPoints.setMap(map);
		}	

		  var sites = [
		  <?php echo implode(',', $markers) ;?>
		];
		  function setMarkers(map, markers) {
		  for (var i = 0; i < markers.length; i++) {
			var sites = markers[i];
			var siteLatLng = new google.maps.LatLng(sites[1], sites[2]);
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
		  }   
		}

		google.maps.event.addDomListener(window, 'load', initialize);
	</script>
</div>
<?php insert_footer_bottom(); ?>
