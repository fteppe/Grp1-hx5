:- use_module(library(http/thread_httpd)).

:- use_module(library(http/http_dispatch)).

:- use_module(library(http/http_parameters)).

:- consult('SD').


addCss:-
	format('<style>~n'),
	format('table, th, td {~n'),
	format('border: 1px solid black;'),
	format('min-width: 10px;'),
	format('height: 10px;'),
	format('}~n'),
	format('</style>~n').

afficherJoueurs:-
	joueur(1,Orientation1,Vie1,Degats1,Defense1),
	joueur(2,Orientation2,Vie2,Degats2,Defense2),
	format('<table>~n'),
	format('<tr><th></th><th>Orientation</th><th>Vie</th><th>Degats</th><th>Protections</th></tr>~n'),
	format('<tr><td>Joueur 1</td><td>'),
	format(Orientation1),format('</td><td>'),
	format(Vie1),format('</td><td>'),
	format(Degats1),format('</td><td>'),
	format(Defense1),
	format('</td></tr>~n'),
	format('<tr><td>Joueur 2</td><td>'),
	format(Orientation2),format('</td><td>'),
	format(Vie2),format('</td><td>'),
	format(Degats2),format('</td><td>'),
	format(Defense2),
	format('</td></tr>~n'),
	format('</table>~n').
	
afficherCase(X, Y) :- 
	case(X,Y,obstacle),!,
	format('<td style="background-color: black"></td>').
	
afficherCase(X, Y) :- 
	case(X,Y,1),!,
	format('<td>1</td>').
	
afficherCase(X, Y) :- 
	case(X,Y,2),!,
	format('<td>2</td>').
	
afficherCase(X, Y) :-
	visites(X,Y),!,
	format('<td style="background-color: red"></td>').
	
afficherCase(_, _) :- 
	format('<td></td>').
	
afficherLine(Y):-
	dimensions(X,_), 
	ExtremeX is X - 1,!,
	format('<tr>' ),
	repeat, 
		between(0,ExtremeX,Long) ,
		afficherCase(Long, Y),
		Long is ExtremeX,!,
	format('</tr>~n').
	
afficherTerrain:-
	dimensions(_, Y),
	ExtremeY is Y - 1,!,
	format('<table>~n'),
	repeat, 
		between(0, ExtremeY, Haut),
		afficherLine(Haut),
		Haut is ExtremeY,!, 
	format('</table>~n').	

afficherPartie:-
	afficherJoueurs,
	afficherTerrain.

server(Port) :-
        http_server(http_dispatch, [port(Port)]).
		
		

:- http_handler(/, default, []).
		
:- http_handler('/init', init, []).

:- http_handler('/draw', draw, []).

default(Request):-
	initialise(50,50,10,1,0,1,[],200,false),
	draw(Request).

draw(Request):-
    format('Content-type: text/html~n~n'),
    format('<html>'),
	format('<head>'),
	addCss,
	format('<title>Howdy</title>'),
	format('</head>'),
	format('<body><h2>Test Affichage</h2>'),
	afficherPartie,
	format('</html>~n').

init(Request) :-
	http_parameters(Request,
                        [ x(X, [integer]),
							y(Y, [integer]),
							obstacles(Obstacles, [integer]),
							life(Life, [integer]),
							damages(Damages, [integer]),
							protections(Protections, [integer]),
							portee(Portee, [integer]),
							keep(Keep,   [optional(true)])
                        ]),
	initialise(X,Y,Life,Damages,Protections,Portee,[],Obstacles,Keep),
	draw(Request).