
:-dynamic case/3.
:-dynamic joueur/5.
:-dynamic dimensions/2.
:-dynamic portee/1.
:-dynamic visites/2.

cleanVisites:-
	visites(X,_),
	repeat,
		retract(visites(Y,_)),
		not(visites(Test,_)),!.
		
cleanVisites.

cleanCases:-
	case(_,_,A),
	repeat,
		retract(case(_,_,_X)),
		not(case(_,_,Type)),!.
		
cleanCases.
 
 cleanMemory:-
 	dimensions(_,_),
	retract(dimensions(_B,_)),
	retract(portee(_C)),
	cleanCases,
	cleanVisites,
	retract(joueur(1,_,_,_,_)),
	retract(joueur(2,_,_,_,_)),!.
	
cleanMemory.

creerJoueurs(0,_,_,_):-!.
	
creerJoueurs(CurrentPlayer,VieJoueurs,DegatsBase,DefenseBase):-
	placerJoueur(CurrentPlayer,VieJoueurs,DegatsBase,DefenseBase),!,
	NextPlayer is CurrentPlayer - 1,
	creerJoueurs(NextPlayer,VieJoueurs,DegatsBase,DefenseBase).

placerJoueur(1,VieJoueurs,DegatsBase,DefenseBase):-
	assert(joueur(1,sud,VieJoueurs,DegatsBase,DefenseBase)),
	assert(case(0,0,1)),!.
	
placerJoueur(2,VieJoueurs,DegatsBase,DefenseBase):-
	assert(joueur(2,nord,VieJoueurs,DegatsBase,DefenseBase)),
	dimensions(X,Y),
	PosX is X-1,
	PosY is Y-1,
	assert(case(PosX,PosY,2)),!.
	
placerJoueur(N).

placerProchainMur(X,Y,Taille,-1):-
	dimensions(LimX,_),
	X < LimX , !,
	NextX is X + 1,
	NextTaille is Taille - 1,
	placerMur(NextX,Y,NextTaille,-1).
	
placerProchainMur(_,_,_,-1).

placerProchainMur(X,Y,Taille,1):-
	X > 0 , !,
	NextX is X - 1,
	NextTaille is Taille - 1,
	placerMur(NextX,Y,NextTaille,1).
	
placerProchainMur(_,_,_,1).

placerProchainMur(X,Y,Taille,2):-
	dimensions(_,LimY),
	Y < LimY , !,
	NextY is Y + 1,
	NextTaille is Taille - 1,
	placerMur(X,NextY,NextTaille,2).
	
placerProchainMur(_,_,_,2).

placerProchainMur(X,Y,Taille,-2):-
	Y > 0 , !,
	NextY is Y - 1,
	NextTaille is Taille - 1,
	placerMur(X,NextY,NextTaille,-2).
	
placerProchainMur(_,_,_,-2).

placerMur(_,_,0,_).
	
placerMur(X,Y,Taille,DirActuelle):-
	assert(case(X,Y,obstacle)),
	repeat,
		random(-2,3,Direction),
		not(Direction = 0),
		not(Direction+DirActuelle = 0),!,
	placerProchainMur(X,Y,Taille,Direction).

placerTypeObstacle(0):-
	dimensions(X,Y),
	LimitX is X,
	LimitY is Y,
	repeat,
	random(0,LimitX,PosX),
	random(0,LimitY,PosY),
	not(case(PosX,PosY,_)),!,
	assert(case(PosX,PosY,obstacle)).
	
placerTypeObstacle(1):-
	dimensions(X,Y),
	LimitX is X,
	LimitY is Y,
	repeat,
	random(0,LimitX,PosX),
	random(0,LimitY,PosY),
	not(case(PosX,PosY,_)),!,
	random(2,21,Taille),
	placerMur(PosX,PosY,Taille,0).

placerTypeObstacle(2):-
	placerTypeObstacle(0).

placerObstacleRandom:-
	random(0,3,Type),
	placerTypeObstacle(Type).

placerObstacles(0).
placerObstacles(N):- 
	placerObstacleRandom,
	Next is N-1,!,
	placerObstacles(Next).

voisinDroit(X1,Y1,X2,Y2):-
	X3 is X1 +1,
	cheminExiste(X3,Y1,X2,Y2).
	
voisinBas(X1,Y1,X2,Y2):-
	Y3 is Y1 +1,
	cheminExiste(X1,Y3,X2,Y2).

voisinGauche(X1,Y1,X2,Y2):-
	X3 is X1 -1,
	cheminExiste(X3,Y1,X2,Y2).
	
voisinHaut(X1,Y1,X2,Y2):-
	Y3 is Y1 -1,
	cheminExiste(X1,Y3,X2,Y2).
	
cheminExiste(X,Y,X,Y).

cheminExiste(X1,Y1,X2,Y2):-
	not(visites(X1,Y1)),
	not(case(X1,Y1,obstacle)),
	assert(visites(X1,Y1)),
	(voisinDroit(X1,Y1,X2,Y2);voisinBas(X1,Y1,X2,Y2);voisinGauche(X1,Y1,X2,Y2);voisinHaut(X1,Y1,X2,Y2)).
	
