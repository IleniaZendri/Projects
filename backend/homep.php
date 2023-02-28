<?php include_once("db.php");

if(isset($_POST["request"])){
	$request = $_POST["request"];

	if($request == "recipes"){

		$db = db_connect();

		$stmt = $db->prepare("SELECT titolo_ricetta, immagine_ricetta, descrizione_ricetta, id_ricetta FROM ricetta");

		$stmt->execute();

		$data= $stmt->fetchAll(PDO::FETCH_ASSOC);
		
		echo json_encode($data);

	} else{
		//error...
	}
}


?>