package tests.chains;

import java.io.IOException;

import org.junit.jupiter.api.Test;

import tools.Convert;

public class SimpleNoOperationsTest {

	String dir = "./test_files/test_chains/simple_no_operations/";
	
	@Test
	void simplestTest() throws IOException {
		byte[] head = Convert.fileToBytes(dir+"simplest/announce_0/head/block");
	
		System.out.println("head: "+ Convert.bytesToString(head));
	}
}
