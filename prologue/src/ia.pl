:-consult('SD').

calculDistance(XJ1,YJ1,XJ2,YJ2,Distance):-
	XJ1 =< XJ2,
	YJ1 =< YJ2,
	Distance is YJ2 - YJ1 + XJ2 - XJ1,!.
	
calculDistance(XJ1,YJ1,XJ2,YJ2,Distance):-
	XJ1 > XJ2,
	calculDistance(XJ2,YJ1,XJ1,YJ2,Distance),!.
	
calculDistance(XJ1,YJ1,XJ2,YJ2,Distance):-
	YJ1 > YJ2,
	calculDistance(XJ1,YJ2,XJ2,YJ1,Distance),!.

createDatasPlayer(Joueur,X,Y,Orient,Vie,Degats,Defense):-
	length(Joueur,6),
	nth0(0,Joueur,X),
	nth0(1,Joueur,Y),
	nth0(2,Joueur,Orient),
	nth0(3,Joueur,Vie),
	nth0(4,Joueur,Degats),
	nth0(5,Joueur,Defense).
	
getDatasPlayer(Joueur,X,Y,Orient,Vie,Degats,Defense):-
	nth0(0,Joueur,X),
	nth0(1,Joueur,Y),
	nth0(2,Joueur,Orient),
	nth0(3,Joueur,Vie),
	nth0(4,Joueur,Degats),
	nth0(5,Joueur,Defense).
	
absenceObstacle(XC,YC,XC,YC,_).

absenceObstacle(X,Y,_,_,_):-
	case(X,Y,obstacle),
	fail.
	
absenceObstacle(XJ,YJ,XC,YC,nord):-
	Ynext is YJ -1,
	absenceObstalce(XJ,YNext,XC,YC,nord).
	
absenceObstacle(XJ,YJ,XC,YC,sud):-
	Ynext is YJ +1,
	absenceObstalce(XJ,YNext,XC,YC,sud).
	
absenceObstacle(XJ,YJ,XC,YC,ouest):-
	Xnext is XJ -1,
	absenceObstalce(XNext,YJ,XC,YC,ouest).
	
absenceObstacle(XJ,YJ,XC,YC,est):-
	Xnext is XJ +1,
	absenceObstalce(XNext,YJ,XC,YC,est).
	
	
aPortee(Portee,Portee).
aPortee(Distance,Portee):-
	Distance < Portee.
	
alignement(X,Y1,X,Y2,nord):-
	Y1 > Y2.
	
alignement(X,Y1,X,Y2,sud):-
	Y2 > Y1.
	
alignement(X1,Y,X2,Y,ouest):-
	X1 > X2.
	
alignement(X1,Y,X2,Y,est):-
	X2 > X1.
	
peutToucher(Joueur,Cible):-
	getDatasPlayer(Joueur,XJ,YJ,OrientJ,_,_,_),
	getDatasPlayer(Cible,XC,YC,_,_,_,_),
	alignement(XJ,YJ,XC,XY,OrientJ),
	absenceObstacle(XJ,YJ,XC,YC,OrientJ),
	calculDistance(XJ,YJ,XC,YC,Distance),
	portee(Portee),
	aPortee(Distance,Portee).
	
	
updateTouche(VieI,0,DegatsSubis,VieF,0):-
	VieF is VieI - DegatsSubis.
	
updateTouche(Vie,DefI,_,Vie,DefF):-
	DefF is DefI - 1.
	
touche(Joueur,Cible,CibleN):-
	peutToucher(Joueur,Cible),!,
	getDatasPlayer(Joueur,_,_,_,_,DegatsSubis,_),
	getDatasPlayer(Cible,X,Y,Orient,VieI,Degats,DefenseI),
	updateTouche(VieI,DefenseI,DegatsSubis,VieF,DefenseF),
	createDatasPlayer(CibleN,X,Y,Orient,VieF,Degats,DefenseF).
	
touche(_,Cible,Cible).

faceAFace(X1,Y,X2,Y,ouest):-
	X2 is X1 + 1.
	
faceAFace(X1,Y,X2,Y,est):-
	X1 is X2 + 1.
	
faceAFace(X,Y1,X,Y2,sud):-
	Y2 is Y1 + 1.
	
faceAFace(X,Y1,X,Y2,nord):-
	Y1 is Y2 + 1.
	
mouvementPossible(X,Y,sud,X,Yav):-
	YAv is Y + 1,
	not(case(X,YAv,obstacle)).
	
mouvementPossible(X,Y,est,XAv,Y):-
	XAv is X + 1,
	not(case(XAv,Y,obstacle)).
	
mouvementPossible(X,Y,nord,X,YAv):-
	YAv is Y - 1,
	not(case(X,YAv,obstacle)).
	
mouvementPossible(X,Y,ouest,XAv,Y):-
	XAv is X - 1,
	not(case(XAv,Y,obstacle)).
		
simulationMouvement(JoueurInit,JoueurFinal):-
	getDatasPlayer(JoueurInit,X,Y,Orient,Vie,Degats,Defense),
	mouvementPossible(X,Y,Orient,NX,NY),!,
	createDatasPlayer(JoueurFinal,NX,NY,Orient,Vie,Degats,Defense).
	
simulationMouvement(Joueur,Joueur).
	
effectuerSimulation(J1,J2,J1N,J2,1,avancer):-
	simulationMouvement(J1,J1N).
		
