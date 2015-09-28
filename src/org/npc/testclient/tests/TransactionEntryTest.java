package org.npc.testclient.tests;

import java.util.Random;
import java.util.UUID;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;

import org.apache.commons.lang3.RandomStringUtils;
import org.npc.testclient.tests.interfaces.TestInterface;
import org.npc.testmodel.api.TransactionEntry;
import org.npc.testmodel.api.TransactionEntryListing;

public class TransactionEntryTest implements TestInterface {
	@Override
	public boolean runTest() {
		if (!this.testTransactionEntryPuts()) {
			return false;
		}

		TransactionEntryListing apiTransactionEntryListing = this.testTransactionEntryListingGet();
		if (apiTransactionEntryListing.getTransactionEntries().size() == PRODUCT_PUT_COUNT) {
			TransactionEntry apiTransactionEntry = this.testTransactionEntryGet(apiTransactionEntryListing.getTransactionEntrys().get(0));
			if (!apiTransactionEntry.getId().equals(new UUID(0, 0))) {
				System.out.println("Successful test.");
			} else {
				System.out.println("TransactionEntry get did not return a valid transaction entry.");
			}
		} else {
			System.out.println("TransactionEntry listing get did not return any transaction entries");
		}
		
		return true;
	}
	
	private boolean testTransactionEntryPuts() {
		boolean successfulPuts = true;
		WebTarget putTransactionEntryTarget = ClientBuilder.newClient().target(BASE_REQUEST_URI).path("apiv0");

		for (int i = 0; i < TRANSACTION_ENTRY_PUT_COUNT; i++) {
			TransactionEntry apiTransactionEntry = this.buildNewTransactionEntry();
			
			apiTransactionEntry = putTransactionEntryTarget.request(MediaType.APPLICATION_JSON).put(Entity.json(apiTransactionEntry), TransactionEntry.class);
			if (apiTransactionEntry == null) {
				System.out.println("TransactionEntry put failed, returned an empty object.");
				successfulPuts = false;
				break;
			} else if (apiTransactionEntry.getId().equals(new UUID(0, 0))) {
				System.out.println("TransactionEntry put failed, instance was not assigned an ID.");
				successfulPuts = false;
				break;
			}
		}
		
		return successfulPuts;
	}

	private TransactionEntryListing testTransactionEntryListingGet() {
		return ClientBuilder.newClient().
			target(BASE_REQUEST_URI).
			path("apiv0").path("transactionEntries").
			request(MediaType.APPLICATION_JSON).get(TransactionEntryListing.class);
	}
	
	private TransactionEntry testTransactionEntryGet(TransactionEntry transactionEntryToTest) {
		return ClientBuilder.newClient().
			target(BASE_REQUEST_URI).
			path("apiv0").path("transactionEntries").path(transactionEntryToTest.getId().toString()).
			request(MediaType.APPLICATION_JSON).get(TransactionEntry.class);
	}
	
	private TransactionEntry buildNewTransactionEntry() {
		return (new TransactionEntry()).setRecordID(-1.0).setTransactionID(-1.0).
			setProductID(-1.0).setPrice(-1.00).setQuantity(-1);
	}

	private Random random;
	
	private static final int TRANSACTION_ENTRY_PUT_COUNT = 7;
	private static final int MIN_TRANSACTION_ENTRY_COUNT = 50;
	private static final int MAX_TRANSACTION_ENTRY_COUNT = 300;
	private static final int DAYS_IN_FIVE_YEARS = 1827;
	private static final int PRODUCT_LOOKUP_CODE_LENGTH = 25;
	private static final String BASE_REQUEST_URI = "http://localhost:8080/test/";
	
	public TransactionEntryTest() {
		random = new Random();
	}
}