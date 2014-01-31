<?php require_once "includes/functions.php"; ?>

<?php insert_header(); ?>
<h1>Welcome to the Walking Tour Displayer</h1>
<div class="wrapper">
  <div id="list">
    <p>Please select a tour that you wish to view</p>
  <?php
	//Connect to database
	$db = new PDO("mysql:host=jakemaguire.co.uk;dbname=walking_tour_database", "webWalk", "123");
	//Begins transaction which will stop any data being added to the database if anything inside the transaction fails
	$db->beginTransaction();        
	
	//Select everything from the listOfWalks table
	$select_walks = $db->prepare("SELECT * FROM listOfWalks");
	//Perfrom this query
	$select_walks->execute();
	
	//Select the name of the photo where the photo's placeID is equal to the placeDescription ID and the placeDescriptionID is equal to the location ID and the location's walkID is equal to the listOfWalks' ID
	$select_photo_query = $db->prepare("SELECT photoUsage.photoName FROM (((photoUsage INNER JOIN placeDescription ON photoUsage.placeID = placeDescription.id) INNER JOIN location ON placeDescription.locationID = location.id) INNER JOIN listOfWalks ON location.walkID = listOfWalks.id) WHERE listOfWalks.id = :id");
	//Assign the id to the variable walkID
	$select_photo_query->bindParam(":id", $walk_id);
	
    echo "<ul class='walkItem'>";
	//While there are still walks to be fetched do...
    while ($walk = $select_walks->fetch(PDO::FETCH_ASSOC)) {
		//Set the walkID to id of the walk in the table
		$walk_id = $walk["id"];
		//Set the walk name to the name of the walk in the table
		$walk_name = $walk["title"];
		
		echo "<li>";
			//Displays a link to the walk which can be clicked and sent with the walkID of that walk for the tour.php page.
			echo "<a href='tour.php?id={$walk_id}'>";
				//Executes the photo query
				$select_photo_query->execute();
				//Recieves the name of the photo and saves it to a local variable
				$photo_select_result = $select_photo_query->fetch(PDO::FETCH_ASSOC);
				//Checks to see if there is a photo present and if there is then display that photo or display a default image
				if ($photo_select_result) {
					echo "<img src='images/walkimages/{$photo_select_result["photoName"]}.jpeg' alt=''/>";
				} else {
					echo "<img src='images/logo.jpg' alt=''/>";
				}
			//Display the name of the walk
			echo "<p>{$walk['title']}</p>";
			echo "</a>";
		echo "</li>";
    }
    echo "</ul>";
  ?>
</div>
<?php insert_footer(); ?>