package tests;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;

import org.junit.jupiter.api.Test;

import merkle.Bytes;
import merkle.Hash;
import merkle.Merkle;

class MerkleTest {

	String[] datas = {
			"test0",
			"test1",
			"test2",
			"test3",
			"test4",
			"test5",
			"test6",
			"test7",
			"test8",
			"test9",
			"test10"
	};
	
	byte[][] hashs = { 
			Hash.digest(Bytes.concat(Bytes.fromStr("0"), Bytes.fromStr("test0"))), 
			Hash.digest(Bytes.concat(Bytes.fromStr("0"), Bytes.fromStr("test1"))), 
			Hash.digest(Bytes.concat(Bytes.fromStr("0"), Bytes.fromStr("test2"))), 
			Hash.digest(Bytes.concat(Bytes.fromStr("0"), Bytes.fromStr("test3"))), 
			Hash.digest(Bytes.concat(Bytes.fromStr("0"), Bytes.fromStr("test4"))), 
			Hash.digest(Bytes.concat(Bytes.fromStr("0"), Bytes.fromStr("test5"))), 
			Hash.digest(Bytes.concat(Bytes.fromStr("0"), Bytes.fromStr("test6"))), 
			Hash.digest(Bytes.concat(Bytes.fromStr("0"), Bytes.fromStr("test7"))), 
			Hash.digest(Bytes.concat(Bytes.fromStr("0"), Bytes.fromStr("test8"))), 
			Hash.digest(Bytes.concat(Bytes.fromStr("0"), Bytes.fromStr("test9"))),
			Hash.digest(Bytes.concat(Bytes.fromStr("0"), Bytes.fromStr("test10"))) 
	};
	
	@Test
	void testMerkleTreeEvenOnly() {
		/* réponse attendue */
		byte[] parent0 = Hash.digest(Bytes.concat(Bytes.fromStr("1"), hashs[0], hashs[1]));
		byte[] parent1 = Hash.digest(Bytes.concat(Bytes.fromStr("1"), hashs[2], hashs[3]));
		byte[] parent2 = Hash.digest(Bytes.concat(Bytes.fromStr("1"), hashs[4], hashs[5]));
		byte[] parent3 = Hash.digest(Bytes.concat(Bytes.fromStr("1"), hashs[6], hashs[7]));

		byte[] grand_parent0 = Hash.digest(Bytes.concat(Bytes.fromStr("1"), parent0, parent1));
		byte[] grand_parent1 = Hash.digest(Bytes.concat(Bytes.fromStr("1"), parent2, parent3));

		byte[] rootHashExpected = Hash.digest(Bytes.concat(Bytes.fromStr("1"), grand_parent0, grand_parent1));
		byte[] rootHashProducted = Merkle.merkleTree(
				Arrays.copyOfRange(datas, 0, 8)).getHash();
		
		System.out.println("\n/*---------- merkleTree(String[] datas) 2*i NOEUDS A TOUS LES NIVEAUX (PAIR)----------*/");
		System.out.println("rootExpected: "+ Bytes.toHex(rootHashExpected));
		System.out.println("rootProducted:"+ Bytes.toHex(rootHashProducted));
	
		assertTrue(Arrays.equals(rootHashExpected, rootHashProducted));
	}

	@Test
	void testMerkleTreeOddOnly() {

		/*réponse attendue*/
		byte[] parent0 = Hash.digest(Bytes.concat(Bytes.fromStr("1"), hashs[0], hashs[1]));
		byte[] parent1 = Hash.digest(Bytes.concat(Bytes.fromStr("1"), hashs[2], hashs[3]));
		byte[] parent2 = Hash.digest(Bytes.concat(Bytes.fromStr("1"), hashs[4], hashs[5]));
		byte[] parent3 = Hash.digest(Bytes.concat(Bytes.fromStr("1"), hashs[6], hashs[7]));

		byte[] grand_parent0 = Hash.digest(Bytes.concat(Bytes.fromStr("1"), parent0, parent1));
		byte[] grand_parent1 = Hash.digest(Bytes.concat(Bytes.fromStr("1"), parent2, parent3));
		
		byte[] arr_grd_parent0 = Hash.digest(Bytes.concat(Bytes.fromStr("1"), grand_parent0, grand_parent1));
		
		byte[] rootHashExpected = Hash.digest(Bytes.concat(Bytes.fromStr("1"), arr_grd_parent0, hashs[8]));
		byte[] rootHashProducted = Merkle.merkleTree(
				Arrays.copyOfRange(datas, 0, 9)).getHash();

		System.out.println("\n/*---------- merkleTree(String[] datas) 2*i+1 NOEUDS A TOUS LES NIVEAUX (IMPAIR) ----------*/");
		System.out.println("rootExpected: "+ Bytes.toHex(rootHashExpected));
		System.out.println("rootProducted:"+ Bytes.toHex(rootHashProducted));
	
		assertTrue(Arrays.equals(rootHashExpected, rootHashProducted));
	}
	
	@Test
	void testMerkleTreeMixed() {
		/*réponse attendue*/
		byte[] parent0 = Hash.digest(Bytes.concat(Bytes.fromStr("1"), hashs[0], hashs[1]));
		byte[] parent1 = Hash.digest(Bytes.concat(Bytes.fromStr("1"), hashs[2], hashs[3]));
		byte[] parent2 = Hash.digest(Bytes.concat(Bytes.fromStr("1"), hashs[4], hashs[5]));
		byte[] parent3 = Hash.digest(Bytes.concat(Bytes.fromStr("1"), hashs[6], hashs[7]));
		byte[] parent4 = Hash.digest(Bytes.concat(Bytes.fromStr("1"), hashs[8], hashs[9]));
		
		byte[] grand_parent0 = Hash.digest(Bytes.concat(Bytes.fromStr("1"), parent0, parent1));
		byte[] grand_parent1 = Hash.digest(Bytes.concat(Bytes.fromStr("1"), parent2, parent3));
		byte[] grand_parent2 = Hash.digest(Bytes.concat(Bytes.fromStr("1"), parent4, hashs[10]));
		
		byte[] arr_grd_parent0 = Hash.digest(Bytes.concat(Bytes.fromStr("1"), grand_parent0, grand_parent1));
		
		byte[] rootHashExpected = Hash.digest(Bytes.concat(Bytes.fromStr("1"), arr_grd_parent0, grand_parent2));
		byte[] rootHashProducted = Merkle.merkleTree(datas).getHash();

		
		System.out.println("\n/*---------- merkleTree(String[] datas) NBS NOEUDS PAIRS ET IMPAIRS (MIXTE) ----------*/");
		System.out.println("rootExpected: "+ Bytes.toHex(rootHashExpected));
		System.out.println("rootProducted:"+ Bytes.toHex(rootHashProducted));
		
		assertTrue(Arrays.equals(rootHashExpected, rootHashProducted));
	}
}
