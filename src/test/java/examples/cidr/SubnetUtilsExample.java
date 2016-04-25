package examples.cidr;

import java.util.Arrays;
import java.util.Scanner;

import panda.net.util.SubnetUtils;
import panda.net.util.SubnetUtils.SubnetInfo;

/**
 * Example class that shows how to use the {@link SubnetUtils} class.
 */
public class SubnetUtilsExample {

	public static void main(String[] args) {
		String subnet = "192.168.0.3/31";
		SubnetUtils utils = new SubnetUtils(subnet);
		SubnetInfo info = utils.getInfo();

		System.out.printf("Subnet Information for %s:\n", subnet);
		System.out.println("--------------------------------------");
		System.out.printf("IP Address:\t\t\t%s\t[%s]\n", info.getAddress(),
			Integer.toBinaryString(info.asInteger(info.getAddress())));
		System.out.printf("Netmask:\t\t\t%s\t[%s]\n", info.getNetmask(),
			Integer.toBinaryString(info.asInteger(info.getNetmask())));
		System.out.printf("CIDR Representation:\t\t%s\n\n", info.getCidrSignature());

		System.out.printf("Supplied IP Address:\t\t%s\n\n", info.getAddress());

		System.out.printf("Network Address:\t\t%s\t[%s]\n", info.getNetworkAddress(),
			Integer.toBinaryString(info.asInteger(info.getNetworkAddress())));
		System.out.printf("Broadcast Address:\t\t%s\t[%s]\n", info.getBroadcastAddress(),
			Integer.toBinaryString(info.asInteger(info.getBroadcastAddress())));
		System.out.printf("Low Address:\t\t\t%s\t[%s]\n", info.getLowAddress(),
			Integer.toBinaryString(info.asInteger(info.getLowAddress())));
		System.out.printf("High Address:\t\t\t%s\t[%s]\n", info.getHighAddress(),
			Integer.toBinaryString(info.asInteger(info.getHighAddress())));

		System.out.printf("Total usable addresses: \t%d\n", Long.valueOf(info.getAddressCountLong()));
		System.out.printf("Address List: %s\n\n", Arrays.toString(info.getAllAddresses()));

		final String prompt = "Enter an IP address (e.g. 192.168.0.10):";
		System.out.println(prompt);
		Scanner scanner = new Scanner(System.in);
		while (scanner.hasNextLine()) {
			String address = scanner.nextLine();
			System.out.println("The IP address [" + address + "] is " + (info.isInRange(address) ? "" : "not ")
					+ "within the subnet [" + subnet + "]");
			System.out.println(prompt);
		}
		scanner.close();
	}

}
