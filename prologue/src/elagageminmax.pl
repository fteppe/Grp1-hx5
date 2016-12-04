:- consult('ia').
:-dynamic meilleurCout/2.
:-dynamic meilleurCoup/2.
:-dynamic unAlpha/2.
:-dynamic unBeta/2.

minimax(Joueur, 0, _, Val, Alpha, Beta) :- tourAct(Joueur), eval(Val),!.

minimax(Joueur, 0, _, Val, Alpha, Beta) :- evalAdv(Val),!.

minimax(_, _, _, Val, Alpha, Beta) :- gameover(_), eval(Val), !.

minimax(Joueur, Profondeur, MeilleurCoupP, MeilleurVal, Alpha, Beta) :- 	
															assert(meilleurCout(Profondeur, -inf)),
															assert(meilleurCoup(Profondeur, avancer)),
															listeCoups(Joueur, ListeCoups),
															joueur(AutreJoueur, _, _, _, _), not(AutreJoueur == Joueur),
															assert(unAlpha(Profondeur, Alpha)),
															assert(unBeta(Profondeur, Beta)),
															repeat, 
															unAlpha(Profondeur, AlphaAct), unBeta(Profondeur, BetaAct), AlphaAct@<BetaAct,
															member(Coup, ListeCoups), initialiseDatas(EtatJ1, EtatJ2),
															effectuerAction(Joueur, Coup),
															NouvelleProfondeur is (Profondeur - 1),
															opposite(BetaAct, OppBetaAct), opposite(AlphaAct, OppAlphaAct),
															minimax(AutreJoueur, NouvelleProfondeur, UnMeilleurCoup, UneMeilleureVal, OppBetaAct, OppAlphaAct),
															annulerAction(Joueur, Coup, EtatJ1, EtatJ2), opposite(UneMeilleureVal, OppUneMeilleurVal),
															meilleurCout(Profondeur, MeillCout), evaluerMax(OppUneMeilleurVal, AlphaAct, Result, Profondeur, Coup), 
															retract(meilleurCout(Profondeur, MeillCout)), assert(meilleurCout(Profondeur, Result)),
															last(ListeCoups, Coup), !, meilleurCout(Profondeur, MeilleurVal), meilleurCoup(Profondeur, MeilleurCoupP), 
															retract(meilleurCout(Profondeur, _)), 
															retract(meilleurCoup(Profondeur, _)),
															retract(unAlpha(Profondeur, _)), retract(unBeta(Profondeur, _)).
																													
opposite(inf, -inf) :- !.
opposite(-inf, inf) :- !.
opposite(Val, -Val) :- !.

evaluerMax(-inf, inf, R, Profondeur, NouveauCoup) :- R = inf, !.

evaluerMax(inf, -inf, R, Profondeur, NouveauCoup) :- R = inf, retract(meilleurCoup(Profondeur, _)), assert(meilleurCoup(Profondeur, NouveauCoup)), retract(unAlpha(Profondeur, _)), assert(unAlpha(Profondeur, inf)), !.

evaluerMax(A, -inf, R, Profondeur, NouveauCoup) :- R is A, retract(meilleurCoup(Profondeur, _)), assert(meilleurCoup(Profondeur, NouveauCoup)), retract(unAlpha(Profondeur, _)), assert(unAlpha(Profondeur, A)),!.

evaluerMax(-inf, A, R, Profondeur, NouveauCoup) :- R is A, !.

evaluerMax(A, inf, R, Profondeur, NouveauCoup) :- R = inf, !.

evaluerMax(inf, A, R, Profondeur, NouveauCoup) :- R = inf, retract(meilleurCoup(Profondeur, _)), assert(meilleurCoup(Profondeur, NouveauCoup)), retract(unAlpha(Profondeur, _)), assert(unAlpha(Profondeur, inf)), !.

evaluerMax(A, B, R, Profondeur, NouveauCoup) :- A >= B, R is A, retract(meilleurCoup(Profondeur, _)), assert(meilleurCoup(Profondeur, NouveauCoup)), retract(unAlpha(Profondeur, _)), assert(unAlpha(Profondeur, A)), !.

evaluerMax(A, B, R, Profondeur, NouveauCoup) :- B > A, R is B, !.

