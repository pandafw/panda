package panda.tool.net;

import java.util.List;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.ParseException;

import panda.lang.Collections;
import panda.lang.Strings;
import panda.net.http.HttpClient;
import panda.net.http.HttpMethod;
import panda.net.http.HttpResponse;
import panda.util.tool.AbstractCommandTool;

/**
 */
public class Http {
	/**
	 * Base main class for code generator. Parse basic command line options.
	 */
	public static class Main extends AbstractCommandTool {
		/**
		 * @param args arguments
		 */
		public static void main(String[] args) {
			Main main = new Main();
			
			Object cg = new Http();

			main.execute(cg, args);
		}

		@Override
		protected void addCommandLineOptions() throws Exception {
			super.addCommandLineOptions();
			
			addCommandLineOption("a", "auth", "WWW Athentication", false);
		}

		@Override
		protected void getCommandLineOptions(CommandLine cl) throws Exception {
			super.getCommandLineOptions(cl);
			
			if (cl.hasOption("a")) {
				setParameter("auth", cl.getOptionValue("a").trim());
			}

			if (Collections.isEmpty(cl.getArgList())) {
				throw new ParseException("URL is required!");
			}
			setParameter("args", cl.getArgList());
		}
	}
	
	/**
	 * Constructor
	 */
	public Http() {
	}

	//---------------------------------------------------------------------------------------
	// properties
	//---------------------------------------------------------------------------------------
	protected String auth;
	protected List<String> args;

	/**
	 * @return the auth
	 */
	public String getAuth() {
		return auth;
	}


	/**
	 * @param auth the auth to set
	 */
	public void setAuth(String auth) {
		this.auth = auth;
	}


	/**
	 * @return the args
	 */
	public List<String> getArgs() {
		return args;
	}


	/**
	 * @param args the args to set
	 */
	public void setArgs(List<String> args) {
		this.args = args;
	}


	/**
	 * execute
	 * @throws Exception if an error occurs
	 */
	public void execute() throws Exception {
		HttpClient hc = new HttpClient();
		
		if (Strings.isNotEmpty(auth)) {
			String[] ss = Strings.split(auth, ':');
			hc.getRequest().setBasicAuthentication(ss[0], ss.length > 1 ? ss[1] : "");
		}
		
		hc.getRequest().setUrl(args.get(0));
		for (int i = 1; i < args.size(); i++) {
			String a = args.get(i);
			int j = a.indexOf('=');
			if (j > 0) {
				hc.getRequest().setParam(a.substring(0, j), a.substring(j + 1));
			}
			else {
				hc.getRequest().setParam(a, "");
			}
		}
		
		hc.getRequest().setMethod(HttpMethod.GET);
		System.out.println(hc.getRequest().toString());
		System.out.println();

		HttpResponse hr = hc.doGet();
		System.out.println(hr.toString());
	}
}
