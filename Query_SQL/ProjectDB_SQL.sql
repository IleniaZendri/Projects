/*PROGETTO SQL A.A 2017/2018

Federica Nido
Eleonora Sandrone
Ilenia Zendri

*/


/*Query 1:
Contare il numero di lingue in cui le release contenute nel database 
sono scritte (il risultato deve contenere
soltanto il numero delle lingue, rinominato “Numero_Lingue”).*/

SELECT COUNT(DISTINCT Language) AS Numero_Lingue
FROM release;

/*COMMENTO
    1. le informazioni richieste sono presenti nella tabella release. L'attributo 
    coinvolto è language.
    2. La chiave primaria è ID, dato che potrebbero verificarsi valori doppi 
    utiliziamo il comando distinct. Inoltre l'attributo language può essere nullo
    3.per trovare il dato richiesto è sufficiente effettuare una select con la 
    funzione count(relativa alle lingue) della tabella sopra elencata, quindi non 
    sono necessarie sotto query
    4.La correttezza è stata dimostrata attraverso la query:

    select distinct language.id
    from language
    intersect
    select distinct release.language
    from release;

Il risultato della query mostra 6 tuple nel Db ridotto e 252 tuple nel Db completo lo stesso risultato della precedente query.
Inoltre, gli id sono gli stessi della query
Select distinct language Numero_lingue
from release;
la quale mostra gli id nella tabella release
*/


/*Query 2:
Elencare gli artisti che hanno cantato canzoni in italiano 
(il risultato deve contenere il nome dell’artista e il nome
della lingua).*/

SELECT DISTINCT artist.name AS Nome_Autore, language.name AS Nome_Lingua
FROM artist_credit_name JOIN artist ON artist_credit_name.artist = artist.id
			JOIN artist_credit ON artist_credit_name.artist_credit = artist_credit.id
			JOIN release ON artist_credit.id = release.artist_credit
			JOIN language ON release.language = language.id
WHERE language.name = 'Italian';

/*COMMENTO
    1. Le informazioni per eseguire la query sono nelle tabelle artist_credit_name, 
    artist, release, language. 
    2. per quanto riguarda le chiavi primarie abbiamo artist_credit e position per la tabella
    artist_credit_name, id per artist, artist_credit,release e language. 
    3. Anche in questo caso utilizziamo la funzione distinct per evitare di elencare artisti con lo stesso nome
    4. per arrivare al risultato effettuiamo una select con i nomi di artista e lingua, 
    effettuiamo i join tra le tabelle elencate al punto 1 ed infime nella where mettiamo la 
    condizione che riguarda il tipo di lingua ricercata
*/


/*Query 3:
Elencare le release di cui non si conosce la lingua 
(il risultato deve contenere soltanto il nome della release).
*/

SELECT release.name AS Release_lingua_sconosciuta
FROM release
WHERE release.language IS NULL;

/*COMMENTO
    1. L'informazione neccessaria è presente nella tabella release e gli attributi utilizzati sono name e language.
    2. la chiave primaria di release è l'attributo id. L'attributo name è di tipo VARCHAR e anche language: quest'ultimo è settato a null.
    3. Per trovare l'informazione richiesta è sufficiente inserire le condizione di NUll per l'attributo language;
    in questo modo verranno solo selezionate le release in cui non si conosce la lingua.
    non è necessario nessun sotto-problema.  
*/


/*Query 4:
Elencare gli artisti il cui nome contiene tutte le vocali ed è composto da una sola parola (il risultato deve
contenere soltanto il nome dell’artista).*/

SELECT name AS nome_artista
FROM artist
WHERE name LIKE '%a%'
	AND name LIKE '%e%'
	AND name LIKE '%i%'
	AND name LIKE '%o%'
	AND name LIKE '%u%'
	AND NOT (name LIKE '% %');
	
	
/*COMMENTO
    1. Abbiamo bisogno della tabella artist e dell'attributo name.
    2. artist ha chiave primaria id.
    3. Nessun sottoproblema, per arrivare al risultato richiesto abbiamo effettuato una select sull'attributo name della tabella artist ed 
    infine abbiamo iserito la condizione per verificare  la presenza di tutte le vocali anche in ordine sparso tramite 
    la funzione LIKE e la condizione per verificare che il nome sia composto da una sola parola con la funzione NOT LIKE
    
    INTERPRETAZIONE
Si considerano come un'unica parola anche le stringhe di caratteri mischiate a simboli matematici, ad esempio Sarah+Franco o
Rossi/Paolo. Se, invece, si volesse interpretare tale sequenza come 2 distinte, basterebbe inserire come altra clausola nella
where un comando del tipo: AND name  NOT LIKE 'carattere_non_desiderato' per ognuno di essi. (Esempio name NOT LIKE '%+%')
*/	


/*Query 5: 
Elencare tutti gli pseudonimi di Prince con il loro tipo, se disponibile (il risultato deve contenere lo pseudonimo
dell'artista, il nome dell’artista (cioè Prince) 
e il tipo di pseudonimo (se disponibile)).*/

SELECT artist_alias.name AS Pseudonimo, artist.name AS Nome_Artista, artist_alias_type.name AS Tipo_Artista
FROM artist JOIN artist_alias ON artist.id = artist_alias.artist
JOIN artist_alias_type ON artist_alias.type = artist_alias_type.id
WHERE artist.name = 'Prince';

/*COMMENTO
    1. Le tabelle coinvolte sono artist, artist_alias e artist_alias_type.
    Gli attributi utilizzati sono artist e type da artist_alias, id da artist e artist_alias_type
    e name per tutti.
    2. ARTIST: la chiave primaria è id, che è di tipo integer, e name è un attributo di tipo varchar.
       ARTIST_ALIAS: Gli attributi artist e type sono chiavi esterne il cui valore è uguale rispettivamente alla chiave primaria di artist
       e alla chiave primaria di artist_alias_type.
       ARTIST_ALIAS_TYPE: id è la chiave primaria della tabella.
    3. Per poter risolvere la query abbiamo applicato una select sull'attributo name di artist_alias, artist e artist_alias_type.
    Dopodichè abbiamo effettuato due join: la prima tra le relazioni artist_alias e artist e la seconda tra artist_alias_type e artist_alias,
    per trovare gli pseudonimi di un determinato artista. Nella where, abbiamo specificato che gli pseudonimi dovevano essere riferiti 
    all'artista Prince.
    
*/

/*Query 6:
Elencare le release di gruppi inglesi ancora in attività 
(il risultato deve contenere il nome del gruppo e il nome
della release e essere ordinato per nome del gruppo e nome della release).*/