effectuerSimulation(J1,J2,J1,J2N,2,avancer):-
	simulationMouvement(J2,J2N).
	
effectuerSimulation(J1,J2,J1N,J2,1,tournerDroite):-
	getDatasPlayer(J1,XJ1,YJ1,OrientJ1,VieJ1,DegatsJ1,DefenseJ1),
	orientationDroite(OrientJ1,OrientJ1N),
	createDatasPlayer(J1N,XJ1,YJ1,OrientJ1N,VieJ1,DegatsJ1,DefenseJ1).
	
effectuerSimulation(J1,J2,J1,J2N,2,tournerDroite):-
	getDatasPlayer(J2,XJ2,YJ2,OrientJ2,VieJ2,DegatsJ2,DefenseJ2),
	orientationDroite(OrientJ2,OrientJ2N),
	createDatasPlayer(J2N,XJ2,YJ2,OrientJ2N,VieJ2,DegatsJ2,DefenseJ2).
	
effectuerSimulation(J1,J2,J1N,J2,1,tournerGauche):-
	getDatasPlayer(J1,XJ1,YJ1,OrientJ1,VieJ1,DegatsJ1,DefenseJ1),
	orientationGauche(OrientJ1,OrientJ1N),
	createDatasPlayer(J1N,XJ1,YJ1,OrientJ1N,VieJ1,DegatsJ1,DefenseJ1).
	
effectuerSimulation(J1,J2,J1,J2N,2,tournerGauche):-
	getDatasPlayer(J2,XJ2,YJ2,OrientJ2,VieJ2,DegatsJ2,DefenseJ2),
	orientationGauche(OrientJ2,OrientJ2N),
	createDatasPlayer(J2N,XJ2,YJ2,OrientJ2N,VieJ2,DegatsJ2,DefenseJ2).
	
effectuerSimulation(J1,J2,J1,J2N,1,tirer):-
	touche(J1,J2,J2N).
	
effectuerSimulation(J1,J2,J1N,J2,2,tirer):-
	touche(J2,J1,J1N).
	
simulationActions(J1,J2,J1N,J2N,avancer,ActionJ2):-
	not(ActionJ2 is avancer),
	getDatasPlayer(J1,XJ1,YJ1,OrientJ1,VieJ1,DegatsJ1,DefenseJ1),
	getDatasPlayer(J2,XJ2,YJ2,OrientJ2,VieJ2,DegatsJ2,DefenseJ2),
	faceAFace(XJ1,YJ1,XJ2,YJ2,OrientJ1),!,
	effectuerSimulation(J1,J2,J1N,J2N,2,ActionJ2).
	
simulationActions(J1,J2,J1N,J2N,ActionJ1,avancer):-
	not(ActionJ1 is avancer),
	getDatasPlayer(J1,XJ1,YJ1,OrientJ1,VieJ1,DegatsJ1,DefenseJ1),
	getDatasPlayer(J2,XJ2,YJ2,OrientJ2,VieJ2,DegatsJ2,DefenseJ2),
	faceAFace(XJ2,YJ2,XJ1,YJ1,OrientJ2),!,
	effectuerSimulation(J1,J2,J1N,J2N,1,ActionJ1).
	

simulationActions(J1,J2,J1,J2,attendre,attendre).
	
simulationActions(J1,J2,J1N,J2N,attendre,ActionJ2):-
	effectuerSimulation(J1,J2,J1N,J2N,2,ActionJ2).
	
simulationActions(J1,J2,J1N,J2N,ActionJ1,attendre):-
	effectuerSimulation(J1,J2,J1N,J2N,1,ActionJ1).

	
simulationActions(J1,J2,J1N,J2N,ActionJ1,tirer):-
	effectuerSimulation(J1,J2,J1Nt,J2Nt,2,tirer),
	effectuerSimulation(J1Nt,J2Nt,J1N,J2N,1,ActionJ1).
	
simulationActions(J1,J2,J1N,J2N,ActionJ1,ActionJ2):-
	effectuerSimulation(J1,J2,J1Nt,J2Nt,1,ActionJ1),
	effectuerSimulation(J1Nt,J2Nt,J1N,J2N,2,ActionJ2).
	
	
initialiseDatas(J1,J2):-
	case(XJ1,YJ1,1),
	joueur(1,OrientJ1,VieJ1,DegatsJ1,DefenseJ1),
	case(XJ2,YJ2,2),
	joueur(2,OrientJ2,VieJ2,DegatsJ2,DefenseJ2),
	createDatasPlayer(J1,XJ1,YJ1,OrientJ1,VieJ1,DegatsJ1,DefenseJ1),
	createDatasPlayer(J2,XJ2,YJ2,OrientJ2,VieJ2,DegatsJ2,DefenseJ2).
	
valeurFeuille(J1,J2,1,Valeur,default):-
	getDatasPlayer(J1,XJ1,YJ1,OrientJ1,VieJ1,DegatsJ1,DefenseJ1),
	getDatasPlayer(J2,XJ2,YJ2,OrientJ2,VieJ2,DegatsJ2,DefenseJ2),
	calculDistance(XJ1,YJ1,XJ2,YJ2,Distance),
	Valeur is VieJ1 - VieJ2 + 100 - Distance.
	
valeurFeuille(J1,J2,2,Valeur,default):-
	valeurFeuille(J2,J1,1,Valeur).