package tests;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;

import org.junit.jupiter.api.Test;

import merkle.Hash;
import merkle.Merkle;

class MerkleTest {

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
			Hash.digest("test9"),
			Hash.digest("teest10"),
	};
	
	@Test
	void testMerkleTreeEvenOnly() {
		/* réponse attendue */
		byte[] parent0 = Hash.digest(hashs[0], hashs[1]);
		byte[] parent1 = Hash.digest(hashs[2], hashs[3]);
		byte[] parent2 = Hash.digest(hashs[4], hashs[5]);
		byte[] parent3 = Hash.digest(hashs[6], hashs[7]);

		byte[] grand_parent0 = Hash.digest(parent0, parent1);
		byte[] grand_parent1 = Hash.digest(parent2, parent3);

		byte[] rootHashExpected = Hash.digest(grand_parent0, grand_parent1);
		byte[] rootHashProducted = Merkle.merkleTree(
				Arrays.copyOfRange(hashs, 0, 8)).getHash();
		
		System.out.println("\n/*---------- merkleTree(byte[] hashs) 2*i NOEUDS A TOUS LES NIVEAUX (PAIR)----------*/");
		System.out.println("rootExpected: "+ Hash.bytesToHex(rootHashExpected));
		System.out.println("rootProducted:"+ Hash.bytesToHex(rootHashProducted));
	
		assertTrue(Arrays.equals(rootHashExpected, rootHashProducted));
	}

	@Test
	void testMerkleTreeOddOnly() {

		/*réponse attendue*/
		byte[] parent0 = Hash.digest(hashs[0], hashs[1]);
		byte[] parent1 = Hash.digest(hashs[2], hashs[3]);
		byte[] parent2 = Hash.digest(hashs[4], hashs[5]);
		byte[] parent3 = Hash.digest(hashs[6], hashs[7]);

		byte[] grand_parent0 = Hash.digest(parent0, parent1);
		byte[] grand_parent1 = Hash.digest(parent2, parent3);
		
		byte[] arr_grd_parent0 = Hash.digest(grand_parent0, grand_parent1);
		
		byte[] rootHashExpected = Hash.digest(arr_grd_parent0, hashs[8]);
		byte[] rootHashProducted = Merkle.merkleTree(
				Arrays.copyOfRange(hashs, 0, 9)).getHash();

		System.out.println("\n/*---------- merkleTree(byte[] hashs) 2*i+1 NOEUDS A TOUS LES NIVEAUX (IMPAIR) ----------*/");
		System.out.println("rootExpected: "+ Hash.bytesToHex(rootHashExpected));
		System.out.println("rootProducted:"+ Hash.bytesToHex(rootHashProducted));
	
		assertTrue(Arrays.equals(rootHashExpected, rootHashProducted));
	}
	
	@Test
	void testMerkleTreeMixed() {
		/*réponse attendue*/
		byte[] parent0 = Hash.digest(hashs[0], hashs[1]);
		byte[] parent1 = Hash.digest(hashs[2], hashs[3]);
		byte[] parent2 = Hash.digest(hashs[4], hashs[5]);
		byte[] parent3 = Hash.digest(hashs[6], hashs[7]);
		byte[] parent4 = Hash.digest(hashs[8], hashs[9]);
		
		byte[] grand_parent0 = Hash.digest(parent0, parent1);
		byte[] grand_parent1 = Hash.digest(parent2, parent3);
		byte[] grand_parent2 = Hash.digest(parent4, hashs[10]);
		
		byte[] arr_grd_parent0 = Hash.digest(grand_parent0, grand_parent1);
		
		byte[] rootHashExpected = Hash.digest(arr_grd_parent0, grand_parent2);
		byte[] rootHashProducted = Merkle.merkleTree(hashs).getHash();

		
		System.out.println("\n/*---------- merkleTree(byte[] hashs) NBS NOEUDS PAIRS ET IMPAIRS (MIXTE) ----------*/");
		System.out.println("rootExpected: "+ Hash.bytesToHex(rootHashExpected));
		System.out.println("rootProducted:"+ Hash.bytesToHex(rootHashProducted));
		
		assertTrue(Arrays.equals(rootHashExpected, rootHashProducted));
	}
}