SELECT DISTINCT artist.name AS Group_Name, release.name as Release
FROM artist_credit_name JOIN artist ON artist_credit_name.artist = artist.id
                        JOIN artist_type ON artist.type = artist_type.id
                        JOIN area ON artist.area = area.id
                        JOIN artist_credit ON artist_credit_name.name = artist_credit.name
                        JOIN release ON release.artist_credit = artist_credit.id 
WHERE artist.end_date_year IS NULL AND artist_type.name = 'Group' AND area.name = 'United Kingdom'
ORDER BY artist.name, release.name;

/* COMMENTO
    1. Le tabelle coinvolte sono artist, release, artist_credit, artist_credit_name, artist_type e area.
    Gli attributi utilizzati sono name, id, type e end_date_year per artist, name e artist_credit per release,
    id per artist_credit, artist_credit e artist per artist_credit_name, id e name per artist_type, name e id per area.
    2. ARTIST: id è la chiave primaria, type è una chiave esterna che si riferisce alla chiave primaria di artist_type,
    end_date_year è di tipo small int e dev'essere un valore nullo e name è di tipo varchar, che corrisponde all'anno di fine di un artista (nel caso
    di un solista l'anno di morte, nel caso di un gruppo musicale l'anno di fine attività).
    RELEASE: artist_credit è una chiave esterna che ha lo stesso valore della chiave primaria di artist_credit.
    ARTIST_CREDIT: id è la chiave primaria.
    ARTIST_CREDIT_NAME: artist_credit è un attributo della chiave primaria di tale tabella (composta da artist_credit e position), ma anche
    una chiave esterna, che corrisponde all'attributo ID di artist_credit.
    Artist è solamente una chiave esterna che si riferisce alla chiave primaria di artist.
    ARTIST_TYPE: id è la chiave primaria e name è di tipo varchar, che rappresenta il tipo di artista (es. solista, gruppo musicale, ecc).
    AREA: id è la chiave primaria. Name rappresenta la zona da dove proviene un artista, che può essere una città, uno Stato, ecc.
    3. Per arrivare al risultato, abbiamo effettuato una select sui campi name di artist e release, per visualizzare il nome delle release e del
    gruppo. Poi abbiamo effettuato 5 join per trovare i gruppi inglesi che sono ancora in attività e dunque individuare prima il tipo di artista
    (in questo caso il gruppo) e poi l'area in cui provengono (in questo caso Inghilterra). Dopodichè, abbiamo posto che l'anno di fine attività
    deve essere nullo. 
*/


/*Query 7:
Trovare le release in cui il nome dell’artista è diverso dal nome accreditato nella release 
(il risultato deve contenere il nome della release, 
il nome dell’artista accreditato (cioè artist_credit.name) e il nome dell’artista
(cioè artist.name))*/

SELECT DISTINCT release.name, artist_credit.name AS Nome_Accreditato, artist.name AS Nome_Artista
FROM release JOIN artist_credit ON release.artist_credit = artist_credit.id
	         JOIN artist_credit_name ON artist_credit.id=artist_credit_name.artist_credit
	         JOIN artist on artist_credit_name.artist=artist.id
WHERE artist.name <> artist_credit.name;


/*COMMENTO
    1. Le tabelle coinvolte sono release, artist_credit, artist e artist_credit_name. 
    Gli attributi usati sono name e artist_credit per release, id e name per artist_credit, artist_credit e artist
    per artist_credit_name, name e id per artist.
    2. RELEASE: name è di tipo varchar, artist_credit è una chiave esterna riferita alla chiave primaria di artist_credit.
    ARTIST_CREDIT: id è la chiave primaria.
    ARTIST_CREDIT_NAME: artist_credit è un attributo che fa parte della sua chiave primaria (composta da artist_credit e position),
    e tale attributo è una chiave esterna il cui valore corrisponde alla chiave primaria di artist_credit. Artist è una chiave esterna.
    ARTIST: id è la chiave primaria di artist.
    3. Per arrivare al risultato, abbiamo effettuato una select sui campi name di release, artist_credit e artist per visualizzare
    il nome della release, il nome dell'artista accreditato e il nome dell'artista. Poi abbiamo effettuato 3 join tra le tabelle coinvolte.
    Nella condizione where, abbiamo specificato che il nome dell'artista deve essere diverso da quello accreditato.
*/

/*Query 8:Trovare gli artisti con meno di tre release (il risultato deve contenere il nome dell’artista ed il numero di
release).*/

SELECT artist.name, COUNT(*) AS Numero_Release
FROM artist JOIN release ON artist.id = release.artist_credit
GROUP BY artist.name
HAVING COUNT(*) < 3
ORDER BY artist.name;


/*COMMENTO
    1. Le relazioni coinvolte sono artist e release(una per l'artista e l'altra per poter contare le release).
    Gli attributi utilizzati sono id e  artist_credit da release, id da artist_credit,
    artist_credit e artist da artist_credit_name, id e name da artist.
    2. RELEASE: artist_credit è una chiave esterna, corrispondente alla chiave primaria della relazione artist_credit.
       ARTIST: id è la chiave primaria della tabella.
    3. Per ottenere il risultato voluto, abbiamo effettuato una select sui campi name di artist e sul numero delle release.
    Successivamente abbiamo fatto un join fra le tabelle citate nel primo punto. Dopo abbiamo utilizzato la clausola GROUP BY per artist.name, poichè nella
    select utilizziamo la funzione aggregata count per contare le release di ogni artista. Infine, per verificare che le release per ogni artista
    visualizzato siano minori di 3, abbiamo usato il comando HAVING sulla funzione count, che dev'essere minore di 3.
    4.Sottratto l'insieme ottenuto dalla stessa query senza nessun predicato sul count:
    select artist.name, count(release.id)
    from artist join release on artist.id = release.artist_credit
    group by artist.name
    Il risultato finale, comprende artisti con un numero di release prodotte superiore a 3, la correttezza è dimostrata.
    */

/*Query 9:
Trovare la registrazione più lunga di un’artista donna (il risultato deve contenere il nome 
della registrazione, la sua durata in minuti e il nome dell’artista; 
tenere conto che le durate sono memorizzate in millesimi di secondo)
(scrivere due versioni della query con e senza operatore aggregato MAX).*/


-- CON OPERATORE AGGREGATO MAX
WITH record_women AS ( 
    SELECT recording.name AS Nome_Record, artist.name AS Nome_Artista, recording.length
    FROM recording JOIN artist_credit ON recording.artist_credit = artist_credit.id
	    JOIN artist_credit_name ON artist_credit.id = artist_credit_name.artist_credit
	    JOIN artist ON artist_credit_name.artist = artist.id
	    JOIN gender ON artist.gender = gender.id
WHERE gender.name = 'Female' AND recording.length IS NOT NULL)

