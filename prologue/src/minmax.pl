:-consult('SD').
:-consult('ia').

:-dynamic actions/1.

actionsPossibles(Liste):-
	length(Liste,4),
	nth0(0,Liste,avancer),
	nth0(1,Liste,tournerGauche),
	nth0(2,Liste,tournerDroite),
	nth0(3,Liste,tirer).
	
toutesAction(Liste):-
	actions(Liste).
	
toutesAction(ListeCombinaisons):-
	actionsPossibles(Liste),
	findall([X,Y],(member(X,Liste),member(Y,Liste)),ListeCombinaisons),
	assert(actions(ListeCombinaisons)).

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

minmax(CurrentDepth,MaxDepth,J1,J2,Bonus,ValueR,ActionJ1,ActionJ2,TypePonderation):-
	simulationActions(J1,J2,J1N,J2N,ActionJ1,ActionJ2,Bonus,BonusF),
	NextDepth is CurrentDepth + 1,
	toutesAction(ListeActions),!,
	findall(Value,(
		member([Action1,Action2],ListeActions),
		minmax(NextDepth,MaxDepth,J1N,J2N,BonusF,Value,Action1,Action2,TypePonderation)),
		Resultats),!,
	calculValeur(Resultats,ValueR).
	
	
choixAction(J1,J2,Bonus,MaxDepth,TypePonderation,Action):-
	toutesAction(ListeActions),
	findall([Value,Action1],(
		member([Action1,Action2],ListeActions),
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
	
