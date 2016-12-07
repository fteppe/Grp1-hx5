/*
	Ensemble de fonctions permettant de jouer une partie via l'utilisation des ia programmées.
*/

:- consult('SD').
:- consult('minmax').
:- consult('AffichagePlateauDeJeu').

:-dynamic ia/2. % Permet de régler les ia.
ia(1, minmaxDefault).
ia(2,minmaxDefault).

%    IA MinMax ==============================	
/*
	Choix d'une action pour une IA MinMax
*/
choisirAction(1,Action):-
	ia(1,minmaxDefault),!,
	initialiseDatas(J1,J2,Bonus),
	choixAction(J1,J2,Bonus,4,default,Action,nonrandom).
	
choisirAction(2,Action):-
	ia(2,minmaxDefault),!,
	initialiseDatas(J1,J2,Bonus),
	choixAction(J2,J1,Bonus,4,default,Action,nonrandom).
	
choisirAction(1,Action):-
	ia(1,minmaxDefense),!,
	initialiseDatas(J1,J2,Bonus),
	choixAction(J1,J2,Bonus,4,defense,Action,nonrandom).
	
choisirAction(2,Action):-
	ia(2,minmaxDefense),!,
	initialiseDatas(J1,J2,Bonus),
	choixAction(J2,J1,Bonus,4,defense,Action,nonrandom).
	
choisirAction(1,Action):-
	ia(1,minmaxRush),!,
	initialiseDatas(J1,J2,Bonus),
	choixAction(J1,J2,Bonus,4,rush,Action,nonrandom).
	
choisirAction(2,Action):-
	ia(2,minmaxRush),!,
	initialiseDatas(J1,J2,Bonus),
	choixAction(J2,J1,Bonus,4,rush,Action,nonrandom).
	
choisirAction(1,Action):-
	ia(1,minmaxRandom),!,
	initialiseDatas(J1,J2,Bonus),
	choixAction(J1,J2,Bonus,4,default,Action,random).
	
choisirAction(2,Action):-
	ia(2,minmaxRandom),!,
	initialiseDatas(J1,J2,Bonus),
	choixAction(J2,J1,Bonus,4,default,Action,random).

% IA AvanceVersEnnemi================================
/*
	Choix d'une action pour une IA avanceVersEnnemi, 
	qui tire lorsque cela est possible et avance vers l'adversaire le plus vite possible sinon.
*/	
choisirAction(Joueur,tirer):-
	ia(Joueur,avanceVersEnnemi),
	vaToucher(Joueur).

choisirAction(Joueur, avancer):-
	ia(Joueur, avanceVersEnnemi),
	joueur(Joueur,nord,_,_,_),
	joueur(AutreJoueur,_,_,_,_),
	not(Joueur == AutreJoueur),
	%not(caseDevant(Joueur,obstacle)), déja inclue dans listecoups
	listeCoups(Joueur,Liste),
	member(avancer,Liste),
	case(X1,Y1,Joueur),
	case(X2,Y2,AutreJoueur),
	Y2<Y1,!.
	
choisirAction(Joueur, avancer):-
	ia(Joueur, avanceVersEnnemi),
	joueur(Joueur,sud,_,_,_),
	joueur(AutreJoueur,_,_,_,_),
	not(Joueur == AutreJoueur),
	%not(caseDevant(Joueur,obstacle)),
	listeCoups(Joueur,Liste),
	member(avancer,Liste),
	case(X1,Y1,Joueur),
	case(X2,Y2,AutreJoueur),
	Y2>Y1,!.
	
choisirAction(Joueur, avancer):-
	ia(Joueur, avanceVersEnnemi),
	joueur(Joueur,est,_,_,_),
	joueur(AutreJoueur,_,_,_,_),
	not(Joueur == AutreJoueur),
	%not(caseDevant(Joueur,obstacle)),
	listeCoups(Joueur,Liste),
	member(avancer,Liste),
	case(X1,Y1,Joueur),
	case(X2,Y2,AutreJoueur),
	X2>X1,!.
	