SELECT Nome_Record, length/3600 AS Seconda_Lunghezza, Nome_Artista
FROM record_women 
WHERE length = (SELECT MAX(length) FROM record_women);


-- SENZA OPERATORE AGGREGATO MAX
WITH record_women AS ( 
    SELECT recording.name AS Nome_Record, artist.name AS Nome_Artista, recording.length
    FROM recording JOIN artist_credit ON recording.artist_credit = artist_credit.id
	    JOIN artist_credit_name ON artist_credit.id = artist_credit_name.artist_credit
	    JOIN artist ON artist_credit_name.artist = artist.id
	    JOIN gender ON artist.gender = gender.id
WHERE gender.name = 'Female' AND recording.length IS NOT NULL) 


SELECT record1.Nome_Record, record1.length/3600 AS Seconda_Lunghezza, record1.Nome_Artista 
FROM record_women record1 
WHERE NOT EXISTS (SELECT record2.Nome_Record 
                  FROM record_women record2 
                  WHERE record2.length > record1.length);
         
/* COMMENTO         
Le registrazioni sono contenute nell'entità recording:  
per avere le informazioni artist_credit_name, artist_credit, artist e gender dei relativi autori è necessario effetuare join con le tabelle. 
Si è posta la condizione sul campo recording.length "is non null" in modo che non assuma valore nullo.
E' stata utilizzata la clausola with per la risoluzione di questa query: attraverso essa, infatti, è stata creata una tabella temporanea (record_women), che 
contiene tutte le registrazioni di sole artiste donne. Questo ha permesso di estrapolare la registrazione con durata più lunga.
VERSIONE 1:
Si estrapola la registrazione avente come lunghezza la massima contenuta 
in record_women.
VERSIONE 2:
Si utilizza la clausola NOT EXISTS e, nello specifico, si seleziona una registrazione da record_women 
tale che non ne esista un'altra con durata più lunga. 

Per verificare il risultato, è stato semplicemente utilizzata la seguente query:
SELECT * 
FROM record_women 
ORDER BY LENGTH DESC 
la quale restituisce il contenuto di record_women in ordine decrescente rispetto alla lunghezza delle registrazioni. 
Ugualmente, usando ASC anziché DESC si sarebbero ottenuti i record in ordine inverso, dovendo così cercare l'ultimo record. 
Si può così verificare che il primo record sia esattamente quello estrapolato dalle due versioni della query.
*/                  
                  



/*
/*Con l'operatore aggregato MAX*/
SELECT recording.name AS Nome_Record, artist.name AS Nome_Artista, (recording.length/3600) AS Tempo_Massimo
FROM recording JOIN artist_credit ON recording.artist_credit = artist_credit.id
	    JOIN artist_credit_name ON artist_credit.id = artist_credit_name.artist_credit
	    JOIN artist ON artist_credit_name.artist = artist.id
	    JOIN gender ON artist.gender = gender.id
WHERE gender.name = 'Female' AND recording.length = (  SELECT MAX(recording.length)
                                                       FROM recording JOIN artist_credit ON recording.artist_credit = artist_credit.id
	                                                   JOIN artist_credit_name ON artist_credit.id = artist_credit_name.artist_credit
	                                                   JOIN artist ON artist_credit_name.artist = artist.id
	                                                   JOIN gender ON artist.gender = gender.id
                                                       WHERE gender.name='Female');

/*COMMENTO
    1. Le tabelle coinvolte sono recording, artist, artist_credit, artist_credit_name e gender. 
    Gli attributi usati sono name, length e artist_credit per recording, name, id e gender per artist, id per artist_credit,
    artist_credit e artist per artist_credit_name,name e id per gender.
    2. RECORDING: l'attributo name è di tipo varchar, l'attributo length è di tipo integer e rappresenta la durata in millesimi di secondo.
    Artist_credit è una chiave esterna che si riferisce al valore della chiave primaria di artist_credit.
    ARTIST: l'attributo id è la chiave primaria, gender è di tipo integer e si riferisce alla chiave primaria della tabella
    gender: può assumere 3 valori (1,2,3) che corrispondono rispettivamente a Male, Female e Other.
    ARTIST_CREDIT: id è la chiave primaria.
    ARTIST_CREDIT_NAME: artist_credit è la chiave esterna che corrisponde alla chiave primaria di artist_credit, artist corrisponde invece
    a quella di artist.
    GENDER: l'attributo id è chiave primaria, name è un varchar che può assumere tre nomi (Male, Female e Other) che sono
    corrispondenti al sesso dell'artista.
    3. PRIMA VERSIONE: abbiamo utilizzato l'operatore aggregato MAX per trovare la registrazione
    più lunga. Dunque abbiamo effettuato una select sul nome dell'artista e della registrazione e sulla sua durata in minuti. Dopodichè,
    abbiamo utilizzato 4 join per trovare il collegamento tra genere e registrazione (in questo caso dovevamo trovare le registrazioni di tutte
    le artiste donne). Per trovare quella più lunga di una sola artista,invece,nella where abbiamo specificato che la recording.length
    deve essere uguale al massimo delle lunghezze di tutte le registrazioni di artiste donne, facendo dunque un'uguaglianza con la sottoquery
    che restituisce il massimo. */
*/

/*Query 10: 
Elencare le lingue cui non corrisponde nessuna release 
(il risultato deve contenere il nome della lingua, il numero
di release in quella lingua, cioè 0, e essere ordinato per lingua) 
(scrivere due versioni della query).*/


-- PRIMA VERSIONE 
SELECT language.name, COUNT(release.language) AS numero_release
FROM language LEFT JOIN release ON language.id = release.language
GROUP BY language.name
HAVING COUNT(release.language) = 0
ORDER BY language.name;


/*COMMENTO
Le tabelle coinvolte sono Language e Release.
Le lingue non coinvolte sono quelle che non sono collegate a nessuna release.
Per questo è necessario un LEFT JOIN che permette di selezionare anche le lingue non collegate alle release.
Raggruppando per ogni lingua, otteniamo il numero di release scritte nella stessa lingua, da cui vengono selezionate solo le lingue con count = 0.
*/


-- SECONDA VERSIONE 
SELECT language.name, COUNT(release.language)
FROM language LEFT JOIN release ON language.id = release.language
WHERE language.id NOT IN (SELECT DISTINCT language 
                          FROM release 
                          WHERE language IS NOT NULL)
GROUP BY language.name
ORDER BY language.name;

/*COMMENTO
Per ogni lingua si selezionano quelle il cui id non è presente all'interno di release.
Nel sottoproblema si selezionano tutte le lingue utilizzate nelle release in cui il campo lingua non è null.
*/



