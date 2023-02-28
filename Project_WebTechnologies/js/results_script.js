function search() {
    var recipeType = $('#recipeType').text();
    var value = $('#value').text();

    $.ajax({
        url: "backend/search.php",
        method: 'POST',
        data: { recipeType: recipeType, value: value },
        dataType: "json",

        success: function (data) {
            console.log("success results");
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
    var resultText = "";

    for (var i = 0; i < array.length; i++) {

        resultText += '<div class = "results_item">';
        resultText += '<p hidden>' + array[i]["id_ricetta"] + ' </p>'

        resultText += '<img src = "' + array[i]["immagine_ricetta"] + '" class = "results_img">';

        resultText += '<div class = "results_title">';
        resultText += '<h4>' + array[i]["titolo_ricetta"] + '</h4>';
        resultText += '</div>';

        resultText += '<div class = "results_text">';
        resultText += '<p>' + array[i]["descrizione_ricetta"] + '</p>';
        resultText += '</div>';

        resultText += '</div>';
    }
    $("#result").append(resultText);
}

$(document).ready(function () {
    search();
});

//Apertura ricetta
$("#result").on("click", ".results_item", function () {
    var tmp = $(this).text();
    var i = 0;
    var id = "";

    while (tmp[i] != " ") {
        id += tmp[i];
        i++;
    }

    window.location = './schedaRicetta.php?id=' + id;
})