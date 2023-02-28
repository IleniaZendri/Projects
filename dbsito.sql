-- phpMyAdmin SQL Dump
-- version 4.8.4
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1:3306
-- Creato il: Ott 27, 2019 alle 02:36
-- Versione del server: 5.7.24
-- Versione PHP: 7.2.14

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `dbsito`
--

-- --------------------------------------------------------

--
-- Struttura della tabella `commenti`
--

DROP TABLE IF EXISTS `commenti`;
CREATE TABLE IF NOT EXISTS `commenti` (
  `ricetta_id` int(11) NOT NULL,
  `utente_id` int(11) NOT NULL,
  `testo` text NOT NULL,
  `timestamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Dump dei dati per la tabella `commenti`
--

INSERT INTO `commenti` (`ricetta_id`, `utente_id`, `testo`, `timestamp`) VALUES
(5, 1, '', '2019-10-19 09:36:02'),
(5, 1, 'Deliziosa', '2019-10-19 09:36:10'),
(5, 1, 'Deliziosa', '2019-10-19 09:36:13'),
(5, 1, 'Deliziosa', '2019-10-19 09:36:14'),
(15, 1, '', '2019-10-19 09:38:55'),
(15, 1, 'Buono', '2019-10-19 09:38:58'),
(5, 1, 'prova', '2019-10-19 19:52:26'),
(5, 1, 'prova', '2019-10-19 19:53:30'),
(5, 1, 'wwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwww', '2019-10-22 20:22:31');

-- --------------------------------------------------------

--
-- Struttura della tabella `guarda_dopo`
--

DROP TABLE IF EXISTS `guarda_dopo`;
CREATE TABLE IF NOT EXISTS `guarda_dopo` (
  `utente_id` int(11) NOT NULL,
  `ricetta_id` int(11) NOT NULL
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Dump dei dati per la tabella `guarda_dopo`
--

INSERT INTO `guarda_dopo` (`utente_id`, `ricetta_id`) VALUES
(3, 5),
(3, 1),
(3, 1);

-- --------------------------------------------------------

--
-- Struttura della tabella `ricetta`
--

DROP TABLE IF EXISTS `ricetta`;
CREATE TABLE IF NOT EXISTS `ricetta` (
  `id_ricetta` int(11) NOT NULL AUTO_INCREMENT,
  `titolo_ricetta` varchar(100) NOT NULL,
  `immagine_ricetta` varchar(100) NOT NULL,
  `tipo_ricetta` varchar(10) NOT NULL,
  `descrizione_ricetta` varchar(2000) NOT NULL,
  PRIMARY KEY (`id_ricetta`)
) ENGINE=MyISAM AUTO_INCREMENT=18 DEFAULT CHARSET=latin1;

--
-- Dump dei dati per la tabella `ricetta`
--

INSERT INTO `ricetta` (`id_ricetta`, `titolo_ricetta`, `immagine_ricetta`, `tipo_ricetta`, `descrizione_ricetta`) VALUES
(1, 'Pancake allo sciroppo d\'acero', 'img/pancakeAlloSciroppoDAcero.jpg', '004', 'Se state progettando un viaggio negli Stati Uniti, preparatevi a degustare il tradizionale american breakfast con i deliziosi pancake, piccoli dischi spugnosi e saporiti che non aspettano altro che essere farciti con le leccornie più invitanti: la tradizione li vuole conditi con il tipico sciroppo d’acero, un dolcificante naturale con un gusto che ricorda quello del miele, usato anche per i fluffy pancake. Ma accanto a questo tipico abbinamento sono ammesse tutte le varianti possibili, a patto che siano super golose: dalla frutta fresca o sciroppata, alle creme fino alle salse al cioccolato. Gli americani non si fanno mancare proprio nulla e spesso accompagnano il piatto di pancake con ciuffi di panna montata, come si fa a resistere? Senza contare che in uno dei classici bar farete davvero difficoltà a scegliere tra le varie torte e gli innumerevoli tipi di muffin, compresi quelli senza glutine. Di sicuro avrete già l’acquolina in bocca, se non volete aspettare la colazione potete preparare i pancake anche per merenda, ecco la nostra ricetta!'),
(2, 'Torta Magica al cioccolato', 'img/tortaMagicaAlCacao.jpg', '004', 'Se avete già scoperto, amato, provato e riprovato la classica torta magica, non può sfuggirvi la sua golosa variante al cacao! Sostituendo parte della farina con il cacao amaro in polvere realizzerete un dolce altrettanto sorprendente… con un unico impasto infatti otterrete tre strati di diversa consistenza: uno superficiale tipo pan di spagna, uno centrale più cremoso e quello inferiore simile ad un flan. Anche in questo caso, per far sì che la magia avvenga gli ingredienti devono essere aggiunti in un ordine ben preciso e alla temperatura indicata. Non occorrono bacchette magiche quindi, solo una grande attenzione ai dettagli, ma non è detto che i vostri ospiti debbano venire per forza a conoscenza del trucco… siete pronti a far uscire dal cilindro una favolosa torta magica al cacao?'),
(4, 'Polpo in umido', 'img/polpoInUmido.jpg', '002', 'Sono davvero tanti i piatti che accomunano le regioni del nostro Bel paese, da nord a sud, ciascuno con le proprie peculiarità. E sempre più spesso è facile ritrovare un filo conduttore. Per esempio, nell’area del mediterraneo, ci sono profumi e colori che caratterizzano la cucina: come il pomodoro, l’aglio e l’origano. Uno dei molluschi più versatili e anche più utilizzati nella nostra cucina è il polpo. Immancabile sulle tavole delle feste preparato all’insalata o in carpaccio, o negli strati di pasta sotto forma di lasagne e persino per le cene informali in casa avvolto in un wrap! Ma oggi abbiamo deciso di stare sul classico preparando un gustosissimo polpo in umido. Attenzione, da non confondere con quello alla Luciana, che è una ricetta napoletana dove il mollusco suole cuocersi nella sua acqua. Questa deliziosa preparazione ha un ottimo condimento che è perfetto per inzuppare il pane, accompagnare i pezzetti di polpo intenerito dalla cottura nel pomodoro sarà davvero un piacere per il palato! Perciò siete pronti a preparare il polpo in umido? Ecco la nostra ricetta!'),
(3, 'Castagnaccio alla toscana', 'img/castagnaccioAllaToscana.jpg', '004', 'Il castagnaccio, o baldino o pattona, è un dolce tipico preparato con la farina di castagne e arricchito con uvetta, pinoli, noci e rosmarino. Difficile stabilire l\'esatta origine del castagnaccio. Noi abbiamo realizzato il castagnaccio alla toscana, ma questa ricetta è diffusa anche in Veneto, Piemonte, Lombardia... tutte regioni in cui la castagna è un ingrediente molto diffuso in cucina. Quello che accomuna tutte le varianti è l\'origine contadina del castagnaccio: acqua, farina di castagne e rosmarino. Già nel \'500 il castagnaccio era molto conosciuto e apprezzato tanto che un padre agostiniano lo cita in un suo scritto. Sembra che l’ideatore del castagnaccio sia stato proprio il toscano Pilade da Lucca che viene nominato nel \"Commentario delle più notabili et mostruose cose d’Italia et altri luoghi\" scritto da Ortensio Orlando e pubblicato a Venezia nel 1553. Fu però a partire dall’800 che i toscani esportarono il castagnaccio nel resto d’Italia e proprio in questo periodo venne arricchito con uvetta, pinoli e rosmarino. Ogni famiglia custodisce la ricetta del castagnaccio, noi vi proponiamo la nostra!'),
(5, 'Minestra di patate', 'img/minestraDiPatate.jpg', '001', 'Il modo migliore per salutare l\'autunno appena iniziato? Portare in tavola una buona minestra calda e saporita, capace di scaldarci durante le prime giornate un po\' più fresche. La minestra di patate è un piatto semplice, che unisce la consistenza morbida delle patate a quella del riso, dando vita a un primo piatto rustico e pieno di gusto. Insaporita dal timo e da un tocco di pomodoro, questa minestra è un comfort food sostanzioso, perfetto per una cena in tutto relax dopo una lunga giornata, ma anche come pranzo del giorno dopo: la minestra di patate, infatti, è in grado di smentire chiunque dica che le minestre riscaldate non sono buone. Se riuscite a conservarne un po\', provatela riscaldata, magari con l\'aggiunta di un po\' di peperoncino: sarà ancora più cremosa e saporita!'),
(6, 'Biscotti al The verde', 'img/biscottiAlTheVerde.jpg', '004', 'Per i veri amanti del tè del pomeriggio la scelta dei biscotti non può essere lasciata al caso: alla cannella, al limone o allo zenzero, sono tanti gli abbinamenti possibili a seconda dei propri gusti e della fragranza della miscela. Con i biscotti al tè verde però non avrete dubbi! Perfetti da accompagnare a una tazza di questa bevanda dalle mille proprietà, sono realizzati con una frolla semi-integrale farcita con una ganache al cioccolato che mette in risalto il delicato aroma del tè senza sovrastarlo. Eleganti e raffinati, i biscotti al tè verde sapranno deliziare i vostri ospiti con la loro semplice originalità!'),
(7, 'Gnocchi al gorgonzola', 'img/gnocchiAlGorgonzola', '001', 'Quando gli gnocchi di patate incontrano il formaggio, l’effetto filante è assicurato! Già nella ricetta degli gnocchi ai 4 formaggi vi avevamo deliziato con un condimento cremoso dal gusto intenso, e cavalcando l’onda di questi sapori vi proponiamo qui gli gnocchi al gorgonzola, morbidi bocconcini che invertono le regole custodendo al loro interno un cuore denso e scioglievole da scoprire al primo morso. Basterà aggiungere al tradizionale procedimento degli gnocchi fatti in casa il passaggio della farcitura e, ad uno ad uno con pazienza, questi gnocchetti prenderanno forma e una volta portati in tavola il loro gusto superlativo ripagherà tutti gli sforzi!'),
(8, 'Muffin Fantasma', 'img/muffinFantasma.jpg', '004', 'Halloween è la festa più paurosa dell’anno: fantasmi, streghe e zucche popolano l’immaginario dei bambini durante questa giornata. Per far rivivere questa atmosfera spettrale con un pizzico di ironia e originalità abbiamo deciso di proporvi questi deliziosi muffin fantasma. Non abbiate paura: non esiste nulla di più dolce e zuccherino di questi dolcetti! L’impasto dei muffin, realizzato con latte condensato per ottenere la giusta morbidezza, è addolcito da gocce di cioccolato bianco. Scoprite come è semplice creare la spettrale copertura e gli occhietti di questi dispettosi fantasmini, li vedrete apparire e... \"sparire\" in fretta dalla tavola in festa! Se desiderate arricchire il vostro buffet a tema, date un\'occhiata alle altre nostre mostruose ricette di cupcake e muffin!'),
(9, 'Nidi di rondine', 'img/nidiDiRondine.jpg', '001', 'Si dice che una rondine non faccia primavera ma i nidi di rondine che vi presentiamo oggi, invece, fanno proprio ricetta della domenica in famiglia. Una sfoglia di pasta fresca preparata in casa, proprio come quella che faceva la nonna con farina e uova fresche e poi farcita con funghi, prosciutto e formaggi... un irresistibile piatto domenicale ricco e saporito. Queste deliziose girandole oltre ad essere buone sono anche molto belle da vedere, infatti il loro ripieno arrotolato nella sfoglia all’uovo ricorda molto una rosa... sì, ma tutta da mangiare! Per questa domenica provate i nidi di rondine, siamo sicuri che faranno la gioia dei vostri commensali e anche la vostra nel sentirvi dire che siete stati dei cuochi bravissimi!'),
(10, 'Gallinella fritta', 'img/gallinellaFritta.jpg', '002', 'La gallinella, detta anche coccio, ha un sapore davvero irresistibile. Gli amanti del pesce sanno benissimo di cosa parliamo, del vero piacere che si prova nel gustare un buon piatto di paccheri o anche un semplice e raffinato guazzetto. Ma tutti sappiamo che al di là di ogni cosa la frittura è la vera apoteosi dei cibi... e perciò perché non provare la gallinella fritta? L\'occasione perfetta per riscoprire quanto possa essere buono il piacere del pesce fritto! In questa versione abbiamo però pensato di preparare una pastella con farina integrale, un tocco di originalità e sapore in più! E il contorno? Per essere coerenti, abbiamo pensato di friggere brevemente anche pomodori e friggitelli perché il piccantino e l\'acido di questi due ingredienti ci sta benissimo con la gallinella fritta, ma voi potete anche scegliere altre verdure di vostro gradimento! Non vi resta che mettervi ai fornelli insieme a noi.'),
(11, 'Patate al forno', 'img/patateAlForno.jpg', '003', 'Una tira l’altra ma non sono le ciliegie... sono le patate! Oggi vi deliziamo con un classico intramontabile della cucina casalinga: le patate al forno. Impossibile non amare questo contorno tipico che sta bene su tutto: carne o pesce a voi la scelta, unica regola è che le patate al forno siano dorate e croccanti, in una parola irresistibili! Come tutte le ricette che al primo sguardo appaiono semplici, anche questa può nascondere delle insidie nella preparazione. Ognuno custodisce il segreto per patate al forno perfette: c\'è chi le passa sotto acqua corrente fredda o in ammollo per eliminare l\'amido, chi le cuoce in forno statico prima coperte con carta alluminio e poi scoprendo la teglia, chi ancora misura millimetricamente gli spicchi o i cubetti per farli tutti uguali e garantire la cottura uniforme. Siete curiosi di conoscere quali sono i nostri trucchi per un risultato da applausi a tavola? Siamo felici di condividerli con voi per farvi assaporare un contorno gustoso che piace sempre a tutti! Per insaporire le patate al forno abbiamo scelto rosmarino e timo, degli evergreen irrinunciabili. Provate anche la versione alla birra, dall\'aroma deciso e particolare, oppure quella alla mediterranea, arricchita con pomodoro e olive! Se siete amanti della cottura al forno, nella stagione autunnale non potrete perdere un altro contorno ugualmente facile e sfizioso da preparare: la zucca al forno! '),
(12, 'Peperonata', 'img/peperonata.jpg', '003', 'La peperonata è un gustosissimo contorno estivo, ideale come accompagnamento a carni delicate o pesce. Esiste anche una variante regionale umbra della peperonata, una ricetta casalinga e molto genuina: la bandiera. Ma i peperoni sono amati in tutta la penisola e il modo di preparare questo piatto estivo varia di luogo in luogo. Perfetta anche solo per essere gustata su calde bruschette, la peperonata è davvero ottima anche servita come sugo nella pasta!'),
(13, 'Parmigiana di melanzane', 'img/parmigianaDiMelanzane.jpg', '002', 'Basta nominarla perché a tavola ci sia un\'ovazione. E\' la regina dei piatti unici, la consolatrice di umori avviliti: la Parmigiana di melanzane. Un piatto condiviso e conteso come origini da nord a sud: Emilia Romagna, Campania (Parmigiana \'e mulignane) e Sicilia (Parmiciana o Patrociane) con alcune varianti di ingredienti e modalità di composizione, ma tutte assolutamente favolose! Vi siete mai chiesti perché si chiami così questo piatto? Il nome \"Parmigiana\" deriverebbe proprio dal siciliano \"Parmiciana\", che in dialetto indica pila di listelle di legno delle persiane: pensate infatti a come vengono disposte le fette di melanzane in teglia e noterete le similitudini. Pochi ingredienti, tanto sapore per un piatto simbolo delle pietanze mediterranee: pomodoro, melanzane, basilico e formaggio. Preparate insieme a noi una succulenta parmigiana di melanzane, farciteci frangranti panini e lasciatevi tentare da altre varianti che trovate sul nostro sito: zucchine, patate, cardi e alici!'),
(14, 'Spaghetti alle vongole', 'img/spaghettiAlleVongole.jpg', '001', 'Direttamente dalla tradizione campana gli spaghetti alle vongole, decisamente uno dei più importanti piatti della cucina italiana e più amati tra i primi piatti di pesce. Una ricetta semplicissima e che conferisce grandissimo sapore a spaghetti o, per qualcuno, vermicelli. Oltre al meraviglioso sapore di mare che il condimento conferisce alla pasta bisogna sottolineare che questo è un primo piatto sciuè sciuè a dispetto di altri grandi classici campani. Già, infatti non è detto che in tutte le case napoletane si respiri soltanto il tradizionale ragù che sta \"pippiando\" da svariate ore. In tanti preparano gli spaghetti alle vongole come alternativa più veloce. Ma oltre ad essere il piatto della domenica è la vera e propria icona del cenone di Natale o Capodanno.'),
(15, 'Mozzarella in carrozza', 'img/mozzarellaInCarrozza.jpg', '000', 'Solo uno chef di origini partenopee poteva rivelarci tutti i segreti per realizzare la mozzarella in carrozza: una ricetta sfiziosa, diffusa anche nel Lazio con alcune differenze, molto semplice da preparare e che richiede piccoli accorgimenti per ottenere un risultato perfetto. Lo chef Andrea Aprea li mostrerà in pochi e fondamentali passaggi. Primo accorgimento tra tutti, la qualità degli ingredienti soprattutto per la mozzarella che deve essere di bufala e preferibilmente del giorno prima. La mozzarella in carrozza, infatti, nasce come una ricetta di riciclo per consumare tutto ciò che rimane dal pranzo precedente e trasformarlo in qualcosa di veramente appetitoso! Questi fagottini dal guscio fragrante e dal cuore filante piaceranno proprio a tutti grazie alla loro semplicità, ma se volete realizzare una versione gourmet lo chef Aprea consiglia di aggiungere all\'interno acciughe, pomodori secchi, basilico e pepe nero. Un\'esplosione di sapori che potrete esaltare utilizzando nella seconda panatura il panko, uno speciale pangrattato Giapponese, al posto del pangrattato classico: la croccantezza che ne risulterà conquisterà tutti morso dopo morso!');

-- --------------------------------------------------------

--
-- Struttura della tabella `tipo_ricetta`
--

DROP TABLE IF EXISTS `tipo_ricetta`;
CREATE TABLE IF NOT EXISTS `tipo_ricetta` (
  `codice_tipo` varchar(3) NOT NULL,
  `nome_tipo` varchar(100) NOT NULL,
  PRIMARY KEY (`codice_tipo`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Dump dei dati per la tabella `tipo_ricetta`
--

INSERT INTO `tipo_ricetta` (`codice_tipo`, `nome_tipo`) VALUES
('001', 'Primo'),
('002', 'Secondo'),
('004', 'Dessert'),
('003', 'Contorno');

-- --------------------------------------------------------

--
-- Struttura della tabella `utente`
--

DROP TABLE IF EXISTS `utente`;
CREATE TABLE IF NOT EXISTS `utente` (
  `id_utente` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(100) CHARACTER SET utf8 NOT NULL,
  `password` varchar(100) CHARACTER SET utf8 NOT NULL,
  `email` varchar(100) CHARACTER SET utf8 NOT NULL,
  PRIMARY KEY (`id_utente`),
  UNIQUE KEY `username` (`username`) USING BTREE,
  UNIQUE KEY `email` (`email`)
) ENGINE=MyISAM AUTO_INCREMENT=4 DEFAULT CHARSET=latin1;

--
-- Dump dei dati per la tabella `utente`
--

INSERT INTO `utente` (`id_utente`, `username`, `password`, `email`) VALUES
(1, 'Ilenia', '9710f9c1b81f9fb272ba005a98c73c43', 'ileniazendri@gmail.com'),
(2, 'Ilenia97', '9710f9c1b81f9fb272ba005a98c73c43', 'ciaozendri@gmail.com'),
(3, 'Prova', 'e10adc3949ba59abbe56e057f20f883e', 'prova@gmail.com');
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
