package merkle;

import java.security.NoSuchAlgorithmException;

public class Node {

	private byte[] hash;

	private Node leftChild;
	private Node rightChild;
	
	/**
	 * Constructeur pour créer un noeud Feuille.
	 * @param hash Hash des données que la feuille représente.
	 */
	public Node(byte[] hash) {
		leftChild = null;
		rightChild = null;
		this.hash = hash;
	}
	
	/**
	 * Constructeur pour créer un noeud à partir de deux noeuds fils.
	 * @param leftChild fils gauche.
	 * @param rightChild fils droit.
	 * @throws NoSuchAlgorithmException 
	 */
	public Node(Node leftChild, Node rightChild) throws NoSuchAlgorithmException {
		this.leftChild = leftChild;
		this.rightChild = rightChild;
		this.hash = Hash.digest(leftChild.getHash(), rightChild.getHash());
	}
	
	/**
	 * Constructeur pour créer un noeud à partir d'un seul noeud fils.
	 * (cas où le nombre de noeuds fils est impair à un niveau de l'arbre de Merkle.)
	 * @param leftChild
	 */
	public Node(Node leftChild) {
		this.leftChild = leftChild;
		this.rightChild = null;
		this.hash = leftChild.getHash();
	}

	public byte[] getHash() {
		return hash;
	}

	public void setHash(byte[] hash) {
		this.hash = hash;
	}
	
	//TODO inutile ?
	public boolean isLeaf() {
		return leftChild==null && rightChild==null;
	}
	
	////////////MAKE A MERKLE TREE///////////////:
	
	/**
	 * Construit un arbre de Merkle à partir d'un tableau de hashs.
	 * @param hashs Tableau de hashs
	 * @return Racine de l'arbre de Merkle représentant le tableau de hashs passé en paramètres.
	 * @throws NoSuchAlgorithmException
	 */
	public static Node merkleTree(byte[][] hashs) throws NoSuchAlgorithmException {
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
	 * @throws NoSuchAlgorithmException
	 */
	private static Node merkleTreeRec(Node[] nodes) throws NoSuchAlgorithmException {
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
				parents[i] = new Node(nodes[2*i]);
			}
			
			/*appel recursif*/
			return merkleTreeRec(parents);
		}
	}
}
