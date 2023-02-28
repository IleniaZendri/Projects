function load() {
    var id = $('#id').text();

    $.ajax({
        url: "backend/search_scheda.php",
        method: 'POST',
        data: { request: 'load', id: id },
        dataType: "json",

        success: function (data) {
            console.log("success");
            console.log(data);
            print(data);
        },

        error: function (data) {
            console.log("error");
            console.log(data);
        },
    });
}

function print(data) {
    var array = data;
    var recipe = array[1][0];
    var comments = array[2];
    var resultText = "";

    resultText += '<h2>' + recipe["titolo_ricetta"] + '</h2>';
    resultText += '<img class="recipe_img" src="' + recipe["immagine_ricetta"] + '" alt = ' + recipe["immagine_ricetta"] + '>';
    resultText += '<p>' + recipe["descrizione_ricetta"] + '</p>';	

    $("#recipe").append(resultText);

    resultText = "";

    for (var i = 0; i < array[2].length; i++) {
        resultText += '<div class = "comment">';
        resultText += '<p>' + comments[i]["username"] + '<span id = "comment_right">' + comments[i]["timestamp"] + '</span> </p>';
        resultText += '<p id = "corpo" >' + comments[i]["testo"] + '</p>';
        resultText += '</div>';
    }

    $("#comment").append(resultText);


}

function writeComment() {
    var id = $('#id').text();
    var testo = $("#addComment").val();
    var UserId = $("#SessionID").text();

    console.log(id, testo, UserId);

    if (testo != "") {

        $.ajax({
            url: "backend/search_scheda.php",
            method: 'POST',
            data: { request: 'addComment', id: id, testo: testo, UserId: UserId },
            dataType: "json",
        });

        printNewComment();

    }

}

function printNewComment() {
    console.log('print');
}

$(document).ready(function () {

    load();

    $('#scriviCommento').click(function () {
        writeComment();
    });
});
