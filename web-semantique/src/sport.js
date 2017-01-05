/* Fonction permettant d'afficher les résultats des recherches Google Custom Search*/
(function() {
	var cx = '013632266919387871672:pgb1ce2nwuq';
	var gcse = document.createElement('script');
	gcse.type = 'text/javascript';
	gcse.async = true;
	gcse.src = 'https://cse.google.com/cse.js?cx=' + cx;
	var s = document.getElementsByTagName('script')[0];
	s.parentNode.insertBefore(gcse, s);
})();

/*Fonction permettant de faire les appels au serveur lorsque l'utilisateur clique sur le bouton rechercher*/
$(document).on('click', "input.gsc-search-button", getResultsThemes);

/*Fonction permettant de faire les appels au serveur lorsque l'utilisateur appui sur le bouton "Entrée"*/
$(document).on('keyup', '#gsc-i-id1', function(e) {
	console.log(e.which);
    if (e.which == 13) {
        e.preventDefault();
		if($('#gsc-i-id1').val() != ""){
			getResultsThemes();
		}
    }
});

 /*Fonction permettant de récupérer les 10 premiers résultats Google Custom Search via l'utilisation de l'API, en format JSON, et de lancer leur traitement*/
function getResults(){
	var waitMessage = '<div class="well"><h3>Searching for new themes...</h2></div>';
	$( "#themes" ).html( waitMessage );
	var input = $('#gsc-i-id1').val(); 
	$.ajax({
		type: 'GET',
		url: "https://www.googleapis.com/customsearch/v1?key=AIzaSyABrXjqXg28Np8AcFzf4_A1tALvf8pWVzs&cx=013632266919387871672:pgb1ce2nwuq&q=" + input + "&alt=json",
		timeout: 3000,
		success: function(data) {
		  console.log(data);
		  //Exemple de résultat
		  var ephData = '{"clusters":[{"pages":[{"classement":0,"url":"http://www.ncaa.com/sports/football"}],"nom":"College Football - Home | NCAA.com "},{"pages":[{"classement":1,"url":"http://www.espn.com/college-football/"}],"nom":"NCAA College Football Teams, Scores, Stats, News, Standings, Rumors - ESPN "},{"pages":[{"classement":2,"url":"http://www.ncaa.com/sports/football/fbs"},{"classement":6,"url":"http://www.ncaa.com/"},{"classement":7,"url":"http://bleacherreport.com/college-football"}],"nom":"FBS College Football - Home | NCAA.com"},{"pages":[{"classement":3,"url":"http://www.nbcsports.com/college-football"}],"nom":"College Football "},{"pages":[{"classement":4,"url":"http://www.ncaa.com/scoreboard/football/fbs"}],"nom":"Scores - College Football FBS "},{"pages":[{"classement":5,"url":"http://www.cbssports.com/college-football/"}],"nom":"NCAA Football - College News, Scores, Stats, Standings, Rumors "},{"pages":[{"classement":8,"url":"http://www.foxsports.com/fantasy/collegefootball/pickem/"},{"classement":9,"url":"http://www.ncaa.org/"}],"nom":"FOX Sports College Football Pick\'em"}]}';
			getResultsThemes(data); //A utiliser pour utiliser notre API(trèèès longue)
			/*displayThemes(JSON.parse(ephData).clusters);*/},
		error: function() {
		console.log('La requête n\'a pas abouti');}
	  });
}

 /*Fonction permettant de récupérer la liste des clusters correspondant aux sites web renvoyés par google custom search via l'utilisation de notre serveur*/
