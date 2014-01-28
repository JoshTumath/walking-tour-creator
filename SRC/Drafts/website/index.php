<?php require_once "includes/functions.php"; ?>

<?php insert_header(); ?>
<h1>Welcome to the Walking Tour Displayer</h1>
<div class="wrapper">
  <div id="list">
    <p>Please select a tour that you wish to view</p>
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
          echo "<p>" . $db_field['title'] . "</p>";
        echo "</a>";
      echo "</li>";
    }
    echo "</ul>";
  ?>
</div>
<?php insert_footer(); ?>