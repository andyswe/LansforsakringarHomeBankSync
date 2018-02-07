package lansforsakringar.api.services.internal;

import org.junit.Assert;
import org.junit.Assume;
import org.junit.Test;

import java.util.Random;

public class ChallengeConverterTest {

	@Test
	public void test() throws Exception {
		Assert.assertEquals(ChallengeConverter.calculateLfHash(new Integer(1458375292)),
				"9ffab7ad2d01a0a550c7df295d96307cfddf9a34");
	}
	@Test
	public void test2() throws Exception {
		System.out.println("Tests start for " + findProperty("CUSTOMERID"));
		Assert.assertEquals(123,new Random().nextInt());

	}

	private String findProperty(String id) {
		String property = System.getProperty(id);
		Assume.assumeNotNull(property);
		return property;
	}
}