annulerAction(Joueur, tirer, EtatInitJ1, EtatInitJ2) :- joueur(Joueur, Orientation,Vie,Degats,Defense), joueur(AutreJoueur,AOrientation,AVie,ADegats,ADefense), not(AutreJoueur == Joueur),
											nth0(3, EtatInitJ1, NouvelleVie), nth0(5, EtatInitJ1, NouvelleDefense),
											nth0(3, EtatInitJ2, ANouvelleVie), nth0(5, EtatInitJ2, ANouvelleDefense),
											retract(joueur(Joueur, Orientation,Vie,Degats,Defense)),
											assert(joueur(Joueur,Orientation,NouvelleVie,Degats,NouvelleDefense)),
											retract(joueur(AutreJoueur,AOrientation,AVie,ADegats,ADefense)),
											assert(joueur(AutreJoueur,AOrientation,ANouvelleVie,ADegats,ANouvelleDefense)),!. 		
												
annulerAction(Joueur,tournerGauche, _, _) :- effectuerAction(Joueur, tournerDroite), !. 

annulerAction(Joueur,tournerDroite, _, _) :- effectuerAction(Joueur, tournerGauche), !.

annulerAction(Joueur,avancer, _, _) :- 	effectuerAction(Joueur, tournerDroite), effectuerAction(Joueur, tournerDroite), 
									effectuerAction(Joueur, avancer), effectuerAction(Joueur, tournerDroite), 
									effectuerAction(Joueur, tournerDroite), !.
									
annulerAction(Joueur,attendre, _, _) :- effectuerAction(Joueur, attendre), !. 
			

eval(Val) :- 	tourAct(Joueur),
				case(XJ1,YJ1,Joueur),
				joueur(Joueur,OrientJ,0,DegatsJ,DefenseJ),
				joueur(AutreJoueur,OrientAJ,VieAJ,DegatsAJ,DefenseAJ),
				not(AutreJoueur == Joueur), case(XJ2,YJ2,AutreJoueur), 
				calculDistance(XJ1, YJ1, XJ2, YJ2, Distance),
				Val = -inf, !.
				
eval(Val) :- 	tourAct(Joueur),
				case(XJ1,YJ1,Joueur), 
				joueur(Joueur,OrientJ,VieJ,DegatsJ,DefenseJ),
				joueur(AutreJoueur,OrientAJ,0,DegatsAJ,DefenseAJ),
				not(AutreJoueur == Joueur), case(XJ2,YJ2,AutreJoueur),
				calculDistance(XJ1, YJ1, XJ2, YJ2, Distance),
				Val = inf, !.
											
eval(Val) :- 	tourAct(Joueur),
				case(XJ1,YJ1,Joueur), 
				joueur(Joueur,OrientJ,VieJ,DegatsJ,DefenseJ),
				joueur(AutreJoueur,OrientAJ,VieAJ,DegatsAJ,DefenseAJ),
				not(AutreJoueur == Joueur), case(XJ2,YJ2, AutreJoueur),
				calculDistance(XJ1, YJ1, XJ2, YJ2, Distance),
				dimensions(X, Y),
				Val is ((X + Y)*10 + 1000 + (-1*VieAJ + -1*DefenseAJ)*50 - Distance*10), !.
				
evalAdv(Val) :- 	tourAct(Joueur),
				case(XJ1,YJ1,Joueur),
				joueur(Joueur,OrientJ,0,DegatsJ,DefenseJ),
				joueur(AutreJoueur,OrientAJ,VieAJ,DegatsAJ,DefenseAJ),
				not(AutreJoueur == Joueur), case(XJ2,YJ2,AutreJoueur), 
				calculDistance(XJ1, YJ1, XJ2, YJ2, Distance),
				Val = -inf, !.
				
evalAdv(Val) :- 	tourAct(Joueur),
				case(XJ1,YJ1,Joueur), 
				joueur(Joueur,OrientJ,VieJ,DegatsJ,DefenseJ),
				joueur(AutreJoueur,OrientAJ,0,DegatsAJ,DefenseAJ),
				not(AutreJoueur == Joueur), case(XJ2,YJ2,AutreJoueur),
				calculDistance(XJ1, YJ1, XJ2, YJ2, Distance),
				Val = inf, !.
											
evalAdv(Val) :- 	tourAct(Joueur),
				case(XJ1,YJ1,Joueur), 
				joueur(Joueur,OrientJ,VieJ,DegatsJ,DefenseJ),
				joueur(AutreJoueur,OrientAJ,VieAJ,DegatsAJ,DefenseAJ),
				not(AutreJoueur == Joueur), case(XJ2,YJ2, AutreJoueur),
				calculDistance(XJ1, YJ1, XJ2, YJ2, Distance),
				dimensions(X, Y),
				Val is ((X + Y)*10 + 1000 + (VieJ + DefenseJ)*50 + Distance*10), !.