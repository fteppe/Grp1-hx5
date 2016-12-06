:-consult('SD').

initialiseBonus(Bonus):-
	findall([X,Y],case(X,Y,bonus),Bonus).

positionEstBonus(X,Y,ListeBonus):-
	member([X,Y],ListeBonus).
	
utiliserBonus(Defense,DefenseF,X,Y,ListeBonusI,ListeBonusF):-
	positionEstBonus(X,Y,ListeBonusI),!,
	DefenseF is Defense +1,
	delete(ListeBonusI,[X,Y],ListeBonusF).
	
utiliserBonus(Defense,Defense,_,_,Bonus,Bonus).

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
	case(X,Y,obstacle),!,
	fail.
	
absenceObstacle(XJ,YJ,XC,YC,nord):-
	YNext is YJ -1,
	absenceObstacle(XJ,YNext,XC,YC,nord).
	
absenceObstacle(XJ,YJ,XC,YC,sud):-
	YNext is YJ +1,
	absenceObstacle(XJ,YNext,XC,YC,sud).
	
absenceObstacle(XJ,YJ,XC,YC,ouest):-
	XNext is XJ -1,
	absenceObstacle(XNext,YJ,XC,YC,ouest).
	
absenceObstacle(XJ,YJ,XC,YC,est):-
	XNext is XJ +1,
	absenceObstacle(XNext,YJ,XC,YC,est).
	
	
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
	alignement(XJ,YJ,XC,YC,OrientJ),
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
	portee(P),
	X2 < X1 + 1.
	
faceAFace(X1,Y,X2,Y,est):-
	X1 is X2 + 1.
	
faceAFace(X,Y1,X,Y2,sud):-
	Y2 is Y1 + 1.
	
faceAFace(X,Y1,X,Y2,nord):-
	Y1 is Y2 + 1.
	
mouvementPossible(X,Y,sud,X,YAv):-
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
		
simulationMouvement(JoueurInit,JoueurTemp,BonusI,BonusF):-
	getDatasPlayer(JoueurInit,X,Y,Orient,Vie,Degats,Defense),
	mouvementPossible(X,Y,Orient,NX,NY),!,
	utiliserBonus(Defense,DefenseF,NX,NY,BonusI,BonusF),
	createDatasPlayer(JoueurTemp,NX,NY,Orient,Vie,Degats,DefenseF).
	
simulationMouvement(Joueur,Joueur,Bonus,Bonus).
	
effectuerSimulation(J1,J2,J1N,J2,1,avancer,BonusI,BonusF):-
	simulationMouvement(J1,J1N,BonusI,BonusF).
		
effectuerSimulation(J1,J2,J1,J2N,2,avancer,BonusI,BonusF):-
	simulationMouvement(J2,J2N,BonusI,BonusF).
	
effectuerSimulation(J1,J2,J1N,J2,1,tournerDroite,Bonus,Bonus):-
	getDatasPlayer(J1,XJ1,YJ1,OrientJ1,VieJ1,DegatsJ1,DefenseJ1),
	orientationDroite(OrientJ1,OrientJ1N),
	createDatasPlayer(J1N,XJ1,YJ1,OrientJ1N,VieJ1,DegatsJ1,DefenseJ1).
	
effectuerSimulation(J1,J2,J1,J2N,2,tournerDroite,Bonus,Bonus):-
	getDatasPlayer(J2,XJ2,YJ2,OrientJ2,VieJ2,DegatsJ2,DefenseJ2),
	orientationDroite(OrientJ2,OrientJ2N),
	createDatasPlayer(J2N,XJ2,YJ2,OrientJ2N,VieJ2,DegatsJ2,DefenseJ2).
	
effectuerSimulation(J1,J2,J1N,J2,1,tournerGauche,Bonus,Bonus):-
	getDatasPlayer(J1,XJ1,YJ1,OrientJ1,VieJ1,DegatsJ1,DefenseJ1),
	orientationGauche(OrientJ1,OrientJ1N),
	createDatasPlayer(J1N,XJ1,YJ1,OrientJ1N,VieJ1,DegatsJ1,DefenseJ1).
	
effectuerSimulation(J1,J2,J1,J2N,2,tournerGauche,Bonus,Bonus):-
	getDatasPlayer(J2,XJ2,YJ2,OrientJ2,VieJ2,DegatsJ2,DefenseJ2),
	orientationGauche(OrientJ2,OrientJ2N),
	createDatasPlayer(J2N,XJ2,YJ2,OrientJ2N,VieJ2,DegatsJ2,DefenseJ2).
	
effectuerSimulation(J1,J2,J1,J2N,1,tirer,Bonus,Bonus):-
	touche(J1,J2,J2N).
	
effectuerSimulation(J1,J2,J1N,J2,2,tirer,Bonus,Bonus):-
	touche(J2,J1,J1N).
	
