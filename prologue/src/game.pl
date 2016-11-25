:- consult('SD').
:- consult('html').
:- consult('AffichagePlateauDeJeu').

ia(1,random).
ia(2,random).

%    ==============================	

choisirAction(Joueur,tirer):-
	ia(Joueur,AvanceVersEnnemi),
	vaToucher(Joueur).

choisirAction(Joueur, avancer):-
	ia(Joueur, AvanceVersEnnemi),
	listeCoups(Joueur,Liste),
	member(avancer,Liste),
	joueur(Joueur,nord,_,_,_),
	joueur(AutreJoueur,_,_,_,_),
	not (Joueur = AutreJoueur),
	case(X1,Y1,Joueur),
	case(X2,Y2,AutreJoueur),
	Y2<Y1.
	
choisirAction(Joueur, avancer):-
	ia(Joueur, AvanceVersEnnemi),
	listeCoups(Joueur,Liste),
	member(avancer,Liste),
	joueur(Joueur,sud,_,_,_),
	joueur(AutreJoueur,_,_,_,_),
	not (Joueur = AutreJoueur),
	case(X1,Y1,Joueur),
	case(X2,Y2,AutreJoueur),
	Y2>Y1.
	
choisirAction(Joueur, avancer):-
	ia(Joueur, AvanceVersEnnemi),
	listeCoups(Joueur,Liste),
	member(avancer,Liste),
	joueur(Joueur,est,_,_,_),
	joueur(AutreJoueur,_,_,_,_),
	not (Joueur = AutreJoueur),
	case(X1,Y1,Joueur),
	case(X2,Y2,AutreJoueur),
	X2>X1.
	
choisirAction(Joueur, avancer):-
	ia(Joueur, AvanceVersEnnemi),
	listeCoups(Joueur,Liste),
	member(avancer,Liste),
	joueur(Joueur,ouest,_,_,_),
	joueur(AutreJoueur,_,_,_,_),
	not (Joueur = AutreJoueur),
	case(X1,Y1,Joueur),
	case(X2,Y2,AutreJoueur),
	X2<X1.
	
% ========================================

choisirAction(Joueur, tournerGauche):-
	ia(Joueur, AvanceVersEnnemi),
	listeCoups(Joueur,Liste),
	member(tournerGauche,Liste),
	joueur(Joueur,nord,_,_,_),
	joueur(AutreJoueur,_,_,_,_),
	not (Joueur = AutreJoueur),
	case(X1,Y1,Joueur),
	case(X2,Y2,AutreJoueur),
	X2<X1.
	
choisirAction(Joueur, tournerGauche):-
	ia(Joueur, AvanceVersEnnemi),
	listeCoups(Joueur,Liste),
	member(tournerGauche,Liste),
	joueur(Joueur,sud,_,_,_),
	joueur(AutreJoueur,_,_,_,_),
	not (Joueur = AutreJoueur),
	case(X1,Y1,Joueur),
	case(X2,Y2,AutreJoueur),
	X2>X1.
	
choisirAction(Joueur, tournerGauche):-
	ia(Joueur, AvanceVersEnnemi),
	listeCoups(Joueur,Liste),
	member(tournerGauche,Liste),
	joueur(Joueur,est,_,_,_),
	joueur(AutreJoueur,_,_,_,_),
	not (Joueur = AutreJoueur),
	case(X1,Y1,Joueur),
	case(X2,Y2,AutreJoueur),
	Y2<Y1.
	
choisirAction(Joueur, tournerGauche):-
	ia(Joueur, AvanceVersEnnemi),
	listeCoups(Joueur,Liste),
	member(tournerGauche,Liste),
	joueur(Joueur,ouest,_,_,_),
	joueur(AutreJoueur,_,_,_,_),
	not (Joueur = AutreJoueur),
	case(X1,Y1,Joueur),
	case(X2,Y2,AutreJoueur),
	Y2>Y1.
	
% ======================================
choisirAction(Joueur, tournerDroite):-
	ia(Joueur, AvanceVersEnnemi),
	listeCoups(Joueur,Liste),
	member(tournerDroite,Liste),
	joueur(Joueur,nord,_,_,_),
	joueur(AutreJoueur,_,_,_,_),
	not (Joueur = AutreJoueur),
	case(X1,Y1,Joueur),
	case(X2,Y2,AutreJoueur),
	X2>X1.
	
choisirAction(Joueur, tournerDroite):-
	ia(Joueur, AvanceVersEnnemi),
	listeCoups(Joueur,Liste),
	member(tournerDroite,Liste),
	joueur(Joueur,sud,_,_,_),
	joueur(AutreJoueur,_,_,_,_),
	not (Joueur = AutreJoueur),
	case(X1,Y1,Joueur),
	case(X2,Y2,AutreJoueur),
	X2<X1.
	
choisirAction(Joueur, tournerDroite):-
	ia(Joueur, AvanceVersEnnemi),
	listeCoups(Joueur,Liste),
	member(tournerDroite,Liste),
	joueur(Joueur,est,_,_,_),
	joueur(AutreJoueur,_,_,_,_),
	not (Joueur = AutreJoueur),
	case(X1,Y1,Joueur),
	case(X2,Y2,AutreJoueur),
	Y2>Y1.
	
choisirAction(Joueur, tournerDroite):-
	ia(Joueur, AvanceVersEnnemi),
	listeCoups(Joueur,Liste),
	member(tournerDroite,Liste),
	joueur(Joueur,ouest,_,_,_),
	joueur(AutreJoueur,_,_,_,_),
	not (Joueur = AutreJoueur),
	case(X1,Y1,Joueur),
	case(X2,Y2,AutreJoueur),
	Y2<Y1.
	
%======================================================	

vaToucher(Joueur):-
	chercherCible(Joueur,Cible),
	not(Cible = obstacle),
	distanceCible(Joueur,Cible,Distance),
	portee(Portee),
	(Distance < Portee ; Distance = Portee).

choisirAction(Joueur,tirer):-
	ia(Joueur,pseudoRandomTir),
	vaToucher(Joueur).

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
	effectuerAction(1,Action1).
	
actionsOrdonnees(Action1,Action2):-
	effectuerAction(1,Action1),
	effectuerAction(2,Action2).
	
playGame(Winner):-
	repeat,
	play(Winner),!.
	
play(X):-
	gameover(X).
	
play(none):-
	dimensions(_,_),
	choisirAction(1,Action1),
	choisirAction(2,Action2),
	actionsOrdonnees(Action1,Action2),!,
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

	