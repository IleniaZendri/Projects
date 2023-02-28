<?php 

session_start();

if (isset($_SESSION["id_utente"])) {
	include("top.php");

	if (isset($_GET["id"])) {
	?>
		<p hidden id="id"><?= $_GET["id"] ?></p>
	<?php
	}
	?>


<link href="css/styleScheda.css" type="text/css" rel="stylesheet" />

<div id="container">

	<div id="recipe">

	</div>

	<div id="comment_container">
		<h3>I commenti degli utenti</h3>

		<div id="scriviCommento">
			<textarea id="addComment" placeholder="Scrivi qui la tua recensione ..." cols="75" rows="5"></textarea>
			<button id="buttonScrivi" type="submit">Scrivi</button><br>
		</div>

		<div id="comment">

		</div>

	</div>

</div>

<script src="js/schedaRicetta_script.js"></script>

<?php 

} else {

	header('Location: http://localhost/SITO/');
}

include("bottom.php"); ?>