choisirAction(Joueur, avancer):-
	ia(Joueur, avanceVersEnnemi),
	joueur(Joueur,ouest,_,_,_),
	joueur(AutreJoueur,_,_,_,_),
	not(Joueur == AutreJoueur),
	%not(caseDevant(Joueur,obstacle)),
	listeCoups(Joueur,Liste),
	member(avancer,Liste),
	case(X1,Y1,Joueur),
	case(X2,Y2,AutreJoueur),
	X2<X1,!.
	
% ========================================

choisirAction(Joueur, tournerGauche):-
	ia(Joueur, avanceVersEnnemi),
	listeCoups(Joueur,Liste),
	member(tournerGauche,Liste),
	joueur(Joueur,nord,_,_,_),
	joueur(AutreJoueur,_,_,_,_),
	not(Joueur == AutreJoueur),
	case(X1,Y1,Joueur),
	case(X2,Y2,AutreJoueur),
	X2<X1,!.
	
choisirAction(Joueur, tournerGauche):-
	ia(Joueur, avanceVersEnnemi),
	listeCoups(Joueur,Liste),
	member(tournerGauche,Liste),
	joueur(Joueur,sud,_,_,_),
	joueur(AutreJoueur,_,_,_,_),
	not(Joueur == AutreJoueur),
	case(X1,Y1,Joueur),
	case(X2,Y2,AutreJoueur),
	X2>X1,!.
	
choisirAction(Joueur, tournerGauche):-
	ia(Joueur, avanceVersEnnemi),
	listeCoups(Joueur,Liste),
	member(tournerGauche,Liste),
	joueur(Joueur,est,_,_,_),
	joueur(AutreJoueur,_,_,_,_),
	not(Joueur == AutreJoueur),
	case(X1,Y1,Joueur),
	case(X2,Y2,AutreJoueur),
	Y2<Y1,!.
	
choisirAction(Joueur, tournerGauche):-
	ia(Joueur, avanceVersEnnemi),
	listeCoups(Joueur,Liste),
	member(tournerGauche,Liste),
	joueur(Joueur,ouest,_,_,_),
	joueur(AutreJoueur,_,_,_,_),
	not(Joueur == AutreJoueur),
	case(X1,Y1,Joueur),
	case(X2,Y2,AutreJoueur),
	Y2>Y1,!.
	
% ======================================
choisirAction(Joueur, tournerDroite):-
	ia(Joueur, avanceVersEnnemi),
	listeCoups(Joueur,Liste),
	member(tournerDroite,Liste),
	joueur(Joueur,nord,_,_,_),
	joueur(AutreJoueur,_,_,_,_),
	not(Joueur == AutreJoueur),
	case(X1,Y1,Joueur),
	case(X2,Y2,AutreJoueur),
	X2>X1,!.
	
choisirAction(Joueur, tournerDroite):-
	ia(Joueur, avanceVersEnnemi),
	listeCoups(Joueur,Liste),
	member(tournerDroite,Liste),
	joueur(Joueur,sud,_,_,_),
	joueur(AutreJoueur,_,_,_,_),
	not(Joueur == AutreJoueur),
	case(X1,Y1,Joueur),
	case(X2,Y2,AutreJoueur),
	X2<X1,!.
	
choisirAction(Joueur, tournerDroite):-
	ia(Joueur, avanceVersEnnemi),
	listeCoups(Joueur,Liste),
	member(tournerDroite,Liste),
	joueur(Joueur,est,_,_,_),
	joueur(AutreJoueur,_,_,_,_),
	not(Joueur == AutreJoueur),
	case(X1,Y1,Joueur),
	case(X2,Y2,AutreJoueur),
	Y2>Y1,!.
	
choisirAction(Joueur, tournerDroite):-
	ia(Joueur, avanceVersEnnemi),
	listeCoups(Joueur,Liste),
	member(tournerDroite,Liste),
	joueur(Joueur,ouest,_,_,_),
	joueur(AutreJoueur,_,_,_,_),
	not(Joueur == AutreJoueur),
	case(X1,Y1,Joueur),
	case(X2,Y2,AutreJoueur),
	Y2<Y1,!.
	
% IA Random======================================================	
/*
	Indique si Joueur peut toucher son adversaire
*/
vaToucher(Joueur):-
	chercherCible(Joueur,Cible),
	not(Cible == obstacle),
	distanceCible(Joueur,Cible,Distance),
	portee(Portee),
	(Distance < Portee ; Distance = Portee).

