 <?php include("topLogin.php"); ?> 

<div class="body">
</div>
<div class="card">
	<div class="form_style">

	<div class="header">
			<div>Site<span>Random</span></div>
		</div>
		<br>

		<form method="post" class="login">
                <input id="username" type="text" placeholder="username" name="user"><br>
                <input id="email" type="text" placeholder="email" name="email"><br>

                <input id="password1" type="password" placeholder="password" name="password"><br>
				<input id="password2" type="password" name="password2" maxlength="30" placeholder="Inserisci di nuovo la password">
				<p id="error"></p>

                <input id="registrati" type="submit" value="Registrati"> 
		</form> 
	</div>
</div>
<body>
