package merkle;

public class Merkle {

	/**
	 * Construit un arbre de Merkle à partir d'un tableau de hashs.
	 * @param hashs Tableau de hashs
	 * @return Racine de l'arbre de Merkle représentant le tableau de hashs passé en paramètres.
	 */
	public static Node merkleTree(byte[][] hashs){
		Node[] leaves = new Node[hashs.length];
		
		for(int i=0; i<hashs.length; i++) {
			leaves[i] = new Node(hashs[i]);
		}
		
		return merkleTreeRec(leaves);
	}
	
	/**
	 * Fonction récursive qui produit un arbre de Merkle à partir d'un tableau de noeuds feuilles.
	 * @param nodes Les noeuds feuilles de l'arbre.
	 * @return le noeud racine de l'arbre.
	 */
	private static Node merkleTreeRec(Node[] nodes)  {
		/*cas terminal*/
		if(nodes.length == 1) {
			return nodes[0];
		}
		
		/*cas récursif*/
		else {
			Node[] parents;
			/*nb fils pair*/
			if(nodes.length %2 == 0) {
				parents = new Node[nodes.length/2];
				
				for(int i=0; i<parents.length; i++) {
					parents[i] = new Node(nodes[2*i], nodes[2*i+1]);
				}
			}
			
			/*nb fils impair*/
			else {
				parents = new Node[nodes.length/2+1];
				
				int i;
				for(i=0; i<parents.length-1; i++) {
					parents[i] = new Node(nodes[2*i], nodes[2*i+1]);
				}
				parents[i] = nodes[2*i];
			}
			
			/*appel recursif*/
			return merkleTreeRec(parents);
		}
	}
}