joueursPeuventSAtteindre:-
	case(X1,Y1,1),!,
	not(case(X1,Y1,obstacle)),
	case(X2,Y2,2),!,
	not(case(X2,Y2,obstacle)),
	cheminExiste(X1,Y1,X2,Y2).

% Ajout Modif F T ==============
	
placerBonus(0).
placerBonus(N):-!,
	placerBonusRandom,
    Next is N-1 ,!,
    placerBonus(Next).

placerBonusRandom:-
	placerTypeBonus(0).

placerTypeBonus(0):-
	dimensions(X,Y),
	LimitX is X,
	LimitY is Y,
	repeat,
	random(0,LimitX,PosX),
	random(0,LimitY,PosY),
	not(case(PosX,PosY,_)),!,
	assert(case(PosX,PosY,bonus)).
	
initialise(DimX,DimY,VieJoueurs,DegatsBase,DefenseBase,Portee,NombreBonus,NombreObstacles,keep):-
	repeat,
	createGame(DimX,DimY,VieJoueurs,DegatsBase,DefenseBase,Portee,NombreBonus,NombreObstacles),!.
	
initialise(DimX,DimY,VieJoueurs,DegatsBase,DefenseBase,Portee,NombreBonus,NombreObstacles,_):-
	repeat,
	cleanMemory,
	createGame(DimX,DimY,VieJoueurs,DegatsBase,DefenseBase,Portee,NombreBonus,NombreObstacles),!,
	cleanVisites.
	
createGame(DimX,DimY,VieJoueurs,DegatsBase,DefenseBase,Portee,NombreBonus,NombreObstacles):-
	assert(dimensions(DimX,DimY)),
	assert(case(_,-1,obstacle)),
	assert(case(-1,_,obstacle)),
	assert(case(_,DimY,obstacle)),
	assert(case(DimX,_,obstacle)),
	placerObstacles(NombreObstacles),
	creerJoueurs(2,VieJoueurs,DegatsBase,DefenseBase),
	assert(portee(Portee)),
	/*placerBonus(NombreBonus),*/!,
	joueursPeuventSAtteindre.
	
% ====================================

caseDevant(Joueur,TypeCase):-
	joueur(Joueur,nord,_,_,_),
	case(X,Y,Joueur),
	YCase is Y - 1,
	case(X,YCase,TypeCase).
	
caseDevant(Joueur,TypeCase):-
	joueur(Joueur,sud,_,_,_),
	case(X,Y,Joueur),
	YCase is Y + 1,
	case(X,YCase,TypeCase).
	
caseDevant(Joueur,TypeCase):-
	joueur(Joueur,est,_,_,_),
	case(X,Y,Joueur),
	XCase is X + 1,
	case(XCase,Y,TypeCase).
	
caseDevant(Joueur,TypeCase):-
	joueur(Joueur,ouest,_,_,_),
	case(X,Y,Joueur),
	XCase is X - 1,
	case(XCase,Y,TypeCase).

peutAvancer([_|ListeCoups],ListeCoups,Joueur):-
	caseDevant(Joueur,obstacle),!.
		
peutAvancer(ListeCoups,ListeCoups,Joueur):-!.
	
listeCoups(Joueur,ListeCoups):- 
	peutAvancer([avancer,tournerGauche,tournerDroite,tirer,attendre],ListeCoups,Joueur).
	
orientationDroite(nord,est).
orientationDroite(est,sud).
orientationDroite(sud,ouest).
orientationDroite(ouest,nord).

tournerDroite(Joueur):-
	joueur(Joueur,Orientation,VieJoueurs,DegatsBase,DefenseBase),
	retract(joueur(Joueur,Orientation,VieJoueurs,DegatsBase,DefenseBase)),
	orientationDroite(Orientation,NouvelleOrientation),
	assert(joueur(Joueur,NouvelleOrientation,VieJoueurs,DegatsBase,DefenseBase)).
	
orientationGauche(nord,ouest).
orientationGauche(ouest,sud).
orientationGauche(sud,est).
orientationGauche(est,nord).

tournerGauche(Joueur):-
	joueur(Joueur,Orientation,VieJoueurs,DegatsBase,DefenseBase),
	retract(joueur(Joueur,Orientation,VieJoueurs,DegatsBase,DefenseBase)),
	orientationGauche(Orientation,NouvelleOrientation),
	assert(joueur(Joueur,NouvelleOrientation,VieJoueurs,DegatsBase,DefenseBase)).

nouvelleCase(Joueur,X,Y,X,NvY):-
	joueur(Joueur,nord,_,_,_),
	NvY is Y - 1.

nouvelleCase(Joueur,X,Y,X,NvY):-
	joueur(Joueur,sud,_,_,_),
	NvY is Y + 1.

nouvelleCase(Joueur,X,Y,NvX,Y):-
	joueur(Joueur,est,_,_,_),
	NvX is X + 1.