/*
	Choix d'une action pour une IA pseudoRandomTir : Si Joueur peut toucher son adversaire il tire, sinon l'action est aléatoire
*/
choisirAction(Joueur,tirer):-
	ia(Joueur,pseudoRandomTir),
	vaToucher(Joueur), !.

choisirAction(Joueur,Action):-
	ia(Joueur,pseudoRandomTir),
	listeCoups(Joueur,Liste),
	length(Liste,Taille),
	random(0,Taille,Choix),
	nth0(Choix,Liste,Action).

/*
	Choix d'une action pour une IA random
*/
choisirAction(Joueur,Action):-
	ia(Joueur,random),
	listeCoups(Joueur,Liste),
	length(Liste,Taille),
	random(0,Taille,Choix),
	nth0(Choix,Liste,Action).

/*
	Vrai si la partie est terminée, et indique le résultat final
*/
gameover(draw):-
	joueur(1,_,0,_,_),
	joueur(2,_,0,_,_).
	
gameover(1):-
	joueur(2,_,0,_,_).
	
gameover(2):-
	joueur(1,_,0,_,_).

/*
	Gère l'ordonnancement des actions des deux joueurs : si l'un tire, il touche son adversaire avant tout mouvment de sa part
*/	
actionsOrdonnees(Action1,tirer):-
	effectuerAction(2,tirer),
	effectuerAction(1,Action1), !.
	
actionsOrdonnees(Action1,Action2):-
	effectuerAction(1,Action1),
	effectuerAction(2,Action2).

/*
	Permet de jouer une partie dans son intégralité, jusqu'à ce que Winner reporte le résultat final
*/	
playGame(Winner,TimeLimit):-
	repeat,
	play(Winner,TimeLimit),!.
	
playTurn(Winner):-
	play(Winner,_).

/*
	Joue un tour de jeu si la partie n'est pas terminée
*/	
play(draw,TimeLimit):-
	get_time(TimeStamp),
	TimeStamp > TimeLimit.

play(X,_):-
	gameover(X),!.
	
play(none,_):-
	dimensions(_,_),
	choisirAction(1,Action1),
	choisirAction(2,Action2),
	
	actionsOrdonnees(Action1,Action2),!,
	/*
	displayBoard,
	sleep(1),*/
	fail.
	
/*
	Permet le lancement de parties à la suite pour la mise en place de tests
*/	
launchTest(Winner,IA1,IA2):-
	assert(ia(1,IA1)), % IA du joueur 1
	assert(ia(2,IA2)), % IA du joueur 2
	initialise(10,10,10,1,0,5,3,3,none),
	get_time(CurrentTime),
	TimeLimit is CurrentTime + 10.0,
	playGame(Winner,TimeLimit),!,
	get_time(FinalTime),
	Diff is CurrentTime-FinalTime,
	writeln(''),
	write(Winner),
	write(' - '),
	write(Diff),
	writeln(''),
	retract(ia(1,_)), % IA du joueur 1
	retract(ia(2,_)). % IA du joueur 2

countTest(0,0,0,X,X,_,_).
	
countTest(J1,J2,Draw,TestActuel,TotalTest,IA1,IA2):-
	cleanMemory,
	launchTest(Result,IA1,IA2),
	updateAndLaunch(Result,J1,J2,Draw,TestActuel,TotalTest,IA1,IA2).
	
updateAndLaunch(1,J1,J2,Draw,TestActuel,TotalTest,IA1,IA2):-
	TestSuivant is TestActuel+1,
	countTest(J1n,J2,Draw,TestSuivant,TotalTest,IA1,IA2),
	J1 is J1n +1.

updateAndLaunch(2,J1,J2,Draw,TestActuel,TotalTest,IA1,IA2):-
	TestSuivant is TestActuel+1,
	countTest(J1,J2n,Draw,TestSuivant,TotalTest,IA1,IA2),
	J2 is J2n+1.

updateAndLaunch(draw,J1,J2,Draw,TestActuel,TotalTest,IA1,IA2):-
	TestSuivant is TestActuel+1,
	countTest(J1,J2,DrawN,TestSuivant,TotalTest,IA1,IA2),
	Draw is DrawN +1.

	
