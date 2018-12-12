package tests;

import merkle.Hash;
import merkle.Merkle;
import merkle.Node;

public class NodeTest {

	public static void main(String[] args) throws Exception {

		//////////// TEST FONCTIONS NODE///////////////
		System.out.println("////////////TEST FONCTIONS NODE///////////////");

		{
			byte[][] hashs = { 
					Hash.digest("test0"), 
					Hash.digest("test1"), 
					Hash.digest("test2"), 
					Hash.digest("test3") 
			};

			// System.out.println(Hash.bytesToHex(Hash.digest(t2)));

			Node n0 = new Node(hashs[0]);
			System.out.println(Hash.bytesToHex(hashs[0]));
			System.out.println(Hash.bytesToHex(n0.getHash()));
			System.out.println(n0.isLeaf());

			Node n1 = new Node(hashs[1]);
			Node n2 = new Node(n0, n1);
			System.out.println(" ");
			System.out.println(Hash.bytesToHex(Hash.digest(hashs[0], hashs[1])));
			System.out.println(Hash.bytesToHex(n2.getHash()));
			System.out.println(n2.isLeaf());

			Node n3 = n2;
			System.out.println(" ");
			System.out.println(Hash.bytesToHex(Hash.digest(hashs[0], hashs[1])));
			System.out.println(Hash.bytesToHex(n3.getHash()));
			System.out.println(n3.isLeaf());
		}

		//////////////////// TEST MERKLE PAIR////////////////////////
		System.out.println("\n/////////////TEST MERKLE PAIR////////////////////////");

		{
			byte[][] hashs = { 
					Hash.digest("test0"), 
					Hash.digest("test1"), 
					Hash.digest("test2"),
					Hash.digest("test3"), 
					Hash.digest("test4"), 
					Hash.digest("test5"), 
					Hash.digest("test6"),
					Hash.digest("test7")
			};

			/* réponse attendue */
			byte[] parent0 = Hash.digest(hashs[0], hashs[1]);
			byte[] parent1 = Hash.digest(hashs[2], hashs[3]);
			byte[] parent2 = Hash.digest(hashs[4], hashs[5]);
			byte[] parent3 = Hash.digest(hashs[6], hashs[7]);

			byte[] grand_parent0 = Hash.digest(parent0, parent1);
			byte[] grand_parent1 = Hash.digest(parent2, parent3);

			byte[] rootHash_expected = Hash.digest(grand_parent0, grand_parent1);

			/* réponse produite */
			byte[] rootHash_producted = Merkle.merkleTree(hashs).getHash();

			/* check */
			System.out.println(Hash.bytesToHex(rootHash_expected));
			System.out.println(Hash.bytesToHex(rootHash_producted));
		}

		//////////////////// TEST MERKLE IMPAIR////////////////////////
		System.out.println("\n/////////////TEST MERKLE IMPAIR////////////////////////");
		
		{
			byte[][] hashs = { 
					Hash.digest("test0"), 
					Hash.digest("test1"), 
					Hash.digest("test2"),
					Hash.digest("test3"), 
					Hash.digest("test4"), 
					Hash.digest("test5"), 
					Hash.digest("test6"),
					Hash.digest("test7"), 
					Hash.digest("test8")
			};
			
			/*réponse attendue*/
			byte[] parent0 = Hash.digest(hashs[0], hashs[1]);
			byte[] parent1 = Hash.digest(hashs[2], hashs[3]);
			byte[] parent2 = Hash.digest(hashs[4], hashs[5]);
			byte[] parent3 = Hash.digest(hashs[6], hashs[7]);
			byte[] parent4 = hashs[8];

			byte[] grand_parent0 = Hash.digest(parent0, parent1);
			byte[] grand_parent1 = Hash.digest(parent2, parent3);
			byte[] grand_parent2 = parent4;
			
			byte[] arr_grd_parent0 = Hash.digest(grand_parent0, grand_parent1);
			byte[] arr_grd_parent1 = grand_parent2;
			
			byte[] rootHash_expected = Hash.digest(arr_grd_parent0, arr_grd_parent1);

			/* réponse produite */
			byte[] rootHash_producted = Merkle.merkleTree(hashs).getHash();

			/* check */
			System.out.println(Hash.bytesToHex(rootHash_expected));
			System.out.println(Hash.bytesToHex(rootHash_producted));
		}
		
		////////////////////TEST MERKLE MIXTE////////////////////////
		System.out.println("\n/////////////TEST MERKLE MIXTE////////////////////////");
		
		{
			byte[][] hashs = { 
					Hash.digest("test0"), 
					Hash.digest("test1"), 
					Hash.digest("test2"),
					Hash.digest("test3"), 
					Hash.digest("test4"), 
					Hash.digest("test5"), 
					Hash.digest("test6"),
					Hash.digest("test7"), 
					Hash.digest("test8"),
					Hash.digest("test9")
			};
			
			/*réponse attendue*/
			byte[] parent0 = Hash.digest(hashs[0], hashs[1]);
			byte[] parent1 = Hash.digest(hashs[2], hashs[3]);
			byte[] parent2 = Hash.digest(hashs[4], hashs[5]);
			byte[] parent3 = Hash.digest(hashs[6], hashs[7]);
			byte[] parent4 = Hash.digest(hashs[8], hashs[9]);
			
			byte[] grand_parent0 = Hash.digest(parent0, parent1);
			byte[] grand_parent1 = Hash.digest(parent2, parent3);
			byte[] grand_parent2 = parent4;
			
			byte[] arr_grd_parent0 = Hash.digest(grand_parent0, grand_parent1);
			byte[] arr_grd_parent1 = grand_parent2;
			
			byte[] rootHash_expected = Hash.digest(arr_grd_parent0, arr_grd_parent1);
			
			/* réponse produite */
			byte[] rootHash_producted = Merkle.merkleTree(hashs).getHash();
			
			/* check */
			System.out.println(Hash.bytesToHex(rootHash_expected));
			System.out.println(Hash.bytesToHex(rootHash_producted));
		}
		
		
	}
}
