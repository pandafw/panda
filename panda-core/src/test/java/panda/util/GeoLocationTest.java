package panda.util;

import org.junit.Assert;
import org.junit.Test;

public class GeoLocationTest {
	@Test
	public void testDistance() {
		GeoLocation loc1 = new GeoLocation("PRINCETON_NJ", 40.366633, 74.640832);
		GeoLocation loc2 = new GeoLocation("ITHACA_NY", 42.443087, 76.488707);
		double distance = loc1.distanceTo(loc2);
		Assert.assertEquals(277397, (int)distance);
	}

	@Test
	public void testSquarePoints() {
		GeoLocation loc1 = new GeoLocation("PRINCETON_NJ", 40.366633, 74.640832);
		GeoLocation[] sps = loc1.squarePoints(100);
		Assert.assertEquals(4, sps.length);
		
		int dis = (int)Math.sqrt(100*100 + 100*100);
		Assert.assertEquals(dis, (int)loc1.distanceTo(sps[0]));
		Assert.assertEquals(dis, (int)loc1.distanceTo(sps[1]));
		Assert.assertEquals(dis, (int)loc1.distanceTo(sps[2]));
		Assert.assertEquals(dis, (int)loc1.distanceTo(sps[3]));
	}
}
