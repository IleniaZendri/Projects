function print(data){
    array = data;
    result_Text = '';

    if(array){
       
        result_Text += ' <h1 class= "display-6">' + array['Titolo']   + ' <button type="button" class="btn btn-primary" id = "read_later"> Leggi più tardi </button> </h1>';
        result_Text += ' <hr class= "my-4">';
        result_Text += ' <img src= " ' + array['Foto'] + ' "> ';
        result_Text += ' <p>' + array['Testo'] + '</p>';
        result_Text += ' <p class = "article_date">  autore: ' + array['Username'] + ' <span id = ""> ' + array['Ora'] + ' <span></p> ';
        result_Text += ' <hr class="my-4">';
        
        $('#new_Comment').show();

    } else{
        result_Text += '<p> Errore nel caricamento della pagina, riprovare in seguito. Ci scusiamo per il disagio </p>'
    }

    $('#article_result').append(result_Text);
}