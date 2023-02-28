$(document).ready(function(){
	$("#logIn").click(function(event){
		event.preventDefault();
		// si recuperano i dati inseriti nei campi password e username
		var username = $('#username').val();
		var password = $('#password').val();
		
		if(password.includes(" ") || username.includes(" ")){
			username = username.replace(/[" "]/g, "");
			password = username.replace(/[" "]/g, "");
		}
	
		// se username o password sono mancanti e si tenta il login viene segnalato all'utnete un errore
		if(username == "" || password == ""){
			$("#error").html("Devi riempire tutti i campi per fare il login."); 
		}
		
		if(username != "" && password != ""){
			$.ajax({
				url:"backend/login.php",
				method:"POST",
				data:{username:username, password:password},
				success:function(data){
					console.log("success");
					console.log(data);
					data = JSON.parse(data);
					data = data.esito;
					if(data == "1"){
						console.log("logged in");
						
						window.location.replace("./home.php");
					} else if(data == "0"){
						$("#error").html("Password o username non corretti.");
					} else if(data == "-2"){
						$("#error").html("E' stato riscontrato un errore, riprova più tardi.");
					}
				},
				error: function(data){
					console.log("error");
					console.log(data);
					alert("Si è verificato un errore, riprovare più tardi.");
				}
			});
		}	
	});
	
	$("p").focusin(function(){
        $("#error").html("");
    });
	
	$("#registrati").click(function(event){
		event.preventDefault();
		
		var username1 = $('#username').val();
		var email = $('#email').val();
		var password1 = $('#password1').val();
		var password2 = $('#password2').val();
		
		username1 = username1.replace(/[" "]/g, "");
		email = email.replace(/[" "]/g, "");
		password1 = password1.replace(/[" "]/g, "");
		password2 = password2.replace(/[" "]/g, "");
		
		var myRegEx = /^[A-z0-9\.\+_-]+@[A-z0-9\._-]+\.[A-z]{2,6}$/;
		
		if(username == "" || email == "" || password1 == "" || password2 == ""){
			$("#error").html("Devi riempire tutti i campi per registrarti.");
		} else if(password1.length<6){
			$("#error").html("La password deve avere almeno 6 caratteri.");
		} else if(password1 != "" && password2 != "" && password1 != password2){
			$("#error").html("Le password inserite non sono uguali.");
		}else if(!myRegEx.test(email)){
			$("#error").html("Indirizzo email non valido.");
		} else {
			$.ajax({
				url:"backend/login.php",
				method:"POST",
				data:{username:username1, email:email, password:password1},
				success:function(data){
					data = JSON.parse(data);
					data = data.esito;
					if(data == "1"){
						window.location.replace("http://localhost/SITO/home.php");
					} else if(data == "0"){
						$("#error").html("Username già esistente.");
					} else if(data == "2"){
						$("#error").html("Email già in uso.");
					}
				},
				error: function(){
					alert("Si è verificato un errore, riprovare più tardi.");
				}
			});
		}	
	});
	
});
