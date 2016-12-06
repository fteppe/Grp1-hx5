:- consult('SD').
% :- consult('minimax').
:- consult('minmax').
:- consult('AffichagePlateauDeJeu').
:-dynamic tourAct/1.

ia(1,minmaxDefault).
ia(2,pseudoRandomTir).

%    ==============================	

choisirAction(1,Action):-
	ia(1,minmaxDefault),!,
	initialiseDatas(J1,J2,Bonus),
	choixAction(J1,J2,Bonus,4,default,Action).
	
choisirAction(2,Action):-
	ia(2,minmaxDefault),!,
	initialiseDatas(J1,J2,Bonus),
	choixAction(J2,J1,Bonus,4,default,Action).

% ================================
	
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
	Y2<Y1,
	effectuerAction(Joueur,avancer),!.
	
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
	Y2>Y1,
	effectuerAction(Joueur,avancer),!.
	
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
	X2>X1,
	effectuerAction(Joueur,avancer),!.
	
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
	X2<X1,
	effectuerAction(Joueur,avancer),!.
	
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
	X2<X1,
	effectuerAction(Joueur,tournerGauche),!.
	
choisirAction(Joueur, tournerGauche):-
	ia(Joueur, avanceVersEnnemi),
	listeCoups(Joueur,Liste),
	member(tournerGauche,Liste),
	joueur(Joueur,sud,_,_,_),
	joueur(AutreJoueur,_,_,_,_),
	not(Joueur == AutreJoueur),
	case(X1,Y1,Joueur),
	case(X2,Y2,AutreJoueur),
	X2>X1,
	effectuerAction(Joueur,tournerGauche),!.
	
choisirAction(Joueur, tournerGauche):-
	ia(Joueur, avanceVersEnnemi),
	listeCoups(Joueur,Liste),
	member(tournerGauche,Liste),
	joueur(Joueur,est,_,_,_),
	joueur(AutreJoueur,_,_,_,_),
	not(Joueur == AutreJoueur),
	case(X1,Y1,Joueur),
	case(X2,Y2,AutreJoueur),
	Y2<Y1,
	effectuerAction(Joueur,tournerGauche),!.
	
choisirAction(Joueur, tournerGauche):-
	ia(Joueur, avanceVersEnnemi),
	listeCoups(Joueur,Liste),
	member(tournerGauche,Liste),
	joueur(Joueur,ouest,_,_,_),
	joueur(AutreJoueur,_,_,_,_),
	not(Joueur == AutreJoueur),
	case(X1,Y1,Joueur),
	case(X2,Y2,AutreJoueur),
	Y2>Y1,
	effectuerAction(Joueur,tournerGauche),!.
	
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
	X2>X1,
	effectuerAction(Joueur,tournerDroite),!.
	
choisirAction(Joueur, tournerDroite):-
	ia(Joueur, avanceVersEnnemi),
	listeCoups(Joueur,Liste),
	member(tournerDroite,Liste),
	joueur(Joueur,sud,_,_,_),
	joueur(AutreJoueur,_,_,_,_),
	not(Joueur == AutreJoueur),
	case(X1,Y1,Joueur),
	case(X2,Y2,AutreJoueur),
	X2<X1,
	effectuerAction(Joueur,tournerDroite),!.
	
choisirAction(Joueur, tournerDroite):-
	ia(Joueur, avanceVersEnnemi),
	listeCoups(Joueur,Liste),
	member(tournerDroite,Liste),
	joueur(Joueur,est,_,_,_),
	joueur(AutreJoueur,_,_,_,_),
	not(Joueur == AutreJoueur),
	case(X1,Y1,Joueur),
	case(X2,Y2,AutreJoueur),
	Y2>Y1,
	effectuerAction(Joueur,tournerDroite),!.
	
choisirAction(Joueur, tournerDroite):-
	ia(Joueur, avanceVersEnnemi),
	listeCoups(Joueur,Liste),
	member(tournerDroite,Liste),
	joueur(Joueur,ouest,_,_,_),
	joueur(AutreJoueur,_,_,_,_),
	not(Joueur == AutreJoueur),
	case(X1,Y1,Joueur),
	case(X2,Y2,AutreJoueur),
	Y2<Y1,
	effectuerAction(Joueur,tournerDroite),!.
	
%======================================================	

vaToucher(Joueur):-
	chercherCible(Joueur,Cible),
	not(Cible == obstacle),
	distanceCible(Joueur,Cible,Distance),
	portee(Portee),
	(Distance < Portee ; Distance = Portee).

choisirAction(Joueur,tirer):-
	ia(Joueur,pseudoRandomTir),
	vaToucher(Joueur), !.

choisirAction(Joueur,Action):-
	ia(Joueur,pseudoRandomTir),
	listeCoups(Joueur,Liste),
	length(Liste,Taille),
	random(0,Taille,Choix),
	nth0(Choix,Liste,Action).

choisirAction(Joueur,Action):-
	ia(Joueur,random),
	listeCoups(Joueur,Liste),
	length(Liste,Taille),
	random(0,Taille,Choix),
	nth0(Choix,Liste,Action).


gameover(draw):-
	joueur(1,_,0,_,_),
	joueur(2,_,0,_,_).
	
gameover(1):-
	joueur(2,_,0,_,_).
	
gameover(2):-
	joueur(1,_,0,_,_).
	
actionsOrdonnees(Action1,tirer):-
	effectuerAction(2,tirer),
	effectuerAction(1,Action1), !.
	
actionsOrdonnees(Action1,Action2):-
	effectuerAction(1,Action1),
	effectuerAction(2,Action2).
	
playGame(Winner):-
	repeat,
	play(Winner),!.
	
playTurn(Winner):-
	play(Winner).
	
play(X):-
	gameover(X),!.
	
play(none):-
	dimensions(_,_),
	choisirAction(1,Action1),
	choisirAction(2,Action2),
	
	actionsOrdonnees(Action1,Action2),!,
	/*joueur(1,Orient1,_,_,_),
	joueur(2,Orient2,_,_,_),
	writeln(Action1),
	writeln(Orient1),
	writeln(Action2),
	writeln(Orient2),
	displayBoard,
	sleep(1),*/
	fail.
	
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

	