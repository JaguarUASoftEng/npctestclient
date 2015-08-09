package org.npc.testclient.tests;

import java.time.LocalDateTime;
import java.util.Random;
import java.util.UUID;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;

import org.apache.commons.lang3.RandomStringUtils;
import org.npc.testclient.tests.interfaces.TestInterface;
import org.npc.testmodel.api.Product;
import org.npc.testmodel.api.ProductListing;

public class ProductTest implements TestInterface {
	@Override
	public boolean runTest() {
		if (!this.testProductPuts()) {
			return false;
		}

		ProductListing apiProductListing = this.testProductListingGet();
		if (apiProductListing.getProducts().size() == PRODUCT_PUT_COUNT) {
			Product apiProduct = this.testProductGet(apiProductListing.getProducts().get(0));
			if (!apiProduct.getId().equals(new UUID(0, 0))) {
				System.out.println("Successful test.");
			} else {
				System.out.println("Product get did not return a valid product.");
			}
		} else {
			System.out.println("Product listing get did not return any products");
		}
		
		return true;
	}
	
	private boolean testProductPuts() {
		boolean successfulPuts = true;
		WebTarget putProductTarget = ClientBuilder.newClient().target(BASE_REQUEST_URI).path("apiv0");

		for (int i = 0; i < PRODUCT_PUT_COUNT; i++) {
			Product apiProduct = this.buildNewProduct();
			
			apiProduct = putProductTarget.request(MediaType.APPLICATION_JSON).put(Entity.json(apiProduct), Product.class);
			if (apiProduct == null) {
				System.out.println("Product put failed, returned an empty object.");
				successfulPuts = false;
				break;
			} else if (apiProduct.getId().equals(new UUID(0, 0))) {
				System.out.println("Product put failed, instance was not assigned an ID.");
				successfulPuts = false;
				break;
			}
		}
		
		return successfulPuts;
	}

	private ProductListing testProductListingGet() {
		return ClientBuilder.newClient().
			target(BASE_REQUEST_URI).
			path("apiv0").path("products").
			request(MediaType.APPLICATION_JSON).get(ProductListing.class);
	}
	
	private Product testProductGet(Product productToTest) {
		return ClientBuilder.newClient().
			target(BASE_REQUEST_URI).
			path("apiv0").path("product").path(productToTest.getId().toString()).
			request(MediaType.APPLICATION_JSON).get(Product.class);
	}
	
	private Product buildNewProduct() {
		return (new Product()).setLookupCode(RandomStringUtils.randomAlphabetic(PRODUCT_LOOKUP_CODE_LENGTH)).
			setCreatedOn(LocalDateTime.now().minusDays(this.random.nextInt(DAYS_IN_FIVE_YEARS))).
			setCount(MIN_PRODUCT_COUNT + this.random.nextInt(MAX_PRODUCT_COUNT - MIN_PRODUCT_COUNT));
	}

	private Random random;
	
	private static final int PRODUCT_PUT_COUNT = 7;
	private static final int MIN_PRODUCT_COUNT = 50;
	private static final int MAX_PRODUCT_COUNT = 300;
	private static final int DAYS_IN_FIVE_YEARS = 1827;
	private static final int PRODUCT_LOOKUP_CODE_LENGTH = 25;
	private static final String BASE_REQUEST_URI = "http://localhost:4567/test/";
	
	public ProductTest() {
		random = new Random();
	}
}