function getResultsThemes()
{
	var input = $('#gsc-i-id1').val(); 
	//var ephData = '{"clusters":[{"pages":[{"classement":0,"url":"http://www.ncaa.com/sports/football"}],"nom":"College Football - Home | NCAA.com "},{"pages":[{"classement":1,"url":"http://www.espn.com/college-football/"}],"nom":"NCAA College Football Teams, Scores, Stats, News, Standings, Rumors - ESPN "},{"pages":[{"classement":2,"url":"http://www.ncaa.com/sports/football/fbs"},{"classement":6,"url":"http://www.ncaa.com/"},{"classement":7,"url":"http://bleacherreport.com/college-football"}],"nom":"FBS College Football - Home | NCAA.com"},{"pages":[{"classement":3,"url":"http://www.nbcsports.com/college-football"}],"nom":"College Football "},{"pages":[{"classement":4,"url":"http://www.ncaa.com/scoreboard/football/fbs"}],"nom":"Scores - College Football FBS "},{"pages":[{"classement":5,"url":"http://www.cbssports.com/college-football/"}],"nom":"NCAA Football - College News, Scores, Stats, Standings, Rumors "},{"pages":[{"classement":8,"url":"http://www.foxsports.com/fantasy/collegefootball/pickem/"},{"classement":9,"url":"http://www.ncaa.org/"}],"nom":"FOX Sports College Football Pick\'em"}]}';
	if(input.replace(/\s/g, '')!=""){
		var waitMessage = '<div class="well" id="waiting"><h3>Searching for new themes...</h3></div>';
		$( "#themes" ).html( waitMessage );
		 $.ajax({
		   url : 'http://localhost:4567/themes?request=' + input,
		   type : 'GET', // Le type de la requête HTTP, ici devenu POST
		   headers: {
						'Access-Control-Allow-Origin': '*'
					},
		   dataType : 'json',
		   success: function(data) {
			  console.log(data);
			  //displayThemes(data.clusters);
			  searchImages(data); //A décommenter pour obtenir les images de chaque vignette (et commenter displayThemes(data.clusters); au-dessus)
			},
			error: function() {
			  $( "#waiting" ).html( '<h3>Clusters haven\'t been retrieved...</h3>' ); }
		});
	}
}

/* Fonction permettant de tronquer une chaîne de caractère*/
String.prototype.trunc = String.prototype.trunc ||
      function(n){
          return (this.length > n) ? this.substr(0,n-1)+'&hellip;' : this;
      };

