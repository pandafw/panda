package panda.wing.processor;

import java.io.InputStream;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import panda.bind.json.JsonObject;
import panda.io.FileNames;
import panda.io.Streams;
import panda.lang.Arrays;
import panda.lang.Charsets;
import panda.lang.ClassLoaders;
import panda.lang.Collections;
import panda.lang.Strings;
import panda.mvc.ActionContext;
import panda.mvc.ActionInfo;
import panda.mvc.MvcConfig;
import panda.mvc.View;
import panda.mvc.processor.ViewProcessor;
import panda.mvc.util.TextProvider;
import panda.wing.mvc.ActionRC;

/**
 */
public class AuthenticateProcessor extends ViewProcessor {
	//--------------------------------------------------------
	// permission
	//--------------------------------------------------------
	/**
	 * PERMISSION_ALL = "*";
	 */
	public final static String PERMISSION_ALL = "*";
	
	/**
	 * PERMISSION_NONE = "-";
	 */
	public final static String PERMISSION_NONE = "-";

	/**
	 * "action-permits.json"
	 */
	public static final String SETTING = "action-permits.json";

	/**
	 * USER_ATTRIBUTE = "user";
	 */
	public static final String USER_ATTRIBUTE = "user";

	/**
	 * action permit map
	 */
	private static Map<String, String[]> actionPermits = new LinkedHashMap<String, String[]>();

	/**
	 * action permit cache
	 */
	private static Map<String, String[]> actionPermitCache = new ConcurrentHashMap<String, String[]>();

	private static final int CHECK_NONE = 0;
	private static final int CHECK_LOGIN = 1;
	private static final int CHECK_SECURE = 2;

	/**
	 * check level: none, login(default), secure
	 */
	private int check = CHECK_LOGIN;
	
	/**
	 * allow unknown uri: true (default), false
	 */
	private static boolean allowUnknownUri = true;

	/**
	 * loginResult
	 */
	private String loginResult;
	
	/**
	 * secureResult
	 */
	private String secureResult;
	
	/**
	 * @param check the check to set
	 */
	public void setCheck(String check) {
		if ("login".equalsIgnoreCase(check)) {
			this.check = CHECK_LOGIN;
		}
		else if ("secure".equals(check)) {
			this.check = CHECK_SECURE;
		}
		else {
			this.check = CHECK_NONE;
		}
	}

	/**
	 * @param allowUnknownUri the allowUnknownUri to set
	 */
	public void setAllowUnknownUri(boolean allowUnknownUri) {
		AuthenticateProcessor.allowUnknownUri = allowUnknownUri;
	}

	/**
	 * @param loginResult the loginResult to set
	 */
	public void setLoginResult(String loginResult) {
		this.loginResult = loginResult;
	}

	/**
	 * @param secureResult the secureResult to set
	 */
	public void setSecureResult(String secureResult) {
		this.secureResult = secureResult;
	}

	/**
	 * loadActionPermits
	 * @throws Exception if an error occurs
	 */
	public synchronized static void loadActionPermits() throws Exception {
		if (actionPermits != null) {
			return;
		}
		
		actionPermits = new LinkedHashMap<String, String[]>();
		actionPermitCache = new ConcurrentHashMap<String, String[]>();

		InputStream is = null;
		try {
			is = ClassLoaders.getResourceAsStream(SETTING);

			if (is != null) {
				JsonObject jo = JsonObject.fromJson(is, Charsets.UTF_8);
				for (Entry<String, Object> en : jo.entrySet()) {
					actionPermits.put(en.getKey(), Strings.split(en.getValue().toString()));
				}
			}
		}
		finally {
			Streams.safeClose(is);
		}
	}

	/**
	 * hasPermission
	 * @param permits permits
	 * @return true if user has permit to access the action
	 */
	public static boolean hasPermission(String[] permits, String uri) {
		return hasPermission(Arrays.asList(permits), uri);
	}
	
	/**
	 * hasPermission
	 * @param permits permits
	 * @param uri uri
	 * @return true if user has permit to access the action
	 */
	public static boolean hasPermission(Collection<String> permits, String uri) {
		if (Collections.isEmpty(actionPermits) || permits.contains(PERMISSION_ALL)) {
			return true;
		}

		String[] ps = actionPermitCache.get(uri);
		if (ps == null) {
			ps = Strings.EMPTY_ARRAY;
			for (Entry<String, String[]> e : actionPermits.entrySet()) {
				if (FileNames.wildcardMatch(uri, e.getKey())) {
					ps = e.getValue();
					break;
				}
			}
			actionPermitCache.put(uri, ps);
		}

		if (Arrays.isEmpty(ps)) {
			// uri is not in the check list
			return allowUnknownUri;
		}

		// user has empty permits
		if (Collections.isEmpty(permits)) {
			return false;
		}

		// user has 'deny all' permits
		if (permits.contains(PERMISSION_NONE)) {
			return false;
		}
		
		for (String p : ps) {
			if (permits.contains(p)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * add error to action
	 * @param ac the action context
	 * @param id msgId
	 */
	protected void addActionError(ActionContext ac, String id) {
		TextProvider tp = ac.getText();
		String msg = tp.getText(id);
		ac.getActionAware().addError(msg);
	}
	
	protected Object getSessionUser(ActionContext ac) {
		Object u = ac.getRequest().getAttribute(USER_ATTRIBUTE);
		if (u == null) {
			HttpSession session = ac.getRequest().getSession(false);
			if (session != null) {
				u = session.getAttribute(USER_ATTRIBUTE);
			}
		}
		return u;
	}
	
	protected boolean isSecureSessionUser(Object su) {
		return false;
	}
	
	protected List<String> getUserPermits(Object su) {
		return null;
	}

	protected void doView(ActionContext ac, String type) throws Throwable {
		View view = evalView(ac.getIoc(), type);
		if (view != null) {
			view.render(ac);
		}
	}
	
	@Override
	public void init(MvcConfig config, ActionInfo ai) throws Throwable {
		loadActionPermits();
	}

	public void process(ActionContext ac) throws Throwable {
		Object su = getSessionUser(ac);
		if (check < CHECK_LOGIN) {
			doNext(ac);
			return;
		}

		if (su == null) {
			addActionError(ac, ActionRC.ERROR_UNLOGIN);
			doView(ac, loginResult);
			return;
		}
		
		if (check == CHECK_SECURE && !isSecureSessionUser(su)) {
			addActionError(ac, ActionRC.ERROR_UNSECURE);
			doView(ac, secureResult);
			return;
		}

		String p = ac.getPath();
		if (hasPermission(getUserPermits(su), p)) {
			doNext(ac);
			return;
		}

		ac.getResponse().sendError(HttpServletResponse.SC_FORBIDDEN);
	}
}
