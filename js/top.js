function top_search(){
    var recipeType = 9999;
    var selector = $('#selettore option:selected').val();
    var value = $('#cerca').val();

    if(selector != 999){
        recipeType = selector;
    }

    console.log("replace");
    window.location = './results.php?recipeType=' + recipeType + '&value=' + value;

}

$(document).ready(function(){
    
    $('select option[value="999"]').attr("selected",true);

    $('#bottoneCerca').click(function (e) { 
        e.preventDefault();
        console.log("cliccato");
        top_search();
    });
    
});

function w3_open() {
    document.getElementById("mySidebar").style.display = "block";
}

function w3_close() {
    document.getElementById("mySidebar").style.display = "none";
}

function later(){    
    document.getElementById("mySidebar").style.display = "none";
    document.getElementById("list").style.display = "block";

    list();
}

function laterClose(){
    document.getElementById("list").style.display = "none";
    w3_open();
}

function list(){
    var session = $("#SessionID").text();
    console.log(session);

    $.ajax({
		url: "backend/list.php",
		method: 'POST',
		data: {request: "loadList", sessionID : session},
		dataType: "json",

		success: function (data) {
			console.log("success");
			console.log(data);
			printList(data);
		},

		error: function(data){
			console.log("error");
			console.log(data);
		}

	});
}

function printList(data){
    var array = data;
    var i = 0;
    var resultText = "";

    for(i = 0; i < array.length; i++){
        resultText += '<div id = "move">';
            resultText += '<div class = "result_item_list">';
                resultText += '<p hidden id = "list_id">' + array[i]["id_ricetta"] + '</p>';
                resultText += '<h5>' + array[i]["titolo_ricetta"] + '</h5>';
                resultText += '<img class="recipe_img_list" src="' + array[i]["immagine_ricetta"] + '" alt = ' + array[i]["immagine_ricetta"] + '>';
                resultText += '<p>' + array[i]["descrizione_ricetta"].substr(0,150) + '...</p>';
            resultText += '</div>';
        resultText += '</div>';
    }

    resultText += '<div id = "drop">';
    resultText += '<p class="trash" ><i class="fa fa-trash"></i> Rimuovi</p>';
    resultText += '</div>';
    	
    $("#listElements").append(resultText);

    
    $(".result_item_list").draggable({ 
        axis: "y" 
    });    
    $("#drop").droppable({
        drop: function(event, ui){

            var session = $("#SessionID").text();
            var list_id = $("#list_id").text();

            console.log(session);
            console.log(list_id);

            deleteList(session, list_id);

            ui.draggable.remove();
        }
    });
}

function deleteList(session, list_id){
    $.ajax({
		url: "backend/list.php",
		method: 'POST',
		data: {request: "deleteList", sessionID : session, ricetta_id: list_id},
		dataType: "json",

		success: function (data) {
			console.log("success");
			console.log(data);
		},

		error: function(data){
			console.log("error");
			console.log(data);
		}

	});
}