/*PRIMA VERSIONE
select language.name Lingua, coalesce(release.id,0) Num_Release
from language full join release on language.id = release.language
where language.id = any(
  					select distinct language.id
  					from language
  					group by language.id
   				 except
  				select release.language
  				from release 
				join language on release.language = language.id)
				order by language.name;
/* COMMENTO                

1. Le tabelle coinvolte sono language e release
2. Abbiamo una chiave esterna di release e chiave primaria di language
3.In questo caso per arrivare al risultato desiderato è necessario utilizzare la sotto-query:
    select distinct language.id
    from language join release on language.id=release.language
la quale ci estrae tutte le lingue comuni tra release e language.
4.per verificare il risultato della query abbiamo utilizzato una query con IN:
    select language.name
    from language
    where language.id in (
    select distinct language.id
    from language
    except
    select language.id
    from language join release on language.id = release.language
    )
    order by language.name
    
    I risultati combaciano. */
*/
				

/*SECONDA VERSIONE
select language.name Lingua, coalesce(release.id,0) Num_Release
  from language full join release on language.id = release.language
  where language.id <> all(
                        select distinct language.id
                        from language
                        group by language.id
                        intersect
                        select release.language
                        from release join language on release.language = language.id)
order by language.name;

/* COMMENTO
1. Le tabelle coinvolte anche in questo caso sono language e release.
2. come per la versione precedente abbiamo chiave esterna di release e chiave primaria di language.
3. per arrivare al risultato richiesto abbiamo utilizzato la seguente sotto-query:
        select distinct language.id
    from language
    group by language.id
        intersect
    select release.language
    from release join language on release.language = language.id
    la quale ci estrae le lingue comuni tra release e language
4.per verificare il risultato della query abbiamo utilizzato una query con EXCEPT:
    select language.name Lingua, coalesce(release.id,0) Num_Release
    from language full join release on language.id = release.language
    where language.id = any(
                      select distinct language.id
                      from language
                      group by language.id
                             except
                      select release.language
                      from release join language on release.language = language.id)
    order by language.name;
    
    I risultati combaciano ancora. */
*/





/*Query 11:
Ricavare la seconda registrazione per lunghezza di un artista uomo (il risultato deve comprendere l'artista
accreditato, il nome della registrazione e la sua lunghezza) (scrivere due versioni della query).*/


-- VERSIONE 1: 
WITH record_men AS ( 
    SELECT recording.name AS Nome_Record, artist.name AS Nome_Artista, recording.length
    FROM recording JOIN artist_credit ON recording.artist_credit = artist_credit.id
	               JOIN artist_credit_name ON artist_credit.id = artist_credit_name.artist_credit
	               JOIN artist ON artist_credit_name.artist = artist.id
	               JOIN gender ON artist.gender = gender.id
    WHERE gender.name = 'Male' AND recording.length IS NOT NULL )
   
SELECT Nome_Record, Nome_Artista, length/3600 AS Secondo_Tempo 
FROM record_men 
WHERE length = (SELECT MAX(length) 
                FROM (SELECT length 
                      FROM record_men 
                      EXCEPT 
                      SELECT MAX(length) FROM record_men) record);



-- VERSIONE 2: 
WITH record_men AS ( 
    SELECT recording.name AS Nome_Record, artist.name AS Nome_Artista, recording.length
    FROM recording JOIN artist_credit ON recording.artist_credit = artist_credit.id
	               JOIN artist_credit_name ON artist_credit.id = artist_credit_name.artist_credit
	               JOIN artist ON artist_credit_name.artist = artist.id
	               JOIN gender ON artist.gender = gender.id
    WHERE gender.name = 'Male' AND recording.length IS NOT NULL )

SELECT Nome_Record, Nome_Artista, length/3600 AS Secondo_Tempo 
FROM record_men 
WHERE length = (SELECT MAX(length) 
                FROM record_men 
                WHERE length < (SELECT MAX(length)
                                FROM record_men));
                                
/*COMMENTO                            
La query 11 è stata risolta in maniera molto simile alla query 9: 
qui, infatti, la clausola WITH restituisce le registrazioni degli artisti uomini (anziché quelle delle donne). 
Per ottenere le informazioni cercate sono stati effettuati i join tra le chiavi delle entità 
recording, artist_credit_name, artist_credit, artist e gender 
per poi estrapolare solo i record con gender = "Male" e con registrazioni che 
non abbiano valore null sul campo length. 
PRIMA VERSIONE:
Per estrarre la seconda registrazione per lunghezza, è stato necessario 
escludere dall'insieme di partenza la registrazione di lunghezza massima. 
Nella prima versione, tale problema è stato risolto effettuando una differenza (operatore EXCEPT)
tra la tabella temporanea della WITH e il record con valore massimo sull'attributo LENGTH. 
SECONDA VERSIONE:
Si parte dalla tabella temporanea della WITH, si estrapolano i record che hanno sul campo LENGTH un valore minore del massimo, 
e poi si estrae da questo secondo insieme il massimo. 
Per verificare il risultato, si è utilizzata la seguente query: 
SELECT Nome_Record, length / 3600, Nome_Artista 
FROM record_men 
ORDER BY length DESC 
la quale restituisce le registrazioni degli artisti uomini in ordine decrescente per lunghezza. Ugualmente, usando ASC anziché DESC 
si sarebbero ottenuti i record in ordine inverso, dovendo così cercare il penultimo record. Infine si è controllato che il secondo 
record di tale interrogazione coincidesse con il risultato delle due versioni della query.
*/




/*
/*PRIMA VERSIONE*/
select artist_credit.name,recording.name,recording.length
from recording join artist_credit on recording.artist_credit=artist_credit.id
where recording.length = (

select max(length)
from recording join artist_credit on recording.artist_credit=artist_credit.id
         join artist_credit_name on artist_credit.id=artist_credit_name.artist_credit
         join artist on artist_credit_name.artist=artist.id
         join gender on artist.gender=gender.id
         where gender.name='Male' and length in(
            select recording.length
            from recording join artist_credit on recording.artist_credit=artist_credit.id
            join artist_credit_name on artist_credit.id=artist_credit_name.artist_credit
            join artist on artist_credit_name.artist=artist.id
            join gender on artist.gender=gender.id
            where gender.name='Male'
                except
                select max(length)
                from recording join artist_credit on recording.artist_credit=artist_credit.id
                join artist_credit_name on artist_credit.id=artist_credit_name.artist_credit
                join artist on artist_credit_name.artist=artist.id
                join gender on artist.gender=gender.id
                where gender.name='Male'  
            ))

