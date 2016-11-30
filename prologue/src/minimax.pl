:- consult('ia').

minimax(_, 0, _, Val) :- eval(Val), !.

minimax(_, _, _, Val) :- gameover(_), eval(Val), !.

minimax(Joueur, Profondeur, MeilleurCoup, MeilleurVal) :- 	listeCoups(Joueur, ListeCoups),
															assert(valeurActuelle(-inf)),
															length(ListeCoups, LongueurListe),
															NewLongueurListe is LongueurListe + 1,
															length(ListeCoupsFuturs,NewLongueurListe),
															length(ListeCouts,NewLongueurListe),
															repeat, 
															nth0(X, ListeCoups, Coup), initialiseDatas(EtatJ1, EtatJ2),
															joueur(AutreJoueur, _, _, _, _), not(AutreJoueur == Joueur),
															effectuerAction(Joueur, Coup),
															NouvelleProfondeur is Profondeur - 1,
															minimax(AutreJoueur, NouvelleProfondeur, UnMeilleurCoup, UneMeilleureVal),
															annulerAction(Joueur, Coup, EtatJ2),
															nth0(Position, ListeCoups, Coup), NouvellePosition is Position + 1,
															nth0(NouvellePosition, ListeCouts, - UneMeilleureVal),
															nth0(NouvellePosition, ListeCoupsFuturs, UnMeilleurCoup),
															last(ListeCoups, Coup), !, max_list(ListeCouts, Min), nth0(PositMin, ListeCouts, Min),
															nth0(PositMin, ListeCoupsFuturs, CoupFutur), MeilleurCoup is CoupFutur, MeilleurVal is Min.
												
												
annulerAction(Joueur, tournerGauche, _) :- effectuerAction(Joueur, tournerDroite). 

annulerAction(Joueur, tournerDroite, _) :- effectuerAction(Joueur, tournerGauche).

annulerAction(Joueur, avancer, _) :- 	effectuerAction(Joueur, tournerDroite), effectuerAction(Joueur, tournerDroite), 
									effectuerAction(Joueur, avancer), effectuerAction(Joueur, tournerDroite), 
									effectuerAction(Joueur, tournerDroite).
									
annulerAction(Joueur, attendre, _) :- effectuerAction(Joueur, attendre). 

annulerAction(Joueur, tirer, EtatInitJ2) :- joueur(AutreJoueur, _, _, _, _, _), not(AutreJoueur == Joueur),
											nth0(3, EtatInitJ2, NouvelleVie), nth0(5, EtatInitJ2, NouvelleDefense),
											retract(joueur(AutreJoueur,Orientation,Vie,Degats,Defense)),
											assert(joueur(AutreJoueur,Orientation,NouvelleVie,Degats,NouvelleDefense)),!. 				

eval(Val) :- 	case(XJ1,YJ1,1),
				turn(Joueur), 
				joueur(Joueur,OrientJ,0,DegatsJ,DefenseJ),
				case(XJ2,YJ2,2),
				joueur(AutreJoueur,OrientAJ,VieAJ,DegatsAJ,DefenseAJ),
				not(AutreJoueur == Joueur), calculDistance(XJ1, YJ1, XJ2, YJ2, Distance),
				Val == -inf.
				
eval(Val) :- 	case(XJ1,YJ1,1),
				turn(Joueur), 
				joueur(Joueur,OrientJ,VieJ,DegatsJ,DefenseJ),
				case(XJ2,YJ2,2),
				joueur(AutreJoueur,OrientAJ,0,DegatsAJ,DefenseAJ),
				not(AutreJoueur == Joueur), calculDistance(XJ1, YJ1, XJ2, YJ2, Distance),
				Val == inf.
											
eval(Val) :- 	case(XJ1,YJ1,1),
				turn(Joueur), 
				joueur(Joueur,OrientJ,VieJ,DegatsJ,DefenseJ),
				case(XJ2,YJ2,2),
				joueur(AutreJoueur,OrientAJ,VieAJ,DegatsAJ,DefenseAJ),
				not(AutreJoueur == Joueur), calculDistance(XJ1, YJ1, XJ2, YJ2, Distance),
				Val is (VieJ-VieAJ + DefenseJ - DefenseAJ)*5 - Distance.