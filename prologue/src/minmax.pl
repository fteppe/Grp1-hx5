:-consult('SD').
:-consult('ia').

:-dynamic actions/1.

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
	
peutAvancerMM([_|ListeCoups],ListeCoups,Joueur):-
	caseDevantMM(Joueur, obstacle),!.
		
peutAvancerMM(ListeCoups,ListeCoups,Joueur):-!.

/* On récupère la liste d'actions effectuables par chaque joueur*/	
actionsPossibles(Joueur, ListeCoups):- 
	peutAvancerMM([avancer, tournerGauche,tournerDroite, tirer],ListeCoups,Joueur).
	
/* On charche toutes les combinaisons d'actions possibles pour le tour actuel */	
toutesAction(Joueur1, Joueur2, ListeCombinaisons):-
	actionsPossibles(Joueur1, Liste1),
	actionsPossibles(Joueur2, Liste2),
	findall([X,Y],(member(X,Liste1),member(Y,Liste2)),ListeCombinaisons).

calculValeur(Resultats,Valeur):-
	sumlist(Resultats,Somme),
	length(Resultats,Taille),
	(Taille>0 ->
	Valeur is Somme / Taille ;
	Valeur is 0	).
	
minmax(_,_,J1,_,_,-10000000,_,_,_):-
	getDatasPlayer(J1,_,_,_,0,_,_),!.
	
minmax(_,_,_,J2,_,10000000,_,_,_):-
	getDatasPlayer(J2,_,_,_,0,_,_),!.
	
minmax(Depth,Depth,J1,J2,Bonus,Value,ActionJ1,ActionJ2,TypePonderation):-
	simulationActions(J1,J2,J1N,J2N,ActionJ1,ActionJ2,Bonus,_),
	valeurFeuille(J1N,J2N,1,Value,TypePonderation),!.
	%valeurFeuilleBis(J1N,J2N,1,Value,TypePonderation),!.

minmax(CurrentDepth,MaxDepth,J1,J2,Bonus,ValueR,ActionJ1,ActionJ2,TypePonderation):-
	simulationActions(J1,J2,J1N,J2N,ActionJ1,ActionJ2,Bonus,BonusF),
	NextDepth is CurrentDepth + 1,
	toutesAction(J1N, J2N, ListeActions), !,
	findall(Value,(
		member([Action1, Action2], ListeActions),
		minmax(NextDepth,MaxDepth,J1N,J2N,BonusF,Value,Action1,Action2,TypePonderation)),
		Resultats),!,
	calculValeur(Resultats,ValueR).
	
	
choixAction(J1,J2,Bonus,MaxDepth,TypePonderation,Action):-
	toutesAction(J1, J2, ListeActions),!,
	findall([Value,Action1],(
		member([Action1, Action2], ListeActions),
		minmax(1,MaxDepth,J1,J2,Bonus,Value,Action1,Action2,TypePonderation)),
		Resultats),!,
	choixResultats(Resultats,Action).

choixResultats([[Value,ActionTmp]|Resultats],Action):-
	parcoursResultats(Value,ActionTmp,Resultats,Action).
	
parcoursResultats(_,Action,[],Action).
	
parcoursResultats(ActualValue,_,[[Value,ActionTmp]|Resultats],Action):-
	Value > ActualValue,!,
	parcoursResultats(Value,ActionTmp,Resultats,Action).
	
parcoursResultats(ActualValue,ActualAction,[[_,_]|Resultats],Action):-
	parcoursResultats(ActualValue,ActualAction,Resultats,Action),!.
	
