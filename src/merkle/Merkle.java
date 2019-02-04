package merkle;

public class Merkle {

	/**
	 * Construit un arbre de Merkle à partir d'un tableau de données non hashées.
	 * @param datas Tableau de données non hashées
	 * @return Racine de l'arbre de Merkle représentant le tableau de données passé en paramètres.
	 */
	public static byte[] getRootHash(byte[][] datas) {
		if(datas == null || datas.length == 0) {
			byte[] two = {2};
			return Hash.digestHMAC(null, two);
		}
		
		Node root = merkleTree(datas);
		return root.getHash();
	}
	
	/**
	 * Construit un arbre de Merkle à partir d'un tableau de Strings non hashées.
	 * @param datas Tableau de Strings non hashées
	 * @return Racine de l'arbre de Merkle représentant le tableau de Strings passé en paramètres.
	 */
	public static byte[] getRootHash(String[] datas) {
		if(datas == null || datas.length == 0) {
			byte[] two = {2};
			return Hash.digestHMAC(null, two);
		}
		
		Node root = merkleTree(datas);
		return root.getHash();
	}
	
	
	
	
	
	private static Node merkleTree(byte[][] datas){
		Node[] leaves = new Node[datas.length];
		
		for(int i=0; i<datas.length; i++) {
			leaves[i] = new Node(datas[i]);
		}
		
		return merkleTreeRec(leaves);
	}
	
	private static Node merkleTree(String[] datas){
		byte[][] datasBytesArrays = new byte[datas.length][];
		
		for(int i=0; i<datas.length; i++) {
			datasBytesArrays[i] = Convert.stringToBytes(datas[i]);
		}
		
		return merkleTree(datasBytesArrays);
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
