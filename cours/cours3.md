## Cours3

Hors cryptomonnaie, l'algorithme de consensus ne peut pas être un proof-of-work ou proof-of-stake (car on n'a pas de récompense à donner à un mineur faisant le calcul).

### algorithmes de consensus possibles :

- Metastable : 50%   
Plus  lent ~ minutes  
En cas d'absence (malhonnêtes + absents > 50%) => réduction de confiance (ralentissement)

- Stable : 66%  
Instantané ~ secondes  
En cas d'absence (malhonnêtes + absents > 33%) => bloqué (mais pas de réduction de confiance)


**déroulement de l'algorithme MetaStable:**  
30 utilisateurs [0,...,29]  
Toutes les x minutes, on prend un utilisateur au hasard, qui aura le droit d'écrire le dernier bloc dans la blockchain.

*hasard distribué:*  
on hashe 1 sur 256 bits.  
Puis on calcule H(1)%30 et on récupère le n° du 1er utilisateur.  
De même pour les 29 autres utilisateurs.


## TP3

### questions 

*Quand nombre impair de noeuds, doit-on créer des noeuds qui ont un seul fils ?*

non. A la place, on fait remonter le noeud impair dans le tableau de parents dans la fonction récursive.

### Règles métiers

voir photo  
Errata : limits.min <= limits.max

**Remarques**

Si quelqu'un envoie un bloc avec au-moins une opération invalide, on refuse le bloc.

Ne pas envoyer des objets Java par réseau en les sérialisant avec les outils de sérialisation Java.
Car dans ce cas le protocole est dépendant du code Java, de la classe objet Java à sérialiser, de la version de l'outil de sérialization Java.

### A faire d'ici la semaine prochaine

Merkle, vérification Merkle, un peu de sérialisation.  
Prototypes de logique métier, BDD qui peut retourner en arrière. (optionnel pour cette semaine)

### Algorithme de consensus choisis

Données à rajouter dans la sérialisation des blocs de la blockchain pour l'algo de consensus:
- le temps de synchronisation (15s entre chaque bloc)
- le temps courant
- le niveau (?)

bloc :

	{
		"pubkey",
		"pred",
		"root",
		"level" : uint (= niv du préd + 1),
		"temps" : uint
	}

**remarque:**  
pour chaque bloc,  
temps (logique) = entier naturel (1, 2, 3 ...)  
temps réel = Temps_génèse + temps X temps_synchronisation = T+t * 15s

**règle de consensus:**  
de tous les blocs valides qu'on connait, on prend celui de niveau le plus élevé

**règles de validité sur les niveaux :**
- a) signature valide,
- b) niveau de bloc = 1 + niveau du bloc prédécesseur
- c) niveau du prédécesseur le plus élevé ou demandeur du bloc le plus élevé
- d) temps du bloc < temps courant + 2s
- e) règle du consensus (qui a le droit d'écrire le nouveau bloc):  
soit l = liste des participants.  
soit une fonction select(int temps, list participants) -> participant  
**select(temps du bloc, l) = signeur du bloc**
- f) bloc prédécesseur valide

*a b c e f règles de réjection, d règle de temporisation.*

**automate:**

		         a && b && c && e && f             d
	----O____________________________________O____________<.> Accept
		| \________________O________________/
		|                         !d
	   [.] Reject


**Accept:** put in Hash(block)  
niveau plus élevé = max_niveau(blocks)



