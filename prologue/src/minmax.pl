/*
	Choix des actions via un algorithme minmax, adapté (selon la technique de Monte Carlo)
*/
:-consult('SD').
:-consult('ia').

/*
	Permet de savoir si un obstacle est présent devant Joueur
*/
caseDevantMM(Joueur,TypeCase):-
	nth0(0,Joueur,X),
	nth0(1,Joueur,Y),
	nth0(2,Joueur,nord),
	YCase is Y - 1,
	case(X,YCase,TypeCase).
	
caseDevantMM(Joueur,TypeCase):-
	nth0(0,Joueur,X),
	nth0(1,Joueur,Y),
	nth0(2,Joueur,sud),
	YCase is Y + 1,
	case(X,YCase,TypeCase).
	
caseDevantMM(Joueur,TypeCase):-
	nth0(0,Joueur,X),
	nth0(1,Joueur,Y),
	nth0(2,Joueur,est),
	XCase is X + 1,
	case(XCase,Y,TypeCase).
	
caseDevantMM(Joueur,TypeCase):-
	nth0(0,Joueur,X),
	nth0(1,Joueur,Y),
	nth0(2,Joueur,ouest),
	XCase is X - 1,
	case(XCase,Y,TypeCase).
	
/*
	Permet de savoir si l'adveraire est est visible par Joueur
*/
chercheCibleMM(Joueur,AutreJoueur,_,Type):-  
	nth0(0,Joueur,X),
	nth0(1,Joueur,Y),
	nth0(0,AutreJoueur,X),
	nth0(1,AutreJoueur,Y),
	Type = joueur,!.
	
chercheCibleMM(Joueur,AutreJoueur,nord,Type):-
	nth0(0,Joueur,X1),
	nth0(1,Joueur,Y1),
	nth0(0,AutreJoueur,X2),
	nth0(1,AutreJoueur,Y2),
	YMax is Y1+1,
	repeat,
	between(1,YMax,Diff),
	NvY is Y1 - Diff,
	((case(X1,NvY,obstacle), 
	Type = obstacle);(X1 = X2, NvY = Y2,
	Type = joueur)),!.
	
chercheCibleMM(Joueur,AutreJoueur,sud,Type):-
	nth0(0,Joueur,X1),
	nth0(1,Joueur,Y1),
	nth0(0,AutreJoueur,X2),
	nth0(1,AutreJoueur,Y2),
	YMin is Y1+1,
	dimensions(_,DimY),
	repeat,
	between(YMin,DimY,NvY),
	((case(X1,NvY,obstacle), 
	Type = obstacle);(X1 = X2, NvY = Y2,
	Type = joueur)),!.	
	
chercheCibleMM(Joueur,AutreJoueur,ouest,Type):-
	nth0(0,Joueur,X1),
	nth0(1,Joueur,Y1),
	nth0(0,AutreJoueur,X2),
	nth0(1,AutreJoueur,Y2),
	XMax is X1+1,
	repeat,
	between(1,XMax,Diff),
	NvX is X1 - Diff,
	((case(NvX,Y1,obstacle), 
	Type = obstacle);(Y1 = Y2, NvX = X2,
	Type = joueur)),!.
	
chercheCibleMM(Joueur,AutreJoueur,est,Type):-
	nth0(0,Joueur,X1),
	nth0(1,Joueur,Y1),
	nth0(0,AutreJoueur,X2),
	nth0(1,AutreJoueur,Y2),
	XMax is X1+1,
	dimensions(DimX,_),
	repeat,
	between(XMax,DimX,NvX),
	((case(NvX,Y1,obstacle), 
	Type = obstacle);(Y1 = Y2, NvX = X2,
	Type = joueur)),!.	

chercherCibleMM(Joueur,AutreJoueur,Cible):-
	nth0(2,Joueur,Orientation),
	chercheCibleMM(Joueur,AutreJoueur,Orientation,Cible).

/*
	Permet de savoir calculer la distance de Manhattan entre deux Joueurs (dont les craactéritiques sont regroupées sous forme de liste)
*/
calculDistanceMM(Joueur,AutreJoueur,Distance):-
	nth0(0,Joueur,X1),
	nth0(1,Joueur,Y1),
	nth0(0,AutreJoueur,X2),
	nth0(1,AutreJoueur,Y2),
	distance(X1,X2,Y1,Y2,Distance).

/*
	Permet de savoir si l'adveraire est à portée de Joueur
*/
vaToucherMM(Joueur, AutreJoueur):-
	chercherCibleMM(Joueur,AutreJoueur,Cible),
	Cible = joueur, !,
	calculDistanceMM(Joueur,AutreJoueur,Distance),
	portee(Portee),
	(Distance < Portee ; Distance = Portee).

/*
	Indique si Joueur peut avancer sans rencontrer d'obstacle
*/	
peutAvancerMM(ListeCoupsInitiale,ListeCoups,Joueur):-
	caseDevantMM(Joueur,obstacle), delete(ListeCoupsInitiale, avancer, ListeCoups),!.
	
peutAvancerMM(ListeCoups,ListeCoups,Joueur):-!.

