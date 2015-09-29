package org.npc.testclient;

import org.npc.testclient.tests.*;

public class Test {
	public static void main(String[] args) {
		(new ProductTest()).runTest();
		
		System.out.println("Completed product test.");
		
		(new TransactionEntryTest()).runTest();
		
		System.out.println("Completed transaction entry test.");
	}
}
