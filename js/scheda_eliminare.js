function load(){
	$.ajax({
		url: "backend/scheda.php",
		method: 'POST',
		data: {request : "recipes"},
		dataType: "json",

		success: function (data) {
			console.log("success");
			console.log(data);
			printPage(data);
		},

		error: function(data){
			console.log("error");
			console.log(data);
		}

	});
}

function printScheda(array){
	var resultText = "";
	var i = 0;

	if(array){
		console.log("pre-for");
		//for(i = 0; i < 4; i++){
			console.log("for");
			resultText += '<div class="w3-quarter">';
			resultText += '<img src="' + array[i]['immagine_ricetta'] + ' style="width:100%">';
			resultText += '<h3>' + array[i]['titolo_ricetta'] + '</h3>';
			resultText += '<p>' + array[i]['descrizione_ricetta'] + '</p>';
			resultText += '</div>';
		//}
    };
};

$( document ).ready(function() {
    load();
    $(".schedaRicetta .scheda").click(function(event){
        var titoloRicetta = $(this).parent().find("h2").text();
        location.href = 'schedaRicetta.php?titoloRicetta=' + titoloRicetta;
    })
});

//schedaRicetta