/*
	Indique si Joueur peut tirer et toucher AutreJoueur
*/		
peutTirerMM(ListeCoupsInitiale,ListeCoups,Joueur, AutreJoueur):-
	not(vaToucherMM(Joueur, AutreJoueur)), delete(ListeCoupsInitiale, tirer, ListeCoups),!.
	
peutTirerMM(ListeCoups,ListeCoups,Joueur, AutreJoueur):-!.


/* 
	Récupération de la liste d'actions effectuables par chaque joueur
*/	
actionsPossibles(Joueur, AutreJoueur, ListeCoups):- 
	peutAvancerMM([avancer, tournerGauche,tournerDroite, tirer],ListeCoupsInt,Joueur),
	peutTirerMM(ListeCoupsInt,ListeCoups,Joueur,AutreJoueur),!.
	
/* 
	Récupération de toutes les combinaisons d'actions possibles pour le tour actuel 
*/	
toutesAction(Joueur, AutreJoueur, ListeCombinaisons):-
	actionsPossibles(Joueur, AutreJoueur, Liste1),
	actionsPossibles(AutreJoueur, Joueur, Liste2),
	findall([X,Y],(member(X,Liste1),member(Y,Liste2)),ListeCombinaisons).

/*
	Calcul de la moyenne des valeurs de feuille obtenues sur une branche
*/
calculValeur(Resultats,Valeur):-
	sumlist(Resultats,Somme),
	length(Resultats,Taille),
	(Taille>0 ->
	Valeur is Somme / Taille ;
	Valeur is 0	).

/*
	Application de l'algorithme minmax
*/	
minmax(_,_,J1,_,_,-10000000,_,_,_):-
	getDatasPlayer(J1,_,_,_,0,_,_),!.
	
minmax(_,_,_,J2,_,10000000,_,_,_):-
	getDatasPlayer(J2,_,_,_,0,_,_),!.
	
minmax(Depth,Depth,J1,J2,Bonus,Value,ActionJ1,ActionJ2,TypePonderation):-
	simulationActions(J1,J2,J1N,J2N,ActionJ1,ActionJ2,Bonus,_), % On simule la combinaison d'action indiquée en entrée
	%valeurFeuille(J1N,J2N,1,Value,TypePonderation),!.
	valeurFeuilleBis(J1N,J2N,1,Value,TypePonderation),!. % On calcule la valeur de la feuille atteinte (en bout de branche)

minmax(CurrentDepth,MaxDepth,J1,J2,Bonus,ValueR,ActionJ1,ActionJ2,TypePonderation):-
	simulationActions(J1,J2,J1N,J2N,ActionJ1,ActionJ2,Bonus,BonusF), % On simule la combinaison d'action indiquée en entrée
	NextDepth is CurrentDepth + 1,
	toutesAction(J1N, J2N, ListeActions), !, % Obtentions des combinaisons d'action possibles
	findall(Value,(
		member([Action1, Action2], ListeActions),
		minmax(NextDepth,MaxDepth,J1N,J2N,BonusF,Value,Action1,Action2,TypePonderation)),
		Resultats),!,
	calculValeur(Resultats,ValueR).
	
/*
	Première itération de l'algorithme minmax (adapté selon la méthode de Monte Carlo)
*/	
choixAction(J1,J2,Bonus,MaxDepth,TypePonderation,Action,Randomisation):-
	toutesAction(J1, J2, ListeActions),!,
	findall([Value,Action1],(
		member([Action1, Action2], ListeActions),
		minmax(1,MaxDepth,J1,J2,Bonus,Value,Action1,Action2,TypePonderation)),
		Resultats),!,
	choixResultats(Resultats,Action,Randomisation).

choixResultats(Resultats,Action,random):-
	totalValeurs(Resultats,Total),
	random_between(0,Total,Rand),
	choixRandom(Rand,Action).
	
/*
	Analyse des résultats de l'algorithme et sélection du meilleur coup
*/
choixResultats([[Value,ActionTmp]|Resultats],Action,_):-
	parcoursResultats(Value,ActionTmp,Resultats,Action).

/*
	Analyse des résultats de l'algorithme et sélection du meilleur coup à partir d'un maximum initial
*/	
parcoursResultats(_,Action,[],Action).
	
parcoursResultats(ActualValue,_,[[Value,ActionTmp]|Resultats],Action):-
	Value > ActualValue,!,
	parcoursResultats(Value,ActionTmp,Resultats,Action).
	
parcoursResultats(ActualValue,ActualAction,[[_,_]|Resultats],Action):-
	parcoursResultats(ActualValue,ActualAction,Resultats,Action),!.

/*
	Calcul et choix des actions avec alea
*/

totalValeurs(Resultats,Total):-
	findall(Value,(member([Value,_],Resultats),ListeValues),
	sum_list(ListeValues,Total).
	
choixRandom([[Value,Action]|_],Rand,Action):-
	not(Value < Rand),!.
	
choixRandom([[Value,ActionTmp]|Resultats],Rand,Action):-
	Value < Rand,
	choixRandom(Resultats,Rand,Action).	