simulationActions(J1,J2,J1N,J2N,avancer,ActionJ2,BonusI,BonusF):-
	not(ActionJ2 = avancer),
	getDatasPlayer(J1,XJ1,YJ1,OrientJ1,_,_,_),
	getDatasPlayer(J2,XJ2,YJ2,_,_,_,_),
	faceAFace(XJ1,YJ1,XJ2,YJ2,OrientJ1),!,
	effectuerSimulation(J1,J2,J1N,J2N,2,ActionJ2,BonusI,BonusF).
	
simulationActions(J1,J2,J1N,J2N,ActionJ1,avancer,BonusI,BonusF):-
	not(ActionJ1 = avancer),
	getDatasPlayer(J1,XJ1,YJ1,_,_,_,_),
	getDatasPlayer(J2,XJ2,YJ2,OrientJ2,_,_,_),
	faceAFace(XJ2,YJ2,XJ1,YJ1,OrientJ2),!,
	effectuerSimulation(J1,J2,J1N,J2N,1,ActionJ1,BonusI,BonusF).
	

simulationActions(J1,J2,J1,J2,attendre,attendre,Bonus,Bonus).
	
simulationActions(J1,J2,J1N,J2N,attendre,ActionJ2,BonusI,BonusF):-
	effectuerSimulation(J1,J2,J1N,J2N,2,ActionJ2,BonusI,BonusF).
	
simulationActions(J1,J2,J1N,J2N,ActionJ1,attendre,BonusI,BonusF):-
	effectuerSimulation(J1,J2,J1N,J2N,1,ActionJ1,BonusI,BonusF).

	
simulationActions(J1,J2,J1N,J2N,ActionJ1,tirer,BonusI,BonusF):-
	effectuerSimulation(J1,J2,J1Nt,J2Nt,2,tirer,BonusI,BonusT),
	effectuerSimulation(J1Nt,J2Nt,J1N,J2N,1,ActionJ1,BonusT,BonusF).
	
simulationActions(J1,J2,J1N,J2N,ActionJ1,ActionJ2,BonusI,BonusF):-
	effectuerSimulation(J1,J2,J1Nt,J2Nt,1,ActionJ1,BonusI,BonusT),
	effectuerSimulation(J1Nt,J2Nt,J1N,J2N,2,ActionJ2,BonusT,BonusF).
	
	
initialiseDatas(J1,J2,Bonus):-
	case(XJ1,YJ1,1),
	joueur(1,OrientJ1,VieJ1,DegatsJ1,DefenseJ1),
	case(XJ2,YJ2,2),
	joueur(2,OrientJ2,VieJ2,DegatsJ2,DefenseJ2),
	createDatasPlayer(J1,XJ1,YJ1,OrientJ1,VieJ1,DegatsJ1,DefenseJ1),
	createDatasPlayer(J2,XJ2,YJ2,OrientJ2,VieJ2,DegatsJ2,DefenseJ2),
	initialiseBonus(Bonus).
	
valeurFeuille(J1,J2,1,Valeur,default):-
	getDatasPlayer(J1,XJ1,YJ1,OrientJ1,VieJ1,DegatsJ1,DefenseJ1),
	getDatasPlayer(J2,XJ2,YJ2,OrientJ2,VieJ2,DegatsJ2,DefenseJ2),
	calculDistance(XJ1,YJ1,XJ2,YJ2,Distance),
	Valeur is VieJ2 * 50 + 100 - Distance.
	
valeurFeuille(J1,J2,2,Valeur,default):-
	valeurFeuille(J2,J1,1,Valeur,default).

valeurFeuilleBis(J1,J2,1,Valeur,default):-
	getDatasPlayer(J1,XJ1,YJ1,OrientJ1,VieJ1,DegatsJ1,DefenseJ1),
	getDatasPlayer(J2,XJ2,YJ2,OrientJ2,VieJ2,DegatsJ2,DefenseJ2),
	Valeur is (2*VieJ1 + DefenseJ1)-(2*VieJ2 + DefenseJ2).

valeurFeuilleBis(J1,J2,1,Valeur,rush):-
	getDatasPlayer(J1,XJ1,YJ1,OrientJ1,VieJ1,DegatsJ1,DefenseJ1),
	getDatasPlayer(J2,XJ2,YJ2,OrientJ2,VieJ2,DegatsJ2,DefenseJ2),
	Valeur is VieJ1 - 2*VieJ2 - 3*Distance.

valeurFeuilleBis(J1,J2,1,Valeur,def):-
	getDatasPlayer(J1,XJ1,YJ1,OrientJ1,VieJ1,DegatsJ1,DefenseJ1),
	getDatasPlayer(J2,XJ2,YJ2,OrientJ2,VieJ2,DegatsJ2,DefenseJ2),
	Valeur is (3*VieJ1 + 2*DefenseJ1)-(VieJ2 + DefenseJ2) + Distance.

valeurFeuilleBis(J1,J2,2,Valeur,TypeIA):-
	valeurFeuilleBis(J2,J1,1,Valeur,TypeIA).
