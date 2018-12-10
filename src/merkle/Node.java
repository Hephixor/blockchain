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
	
	public Node(Node leftChild, Node rightChild) {
		this.leftChild = leftChild;
		this.rightChild = rightChild;
	}

	public byte[] getHash() {
		return hash;
	}
}
