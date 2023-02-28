<?php
session_start();

if (isset($_SESSION["id_utente"])) {
    include("top.php");

    if(isset($_GET["recipeType"]) && isset($_GET["value"])){
?>

<p hidden id = "recipeType"><?=$_GET["recipeType"]?></p>
<p hidden id = "value"><?=$_GET["value"]?></p>

<?php
}
?>

<div id = "results_container">
    <div id = "result">

    </div>

</div>

<script src = "./js/results_script.js"></script>

<?php

} else {

	header('Location: http://localhost/SITO/');
}
include("bottom.php");
?>