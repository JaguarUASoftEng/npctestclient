package org.npc.testclient;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.MediaType;

import org.npc.testmodel.api.Product;
import org.npc.testmodel.api.ProductListing;

public class Test {
	public static void main(String[] args) {
		testTestService();
		
		System.out.println("Completed test.");
	}
	
	private static void testTestService() {
		ProductListing productListing = testProductListingGet();
		if (productListing.getProducts().size() > 0) {
			Product product = testProductGet(productListing.getProducts().get(0));
			if (product.getCount() > 0) {
				System.out.println("Successful test.");
			} else {
				System.out.println("Product get did not return a valid product.");
			}
		} else {
			System.out.println("Product listing get did not return any products");
		}
	}

	private static ProductListing testProductListingGet() {
		return ClientBuilder.newClient().target(baseRequestUri).path("apiv0").path("products").request(MediaType.APPLICATION_JSON).get(ProductListing.class);
	}
	
	private static Product testProductGet(Product productToTest) {
		return ClientBuilder.newClient().target(baseRequestUri).
			path("apiv0").path("product").path(productToTest.getId().toString()).
			request(MediaType.APPLICATION_JSON).get(Product.class);
	}
	
	private static final String baseRequestUri = "http://localhost:8080/test/";
}
