% Valeurs de santé
health(X, Health) :- X == 'P1', Health is -50.
health(X, Health) :- X == 'P2', Health is 5.

% Test si le joueur est en vie
isAlive(Player) :- health(Player, Health), not(Health =< 0).

% On change le joueur
changePlayer('P1','P2').
changePlayer('P2','P1').

%%%% Test if the game is finished %%%
gameover(Winner) :- changePlayer(Winner, Other), isAlive(Winner), not(isAlive(Other)) ,!.  % There exists a winning configuration: We cut!
gameover('Draw') :- changePlayer(Winner, Other), not(isAlive(Other)), not(isAlive(Winner)). % the Board is fully instanciated (no free variable): Draw.

play(_):- gameover(Winner), !, write('Game is Over. Winner: '), writeln(Winner).