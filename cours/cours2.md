## PDEV projet :


### listes :
personnes / noeuds  
personnes / parcitipants -> clef publique  
événements  
tempsp constant~


### Evénement :
T début / T fin  
lieu  
période de souscription (inscriptions, seuil)  
?invitations  
titre/type


### Règles métier : 
pas de participation double  
participation ou pas  
?participation par le proposeur  

événement valide si seuil atteint à la fin de la période de souscription  
période de souscription doit être avant la date de début de l'événement  
résistance Sybille sur les identités  


### Types de transactions :
création d'événement  
subscription d'événement  
gestion de clefs (SECP 256 pour la crypto)


### Format des transactions (V1) :

**OP :**

	[taille (T) sur 32 bits Big Endian du message suivant] +
	[ [JSON valide sur (T - taille_signature) octets] + [signature (hash) sur 256 bits] ]


### Structure des JSON :

**partie commune :** 

	{
		"pub_key" : String (hexa),
		"type_transact" : "creation" / "register" (String),
		"payload" : {---}
	 }

**payload :** 

- création :

		{
			"name" : String,
			"description" : String,
			"date" : 
				{
					"begin" : FormatDateStandard,
					"end" : FormatDateStandard
					"end_subscription" : FormatDateStandard
				},
			"location" : String,
			"limits" : 
				{
					"min" : int @Nullable,
					"max" : int @Nullable
				}
		}

- inscription : 

		{
			"event_hash" : String (hexa) 
		}


### Structure d'un bloc :

Rappel:  
**B = taille + JSON(données) + signature (hexa)**

Description du JSON :

	{	
		"pub_key" (créateur du bloc) : String (hexa),
		"hash_prev_bloc" : String (hexa),
		"root_hash" : String (hexa)
	}
			