/*SECONDA VERSIONE*/
select artist_credit.name,recording.name,recording.length
from recording join artist_credit on recording.artist_credit=artist_credit.id
where recording.length = (
select max(length)
from recording join artist_credit on recording.artist_credit=artist_credit.id
         join artist_credit_name on artist_credit.id=artist_credit_name.artist_credit
         join artist on artist_credit_name.artist=artist.id
         join gender on artist.gender=gender.id
         where gender.name='Male'and length < (
            select max(length)
            from recording join artist_credit on recording.artist_credit=artist_credit.id
            join artist_credit_name on artist_credit.id=artist_credit_name.artist_credit
            join artist on artist_credit_name.artist=artist.id
            join gender on artist.gender=gender.id
            where gender.name='Male'  
         ))
         
/*COMMENTO:
    1. Le informazioni provengono dalle tabelle artist_credit, recording, artist_credit_name, gender e artist.
    Gli attributi usati sono name e id per artist_credit, name, length e artist_credit per recording, artist_credit e artist
    per artist_credit_name, name e id per gender, gender e id per artist.
    2. ARTIST_CREDIT: l'attributo name è di tipo varchar e corrisponde al nome dell'artista accreditato. L'attributo id è la chiave primaria.
    RECORDING: name corrisponde al nome della registrazione, length la sua lunghezza e artist_credit è la chiave esterna corrispondente alla
    chiave primaria di artist_credit.
    ARTIST_CREDIT_NAME: artist_credit e artist sono chiavi esterne.
    GENDER: name è un attributo che può assumere come nome il sesso dell'artista ("Male", "Female", "Other") e id è la chiave primaria.
    ARTIST: gender è la chiave esterna corrispondente alla chiave primaria di artist. E' un integer che può assumere 3 valori (1,2,3) corrispondenti
    al sesso. id è la chiave primaria.
    3. PRIMA VERSIONE: abbiamo effettuato la select del nome dell'artista accreditato, del nome della registrazione e della sua lunghezza.
    Abbiamo stabilito che la lunghezza deve essere uguale alla sottoquery che prende la registrazione più lunga di un artista uomo. Tale dato deve
    fare parte della sottoquery che trova le registrazioni di un artista uomo, escludendo quelle che trovano il massimo (EXCEPT). Dunque, in questo modo,
    troviamo la seconda registrazione più lunga.
    SECONDA VERSIONE: in questa versione, invece, abbiamo eguagliato la lunghezza della registrazione con la lunghezza massima delle registrazioni
    di un artista uomo. Successivamente tale valore l'abbiamo posto come minore della sottoquery che restituisce il massimo, in modo tale che 
    venga prelevata la seconda registrazione più lunga.
    4.utilizzo "Coalesce" perchè Recording_length può essere nullo.
    5.Mostro in ordine decrescente di lunghezza i nomi di artisti:
    select recording.name NOME_RECORD,artist.name NOME_ARTISTA , recording.length TEMPO_MASSIMO
    from artist join gender  on artist.gender=gender.id join
    order by TEMPO_MASSIMO desc; */
*/



/*Query 12:
Per ogni stato esistente riportare la lunghezza totale delle registrazioni di artisti 
di quello stato (il risultato deve comprendere il nome dello stato 
e la lunghezza totale in minuti delle registrazioni (0 se lo stato non ha
registrazioni))(scrivere due versioni della query).*/

/*PRIMA VERSIONE*/
SELECT DISTINCT area.name AREA, SUM(COALESCE(recording.length, 0)) / 60000 AS Length_Min
FROM area JOIN area_type ON area.type=area_type.id
	  JOIN artist ON area.id=artist.area
	  JOIN artist_credit_name ON artist.id=artist_credit_name.artist
	  JOIN artist_credit ON artist_credit_name.artist_credit=artist_credit.id
	  JOIN recording ON artist_credit.id=recording.artist_credit
WHERE area_type.name='Country' 
GROUP BY area.id
ORDER BY area.name;

/*SECONDA VERSIONE*/  
select distinct area.name AREA, SUM(COALESCE(recording.length, 0)) / 60000 AS Length_Min
from area_type join area on area.type = area_type.id 
                   join artist on artist.area = area.id 
                   join artist_credit_name on artist.id = artist_credit_name.artist 
                   join artist_credit on artist_credit_name.artist_credit = artist_credit.id 
                   join recording on recording.artist_credit = artist_credit.id 
where area_type.name in (select area_type.name
                         from area join area type on area.type=area_type.id
                         where area_type.name='Country')
GROUP BY area.id
ORDER BY area.name;

 /*COMMENTO:
    1.Le tabelle coinvolte sono area, recording,artist,artist_credit_name,artist_credit e area_type.
    Gli attributi sono name,type e id per area, length e artist_credit per recording, area e id per artist, artist e artist_credit 
    per artist_credit_name, id per artist_credit, name e id per area_type.
    2. AREA: name corrisponde al nome della zona: può essere il nome di una città, di uno Stato, di una regione, ecc.(es Inghilterra, Roma, ecc.)
    Type è un integer ed è chiave esterna, corrispondente alla chiave primaria di area_type.
    id è chiave primaria di area.
    RECORDING: length è di tipo integer e artist_credit è chiave esterna corrispondente alla chiave primaria della tabella omonima.
    ARTIST: area è di tipo integer ed è chiave esterna mentre invece id è chiave primaria.
    ARTIST_CREDIT_NAME: artist e artist_credit sono chiavi esterne corrispondenti alle chiavi primarie delle tabelle omonime.
    ARTIST_CREDIT: id è la chiave primaria
    AREA_TYPE: name è di tipo varchar e rappresenta la tipologia di area ("Country", "Municipality", "City"...).
    id è chiave primaria.
    3.PRIMA VERSIONE: per ottenere il risultato, abbiamo fatto una select sul nome dell'area e sulla somma delle lunghezze di tutte le registrazioni
    convertite in minuti. Per trovare le registrazioni di artisti di un determinato stato, abbiamo effettuato delle join tra le tabelle sopra 
    citate. Nella clausola where, abbiamo specificato che il tipo di area doveva essere uno Stato, ovvero "Country".
    Dato che abbiamo utilizzato la funzione aggregata sum, abbiamo usato il comando GROUP BY su area.id.
    SECONDA VERSIONE: I join si effettuano tra recording-artist_credit, artist_credit_name-artist_credit, artist-artist_credit_name, area-artist,
    area_type-area.
    La sotto-query ci estra tutte le area.name in cui è verificata la condizione area_type.name = 'Country', cioè appunto i country intesi come nazioni.
						 SELECT area_type.name
						 FROM area join area type ON area.type = area_type.id
						 WHERE area_type.name = 'Country'
    4. Abbiamo raggruppato per id perchè name non ha il vincolo di unique.
    5.Usiamo "coalesce" perchè Recording_length può essere nullo
*/





