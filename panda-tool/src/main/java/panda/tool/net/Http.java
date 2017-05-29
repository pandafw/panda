package panda.tool.net;

import java.io.IOException;
import java.util.List;

import panda.args.Argument;
import panda.args.Option;
import panda.lang.Exceptions;
import panda.lang.Strings;
import panda.net.http.HttpClient;
import panda.net.http.HttpMethod;
import panda.net.http.HttpResponse;
import panda.tool.AbstractCommandTool;

/**
 */
public class Http extends AbstractCommandTool {
	/**
	 * @param args arguments
	 */
	public static void main(String[] args) {
		new Http().execute(args);
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
	protected String url;
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
	@Option(opt='a', option="auth", arg="AUTH", usage="WWW Athentication")
	public void setAuth(String auth) {
		this.auth = auth;
	}

	@Argument(name="URL", index=0, usage="The URL to access")
	public void setUrl(String url) {
		this.url = url;
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
	@Argument(name="PARAMS", usage="The query paramters")
	public void setArgs(List<String> args) {
		this.args = args;
	}


	/**
	 * execute
	 */
	public void execute() {
		HttpClient hc = new HttpClient();
		
		if (Strings.isNotEmpty(auth)) {
			String[] ss = Strings.split(auth, ':');
			hc.getRequest().setBasicAuthentication(ss[0], ss.length > 1 ? ss[1] : "");
		}
		
		hc.getRequest().setUrl(url);
		for (String a : args) {
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

		try {
			HttpResponse hr = hc.doGet();
			System.out.println(hr.toString());
		}
		catch (IOException e) {
			throw Exceptions.wrapThrow(e);
		}
	}
}
