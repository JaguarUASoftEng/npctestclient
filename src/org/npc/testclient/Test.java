package org.npc.testclient;

import org.npc.testclient.tests.ProductTest;

public class Test {
	public static void main(String[] args) {
		(new ProductTest()).runTest();
		
		System.out.println("Completed test.");
	}
	
}
