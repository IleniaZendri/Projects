<?php
include("topLogin.php");

session_start();
session_destroy();
?>

<div class="body">
</div>
<div class="card">
	<div class="form_style">

		<div class="header">
			<div>Site<span>Random</span></div>
		</div>
		<br>
		<form method="post" class="login">
			<input id="username" type="text" placeholder="username" name="name"><br>
			<input id="password" type="password" placeholder="password" name="password"><br>
			<div class="register">
				<p> Non sei ancora registrato? <a href="./registrazione.php">Clicca qui</a></p>
			</div>
			<input id="logIn" type="button" value="Login">
			<p id="error"></p>
		</form>
	</div>
</div>
<body>