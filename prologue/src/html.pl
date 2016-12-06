:- use_module(library(http/thread_httpd)).

:- use_module(library(http/http_dispatch)).

:- use_module(library(http/http_parameters)).

:- use_module(library(http/http_cors)).

:- consult('game').

/*
	
*/
afficherDashboard:-
	joueur(1,Orientation1,Vie1,Degats1,Defense1),
	joueur(2,Orientation2,Vie2,Degats2,Defense2),
	format('{"dashboard" : ['),
	format('{'),
	format('"joueur" : 1,'),
	format('"orientation" : "'),
	format(Orientation1),format('",'),
	format('"vie" : '),
	format(Vie1),format(','),
	format('"degats" : '),
	format(Degats1),format(','),
	format('"defense" : '),
	format(Defense1),
	format('},'),
	format('{'),
	format('"joueur" : 2,'),
	format('"orientation" : "'),
	format(Orientation2),format('",'),
	format('"vie" : '),
	format(Vie2),format(','),
	format('"degats" : '),
	format(Degats2),format(','),
	format('"defense" : '),
	format(Defense2),
	format('}]}').
	
afficherCase(X, Y) :- 
	case(X,Y,obstacle),!,
	%format('<td style="background-color: black"></td>').
	format('<td><span class="glyphicon glyphicon-tree-deciduous" style="color: darkgreen;"></span></td>').
	
afficherCase(X, Y) :- 
	case(X,Y,bonus),!,
	%format('<td style="background-color: green"></td>').
	format('<td><span class="glyphicon glyphicon-plus" style="color: red;"></span></td>').
	
afficherCase(X, Y) :- 
	case(X,Y,1),!,
	format('<td><span class="glyphicon glyphicon-user" style="color: blue;"></span></td>').
	
afficherCase(X, Y) :- 
	case(X,Y,2),!,
	format('<td><span class="glyphicon glyphicon-user" style="color: red;"></span></td>').
	
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
	format('<table id="terrain">~n'),
	repeat, 
		between(0, ExtremeY, Haut),
		afficherLine(Haut),
		Haut is ExtremeY,!, 
	format('</table>~n').	

server(Port) :-
        http_server(http_dispatch, [port(Port)]).
		
:- set_setting(http:cors, [*]).	
		
:- http_handler(/, default, []).
		
:- http_handler('/init', init, []).

:- http_handler('/dashboard', getDashboard, []).

:- http_handler('/ground', getGround, []).

:- http_handler('/turn', turn, []).


default(Request):-
	cors_enable,
	initialise(10,10,10,1,0,5,5,3,false),
	format('Content-type: application/json~n~n'),
	format('{~n"result" : "true"~n}').

turn(Request):-	
	cors_enable,
	not(playTurn(Winner)),
	format('Content-type: application/json~n~n'),
	format('{~n"result" : "true"~n}'),!. 
	
turn(Request):-	
	playTurn(Winner),
	format('Content-type: application/json~n~n'),
	format('{~n"result" : "'),format(Winner), format('"~n}'),!. 
	
getDashboard(Request) :-
	cors_enable,
	format('Content-type: application/json~n~n'),
	afficherDashboard.
	
getGround(Request) :-
	cors_enable,
	format('Content-type: text/html~n~n'),
	afficherTerrain.

init(Request) :-
	cors_enable,
	http_parameters(Request,
                        [ x(X, [integer]),
							y(Y, [integer]),
							obstacles(Obstacles, [integer]),
							life(Life, [integer]),
							damages(Damages, [integer]),
							protections(Protections, [integer]),
							portee(Portee, [integer]),
							bonus(Bonus, [integer]),
							keep(Keep,   [optional(true)])
                        ]),
	initialise(X,Y,Life,Damages,Protections,Portee,Bonus,Obstacles,Keep),
	draw(Request).