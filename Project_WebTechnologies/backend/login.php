<?php 
include_once("db.php");
session_start();

if(isset($_POST["username"]) && isset($_POST["email"])){
	$username = htmlspecialchars($_POST["username"]);
	$password = md5(htmlspecialchars($_POST["password"]));
	$email = htmlspecialchars($_POST["email"]);
	$regReturn = registrazione($username, $password, $email);
	$array = array("esito"=>$regReturn);
	$json = json_encode($array);
	print $json;
} else if(isset($_POST["username"])){
	$username = htmlspecialchars($_POST["username"]);
	$password = md5(htmlspecialchars($_POST["password"]));
	$loginReturn = login($username, $password);	
	$array = array("esito"=>$loginReturn);
	$json = json_encode($array);
	print $json;
}



function login($username, $password) {
	try{
		$db = db_connect();
		$username = $db->quote($username);
		$password = $db->quote($password);
		
		$query = "SELECT password, id_utente
				FROM utente
				WHERE username=".$username;
				
		$rows = $db->query($query);
		$numRighe=$rows->rowCount();

		if($numRighe>0){
			foreach($rows as $row){
				if("'".($row["password"])."'" == $password){
					$_SESSION["id_utente"] = $row["id_utente"];
					$_SESSION["username"] = $username;
					return 1;
				} else return 0; // username esistente ma password non corretta
			}
			
		} else return 0; // username non presente nel db
		
	}catch (PDOException $ex){
		return -2; // errore
	}
}

function ret1(){
 return 1;
}

function registrazione($username, $password, $email){
	try{
		$db = db_connect();
		$username = $db->quote($username);
		$password = $db->quote($password);
		$email = $db->quote($email);
		
		$query = "INSERT INTO utente(username, password, email)
				VALUES(".$username.", ".$password.", ".$email.")";
		
		$res = $db->exec($query);
		if($res){
			$_SESSION["username"] = $username;
			$query = "SELECT id_utente
				FROM utente
				WHERE username=".$username;
			$rows = $db->query($query);
			foreach($rows as $row){
				$_SESSION["id_utente"] = $row["id_utente"];
			}
			return 1; // registrazione effettuata con successo
		} else {
			$query = "SELECT COUNT(*) AS email_presente
				FROM utente
				WHERE email=".$email;
			$rows = $db->query($query);
			foreach($rows as $row){
				if($row["email_presente"]=="1"){
					return 2; //registrazione non effettuata: l'email inserita è già presente nel db
				} else return 0;
			}
		}
		return 0; // registrazione non effettuata
		
	}catch (PDOException $ex){
		return -2; // errore
	}	
}
?>