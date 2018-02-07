package lansforsakringar.api.services.internal;

import lansforsakringar.api.beans.AccountType;
import lansforsakringar.api.beans.AccountList;
import lansforsakringar.api.beans.CardList;
import lansforsakringar.api.beans.TransactionList;

import lansforsakringar.api.services.RestClient;
import org.junit.Assert;
import org.junit.Assume;
import org.junit.Test;

/**
 * Example usage of the RestClient.
 * 
 * To use this test, add the following VM arguments:
 * 
 * <pre>
 * -DCUSTOMERID=SSID -DPINCODE=PIN
 * </pre>
 * 
 * SSID is your Swedish social security number
 * 
 * PINCODE is a four digit pin code for your Lansforsakringar Internet bank account.
 */
public class RedstClientIntegrationTest {

    @Test
    public void test() {
        System.out.println("Tests start for " + findProperty("CUSTOMERID"));
        Assert.assertTrue(false);

    }

	@Test
	public void shouldLoginAndFetchTransaction() throws Exception {
		System.out.println("Tests start for " + findProperty("CUSTOMERID"));
		// Shake hands and login
		RestClient client = RestClient.createAuthenticatedClient(findProperty("CUSTOMERID"), findProperty("PINCODE"));

		// List my SAVINGS accounts
		AccountList accounts = client.getAccounts(AccountType.SAVING);
		System.out.println("My accounts " + accounts);
        System.out.println("My accounts " + accounts);
		Assert.assertFalse(accounts.getAccounts().isEmpty());

		// Fetch first page of transactions (20 of 'em) from the first of my accounts
		TransactionList transactions = client.getTransactions(accounts.getAccounts().get(0), 0);
		//Assertions.assertThat(transactions.getTransactions()).hasSize(20);
		
		// List my DEBIT and CREDIT cards
		CardList cards = client.getCards();
		//Assertions.assertThat(cards.getCards()).isNotEmpty();
	}

	private String findProperty(String id) {
		String property = System.getProperty(id);
		Assume.assumeNotNull(property);
		return property;
	}

}
