package panda.net.whois;

import java.io.IOException;
import java.net.IDN;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import panda.lang.Arrays;
import panda.lang.Strings;
import panda.log.Log;
import panda.log.Logs;

/**
 * port from python-whois
 */
public class NICClient {
	private static final Log log = Logs.getLog(NICClient.class);

	public static final String ABUSEHOST = "whois.abuse.net";
	public static final String NICHOST = "whois.crsnic.net";
	public static final String INICHOST = "whois.networksolutions.com";
	public static final String DNICHOST = "whois.nic.mil";
	public static final String GNICHOST = "whois.nic.gov";
	public static final String ANICHOST = "whois.arin.net";
	public static final String LNICHOST = "whois.lacnic.net";
	public static final String RNICHOST = "whois.ripe.net";
	public static final String PNICHOST = "whois.apnic.net";
	public static final String MNICHOST = "whois.ra.net";
	public static final String SNICHOST = "whois.6bone.net";
	public static final String BNICHOST = "whois.registro.br";
	public static final String NORIDHOST = "whois.norid.no";
	public static final String IANAHOST = "whois.iana.org";
	public static final String PANDIHOST = "whois.pandi.or.id";
	public static final String DENICHOST = "de.whois-servers.net";

	public static final String AI_HOST = "whois.nic.ai";
	public static final String AR_HOST = "whois.nic.ar";
	public static final String BY_HOST = "whois.cctld.by";
	public static final String HR_HOST = "whois.dns.hr";
	public static final String APP_HOST = "whois.nic.google";
	public static final String CL_HOST = "whois.nic.cl";
	public static final String CR_HOST = "whois.nic.cr";
	public static final String DE_HOST = "whois.denic.de";
	public static final String DK_HOST = "whois.dk-hostmaster.dk";
	public static final String DO_HOST = "whois.nic.do";
	public static final String CA_HOST = "whois.ca.fury.ca";
	public static final String HK_HOST = "whois.hkirc.hk";
	public static final String HN_HOST = "whois.nic.hn";
	public static final String MONEY_HOST = "whois.nic.money";
	public static final String JOBS_HOST = "whois.nic.jobs";
	public static final String LAT_HOST = "whois.nic.lat";
	public static final String LI_HOST = "whois.nic.li";
	public static final String MX_HOST = "whois.mx";
	public static final String PE_HOST = "kero.yachay.pe";
	public static final String ONLINE_HOST = "whois.nic.online";

	public static final String QNICHOST_TAIL = ".whois-servers.net";

	public static final Set<String> IP_WHOIS_HOSTS = Arrays.toSet(LNICHOST, RNICHOST, PNICHOST, BNICHOST, PANDIHOST);

	public static final Map<String, String> TLD_WHOIS_HOSTS = Arrays.toMap(
		"ai", AI_HOST,
		"app", APP_HOST,
		"money", MONEY_HOST,
		"online", ONLINE_HOST,
		"cl", CL_HOST,
		"ar", AR_HOST,
		"by", BY_HOST,
		"cr", CR_HOST,
		"ca", CA_HOST,
		"do", DO_HOST,
		"de", DE_HOST,
		"hk", HK_HOST,
		"hn", HN_HOST,
		"jobs", JOBS_HOST,
		"lat", LAT_HOST,
		"li", LI_HOST,
		"mx", MX_HOST,
		"pe", PE_HOST
	);

	private Pattern whoisPattern = Pattern.compile("Whois Server: (.*)", Pattern.CASE_INSENSITIVE);

	private String findWhoisServer(String result, String server, String query) {
		// Search the initial TLD lookup results for the regional-specifc
		// whois server for getting contact details.

		Matcher match = whoisPattern.matcher(result);
		if (match.find()) {
			String nhost = Strings.strip(match.group(1));

			// if the whois address is domain.tld/something then
			// s.connect((hostname, 43)) does not work
			if (Strings.contains(nhost, '/')) {
				return null;
			}
			
			if (ANICHOST.equals(server)) {
				for (String nichost : IP_WHOIS_HOSTS) {
					if (Strings.contains(result, nichost)) {
						return nichost;
					}
				}
			}
			
			return nhost;
		}

		return null;
	}

	private String chooseServer(String domain) {
		String tld = Strings.substringAfterLast(domain, '.');
		if (Strings.isEmpty(tld)) {
			return null;
		}
		
		if (tld.endsWith("-norid")) {
			return NORIDHOST;
		}
		if (tld.endsWith("id")) {
			return PANDIHOST;
		}
		if (tld.endsWith("hr")) {
			return HR_HOST;
		}

		if (Character.isDigit(tld.charAt(0))) {
			return ANICHOST;
		}

		String host = TLD_WHOIS_HOSTS.get(tld);
		if (Strings.isNotEmpty(host)) {
			return host;
		}
		return tld + QNICHOST_TAIL;
	}

	public String whois(String query) {
		return whois(query, null, false, false);
	}

	public String whois(String query, String server) {
		return whois(query, server, false, false);
	}

	public String whois(String query, String server, boolean quick) {
		return whois(query, server, quick, false);
	}

	public String whois(String query, String server, boolean quick, boolean manyResults) {
		query = IDN.toASCII(query);
		
		if (Strings.isEmpty(server)) {
			server = chooseServer(query);
			if (Strings.isEmpty(server)) {
				return "";
			}
		}
		
		// Perform initial lookup with TLD whois server
		// then, if the quick flag is false, search that result
		// for the region-specifc whois server and do a lookup
		// there for contact details

		if (DENICHOST.equals(server)) {
			query = "-T dn,ace -C UTF-8 " + query;
		}
		else if (DK_HOST.equals(server)) {
			query = " --show-handles " + query;
		}
		else if (server.endsWith(QNICHOST_TAIL) && manyResults) {
			query = '=' + query;
		}

		String response = "";

		WhoisClient whois = new WhoisClient();

		// We want to timeout if a response takes longer than 10 seconds
		whois.setDefaultTimeout(10000);
		try {
			whois.connect(server);
			response = whois.query(query);
			whois.disconnect();
		}
		catch (IOException e) {
			log.warn("Failed to query '" + query + "' from " + server, e);
			return response;
		}

		if (Strings.contains(response, "with \"=xxx\"")) {
			return whois(query, server, quick, true);
		}

		String nhost = null;
		if (!quick) {
			nhost = findWhoisServer(response, server, query);
		}

		if (Strings.isNotEmpty(nhost) && !Strings.equalsIgnoreCase(server, nhost)) {
			response += '\n' + Strings.repeat('=', 79) + '\n';
			response += whois(query, nhost, true);
		}
		return response;
	}
}