/*Query 13:
Ricavare gli artisti britannici che hanno pubblicato almeno 10 release (il risultato 
deve contenere il nome dell’artista, il nome dello stato (cioè United Kingdom) e il numero di release) 
(scrivere due versioni della query).*/
/*PRIMA VERSIONE*/
SELECT artist.name NOME_ARTISTA, area.name UK, COUNT(DISTINCT release.id) Numero_Di_Release
FROM release JOIN artist_credit ON release.artist_credit = artist_credit.id
	     JOIN artist_credit_name ON artist_credit.id=artist_credit_name.artist_credit
	     JOIN artist ON artist_credit_name.artist=artist.id
	     JOIN area ON artist.area=area.id
WHERE area.name = 'United Kingdom'
GROUP BY artist.id,area.id
HAVING COUNT(DISTINCT release.id) >= 10;



/*SECONDA VERSIONE*/ 
SELECT artist.name NOME_ARTISTA, area.name UK, COUNT(DISTINCT release.id) Numero_Di_Release
FROM area JOIN artist ON artist.area=area.id 
          JOIN release ON release.artist_credit=artist.id
WHERE area.name = any(
                        SELECT area.name
                        FROM area JOIN artist ON artist.area=area.id
                        WHERE area.name = 'United Kingdom' )
GROUP BY artist.id,area.id
HAVING COUNT(DISTINCT release.id) >= 10;
    
/*COMMENTO:
    1. Le tabelle che abbiamo utilizzato sono artist, area, release, artist_credit e artist_credit_name.
    Gli attributi invece sono name, id e area per artist, name e id per area, name e artist_credit per release,
    id per artist_credit, artist e artist_credit per artist_credit_name.
    2. ARTIST: name ha come valore il nome dell'artista. Area è chiave esterna e corrisponde alla chiave primaria di area: è di tipo
    integer. Id è la chiave primaria di artist.
    AREA: name corrisponde al nome dell'area, id è chiave primaria.
    RELEASE: name è il nome della release e artist_credit è chiave esterna corrispondente alla chiave primaria della tabella omonima.
    ARTIST_CREDIT: id è chiave primaria.
    ARTIST_CREDIT_NAME: artist e artist_credit sono chiavi esterne.
    3. PRIMA VERSIONE: Per prima cosa, abbiamo effettuato una select sul nome dell'artista, sul nome dell'area e sul numero delle release, che troviamo
    tramite la funzione aggregata COUNT. Dopodichè, per trovare le release correleate agli artisti, effettuiamo le join tra le tabelle.
    Nella where, siccome vogliamo gli artisti britannici che hanno pubblicato delle release, stabiliamo che l'area deve essere "United Kingdom".
    Infine, per stabilire che le release pubblicate debbano essere almeno 10, utilizziamo la clausola having su count maggiore o uguale a 10.
    SECONDA VERSIONE: Abbiamo effettuato una select  sul nome dell'artista, sul nome dell'area e sul numero delle release, che troviamo
    tramite la funzione aggregata COUNT.
    Nella where abbiamo effettuato una sotto query, effettuando una select su area.name della tabella area in join con artist; in questo modo la sotto-query ci 
    restituisce tutti i nomi di area che corrispondono alla condizione della where, ovvero United Kingdom.
*/


/*Query 14:
Considerando il numero medio di tracce tra le release pubblicate su CD, ricavare gli artisti che hanno pubblicato
esclusivamente release con più tracce della media (il risultato deve contenere il nome dell’artista e il numero di
release ed essere ordinato per numero di release discendente) (scrivere due versioni della query).*/

/*PRIMA VERSIONE*/
SELECT artist.name, COUNT(release.name) 
FROM artist JOIN artist_credit_name ON artist.id = artist_credit_name.artist
            JOIN artist_credit ON artist_credit_name.artist_credit = artist_credit.id
            JOIN release ON artist_credit.id = release.artist_credit
GROUP BY artist.name, release.name
HAVING COUNT(release.name) > ((SELECT DISTINCT COUNT(release.name) 
                               FROM release) / 
                                    (SELECT DISTINCT COUNT(release.name) 
                                     FROM release
                                     JOIN medium ON release.id = medium.release
                                     JOIN medium_format ON medium.format = medium_format.id
                                     WHERE medium_format.name = 'CD')) 
ORDER BY COUNT(release.name) DESC;

/*COMMENTO
Le informazioni correlate sono: 
l'attributo 'name' della tabella artist (NOT NULL) e il conteggio dei valori dell'attributo 'name' nella tabella release.
Al fine di elencare gli artisti che hanno rilasciato più tracce della media delle release su CD si sono unite le tabelle artist e 
artist_credit_name tramite gli attributi 'ID' (chiave primaria) e 'artist' chiave esterna). 
Successivamente si è legato artist_credit usando il vincolo tra la sua chiave primaria 'id' e la chiave esterna 'artist_credit' della 
tabella artist_credit_name. Infine si è legata la tabella release tramite la sua chaive esterna 'artist_credit' e la chiave pimaria 'id' di 
artist_credit.
Infine, si è raggruppato per nome dell'artista e nome del rilascio, a cui si è aggiunta na condizione:
il conteggio dei nomi della release deve essere maggiore dei risultati della query annidata che visualizza il risultato della 
divisione fra il conteggio di tutti i release e dei release su CD.
*/


/*SECONDA VERSIONE*/
SELECT artist.name, COUNT(release.id) AS uscita 
FROM artist JOIN artist_credit_name ON artist.id = artist_credit_name.artist
	        JOIN artist_credit ON artist_credit_name.artist_credit = artist_credit.id
            JOIN release ON artist_credit.id = release.artist_credit
            GROUP BY artist.name, release.name
            HAVING COUNT(release.id) > (SELECT AVG(MEDIA) 
                                        FROM (SELECT COUNT(release.name) AS MEDIA 
                                              FROM release JOIN medium ON release.id = medium.release
                                                           JOIN medium_format ON medium.format = medium_format.id
                                              WHERE medium_format.name = 'CD' 
                                              GROUP BY release.name) AS TabMedia)
												
ORDER BY uscita DESC;


