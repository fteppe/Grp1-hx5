/*
	Ensemble de fonctions permettant de jouer une partie via l'utilisation des ia programmées.
*/

:- consult('SD').
:- consult('minmax').
:- consult('AffichagePlateauDeJeu').


ia(1,pseudoRandomTir). % IA du joueur 1
ia(2,pseudoRandomTir). % IA du joueur 2

%    IA MinMax ==============================	
/*
	Choix d'une action pour une IA MinMax
*/
choisirAction(1,Action):-
	ia(1,minmaxDefault),!,
	initialiseDatas(J1,J2,Bonus),
	choixAction(J1,J2,Bonus,4,default,Action).
	
choisirAction(2,Action):-
	ia(2,minmaxDefault),!,
	initialiseDatas(J1,J2,Bonus),
	choixAction(J2,J1,Bonus,4,default,Action).
	
choisirAction(1,Action):-
	ia(1,minmaxDefense),!,
	initialiseDatas(J1,J2,Bonus),
	choixAction(J1,J2,Bonus,4,defense,Action).
	
choisirAction(2,Action):-
	ia(2,minmaxDefense),!,
	initialiseDatas(J1,J2,Bonus),
	choixAction(J2,J1,Bonus,4,defense,Action).

% IA AvanceVersEnnemi================================
	
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
playGame(Winner):-
	repeat,
	play(Winner),!.
	
playTurn(Winner):-
	play(Winner).

/*
	Joue un tour de jeu si la partie n'est pas terminée
*/	
play(X):-
	gameover(X),!.
	
play(none):-
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
launchTest(Winner):-
	initialise(10,10,10,1,0,10,0,10,none),
	playGame(Winner),!.

countTest(0,0,0,X,X).
	
countTest(J1,J2,Draw,TestActuel,TotalTest):-
	cleanMemory,
	launchTest(Result),
	updateAndLaunch(Result,J1,J2,Draw,TestActuel,TotalTest).
	
updateAndLaunch(1,J1,J2,Draw,TestActuel,TotalTest):-
	TestSuivant is TestActuel+1,
	countTest(J1n,J2,Draw,TestSuivant,TotalTest),
	J1 is J1n +1.

updateAndLaunch(2,J1,J2,Draw,TestActuel,TotalTest):-
	TestSuivant is TestActuel+1,
	countTest(J1,J2n,Draw,TestSuivant,TotalTest),
	J2 is J2n+1.

updateAndLaunch(draw,J1,J2,Draw,TestActuel,TotalTest):-
	TestSuivant is TestActuel+1,
	countTest(J1,J2,DrawN,TestSuivant,TotalTest),
	Draw is DrawN +1.

	