<?php
include_once("db.php");

if (isset($_POST["request"])) {

    $request = $_POST["request"];

    if (isset($_POST["id"]) && $request == "load") { //Metodo Load

        $id = $_POST["id"];

        $db = db_connect();

        $stmt = $db->prepare("SELECT id_ricetta, titolo_ricetta, immagine_ricetta, descrizione_ricetta FROM ricetta WHERE id_ricetta = $id");

        $stmt->execute();

        $data = $stmt->fetchAll(PDO::FETCH_ASSOC);
        $final['1'] = $data;

        $stmt = $db->prepare("SELECT username, ricetta_id, utente_id, testo, timestamp FROM commenti JOIN utente on commenti.utente_id = utente.id_utente WHERE ricetta_id = $id");

        $stmt->execute();

        $data1 = $stmt->fetchAll(PDO::FETCH_ASSOC);

        $final['2'] = $data1;

        echo json_encode($final);

    } else if(isset($_POST["id"]) && isset($_POST["testo"]) && isset($_POST["UserId"]) && $request == "addComment"){ //metodo writeComment
        
        $db = db_connect();

        $ricetta_id = $db->quote(trim($_POST["id"])); 
        $utente_id = $db->quote(trim($_POST["UserId"])); 
        $testo = $db->quote(trim($_POST["testo"])); 

        $stmt = $db->prepare("INSERT INTO commenti (ricetta_id, utente_id, testo) VALUES ($ricetta_id, $utente_id, $testo)");

        $stmt->execute();

    } 
}
