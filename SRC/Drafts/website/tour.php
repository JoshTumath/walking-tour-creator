<?php require_once "includes/functions.php"; ?>

<?php
// TODO: What happens if ID is not set?
//if (!isset($_GET['id'])) {
  
//}

$walk_id = $_GET['id'];

//---------- Connect to Database ----------//			
$db = new PDO("mysql:host=jakemaguire.co.uk;dbname=walking_tour_database", "webWalk", "123");

//---------- Select Walk Information Based on Walk ID ----------//
$walk = $db->prepare("SELECT * FROM listOfWalks WHERE id = :walkid");
$walk->bindParam(":walkid", $walk_id);
$walk->execute();

$walk_data = $walk->fetch(PDO::FETCH_ASSOC);
$title = $walk_data['title'];
$short_desc = $walk_data['shortDesc'];
$long_desc = $walk_data['longDesc'];
$distance = $walk_data['distance'];
$duration = $walk_data['time'];

//--------- Queries to get ID's from Locations and Points of Interest --------------//
$select_poi = $db->prepare("SELECT location.id FROM location WHERE poi='1' and walkID = :walkid");
$select_poi->bindParam(":walkid", $walk_id);
$select_poi->execute();

$select_location = $db->prepare("SELECT location.id FROM location WHERE poi='0' and walkID = :walkid");
$select_location->bindParam(":walkid", $walk_id);
$select_location->execute();

//--------- Queries to get latitudes and longitudes for a location from its id ---------//
$locations_lat_lng_query = $db->prepare("SELECT location.latitude, location.longitude FROM location WHERE location.id = :locationid ORDER BY location.timestamp");
$locations_lat_lng_query->bindParam(":locationid", $location_id);

//--------- Queries to get data for Point of Interest markers --------//
$select_marker_info = $db->prepare("SELECT placeDescription.title, placeDescription.description, location.latitude, location.longitude FROM (placeDescription INNER JOIN location ON placeDescription.locationID = location.id) WHERE location.id = :locationid");
$select_marker_info->bindParam(":locationid", $poi_id);

$select_photo_name = $db->prepare("SELECT photoUsage.photoName FROM((photoUsage INNER JOIN placeDescription on photoUsage.placeID = placeDescription.ID)INNER JOIN location ON placeDescription.locationID = location.id) WHERE location.id = :locationid");
$select_photo_name->bindParam(":locationid", $poi_id);

//--------- Fills coordinates with latitudes and longitudes of normal points -----------//
while ($location = $select_location->fetch(PDO::FETCH_ASSOC)) {
	$location_id = $location["id"];
	$locations_lat_lng_query->execute();
	$location_coords = $locations_lat_lng_query->fetch(PDO::FETCH_ASSOC);
	
	$coordinates[] = "new google.maps.LatLng(" . $location_coords['latitude'] . ", " . $location_coords['longitude'] . ")";
}

//-------- Fills markers with latitude, longitude, title and content ---------//
while ($poi = $select_poi->fetch(PDO::FETCH_ASSOC)) {
	$poi_id = $poi["id"];
	$select_marker_info->execute();
	$marker_info = $select_marker_info->fetch(PDO::FETCH_ASSOC);
	$select_photo_name->execute();
	
	$marker_html = "<p>" . $marker_info['description'] . "</p>";
	while ($photo = $select_photo_name->fetch(PDO::FETCH_ASSOC)) {
		$marker_html = "{$marker_html} <img class='tourimg' src='images/walkimages/{$photo['photoName']}.jpeg' alt='' />";
	}
	
	$markers[] = '["' . $marker_info['title'] . '",' . $marker_info['latitude'] . ',' . $marker_info['longitude'] . ', "' . $marker_html . '"]';
}

?>

<?php insert_header("tour"); ?>
<aside>
	<h1><span><b><?php echo $title;?></b></span></h1>
	<p><span><b>Short Description: </b><?php echo $short_desc;?></span></p>
	<p><span><b>Long Description: </b><?php echo $long_desc;?></span></p>
	<p><span><b>Distance: </b><span id="distance"></span> miles</span></p>
	<p><span><b>Minutes: </b><?php echo ($duration / 60) . ($duration % 60);?></span></p>
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



