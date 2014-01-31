<?php require_once "includes/functions.php"; ?>

<?php insert_header(); ?>
<h1>Welcome to the Walking Tour Displayer</h1>
<div class="wrapper">
  <div id="list">
    <p>Please select a tour that you wish to view</p>
  <?php
	$db = new PDO("mysql:host=jakemaguire.co.uk;dbname=walking_tour_database", "webWalk", "123");
	$db->beginTransaction();        
	
	$select_walks = $db->prepare("SELECT * FROM listOfWalks");
	$select_walks->execute();
	
	$select_photo_query = $db->prepare("SELECT photoUsage.photoName FROM (((photoUsage INNER JOIN placeDescription ON photoUsage.placeID = placeDescription.id) INNER JOIN location ON placeDescription.locationID = location.id) INNER JOIN listOfWalks ON location.walkID = listOfWalks.id) WHERE listOfWalks.id = :id");
	$select_photo_query->bindParam(":id", $walk_id);
	
    echo "<ul class='walkItem'>";
    while ($walk = $select_walks->fetch(PDO::FETCH_ASSOC)) {
		$walk_id = $walk["id"];
		$walk_name = $walk["title"];
		
		echo "<li>";
			echo "<a href='tour.php?id={$walk_id}'>";
				$is_proper_name = $select_photo_query->execute();
				$photo_select_result = $select_photo_query->fetch(PDO::FETCH_ASSOC);
				
				if ($photo_select_result) {
					echo "<img src='images/walkimages/{$photo_select_result["photoName"]}.jpeg' alt=''/>";
				} else {
					echo "<img src='images/logo.jpg' alt=''/>";
				}
			echo "<p>{$walk['title']}</p>";
			echo "</a>";
		echo "</li>";
    }
	
    echo "</ul>";
  ?>
</div>
<?php insert_footer(); ?>