/*COMMENTO:
    1. Le tabelle utilizzate sono artist, release, artist_credit_name, artist_credit, medium e medium_format.
    Gli attributi sono name e id artist, id, name e artist_credit per release, artist e artist_credit per artist_credit_name,
    id per artist credit, release e format per medium, id e name per medium_format.
    2. ARTIST: name corrisponde al nome dell'artista, id è la chiave primaria.
    RELEASE: id è la chiave primaria, name è il nome della release e artist_credit è chiave esterna e corrisponde alla chiave primaria della
    tabella omonima.
    ARTIST_CREDIT_NAME: artist e artist_credit sono chiavi esterne che compaiono come valori di chiave primaria nelle omonime relazioni.
    ARTIST_CREDIT: id è la chiave primaria della relazione.
    MEDIUM: release corrisponde alla chiave primaria della tabella omonima, format è chiave esterna e corrisponde alla chiave primaria di medium_format.
    MEDIUM_FORMAT: id è la chiave primaria della relazione, name corrisponde al nome del supporto audio utilizzato (in questo caso CD).
    3. Nella sotto-query calcolo la media delle release in formato CD.
    4. Prima di tutto effettuo il conteggio di tutte le release in formato CD raggruppate per artista.
    Successivamente le filtro in base a quelle con il valore maggiore di quello restituito dalla query precedente.
*/






/*Query 15:
Ricavare il primo artista morto dopo Louis Armstrong 
(il risultato deve contenere il nome dell’artista, la sua data
di nascita e la sua data di morte) (scrivere due versioni della query).*/

/*PRIMA VERSIONE*/			
SELECT artist.name, make_date(begin_date_year, begin_date_month, begin_date_day) AS data_nascita, 
       make_date(end_date_year, end_date_month, end_date_day) AS data_morte
FROM artist JOIN artist_type ON artist.type = artist_type.id
WHERE artist_type.name = 'Person' AND make_date(end_date_year, end_date_month, end_date_day) =
        (SELECT MIN(make_date(end_date_year, end_date_month, end_date_day))
         FROM artist 
         WHERE make_date(end_date_year, end_date_month, end_date_day) > 
                                                                    (SELECT make_date(end_date_year, end_date_month, end_date_day)
                                                                     FROM artist
                                                                     WHERE artist.name = 'Louis Armstrong'));

/*SECONDA VERSIONE*/
SELECT artist.name, make_date(begin_date_year, begin_date_month, begin_date_day) AS data_nascita,
       make_date(end_date_year, end_date_month, end_date_day) AS data_morte
FROM artist JOIN artist_type ON artist.type=artist_type.id
WHERE artist_type.name = 'Person'
GROUP BY artist.name, data_nascita, data_morte 
HAVING MIN(make_date(end_date_year, end_date_month, end_date_day)) =
            (SELECT MIN(make_date(end_date_year,end_date_month, end_date_day)) AS data_morte
             FROM artist 
             WHERE make_date(end_date_year,end_date_month, end_date_day) > 
                                                    (SELECT make_date(end_date_year, end_date_month, end_date_day)
                                                     FROM artist
                                                     WHERE artist.name = 'Louis Armstrong'));
      
/*COMMENTO
1. le tabelle utilizzate per trovare il risultato desiderato sono artist, artist_type
2. Per quanto riguarda gli attributi abbiamo .type di artist che gode del vincolo di integrità referenziale e .id di artist_type
   che è la chiave primaria quindi non può assumere valori NULL.
   inoltre la chiave di artist è id.
3. PRIMA VERSIONE:per prima cosa abbiamo effettuato una select sul nome dell'artista e sulla data di nascita/morte utilizzando la funzione
make_date. Dopodichè abbiamo messo in join le tabelle indicate al primo punto. Nella where abbiamo specificato che l'artista deve essere
una persona (poichè gli attributi di inizio e fine data si riferiscono alla data di nascita e morte dell'artista) e abbiamo stabilito che
la data di morte deve essere uguale al minimo di tutte le altre date (prima sottoquery), che a sua volta è maggiore della data di morte
di Louis Armstrong.
SECONDA VERSIONE: per prima cosa abbiamo effettuato una select sul nome dell'artista e sulla data di nascita/morte utilizzando la funzione
make_date. Dopodichè abbiamo messo in join le tabelle indicate al primo punto. Qui abbiamo utilizzato la clausola HAVING per uguagliare il minimo
alla sottoquery che restituisce la data  di morte immediatamente successiva a quella di Louis Armstrong. 
*/


/*Query 16:
Elencare le coppie di etichette discografiche che non hanno mai fatto uscire una release in comune ma hanno fatto
uscire una release in collaborazione con una medesima terza etichetta (il risultato deve comprendere i nomi delle
coppie di etichette discografiche) (scrivere due versioni della query).*/
/*PRIMA VERSIONE*/
	SELECT l2.label,l4.label
		FROM release_label l1 JOIN release_label l2 ON (l1.release = l2.release)  
		JOIN  release_label l3 ON(l3.label = l1.label)  
		JOIN release_label l4 ON (l3.release = l4.release)
		WHERE l2.label <> l1.label AND l3.label <> l4.label AND l4.label <> l2.label AND (l2.label <> ALL(
			SELECT l1.label
			FROM release_label l1 JOIN release_label l2 ON (l1.release = l2.release)  
			JOIN  release_label l3 ON (l3.label = l1.label)  
			JOIN release_label l4 ON (l3.release = l4.release)
			WHERE l2.label <> l1.label AND l3.label <> l4.label AND l4.label <> l2.label
			) OR (l2.label = ANY(
				SELECT l1.label
				FROM release_label l1 JOIN release_label l2 ON (l1.release = l2.release)  
				JOIN  release_label l3 ON (l3.label = l1.label)  
				JOIN release_label l4 ON (l3.release = l4.release)
				WHERE l2.label <> l1.label AND l3.label <> l4.label AND l4.label <> l2.label ) AND (l4.label <> l2.label OR l4.label <> l1.label))
			) AND l4.label > l2.label
		GROUP BY l1.label, l2.label, l4.label
		ORDER BY l2.label, l4.label;
		
/*SECONDA VERSIONE*/
    SELECT l2.label, l4.label
		FROM release_label l1 JOIN release_label l2 ON (l1.release = l2.release) 
		JOIN release_label l3 ON (l3.label = l1.label)  
		JOIN release_label l4 ON (l3.release = l4.release)
		WHERE l2.label <> l1.label AND l3.label <> l4.label AND l4.label <> l2.label AND (l2.label NOT IN (
			SELECT l1.label
			FROM release_label l1 JOIN release_label l2 ON (l1.release = l2.release)  
			JOIN  release_label l3 ON (l3.label = l1.label)  
			JOIN release_label l4 ON (l3.release = l4.release)
			WHERE l2.label <> l1.label AND l3.label <> l4.label AND l4.label <> l2.label
		) OR (l2.label IN (
			SELECT l1.label
			FROM release_label l1 JOIN release_label l2 ON (l1.release = l2.release)  
			JOIN  release_label l3 ON (l3.label = l1.label)  
			JOIN release_label l4 ON (l3.release = l4.release)
			WHERE l2.label <> l1.label AND l3.label <> l4.label AND l4.label <> l2.label ) AND (l4.label <> l2.label OR l4.label <> l1.label))
		) AND l4.label > l2.label
		GROUP BY l1.label, l2.label, l4.label
		ORDER BY l2.label, l4.label;
        
