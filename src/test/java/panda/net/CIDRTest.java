package panda.net;

import org.junit.Assert;
import org.junit.Test;

import panda.lang.Strings;

public class CIDRTest {

	protected void matchAddress(String aAddress, String aCIDR, String aSubnet) {
		matchAddress(aAddress, aCIDR, aSubnet, true);
	}

	protected void matchAddress(String aAddress, String aCIDR, String aSubnet, boolean a_bSuccess) {
		CIDR addr = new CIDR();
		CIDR cidr = new CIDR();
		
		addr.setCIDR(aAddress);
		
		// CIDR form of mask
		cidr.setCIDR(aCIDR);

		Assert.assertEquals(aSubnet, cidr.toString());
		if (Strings.contains(aSubnet, '/')) {
			Assert.assertEquals(Strings.substringBefore(aSubnet, '/'), cidr.toAddress());
		}
		
		// the address should be part of both the supplied subnets if expected to be so
		Assert.assertEquals(a_bSuccess, cidr.include(addr));
		
		// match all
		cidr.setMatchAll();
		Assert.assertTrue(cidr.include(addr));
		
		// match none
		cidr.setMatchNone();
		Assert.assertFalse(cidr.include(addr));
	}

	@Test
	public void testCIDRv4()
	{
		matchAddress("127.0.0.1",       "127.0.0.1",        "127.0.0.1/32");
		matchAddress("127.0.0.1",       "127.1.1.1/0",      "0.0.0.0/0");
		matchAddress("127.0.0.1",       "127.1.1.1/8",      "127.0.0.0/8");
		matchAddress("127.0.0.1",       "127.0.0.0/16",     "127.0.0.0/16");
		matchAddress("127.0.0.1",       "127.0.0.0/24",     "127.0.0.0/24");
		matchAddress("127.0.0.1",       "127.0.0.0/31",     "127.0.0.0/31");
		matchAddress("127.0.0.1",       "127.0.0.1/32",     "127.0.0.1/32");
		
		matchAddress("127.0.0.1",       "127.0.0.0",        "127.0.0.0/32", false);
		matchAddress("127.0.0.1",       "127.0.0.0/32",     "127.0.0.0/32", false);
		
		matchAddress("168.196.12.155",  "168.196.12.155/0", "0.0.0.0/0");
		matchAddress("168.196.12.155",  "1.0.0.0/0",        "0.0.0.0/0");
		matchAddress("168.196.12.155",  "0.0.0.0/0",        "0.0.0.0/0");
		matchAddress("0.0.0.0",         "0.0.0.0/0",        "0.0.0.0/0");
		matchAddress("255.255.255.255", "0.0.0.0/0",        "0.0.0.0/0");
		
		matchAddress("168.196.12.155",  "168.196.12.155/1", "128.0.0.0/1");
		matchAddress("168.196.12.155",  "168.0.0.1/1",      "128.0.0.0/1");
		matchAddress("168.196.12.155",  "169.0.0.0/1",      "128.0.0.0/1");
		matchAddress("168.196.12.155",  "128.0.0.0/1",      "128.0.0.0/1");
		
		matchAddress("193.196.12.155",  "193.1.1.1/2",      "192.0.0.0/2");
		matchAddress("225.196.12.155",  "225.1.1.1/3",      "224.0.0.0/3");
		matchAddress("241.196.12.155",  "241.1.1.1/4",      "240.0.0.0/4");
		matchAddress("249.196.12.155",  "249.1.1.1/5",      "248.0.0.0/5");
		matchAddress("253.196.12.155",  "253.1.1.1/6",      "252.0.0.0/6");
		matchAddress("255.196.12.155",  "255.1.1.1/7",      "254.0.0.0/7");
		
		matchAddress("168.196.12.155",  "168.1.1.1/8",      "168.0.0.0/8");
		matchAddress("168.196.12.155",  "168.196.12.155/8", "168.0.0.0/8");
		matchAddress("168.123.123.123", "168.1.1.1/8",      "168.0.0.0/8");
		
		matchAddress("169.196.12.155",  "168.196.12.155/8", "168.0.0.0/8", false);
		matchAddress("169.123.123.123", "168.1.1.1/8",      "168.0.0.0/8",      false);
		
		matchAddress("168.196.12.155",  "168.128.0.0/9",    "168.128.0.0/9");
		matchAddress("168.196.12.155",  "168.196.0.0/10",   "168.192.0.0/10");
		matchAddress("168.196.12.155",  "168.196.0.0/11",   "168.192.0.0/11");
		matchAddress("168.196.12.155",  "168.196.0.0/12",   "168.192.0.0/12");
		matchAddress("168.196.12.155",  "168.196.0.0/13",   "168.192.0.0/13");
		matchAddress("168.196.12.155",  "168.196.0.0/14",   "168.196.0.0/14");
		matchAddress("168.196.12.155",  "168.196.0.0/15",   "168.196.0.0/15");
		
		matchAddress("168.196.12.155",  "168.196.0.0/16",   "168.196.0.0/16");
		matchAddress("168.196.12.155",  "168.196.1.1/16",   "168.196.0.0/16");
		matchAddress("168.196.12.155",  "168.197.0.0/16",   "168.197.0.0/16", false);
		
		matchAddress("168.196.12.155", "168.196.12.0/17",   "168.196.0.0/17");
		matchAddress("168.196.12.155", "168.196.12.0/18",   "168.196.0.0/18");
		matchAddress("168.196.12.155", "168.196.12.0/19",   "168.196.0.0/19");
		matchAddress("168.196.12.155", "168.196.12.0/20",   "168.196.0.0/20");
		matchAddress("168.196.12.155", "168.196.12.0/21",   "168.196.8.0/21");
		matchAddress("168.196.12.155", "168.196.12.0/22",   "168.196.12.0/22");
		matchAddress("168.196.12.155", "168.196.12.0/23",   "168.196.12.0/23");
		
		matchAddress("168.196.12.155", "168.196.12.0/24",   "168.196.12.0/24");
		matchAddress("168.196.12.155", "168.196.13.0/24",   "168.196.13.0/24", false);
		
		matchAddress("168.196.12.155", "168.196.12.155/25", "168.196.12.128/25");
		matchAddress("168.196.12.155", "168.196.12.155/26", "168.196.12.128/26");
		matchAddress("168.196.12.155", "168.196.12.155/27", "168.196.12.128/27");
		matchAddress("168.196.12.155", "168.196.12.155/28", "168.196.12.144/28");
		matchAddress("168.196.12.155", "168.196.12.155/29", "168.196.12.152/29");
		matchAddress("168.196.12.155", "168.196.12.155/30", "168.196.12.152/30");

		matchAddress("168.196.12.155", "168.196.12.154/31", "168.196.12.154/31");
		matchAddress("168.196.12.155", "168.196.12.155/31", "168.196.12.154/31");
		matchAddress("168.196.12.155", "168.196.12.156/31", "168.196.12.156/31", false);
		    
		matchAddress("168.196.12.155", "168.196.12.155/32", "168.196.12.155/32");
		matchAddress("168.196.12.155", "168.196.12.154/32", "168.196.12.154/32", false);
		
		matchAddress("202.13.7.10",     "202.13.7.1/24",    "202.13.7.0/24");
		matchAddress("202.13.7.99",     "202.13.7.32/24",   "202.13.7.0/24");
		matchAddress("192.168.4.80",    "192.168.4.0/24",   "192.168.4.0/24");
		matchAddress("192.168.4.88",    "192.168.4.0/24",   "192.168.4.0/24");
		matchAddress("192.168.4.80",    "127.0.0.1/32",     "127.0.0.1/32", false);
		
		// ip allv4
		matchAddress("127.168.2.1", "allv4", "0.0.0.0/0", true);
		
		// ip all
		matchAddress("127.168.2.1", "all", "all", true);
		
		CIDR cidr1 = new CIDR();
		CIDR cidr2 = new CIDR();
		
		cidr1.setCIDR("all");
		cidr2.setMatchAll();
		
		Assert.assertEquals(cidr1, cidr2);
		
		// ip none
		matchAddress("127.168.2.1", "none", "none", false);
		
		cidr1.setCIDR("none");
		cidr2.setMatchNone();
		
		Assert.assertEquals(cidr1, cidr2);
	}

