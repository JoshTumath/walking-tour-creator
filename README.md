# README


## Web site installation

### Requirements 

- PHP 5.1+
- PHP PDO module
- Any SQL Database (Requires minor changes to files to use anything but mysql)
- Any web server

### Installation

1. Open zip file containing git repository. 
2. Copy all files from directory "SRC/Controlled/website" on master branch to
   the web server.
3. Run walking\_tour\_database.sql to create the database in your chosen
   server.
4. Edit upload.php, tour.php and index.php to add the correct settings for your
   database server. This options should be put on the line
   "$db = new PDO('TYPE\_OF\_DB:host=HOSTNAME;db=walking\_tour\_database',
   'DB\_USER\_NAME', 'DB\_PASSWORD')"
5. Finished.



# App installation

### Installation

1. Change phone settings to allow application installs from unknown locations.
2. Copy the WalkingTourCreator.apk to the phone and choose to install it.
3. Finished.