/*Fonction permettant d'afficher les clusters obtenus sous la forme de vignettes*/
function displayThemes(clusters, imageArray) {
	var nbThumbnails = 6; // Nombre de vignettes par page
	var thumbnails = ""; //Code HTML des vignettes
	var length = clusters.length; //Nombre de clusters
	var nbMaxLinksDisplayed = 3; // Nombre de lien par popover
	var nbLinks; // Nombre de lien pour un cluster
	console.log(length);
	var i;
	var j;
	var modulo;
	var modTot = Math.floor(length/(nbThumbnails+1)); //Nombre de pages de vignettes
	var beginning; //Début du code HTML des snippets
	var lastThumb; //Booléen permettant de savoir si une vignette est la dernière de la page (pour un meilleur afiichage du popover)
	for(i=0; i < length; i++){
		nbLinks = clusters[i].pages.length;
		modulo = (i+1)%nbThumbnails;
		lastThumb = (modulo == 0 && i>0);
		nbLinksDisplayed = Math.min(nbMaxLinksDisplayed, nbLinks);							
		thumbnails = thumbnails + '<div class="col-sm-2 col-md-15">' +
						  '<a class="thumbnail" name="' + clusters[i].nom + '" href="#">' + 
							'<img src="'/*http://placehold.it/250x250"'*/ + imageArray[i].link + '" alt="Image" style="max-width:100%;" />' +
							'<div class="caption">' +
							'<h5 class="snippet">' + clusters[i].nom.trunc(55) + '</h5></div></a>' +
							'<span class="btn ' + (lastThumb ? 'lastPopov' : 'popov') +'" rel="popover" data-content="<table class=\'table\'>';
		/*Affichage de chaque vignette*/
		for(j=0 ; j < nbLinksDisplayed ; j++){
			thumbnails = thumbnails + '<tr> <td>' + '<a href=\'' + clusters[i].pages[j].url + '\' title=\''+ clusters[i].pages[j].url +'\'>' + 
			clusters[i].pages[j].url + '</a></td> </tr>';
		}
		thumbnails = thumbnails + '</table>"' + 
								' title="Links">' +
								'<i class="glyphicon glyphicon-chevron-down"></i>' +
								'</span>' +
								'</div>';
		if(lastThumb) {
			thumbnails = thumbnails + "</div>" +
					"</div>";
					if(i != length-1) {
						thumbnails = thumbnails + '<div class="item">' +
						'<div class="row-fluid">';
					}
		} else {
			if (i == length -1){
				thumbnails = thumbnails + "</div>" +
					"</div>";
			}
		}
	}
	beginning = '<div class="row">' +
						'<div class="col-md-12">' +
						'<div class="well">' +
						'<div id="myCarousel" class="carousel slide">' +
						'<ol class="carousel-indicators">' +
						'<li data-target="#myCarousel" data-slide-to="0" class="active"></li>';
								
	/*Affichage des points permettant de changer de page de vignettes*/
	for(i=0; i < modTot; i++) {
		beginning = beginning + '<li data-target="#myCarousel" data-slide-to="' + (i+1) +'"></li>';
	}					 
	beginning = beginning +	'</ol>' +
							'<div class="carousel-inner">' +
							'<div class="item active">' +
							'<div class="row-fluid">';
	thumbnails = thumbnails + '</div>' +
                '<a class="left carousel-control" href="#myCarousel" data-slide="prev"></a>' +
				'<a class="right carousel-control" href="#myCarousel" data-slide="next"></a>' +
				'</div>' +  
				'</div>' +  
				'</div>' +
				'</div>';
								
	thumbnails = beginning + thumbnails;
	$( "#themes" ).html( thumbnails );
	$('.thumbnail').off('click');
	
	/*Si on clique sur une vignette, une recherche google correspondante est lancée*/
	$(".thumbnail").click(function(e){
    var text = $(this).attr("name");
	$('#gsc-i-id1').val(text);
	$( "input.gsc-search-button" ).click();
	console.log(text);
   });
   
   /* Active l'affichage de liens si on clique sur le flèche correspondant à une vignette*/
   activePopups();
   stopSliding();
}

/* Cherche une image pour chaque vignette*/
function searchImages(data) {
	var imageArray =[];
	var indice = 0;
	getImage(data.clusters, indice, imageArray);
}

/*Cherche une image pour une vignette*/
function getImage(clusters, indice, imageArray) {
	var input = clusters[indice].nom;
	$.ajax({
		type: 'GET',
		url: "https://www.googleapis.com/customsearch/v1?key=AIzaSyAXNERGjTHnxRaO6lggPtnUxIAL4vBRDBk&cx=013632266919387871672:pgb1ce2nwuq&searchType=image&q=" + input + "&alt=json",
		timeout: 3000,
		success: function(imData) {
			repeat(imData, clusters, indice, imageArray);
		},
		error: function() {
		  /*alert('La requête n\'a pas abouti');*/ }
	  });

}

/* Cherche une image pour une nouvelle vignette sauf si chaque cluster a été traité, auquel cas elles sont affichées*/
function repeat(imData, clusters, indice, imageArray) {
	if(imData.hasOwnProperty('items')){
	imageArray.push(imData.items[0]);
	} else {
		imageArray.push(JSON.parse('{"link":"http://placehold.it/250x250"}'));
	}
	  if(indice == clusters.length - 1) {
		displayThemes(clusters, imageArray);
	  } else {
		  getImage(clusters, (indice + 1), imageArray);
	  }

}

/*Active les popovers de chaque vignette*/
function activePopups() {
	$('.popov').popover({html:true, container: 'body', placement: 'right'});
	
	//On affiche la popover de la dernière vignette à gauche pour éviter qu'elle sorte du cadre*/
	$('.lastPopov').popover({html:true, container: 'body', placement: 'left'});
	console.log("Popover");
}

/*Défilement du carousel stoppé*/
function stopSliding (){
    $('#myCarousel').carousel({
	    interval: false
	})
}