	@Test
	public void testCIDRv6() {
		matchAddress("[ffff:ffff:ffff:ffff:ffff:ffff:ffff:ffff]", "ffff:ffff:ffff:ffff:ffff:ffff:ffff:ffff%0", "[ffff:ffff:ffff:ffff:ffff:ffff:ffff:ffff]");
		
		matchAddress("[ffff:ffff:ffff:ffff:ffff:ffff:ffff:ffff]", "f23f::ffff%0/0", "[fe3f:f43f:f64f::f75f:f22f]/0");
		matchAddress("[ffff:ffff:ffff:ffff:ffff:ffff:ffff:ffff]", "8111::ffff%1/1", "[fe3f:f43f:f64f::f75f:f22f]/1");
		matchAddress("[ffff:ffff:ffff:ffff:ffff:ffff:ffff:ffff]", "c111::ffff%2/2", "[fe3f:f43f:f64f::f75f:f22f]/2");
		matchAddress("[ffff:ffff:ffff:ffff:ffff:ffff:ffff:ffff]", "e111::ffff%3/3", "[fe3f:f43f:f64f::f75f:f22f]/3");
		matchAddress("[ffff:ffff:ffff:ffff:ffff:ffff:ffff:ffff]", "f111::ffff%4/4", "[fe3f:f43f:f64f::f75f:f22f]/4");
		matchAddress("[ffff:ffff:ffff:ffff:ffff:ffff:ffff:ffff]", "f811::ffff%5/5", "[f83f:f43f:f64f::f75f:f22f]/5");
		matchAddress("[ffff:ffff:ffff:ffff:ffff:ffff:ffff:ffff]", "fc11::ffff%6/6", "[fc3f:f43f:f64f::f75f:f22f]/6");
		matchAddress("[ffff:ffff:ffff:ffff:ffff:ffff:ffff:ffff]", "fe11::ffff%7/7", "[fe3f:f43f:f64f::f75f:f22f]/7");
		matchAddress("[ffff:ffff:ffff:ffff:ffff:ffff:ffff:ffff]", "ff11::ffff%8/8", "[ff3f:f43f:f64f::f75f:f22f]/8");
		matchAddress("[ffff:ffff:ffff:ffff:ffff:ffff:ffff:ffff]", "ff81::ffff%9/9", "[ff8e:f43f:f64f::f75f:f22f]/9");
		matchAddress("[ffff:ffff:ffff:ffff:ffff:ffff:ffff:ffff]", "ffc1::ffff%10/10", "[ffce:f43f:f64f::f75f:f22f]/10");
		matchAddress("[ffff:ffff:ffff:ffff:ffff:ffff:ffff:ffff]", "ffe1::ffff%11/11", "[ffee:f43f:f64f::f75f:f22f]/11");
		matchAddress("[ffff:ffff:ffff:ffff:ffff:ffff:ffff:ffff]", "fff1::ffff%12/12", "[fffe:f43f:f64f::f75f:f22f]/12");
		matchAddress("[ffff:ffff:ffff:ffff:ffff:ffff:ffff:ffff]", "fff8::ffff%13/13", "[fff8:f43f:f64f::f75f:f22f]/13");
		matchAddress("[ffff:ffff:ffff:ffff:ffff:ffff:ffff:ffff]", "fffc::ffff%14/14", "[fffc:f43f:f64f::f75f:f22f]/14");
		matchAddress("[ffff:ffff:ffff:ffff:ffff:ffff:ffff:ffff]", "fffe::ffff%15/15", "[fffe:f43f:f64f::f75f:f22f]/15");
		matchAddress("[ffff:ffff:ffff:ffff:ffff:ffff:ffff:ffff]", "ffff::ffff%16/16", "[ffff:f43f:f64f::f75f:f22f]/16");
		matchAddress("[ffff:ffff:ffff:ffff:ffff:ffff:ffff:ffff]", "e23f::ffff%16/16", "[e23f:f43f:f64f::f75f:f22f]/16", false);
		
		matchAddress("[1234:ffff:ffff:ffff:ffff:ffff:ffff:ffff]", "1234:8111::ffff%17/17", "[1234:fe3f:f43f:f64f::f75f:f22f]/17");
		matchAddress("[1234:ffff:ffff:ffff:ffff:ffff:ffff:ffff]", "1234:c111::ffff%18/18", "[1234:fe3f:f43f:f64f::f75f:f22f]/18");
		matchAddress("[1234:ffff:ffff:ffff:ffff:ffff:ffff:ffff]", "1234:e111::ffff%19/19", "[1234:fe3f:f43f:f64f::f75f:f22f]/19");
		matchAddress("[1234:ffff:ffff:ffff:ffff:ffff:ffff:ffff]", "1234:f111::ffff%20/20", "[1234:fe3f:f43f:f64f::f75f:f22f]/20");
		matchAddress("[1234:ffff:ffff:ffff:ffff:ffff:ffff:ffff]", "1234:f811::ffff%21/21", "[1234:f83f:f43f:f64f::f75f:f22f]/21");
		matchAddress("[1234:ffff:ffff:ffff:ffff:ffff:ffff:ffff]", "1234:fc11::ffff%22/22", "[1234:fc3f:f43f:f64f::f75f:f22f]/22");
		matchAddress("[1234:ffff:ffff:ffff:ffff:ffff:ffff:ffff]", "1234:fe11::ffff%23/23", "[1234:fe3f:f43f:f64f::f75f:f22f]/23");
		matchAddress("[1234:ffff:ffff:ffff:ffff:ffff:ffff:ffff]", "1234:ff11::ffff%24/24", "[1234:ff3f:f43f:f64f::f75f:f22f]/24");
		matchAddress("[1234:ffff:ffff:ffff:ffff:ffff:ffff:ffff]", "1234:ff81::ffff%25/25", "[1234:ff8e:f43f:f64f::f75f:f22f]/25");
		matchAddress("[1234:ffff:ffff:ffff:ffff:ffff:ffff:ffff]", "1234:ffc1::ffff%26/26", "[1234:ffce:f43f:f64f::f75f:f22f]/26");
		matchAddress("[1234:ffff:ffff:ffff:ffff:ffff:ffff:ffff]", "1234:ffe1::ffff%27/27", "[1234:ffee:f43f:f64f::f75f:f22f]/27");
		matchAddress("[1234:ffff:ffff:ffff:ffff:ffff:ffff:ffff]", "1234:fff1::ffff%28/28", "[1234:fffe:f43f:f64f::f75f:f22f]/28");
		matchAddress("[1234:ffff:ffff:ffff:ffff:ffff:ffff:ffff]", "1234:fff8::ffff%29/29", "[1234:fff8:f43f:f64f::f75f:f22f]/29");
		matchAddress("[1234:ffff:ffff:ffff:ffff:ffff:ffff:ffff]", "1234:fffc::ffff%30/30", "[1234:fffc:f43f:f64f::f75f:f22f]/30");
		matchAddress("[1234:ffff:ffff:ffff:ffff:ffff:ffff:ffff]", "1234:fffe::ffff%31/31", "[1234:fffe:f43f:f64f::f75f:f22f]/31");
		matchAddress("[1234:ffff:ffff:ffff:ffff:ffff:ffff:ffff]", "1234:ffff::ffff%32/32", "[1234:ffff:f43f:f64f::f75f:f22f]/32");
		matchAddress("[1234:ffff:ffff:ffff:ffff:ffff:ffff:ffff]", "1234:e23f::ffff%32/32", "[1234:e23f:f43f:f64f::f75f:f22f]/32", false);
		
		matchAddress("[1234:5678:ffff:ffff:ffff:ffff:ffff:ffff]", "1234:5678:8111::ffff%33/33", "[1234:5678:fe3f:f43f::f75f:f22f]/33");
		matchAddress("[1234:5678:ffff:ffff:ffff:ffff:ffff:ffff]", "1234:5678:c111::ffff%34/34", "[1234:5678:fe3f:f43f::f75f:f22f]/34");
		matchAddress("[1234:5678:ffff:ffff:ffff:ffff:ffff:ffff]", "1234:5678:e111::ffff%35/35", "[1234:5678:fe3f:f43f::f75f:f22f]/35");
		matchAddress("[1234:5678:ffff:ffff:ffff:ffff:ffff:ffff]", "1234:5678:f111::ffff%36/36", "[1234:5678:fe3f:f43f::f75f:f22f]/36");
		matchAddress("[1234:5678:ffff:ffff:ffff:ffff:ffff:ffff]", "1234:5678:f811::ffff%37/37", "[1234:5678:f83f:f43f::f75f:f22f]/37");
		matchAddress("[1234:5678:ffff:ffff:ffff:ffff:ffff:ffff]", "1234:5678:fc11::ffff%38/38", "[1234:5678:fc3f:f43f::f75f:f22f]/38");
		matchAddress("[1234:5678:ffff:ffff:ffff:ffff:ffff:ffff]", "1234:5678:fe11::ffff%39/39", "[1234:5678:fe3f:f43f::f75f:f22f]/39");
		matchAddress("[1234:5678:ffff:ffff:ffff:ffff:ffff:ffff]", "1234:5678:ff11::ffff%40/40", "[1234:5678:ff3f:f43f::f75f:f22f]/40");
		matchAddress("[1234:5678:ffff:ffff:ffff:ffff:ffff:ffff]", "1234:5678:ff81::ffff%41/41", "[1234:5678:ff8e:f43f::f75f:f22f]/41");
		matchAddress("[1234:5678:ffff:ffff:ffff:ffff:ffff:ffff]", "1234:5678:ffc1::ffff%42/42", "[1234:5678:ffce:f43f::f75f:f22f]/42");
		matchAddress("[1234:5678:ffff:ffff:ffff:ffff:ffff:ffff]", "1234:5678:ffe1::ffff%43/43", "[1234:5678:ffee:f43f::f75f:f22f]/43");
		matchAddress("[1234:5678:ffff:ffff:ffff:ffff:ffff:ffff]", "1234:5678:fff1::ffff%44/44", "[1234:5678:fffe:f43f::f75f:f22f]/44");
		matchAddress("[1234:5678:ffff:ffff:ffff:ffff:ffff:ffff]", "1234:5678:fff8::ffff%45/45", "[1234:5678:fff8:f43f::f75f:f22f]/45");
		matchAddress("[1234:5678:ffff:ffff:ffff:ffff:ffff:ffff]", "1234:5678:fffc::ffff%46/46", "[1234:5678:fffc:f43f::f75f:f22f]/46");
		matchAddress("[1234:5678:ffff:ffff:ffff:ffff:ffff:ffff]", "1234:5678:fffe::ffff%47/47", "[1234:5678:fffe:f43f::f75f:f22f]/47");
		matchAddress("[1234:5678:ffff:ffff:ffff:ffff:ffff:ffff]", "1234:5678:ffff::ffff%48/48", "[1234:5678:ffff:f43f::f75f:f22f]/48");
		matchAddress("[1234:5678:ffff:ffff:ffff:ffff:ffff:ffff]", "1234:5678:e23f::ffff%48/48", "[1234:5678:e23f:f43f::f75f:f22f]/48", false);
		
		matchAddress("[1234:5678:9abc:ffff:ffff:ffff:ffff:ffff]", "1234:5678:9abc:8111::ffff%49/49", "[1234:5678:9abc:fe3f:f43f::f75f:f22f]/49");
		matchAddress("[1234:5678:9abc:ffff:ffff:ffff:ffff:ffff]", "1234:5678:9abc:c111::ffff%50/50", "[1234:5678:9abc:fe3f:f43f::f75f:f22f]/50");
		matchAddress("[1234:5678:9abc:ffff:ffff:ffff:ffff:ffff]", "1234:5678:9abc:e111::ffff%51/51", "[1234:5678:9abc:fe3f:f43f::f75f:f22f]/51");
		matchAddress("[1234:5678:9abc:ffff:ffff:ffff:ffff:ffff]", "1234:5678:9abc:f111::ffff%52/52", "[1234:5678:9abc:fe3f:f43f::f75f:f22f]/52");
		matchAddress("[1234:5678:9abc:ffff:ffff:ffff:ffff:ffff]", "1234:5678:9abc:f811::ffff%53/53", "[1234:5678:9abc:f83f:f43f::f75f:f22f]/53");
		matchAddress("[1234:5678:9abc:ffff:ffff:ffff:ffff:ffff]", "1234:5678:9abc:fc11::ffff%54/54", "[1234:5678:9abc:fc3f:f43f::f75f:f22f]/54");
		matchAddress("[1234:5678:9abc:ffff:ffff:ffff:ffff:ffff]", "1234:5678:9abc:fe11::ffff%55/55", "[1234:5678:9abc:fe3f:f43f::f75f:f22f]/55");
		matchAddress("[1234:5678:9abc:ffff:ffff:ffff:ffff:ffff]", "1234:5678:9abc:ff11::ffff%56/56", "[1234:5678:9abc:ff3f:f43f::f75f:f22f]/56");
		matchAddress("[1234:5678:9abc:ffff:ffff:ffff:ffff:ffff]", "1234:5678:9abc:ff81::ffff%57/57", "[1234:5678:9abc:ff8e:f43f::f75f:f22f]/57");
		matchAddress("[1234:5678:9abc:ffff:ffff:ffff:ffff:ffff]", "1234:5678:9abc:ffc1::ffff%58/58", "[1234:5678:9abc:ffce:f43f::f75f:f22f]/58");
		matchAddress("[1234:5678:9abc:ffff:ffff:ffff:ffff:ffff]", "1234:5678:9abc:ffe1::ffff%59/59", "[1234:5678:9abc:ffee:f43f::f75f:f22f]/59");
		matchAddress("[1234:5678:9abc:ffff:ffff:ffff:ffff:ffff]", "1234:5678:9abc:fff1::ffff%60/60", "[1234:5678:9abc:fffe:f43f::f75f:f22f]/60");
		matchAddress("[1234:5678:9abc:ffff:ffff:ffff:ffff:ffff]", "1234:5678:9abc:fff8::ffff%61/61", "[1234:5678:9abc:fff8:f43f::f75f:f22f]/61");
		matchAddress("[1234:5678:9abc:ffff:ffff:ffff:ffff:ffff]", "1234:5678:9abc:fffc::ffff%62/62", "[1234:5678:9abc:fffc:f43f::f75f:f22f]/62");
		matchAddress("[1234:5678:9abc:ffff:ffff:ffff:ffff:ffff]", "1234:5678:9abc:fffe::ffff%63/63", "[1234:5678:9abc:fffe:f43f::f75f:f22f]/63");
		matchAddress("[1234:5678:9abc:ffff:ffff:ffff:ffff:ffff]", "1234:5678:9abc:ffff::ffff%64/64", "[1234:5678:9abc:ffff:f43f::f75f:f22f]/64");
		matchAddress("[1234:5678:9abc:ffff:ffff:ffff:ffff:ffff]", "1234:5678:9abc:e23f::ffff%64/64", "[1234:5678:9abc:e23f:f43f::f75f:f22f]/64", false);
		
		matchAddress("[1234:5678:9abc:cdef:ffff:ffff:ffff:ffff]", "1234:5678:9abc:cdef:8111::ffff%65/65", "[1234:5678:9abc:cdef:fe3f::f75f:f22f]/65");
		matchAddress("[1234:5678:9abc:cdef:ffff:ffff:ffff:ffff]", "1234:5678:9abc:cdef:c111::ffff%66/66", "[1234:5678:9abc:cdef:fe3f::f75f:f22f]/66");
		matchAddress("[1234:5678:9abc:cdef:ffff:ffff:ffff:ffff]", "1234:5678:9abc:cdef:e111::ffff%67/67", "[1234:5678:9abc:cdef:fe3f::f75f:f22f]/67");
		matchAddress("[1234:5678:9abc:cdef:ffff:ffff:ffff:ffff]", "1234:5678:9abc:cdef:f111::ffff%68/68", "[1234:5678:9abc:cdef:fe3f::f75f:f22f]/68");
		matchAddress("[1234:5678:9abc:cdef:ffff:ffff:ffff:ffff]", "1234:5678:9abc:cdef:f811::ffff%69/69", "[1234:5678:9abc:cdef:f83f::f75f:f22f]/69");
		matchAddress("[1234:5678:9abc:cdef:ffff:ffff:ffff:ffff]", "1234:5678:9abc:cdef:fc11::ffff%70/70", "[1234:5678:9abc:cdef:fc3f::f75f:f22f]/70");
		matchAddress("[1234:5678:9abc:cdef:ffff:ffff:ffff:ffff]", "1234:5678:9abc:cdef:fe11::ffff%71/71", "[1234:5678:9abc:cdef:fe3f::f75f:f22f]/71");
		matchAddress("[1234:5678:9abc:cdef:ffff:ffff:ffff:ffff]", "1234:5678:9abc:cdef:ff11::ffff%72/72", "[1234:5678:9abc:cdef:ff3f::f75f:f22f]/72");
		matchAddress("[1234:5678:9abc:cdef:ffff:ffff:ffff:ffff]", "1234:5678:9abc:cdef:ff81::ffff%73/73", "[1234:5678:9abc:cdef:ff8e::f75f:f22f]/73");
		matchAddress("[1234:5678:9abc:cdef:ffff:ffff:ffff:ffff]", "1234:5678:9abc:cdef:ffc1::ffff%74/74", "[1234:5678:9abc:cdef:ffce::f75f:f22f]/74");
		matchAddress("[1234:5678:9abc:cdef:ffff:ffff:ffff:ffff]", "1234:5678:9abc:cdef:ffe1::ffff%75/75", "[1234:5678:9abc:cdef:ffee::f75f:f22f]/75");
		matchAddress("[1234:5678:9abc:cdef:ffff:ffff:ffff:ffff]", "1234:5678:9abc:cdef:fff1::ffff%76/76", "[1234:5678:9abc:cdef:fffe::f75f:f22f]/76");
		matchAddress("[1234:5678:9abc:cdef:ffff:ffff:ffff:ffff]", "1234:5678:9abc:cdef:fff8::ffff%77/77", "[1234:5678:9abc:cdef:fff8::f75f:f22f]/77");
		matchAddress("[1234:5678:9abc:cdef:ffff:ffff:ffff:ffff]", "1234:5678:9abc:cdef:fffc::ffff%78/78", "[1234:5678:9abc:cdef:fffc::f75f:f22f]/78");
		matchAddress("[1234:5678:9abc:cdef:ffff:ffff:ffff:ffff]", "1234:5678:9abc:cdef:fffe::ffff%79/79", "[1234:5678:9abc:cdef:fffe::f75f:f22f]/79");
		matchAddress("[1234:5678:9abc:cdef:ffff:ffff:ffff:ffff]", "1234:5678:9abc:cdef:ffff::ffff%80/80", "[1234:5678:9abc:cdef:ffff::f75f:f22f]/80");
		matchAddress("[1234:5678:9abc:cdef:ffff:ffff:ffff:ffff]", "1234:5678:9abc:cdef:e23f::ffff%80/80", "[1234:5678:9abc:cdef:e23f::f75f:f22f]/80", false);
		
		matchAddress("[0123:1234:5678:9abc:cdef:ffff:ffff:ffff]", "0123:1234:5678:9abc:cdef:8111::ffff%81/81", "[0123:1234:5678:9abc:cdef:fe3f:f75f:f22f]/81");
		matchAddress("[0123:1234:5678:9abc:cdef:ffff:ffff:ffff]", "0123:1234:5678:9abc:cdef:c111::ffff%82/82", "[0123:1234:5678:9abc:cdef:fe3f:f75f:f22f]/82");
		matchAddress("[0123:1234:5678:9abc:cdef:ffff:ffff:ffff]", "0123:1234:5678:9abc:cdef:e111::ffff%83/83", "[0123:1234:5678:9abc:cdef:fe3f:f75f:f22f]/83");
		matchAddress("[0123:1234:5678:9abc:cdef:ffff:ffff:ffff]", "0123:1234:5678:9abc:cdef:f111::ffff%84/84", "[0123:1234:5678:9abc:cdef:fe3f:f75f:f22f]/84");
		matchAddress("[0123:1234:5678:9abc:cdef:ffff:ffff:ffff]", "0123:1234:5678:9abc:cdef:f811::ffff%85/85", "[0123:1234:5678:9abc:cdef:f83f:f75f:f22f]/85");
		matchAddress("[0123:1234:5678:9abc:cdef:ffff:ffff:ffff]", "0123:1234:5678:9abc:cdef:fc11::ffff%86/86", "[0123:1234:5678:9abc:cdef:fc3f:f75f:f22f]/86");
		matchAddress("[0123:1234:5678:9abc:cdef:ffff:ffff:ffff]", "0123:1234:5678:9abc:cdef:fe11::ffff%87/87", "[0123:1234:5678:9abc:cdef:fe3f:f75f:f22f]/87");
		matchAddress("[0123:1234:5678:9abc:cdef:ffff:ffff:ffff]", "0123:1234:5678:9abc:cdef:ff11::ffff%88/88", "[0123:1234:5678:9abc:cdef:ff3f:f75f:f22f]/88");
		matchAddress("[0123:1234:5678:9abc:cdef:ffff:ffff:ffff]", "0123:1234:5678:9abc:cdef:ff81::ffff%89/89", "[0123:1234:5678:9abc:cdef:ff8e:f75f:f22f]/89");
		matchAddress("[0123:1234:5678:9abc:cdef:ffff:ffff:ffff]", "0123:1234:5678:9abc:cdef:ffc1::ffff%90/90", "[0123:1234:5678:9abc:cdef:ffce:f75f:f22f]/90");
		matchAddress("[0123:1234:5678:9abc:cdef:ffff:ffff:ffff]", "0123:1234:5678:9abc:cdef:ffe1::ffff%91/91", "[0123:1234:5678:9abc:cdef:ffee:f75f:f22f]/91");
		matchAddress("[0123:1234:5678:9abc:cdef:ffff:ffff:ffff]", "0123:1234:5678:9abc:cdef:fff1::ffff%92/92", "[0123:1234:5678:9abc:cdef:fffe:f75f:f22f]/92");
		matchAddress("[0123:1234:5678:9abc:cdef:ffff:ffff:ffff]", "0123:1234:5678:9abc:cdef:fff8::ffff%93/93", "[0123:1234:5678:9abc:cdef:fff8:f75f:f22f]/93");
		matchAddress("[0123:1234:5678:9abc:cdef:ffff:ffff:ffff]", "0123:1234:5678:9abc:cdef:fffc::ffff%94/94", "[0123:1234:5678:9abc:cdef:fffc:f75f:f22f]/94");
		matchAddress("[0123:1234:5678:9abc:cdef:ffff:ffff:ffff]", "0123:1234:5678:9abc:cdef:fffe::ffff%95/95", "[0123:1234:5678:9abc:cdef:fffe:f75f:f22f]/95");
		matchAddress("[0123:1234:5678:9abc:cdef:ffff:ffff:ffff]", "0123:1234:5678:9abc:cdef:ffff::ffff%96/96", "[0123:1234:5678:9abc:cdef:ffff:f75f:f22f]/96");
		matchAddress("[0123:1234:5678:9abc:cdef:ffff:ffff:ffff]", "0123:1234:5678:9abc:cdef:e23f::ffff%96/96", "[0123:1234:5678:9abc:cdef:e23f:f75f:f22f]/96", false);
		
		matchAddress("[4321:0123:1234:5678:9abc:cdef:ffff:ffff]", "4321:0123:1234:5678:9abc:cdef:8111:ffff%97/97",  "[4321:0123:1234:5678:9abc:cdef:fe3f:f22f]/97");
		matchAddress("[4321:0123:1234:5678:9abc:cdef:ffff:ffff]", "4321:0123:1234:5678:9abc:cdef:c111:ffff%98/98",  "[4321:0123:1234:5678:9abc:cdef:fe3f:f22f]/98");
		matchAddress("[4321:0123:1234:5678:9abc:cdef:ffff:ffff]", "4321:0123:1234:5678:9abc:cdef:e111:ffff%99/99",  "[4321:0123:1234:5678:9abc:cdef:fe3f:f22f]/99");
		matchAddress("[4321:0123:1234:5678:9abc:cdef:ffff:ffff]", "4321:0123:1234:5678:9abc:cdef:f111:ffff%100/100", "[4321:0123:1234:5678:9abc:cdef:fe3f:f22f]/100");
		matchAddress("[4321:0123:1234:5678:9abc:cdef:ffff:ffff]", "4321:0123:1234:5678:9abc:cdef:f811:ffff%101/101", "[4321:0123:1234:5678:9abc:cdef:f83f:f22f]/101");
		matchAddress("[4321:0123:1234:5678:9abc:cdef:ffff:ffff]", "4321:0123:1234:5678:9abc:cdef:fc11:ffff%102/102", "[4321:0123:1234:5678:9abc:cdef:fc3f:f22f]/102");
		matchAddress("[4321:0123:1234:5678:9abc:cdef:ffff:ffff]", "4321:0123:1234:5678:9abc:cdef:fe11:ffff%103/103", "[4321:0123:1234:5678:9abc:cdef:fe3f:f22f]/103");
		matchAddress("[4321:0123:1234:5678:9abc:cdef:ffff:ffff]", "4321:0123:1234:5678:9abc:cdef:ff11:ffff%104/104", "[4321:0123:1234:5678:9abc:cdef:ff3f:f22f]/104");
		matchAddress("[4321:0123:1234:5678:9abc:cdef:ffff:ffff]", "4321:0123:1234:5678:9abc:cdef:ff81:ffff%105/105", "[4321:0123:1234:5678:9abc:cdef:ff8e:f22f]/105");
		matchAddress("[4321:0123:1234:5678:9abc:cdef:ffff:ffff]", "4321:0123:1234:5678:9abc:cdef:ffc1:ffff%106/106", "[4321:0123:1234:5678:9abc:cdef:ffce:f22f]/106");
		matchAddress("[4321:0123:1234:5678:9abc:cdef:ffff:ffff]", "4321:0123:1234:5678:9abc:cdef:ffe1:ffff%107/107", "[4321:0123:1234:5678:9abc:cdef:ffee:f22f]/107");
		matchAddress("[4321:0123:1234:5678:9abc:cdef:ffff:ffff]", "4321:0123:1234:5678:9abc:cdef:fff1:ffff%108/108", "[4321:0123:1234:5678:9abc:cdef:fffe:f22f]/108");
		matchAddress("[4321:0123:1234:5678:9abc:cdef:ffff:ffff]", "4321:0123:1234:5678:9abc:cdef:fff8:ffff%109/109", "[4321:0123:1234:5678:9abc:cdef:fff8:f22f]/109");
		matchAddress("[4321:0123:1234:5678:9abc:cdef:ffff:ffff]", "4321:0123:1234:5678:9abc:cdef:fffc:ffff%110/110", "[4321:0123:1234:5678:9abc:cdef:fffc:f22f]/110");
		matchAddress("[4321:0123:1234:5678:9abc:cdef:ffff:ffff]", "4321:0123:1234:5678:9abc:cdef:fffe:ffff%111/111", "[4321:0123:1234:5678:9abc:cdef:fffe:f22f]/111");
		matchAddress("[4321:0123:1234:5678:9abc:cdef:ffff:ffff]", "4321:0123:1234:5678:9abc:cdef:ffff:ffff%112/112", "[4321:0123:1234:5678:9abc:cdef:ffff:f22f]/112");
		matchAddress("[4321:0123:1234:5678:9abc:cdef:ffff:ffff]", "4321:0123:1234:5678:9abc:cdef:e23f:ffff%112/112", "[4321:0123:1234:5678:9abc:cdef:e23f:f22f]/112", false);
		
		matchAddress("[8765:4321:0123:1234:5678:9abc:cdef:ffff]", "8765:4321:0123:1234:5678:9abc:cdef:8111%113/113", "[8765:4321:0123:1234:5678:9abc:cdef:fe3f]/113");
		matchAddress("[8765:4321:0123:1234:5678:9abc:cdef:ffff]", "8765:4321:0123:1234:5678:9abc:cdef:c111%114/114", "[8765:4321:0123:1234:5678:9abc:cdef:fe3f]/114");
		matchAddress("[8765:4321:0123:1234:5678:9abc:cdef:ffff]", "8765:4321:0123:1234:5678:9abc:cdef:e111%115/115", "[8765:4321:0123:1234:5678:9abc:cdef:fe3f]/115");
		matchAddress("[8765:4321:0123:1234:5678:9abc:cdef:ffff]", "8765:4321:0123:1234:5678:9abc:cdef:f111%116/116", "[8765:4321:0123:1234:5678:9abc:cdef:fe3f]/116");
		matchAddress("[8765:4321:0123:1234:5678:9abc:cdef:ffff]", "8765:4321:0123:1234:5678:9abc:cdef:f811%117/117", "[8765:4321:0123:1234:5678:9abc:cdef:f83f]/117");
		matchAddress("[8765:4321:0123:1234:5678:9abc:cdef:ffff]", "8765:4321:0123:1234:5678:9abc:cdef:fc11%118/118", "[8765:4321:0123:1234:5678:9abc:cdef:fc3f]/118");
		matchAddress("[8765:4321:0123:1234:5678:9abc:cdef:ffff]", "8765:4321:0123:1234:5678:9abc:cdef:fe11%119/119", "[8765:4321:0123:1234:5678:9abc:cdef:fe3f]/119");
		matchAddress("[8765:4321:0123:1234:5678:9abc:cdef:ffff]", "8765:4321:0123:1234:5678:9abc:cdef:ff11%120/120", "[8765:4321:0123:1234:5678:9abc:cdef:ff3f]/120");
		matchAddress("[8765:4321:0123:1234:5678:9abc:cdef:ffff]", "8765:4321:0123:1234:5678:9abc:cdef:ff81%121/121", "[8765:4321:0123:1234:5678:9abc:cdef:ff8e]/121");
		matchAddress("[8765:4321:0123:1234:5678:9abc:cdef:ffff]", "8765:4321:0123:1234:5678:9abc:cdef:ffc1%122/122", "[8765:4321:0123:1234:5678:9abc:cdef:ffce]/122");
		matchAddress("[8765:4321:0123:1234:5678:9abc:cdef:ffff]", "8765:4321:0123:1234:5678:9abc:cdef:ffe1%123/123", "[8765:4321:0123:1234:5678:9abc:cdef:ffee]/123");
		matchAddress("[8765:4321:0123:1234:5678:9abc:cdef:ffff]", "8765:4321:0123:1234:5678:9abc:cdef:fff1%124/124", "[8765:4321:0123:1234:5678:9abc:cdef:fffe]/124");
		matchAddress("[8765:4321:0123:1234:5678:9abc:cdef:ffff]", "8765:4321:0123:1234:5678:9abc:cdef:fff8%125/125", "[8765:4321:0123:1234:5678:9abc:cdef:fff8]/125");
		matchAddress("[8765:4321:0123:1234:5678:9abc:cdef:ffff]", "8765:4321:0123:1234:5678:9abc:cdef:fffc%126/126", "[8765:4321:0123:1234:5678:9abc:cdef:fffc]/126");
		matchAddress("[8765:4321:0123:1234:5678:9abc:cdef:ffff]", "8765:4321:0123:1234:5678:9abc:cdef:fffe%127/127", "[8765:4321:0123:1234:5678:9abc:cdef:fffe]/127");
		matchAddress("[8765:4321:0123:1234:5678:9abc:cdef:ffff]", "8765:4321:0123:1234:5678:9abc:cdef:ffff%128/128", "[8765:4321:0123:1234:5678:9abc:cdef:ffff]/128");
		matchAddress("[8765:4321:0123:1234:5678:9abc:cdef:ffff]", "8765:4321:0123:1234:5678:9abc:cdef:e23f%128/128", "[8765:4321:0123:1234:5678:9abc:cdef:e23f]/128", false);
		
		// ip allv6
		matchAddress("::f:1", "allv6", "[::1]/0", true);
		
		// ip all
		matchAddress("::f:1", "all", "all", true);
		
		// ip none
		matchAddress("::f:1", "none", "none", false);
		
		// ip v4 & v6
		matchAddress("::f:1", "allv4", "0.0.0.1/0", false);
		
		matchAddress("127.168.2.1", "::ffff:255.255.255.255/0", "[::0:ffff:255.255.255.255]/0", false);
		matchAddress("127.168.2.1", "::ffff:7fa8:0/112", "[::0:ffff:7fa8:0]/112", false);
		matchAddress("127.168.2.1", "::ffff:7fa9:0/112", "[::0:ffff:7fa9:0]/112", false);
		
		matchAddress("::ffff:7fa8:0", "127.168.2.1/16", "127.168.2.1/255.255.0.0", false);
		matchAddress("::ffff:7fa9:0", "127.168.2.1/16", "127.168.2.1/255.255.0.0", false);
		matchAddress("fe80::0", "127.168.2.1/16", "127.168.2.1/255.255.0.0", false);
		matchAddress("fe80::1:0", "127.168.2.1/16", "127.168.2.1/255.255.0.0", false);
	}

}
