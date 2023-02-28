<?php
include_once("db.php");

if (isset($_POST["value"])) {

    if (isset($_POST["recipeType"]) && $_POST["recipeType"] != 9999) { //tipologia e valore selezionati
        $recipeType = $_POST["recipeType"];

        $db = db_connect();

        $titolo_ricetta = $db->quote('%' . trim($_POST["value"]) . '%');

        $stmt = $db->prepare("SELECT id_ricetta, titolo_ricetta, immagine_ricetta, descrizione_ricetta FROM ricetta WHERE tipo_ricetta = $recipeType AND titolo_ricetta LIKE $titolo_ricetta");

        $stmt->execute();

        $data = $stmt->fetchAll(PDO::FETCH_ASSOC);

        echo json_encode($data);
    } else { //tipologia non selezionata, ma valore si

        $db = db_connect();

        $titolo_ricetta = $db->quote('%' . trim($_POST["value"]) . '%');

        $stmt = $db->prepare("SELECT id_ricetta, titolo_ricetta, immagine_ricetta, descrizione_ricetta FROM ricetta WHERE titolo_ricetta LIKE $titolo_ricetta");

        $stmt->execute();

        $data = $stmt->fetchAll(PDO::FETCH_ASSOC);

        echo json_encode($data);
    }
} else if (isset($_POST["recipeType"])) { //Ricerca per tipo
    
    $recipeType = $_POST["recipeType"];

    $db = db_connect();
    if($recipeType != 9999){ //tipo selezionato ma non valore
        $stmt = $db->prepare("SELECT id_ricetta, titolo_ricetta, immagine_ricetta, descrizione_ricetta FROM ricetta WHERE tipo_ricetta = $recipeType");
    }else { //ne tipo ne valore selezionati
        $stmt = $db->prepare("SELECT id_ricetta, titolo_ricetta, immagine_ricetta, descrizione_ricetta FROM ricetta");
    }

    $stmt->execute();

    $data = $stmt->fetchAll(PDO::FETCH_ASSOC);

    echo json_encode($data);
}
