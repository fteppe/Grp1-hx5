:-consult('SD').

calculDistance(XJ1,YJ1,XJ2,YJ2,Distance):-
	XJ1 < XJ2,
	YJ1 < YJ2,
	Distance is YJ2 - YJ1 + XJ2 - XJ1.
	
calculDistance(XJ1,YJ1,XJ2,YJ2,Distance):-
	XJ1 > XJ2,
	calculDistance(XJ2,YJ1,XJ1,YJ2,Distance).
	
calculDistance(XJ1,YJ1,XJ2,YJ2,Distance):-
	YJ1 > YJ2,
	calculDistance(XJ1,YJ2,XJ2,YJ1,Distance).

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

simulationActions(J1,J2,J1N,J2N,ActionJ1,ActionJ2):-
	getDatasPlayer(J1,XJ1,YJ1,OrientJ1,VieJ1,DegatsJ1,DefenseJ1),
	getDatasPlayer(J2,XJ2,YJ2,OrientJ2,VieJ2,DegatsJ2,DefenseJ2),
	createDatasPlayer(J1N,XJ1,YJ1,OrientJ1,VieJ1,DegatsJ1,DefenseJ1),
	createDatasPlayer(J2N,XJ2,YJ2,OrientJ2,VieJ2,DegatsJ2,DefenseJ2).
	
initialiseDatas(J1,J2):-
	case(XJ1,YJ1,1),
	joueur(1,OrientJ1,VieJ1,DegatsJ1,DefenseJ1),
	case(XJ2,YJ2,2),
	joueur(2,OrientJ2,VieJ2,DegatsJ2,DefenseJ2),
	createDatasPlayer(J1,XJ1,YJ1,OrientJ1,VieJ1,DegatsJ1,DefenseJ1),
	createDatasPlayer(J2,XJ2,YJ2,OrientJ2,VieJ2,DegatsJ2,DefenseJ2).
	
valeurFeuille(J1,J2,1,Valeur):-
	getDatasPlayer(J1,XJ1,YJ1,OrientJ1,VieJ1,DegatsJ1,DefenseJ1),
	getDatasPlayer(J2,XJ2,YJ2,OrientJ2,VieJ2,DegatsJ2,DefenseJ2),
	calculDistance(XJ1,YJ1,XJ2,YJ2,Distance),
	Valeur is VieJ1 - VieJ2 + 100 - Distance.
	
valeurFeuille(J1,J2,2,Valeur):-
	valeurFeuille(J2,J1,1,Valeur).