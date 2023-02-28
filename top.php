<!DOCTYPE html>
<html>
<title>W3.CSS Template</title>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="stylesheet" href="https://www.w3schools.com/w3css/4/w3.css">
<link rel="stylesheet" href="https://fonts.googleapis.com/css?family=Karma">

<link href="css/styleTop.css" type="text/css" rel="stylesheet" />
<link href="css/styleHome.css" type="text/css" rel="stylesheet" />
<link href="css/results_css.css" type="text/css" rel="stylesheet" />
<link href="css/style_css.css" type="text/css" rel="stylesheet" />
<link href="css/list.css" type="text/css" rel="stylesheet" />

<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>

<script src="https://code.jquery.com/ui/1.12.1/jquery-ui.js"></script>

<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">

<script src="js/top.js"></script>

<body>

<?php if (isset($_SESSION["id_utente"])) {
?>

<p hidden id="SessionID"> <?= $_SESSION["id_utente"] ?> </p>
	<!-- Top menu -->
	<div class="w3-top">
		<div class="w3-xlarge margin">
			<div class="w3-button w3-padding-16 w3-left" onclick="w3_open()">☰</div>

			<div class="w3-sidebar w3-bar-block w3-border-right" id="mySidebar">
				<button onclick="w3_close()" class="w3-bar-item w3-large hover">Close &times;</button>
				<a href="./home.php" class="w3-bar-item w3-button">Home</a>
				<p onclick="later()" id="later" class="w3-bar-item w3-button">"Leggi più tardi"</p>
				<a href="logout.php" class="w3-bar-item w3-button">Log out</a>
			</div>

			<!-- Lista elementi da leggere dopo -->
			<div class="w3-sidebar w3-bar-block w3-border-right" id="list">
				<button onclick="laterClose()" class="w3-bar-item w3-large hover">Go back &#8592;</button>
				
				<div id="listElements">
				
				</div>
			</div>

			<div class="w3-right w3-padding-16">
				<select id="selettore">
					<option value="999">-- Tipo di piatto --</option>
					<option value="000">Antipasti</option>
					<option value="001">Primi</option>
					<option value="002">Secondi</option>
					<option value="003">Contorni</option>
					<option value="004">Dessert</option>
				</select>
				<input id="cerca" type="text" name="cerca" placeholder="Cerca...">
				<input id="bottoneCerca" type="submit" value="">
			</div>

			<div class=" centered w3-padding-16">My Food</div>

		</div>
	</div>

	<?php
} else {

	header('Location: http://localhost/SITO/');
}
?>