### Misc

pierre.chambart@ocaml.pro

keywords :  
*CRYPTOmonnaies, listes chaînées, transactions, vérification sécurité, registre distribué, confiance, réseau P2P, consensus, règles économiques, double spend, a/synchrone, fork, estimation quorum, Débit, confiance distribuée, SYBIL, POW, consensus social, NAT/HOLE punching*

## TP
Arbre de Merkle  
Chat P2P


## Blockchain

chaine de blocks :

    [ID S->D N Ss(T)]

    0____     [xxxxxxxx]
    [A:10
    |
    |
    |
    |
    [____

    1____     [P:0     ]
    [A:5      [A->B : 5]
    |B:5
    |
    |
    |
    [____


    2____     [P:1     ]
    [A:5      [B->C : 3]
    |B:2
    |C:3
    |
    |
    [____


    2'___     [P:1     ]
    [A:8      [B->A : 3]
    |B:-1
    |C:3
    |
    |
    [____

B est à -1 : **impossible**

Si 2 et 2' existent au même moment : besoin de faire un concensus (unification des blocks acceptés (vue permanente du registre mono-état))

-> "Qui choisit la tête?"
- BitCoin = aléatoirement si on prend 1, 2 ou 2'
- Calcul de score