nouvelleCase(Joueur,X,Y,NvX,Y):-
	joueur(Joueur,ouest,_,_,_),
	NvX is X - 1.	

% Modif T F =====================

obtenirBonus(Joueur,X,Y):-
	case(X,Y,bonus),
	joueur(Joueur,Orientation,Vie,Degats,Defense),
	NouvelleDefense is Defense + 1,
	retract(case(X,Y,bonus)),
	retract(joueur(Joueur,Orientation,Vie,Degats,Defense)),
	assert(joueur(Joueur,Orientation,Vie,Degats,NouvelleDefense)),!.
	
obtenirBonus(Joueur,X,Y):-
	case(X,Y,_).

avancer(Joueur):-
	case(X,Y,Joueur),
	nouvelleCase(Joueur,X,Y,NvX,NvY),
	obtenirBonus(Joueur,NvX,NvY),!,
	retract(case(X,Y,Joueur)),
	assert(case(NvX,NvY,Joueur)).


avancer(Joueur):-
	case(X,Y,Joueur),
	nouvelleCase(Joueur,X,Y,NvX,NvY),
	retract(case(X,Y,Joueur)),
	assert(case(NvX,NvY,Joueur)).
	
% ===============================


distance(X,X,Y1,Y2,Distance):-
	Y1 < Y2,
	Distance is Y2 - Y1. 

distance(X,X,Y1,Y2,Distance):-
	Y2 < Y1,
	Distance is Y1 - Y2. 

distance(X1,X2,Y,Y,Distance):-
	X1 < X2,
	Distance is X2 - X1. 

distance(X1,X2,Y,Y,Distance):-
	X2 < X1,
	Distance is X1 - X2. 
	
chercheCible(Joueur,nord,Type):-
	case(X,Y,Joueur),
	YMax is Y+1,
	repeat,
	between(1,YMax,Diff),
	NvY is Y - Diff,
	case(X,NvY,Type), 
	member(Type,[1,2,obstacle]),!.
	
chercheCible(Joueur,sud,Type):-
	case(X,Y,Joueur),
	YMin is Y+1,
	dimensions(_,DimY),
	repeat,
	between(YMin,DimY,NvY),
	case(X,NvY,Type), 
	member(Type,[1,2,obstacle]),!.	
	
chercheCible(Joueur,ouest,Type):-
	case(X,Y,Joueur),
	XMax is X+1,
	repeat,
	between(1,XMax,Diff),
	NvX is X - Diff,
	case(NvX,Y,Type), 
	member(Type,[1,2,obstacle]),!.
	
chercheCible(Joueur,est,Type):-
	case(X,Y,Joueur),
	XMax is X+1,
	dimensions(DimX,_),
	repeat,
	between(XMax,DimX,NvX),
	case(NvX,Y,Type), 
	member(Type,[1,2,obstacle]),!.
	
chercherCible(Joueur,Cible):-
	joueur(Joueur,Orientation,_,_,_),
	chercheCible(Joueur,Orientation,Cible).
	
calculDistance(Joueur,AutreJoueur,Distance):-
	case(X1,Y1,Joueur),
	case(X2,Y2,AutreJoueur),
	distance(X1,X2,Y1,Y2,Distance).
	
distanceCible(Joueur,AutreJoueur,Distance):-
	not(Joueur = AutreJoueur),
	not(AutreJoueur = obstacle),!,
	calculDistance(Joueur,AutreJoueur,Distance).

toucher(Joueur,AutreJoueur):-
	joueur(Joueur,_,_,DegatsInflige,_),
	joueur(AutreJoueur,Orientation,Vie,Degats,Defense),
	Defense > 0,
	NouvelleDefense is Defense -1,
	retract(joueur(AutreJoueur,Orientation,Vie,Degats,Defense)),
	assert(joueur(AutreJoueur,Orientation,Vie,Degats,NouvelleDefense)),!.
	
toucher(Joueur,AutreJoueur):-
	joueur(Joueur,_,_,DegatsInflige,_),
	joueur(AutreJoueur,Orientation,Vie,Degats,Defense),
	NouvelleVie is Vie - DegatsInflige,
	retract(joueur(AutreJoueur,Orientation,Vie,Degats,Defense)),
	assert(joueur(AutreJoueur,Orientation,NouvelleVie,Degats,Defense)),!.
		
tirer(Joueur):-
	chercherCible(Joueur,Cible),
	not(Cible = obstacle),
	distanceCible(Joueur,Cible,Distance),
	portee(Portee),
	(Distance < Portee; Distance = Portee),
	toucher(Joueur,Cible).
	
tirer(Joueur).
	
effectuerAction(Joueur,attendre).
effectuerAction(Joueur,avancer):-
	avancer(Joueur).
effectuerAction(Joueur,tournerDroite):-
	tournerDroite(Joueur).
effectuerAction(Joueur,tournerGauche):-
	tournerGauche(Joueur).
effectuerAction(Joueur,tirer):-
	tirer(Joueur).
