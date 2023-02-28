<?php include_once("db.php");

if (isset($_POST["request"])) {
    $request = $_POST["request"];

    if ($request == "addList") {
        //addList
        if (isset($_POST["sessionID"]) && isset($_POST["ricetta_id"])) {

            $sessionID = $_POST["sessionID"];
            $ricetta_id = $_POST["ricetta_id"];

            $db = db_connect();

            $stmt = $db->prepare("INSERT INTO guarda_dopo (utente_id, ricetta_id) VALUES ($sessionID, $ricetta_id)");

            $stmt->execute();

            $data = $stmt->fetchAll(PDO::FETCH_ASSOC);

            echo json_encode($data);
        }
    }

    if ($request == "deleteList") {
        //deleteList
        if (isset($_POST["sessionID"]) && isset($_POST["ricetta_id"])) {

            $sessionID = $_POST["sessionID"];
            $ricetta_id = $_POST["ricetta_id"];

            $db = db_connect();

            $stmt = $db->prepare("DELETE FROM guarda_dopo WHERE utente_id = $sessionID AND ricetta_id = $ricetta_id");

            $stmt->execute();

            $data = $stmt->fetchAll(PDO::FETCH_ASSOC);

            echo json_encode($data);
        }
    }

    if ($request == "loadList") {

        //loadList
        if (isset($_POST["sessionID"])) {

            $sessionID = $_POST["sessionID"];

            $db = db_connect();

            $stmt = $db->prepare("SELECT titolo_ricetta, immagine_ricetta, descrizione_ricetta, id_ricetta FROM ricetta JOIN guarda_dopo ON guarda_dopo.ricetta_id = ricetta.id_ricetta WHERE guarda_dopo.utente_id = $sessionID");

            $stmt->execute();

            $data = $stmt->fetchAll(PDO::FETCH_ASSOC);

            echo json_encode($data);
        }
    }
}
