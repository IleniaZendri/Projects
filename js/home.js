function load() {
	$.ajax({
		url: "backend/homep.php",
		method: 'POST',
		data: { request: "recipes" },
		dataType: "json",

		success: function (data) {
			console.log("success");
			console.log(data);
			printPage(data);
		},

		error: function (data) {
			console.log("error");
			console.log(data);
		}

	});
}

function printPage(array) {

	var resultText = "";
	var i = 0;

	if (array) {
		for (i = 0; i < 4; i++) {

			resultText += '<div class="button_container">';
			resultText += '<div class="w3-quarter" data-id = "' + array[i]["id_ricetta"] + '">';
			resultText += '<img src="' + array[i]['immagine_ricetta'] + '" alt="Pasta and Wine" style="width:100%">';
			resultText += '<h3>' + array[i]['titolo_ricetta'] + '</h3>';
			var testo = array[i]['descrizione_ricetta'].substr(0, 80);
			resultText += '<p>' + testo + "..." + '<br>' + '<input class="inputscheda" type="button" value="Scopri di più"> </input>' + '</p>';
			resultText += '</div>';
			resultText += '<button class = "btn" value = "'+ array[i]["id_ricetta"] +'"><i style="font-size:24px" class="fa">&#xf017;</i></button>';
			resultText += '</div>';
		}

		resultText += '</div>';
		resultText += '<div class="w3-row-padding w3-padding-16 w3-center">';

		for (i; i < array.length; i++) {
			resultText += '<div class="button_container">';
			resultText += '<div class="w3-quarter" data-id = "' + array[i]["id_ricetta"] + '">';
			resultText += '<img src="' + array[i]['immagine_ricetta'] + '" alt="Pasta and Wine" style="width:100%">';
			resultText += '<h3>' + array[i]['titolo_ricetta'] + '</h3>';
			var testo = array[i]['descrizione_ricetta'].substr(0, 80);
			resultText += '<p>' + testo + "..." + '<br>' + '<input class="inputscheda" type="button" value="Scopri di più"> </input>' + '</p>';
			resultText += '</div>';
			resultText += '<button class = "btn" value = "'+ array[i]["id_ricetta"] +'"><i style="font-size:24px" class="fa">&#xf017;</i></button>';
			resultText += '</div>';
		}
	}
	$('#food').append(resultText);


}

$(document).ready(function () {
	load();
});

$("#food").on("click", ".w3-quarter", function () {
	var id = $(this).data("id");
	window.location = './schedaRicetta.php?id=' + id;
});

$("#food").on("click", ".btn", function () {

	var session = $("#SessionID").text();
	var recipe_id = $(this).val();	

	$.ajax({
		url: "backend/list.php",
		method: 'POST',
		data: { request: "addList", sessionID: session, ricetta_id: recipe_id},
		dataType: "json",
	});
	
});