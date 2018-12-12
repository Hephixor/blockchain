package merkle;


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
	 */
	public Node(Node leftChild, Node rightChild) {
		this.leftChild = leftChild;
		this.rightChild = rightChild;
		this.hash = Hash.digest(leftChild.getHash(), rightChild.getHash());
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
}