/*COMMENTO 
1. Per trovare il risultato richiesto abbiamo bisogno solo della tabella release_label dato che effettuo solo self join
2. Gli attributi di self join che riguardano la tabella abbiamo .id che è chiave primaria e .release che gode del vincolo di integrità
referenziale quindi non possono assumere valori NULL.
3. Eseguiamo il  join per mezzo del campo .release (l1.release = l2.release)
4.Innanzitutto creiamo le coppie basandoci sulle release fatte in comune con altre due ipotetiche coppie a caso (che devono essere
  diverse tra loro e diverse dalle due che voglio estarre). Successivamente nella prima sotto-query controlliamo che il primo membro 
  della coppia abbia fatto release con l'altra etichetta discografica che voglio estrarre. Poi controllero' lo stesso sul secondo
  membro, ma stavolta riferito al primo della coppia.
*/


/*Query 17 (facoltativa):
Trovare il nome e la lunghezza della traccia più lunga appartenente a una release rilasciata in almeno due paesi (il
risultato deve contenere il nome della traccia e la sua lunghezza in secondi) (scrivere due versioni della query).*/
/*PRIMA VERSIONE*/
SELECT name, length/1000/60 AS length 
FROM track
WHERE length = (SELECT MAX(length)
                FROM release_country AS r1
                JOIN release_country AS r2 ON r1.release= r2.release AND r1.country <> r2.country
                JOIN release ON release.id = r1.release
                JOIN artist_credit ON release.artist_credit = artist_credit.id
                JOIN track ON track.artist_credit = artist_credit.id);



/*SECONDA VERSIONE*/
SELECT DISTINCT(track.name), length/1000/60
FROM release_country AS r1 
JOIN release_country AS r2 ON r1.release=r2.release AND r1.country<>r2.country 
JOIN release ON release.id = r1.release
JOIN artist_credit ON release.artist_credit = artist_credit.id
JOIN track ON track.artist_credit = artist_credit.id
WHERE length IS NOT NULL AND length >= ALL
                                    (SELECT length FROM release_country AS r1 
                                     JOIN release_country AS r2 ON r1.release = r2.release AND r1.country <> r2.country 
                                     JOIN release ON release.id = r1.release
                                     JOIN artist_credit ON release.artist_credit = artist_credit.id
                                     JOIN track ON track.artist_credit=artist_credit.id
                                     WHERE length IS NOT NULL);
                                     
/*COMMENTO PRIMA VERSIONE
1.Per rispondere all'interrogazione abbiamo bisogno delle tabelle Release_country, Release, Artist_credit e Track
2.Gli attributi di self join su Release_country .release e .country sono chiavi primarie, di conseguenza non
ammettono valori NULL; allo stesso modo gli attributi .id di Release, .release di Release_country r1, .artist_credit di Release, 
.id di Artist_credit sono chiavi quindi non ammettono valori NULL.
Per quanto riguarda invece .artist_credit di Track e .id di artist_credit godono del vincolo di integrità referenziale  e sono
chiavi quindi non ammettono valori NULL
3. Per trovare i nome e la lunghezza della traccia più lunga appartenente a una release rilasciata in almeno due paesi effettuiamo 
una selezione di .mane e di .length da Track dove l'attributo length dev'essere uguale al massimo di length che prendiamo utilizzando
l'operatore MAX.
Successivamente si effettua un self join sull'attributo release_country specificando che l'attributo release di r1 dev'essere uguale
a quello di r2 in modo da prendere la stessa release e che l'attributo .country di r1 dev'essere diverso da quello di r2 per specificare
che i paesi devono essere diversi(in questo modo si soddisfa la condizione che devono essere almeno due).
Dopodichè si effettua un ulteriore join con release confrontando gli attributi .id e .release in join con artist_credit
confrontando .artist_credit con .id ed infine in join con Track confrontando .artist_credit con .id per poter ottenere tutte le informazioni
richieste.

COMMENTO SECONDA VERSIONE
1.Per rispondere all'interrogazione abbiamo bisogno delle tabelle Release_country, Release, Artist_credit e Track
2.Gli attributi di self join su Release_country .release e .country sono chiavi primarie, di conseguenza non
ammettono valori NULL; allo stesso modo gli attributi .id di Release, .release di Release_country r1, .artist_credit di Release, 
.id di Artist_credit sono chiavi quindi non ammettono valori NULL.
Per quanto riguarda invece .artist_credit di Track e .id di artist_credit godono del vincolo di integrità referenziale  e sono
chiavi quindi non ammettono valori NULL.
3.Per trovare il nome e la lunghezza della traccia più lunga appartenente a una release rilasciata in almeno due paesi:  Nella sottoquery esterna
facciamo la selezione dell'attributo .name di Track, specificando con una DISTINCT di non prendere più valori uguali per la traccia.
Poi si effettua una self join sulla tabella release_country specificando che l'attributo release di r1 dev'essere uguale a quello di r2 in modo da 
prendere la stessa release e che l'attributo .country di r1 dev'essere diverso da quello di r2 per specificare che i paesi devono essere più di uno.
Dopodichè si effettua un ulteriore join con Release confrontando gli attributi .id e .release in join con artist_credit confrontando .artist_credit 
con .id e infine in join con Track confrontando .artist_credit con .id per poter ottenere tutte le informazioni richieste dall'interrogazione.
Nella condizione WHERE si specifica che l'attributo .length dev'essere NOT NULL e maggiore di tutti i risultati estratti dalla query interna.
Nella sottoquery interna si seleziona length da Release_Country (r1) in self join con Release_Country (r2) sull'attributo release_country specificando 
che l'attributo release di r1 dev'essere uguale a quello di r2 in modo da prendere la stessa release e che l'attributo .country di r1 dev'essere diverso 
da quello di r2 per specificare che i paesi devono essere più di uno.
Dopodichè si effettua un ulteriore join con Release confrontando gli attributi .id e .release in join con artist_credit confrontando .artist_credit con 
.id e infine in join con Track confrontando .artist_credit con .id per poter ottenere tutte le informazioni richieste dall'interrogazione.
Infine nella condizione WHERE si specifica che l'attributo length dev'essere NOT NULL.


*/





