package merkle;


public class Node {

	private byte[] hash;

	private Node leftChild;
	private Node rightChild;
	
	/**
	 * Constructeur pour créer un noeud Feuille.
	 * @param data Hash des données que la feuille représente.
	 */
	public Node(byte[] data) {
		leftChild = null;
		rightChild = null;
		byte[] zero = {0};
		this.hash = Hash.digestHMAC(data, zero);
	}
	
	/**
	 * Constructeur pour créer un noeud à partir de deux noeuds fils.
	 * @param leftChild fils gauche.
	 * @param rightChild fils droit.
	 */
	public Node(Node leftChild, Node rightChild) {
		this.leftChild = leftChild;
		this.rightChild = rightChild;
		byte[] one = {1};
		this.hash = Hash.digestHMAC(
				Bytes.concat(
						leftChild.getHash(), 
						rightChild.getHash()),
				one);
	}

	public byte[] getHash() {
		return hash;
	}

	public void setHash(byte[] hash) {
		this.hash = hash;
	}

}
