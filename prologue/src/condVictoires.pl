:- dynamic player/2.

createPlayer(NumPlayer,Health) :- assert(player(NumPlayer,Health)).

%touche(Player) :- health(Player,Health), Health(X), assert(Health(X-1)).
touch(Player) :- player(Player, X),Y is X-1, retract(player(Player, X)), assert(player(Player,Y)).

% Test si le joueur est en vie
isAlive(Player) :- player(Player,Health), not(Health =< 0).

%%%% Test if the game is finished %%%
gameover('Draw') :- player(X,_), not(isAlive(X)), player(Y,_), X\==Y, not(isAlive(Y)), !. % Draw.
gameover(Winner) :- player(Winner,_), isAlive(Winner), player(Y,_), Winner\==Y, not(isAlive(Y)), !.  % There exists a winning configuration: We cut!

play():- gameover(Winner), !, write('Game is Over. Winner: '), writeln(Winner).
play():- playAll(), play().

playAll() :- player(X,_), touch(X), player(X,Y), writeln(X: Y).

play(_):- gameover(Winner), !, write('Game is Over. Winner: '), writeln(Winner).
init:- createPlayer(0,25),createPlayer(1,25), play().
