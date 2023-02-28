<?php

function db_connect() {

	try {
		$db = new PDO("mysql:dbname=dbsito;host=localhost;charset=utf8", "root", "");
	} catch (PDOException $e) {
		echo 'Connection failed: ' . $e->getMessage();
	}        
	return $db;
}
 
?>