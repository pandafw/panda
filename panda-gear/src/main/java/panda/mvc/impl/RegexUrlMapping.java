package panda.mvc.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import panda.ioc.annotation.IocBean;
import panda.lang.Strings;
import panda.mvc.UrlMapping;

/**
 * Regular expression url mapping
 * 
 * The regular expression must end with $.
 *
 */
@IocBean(type=UrlMapping.class)
public class RegexUrlMapping extends AbstractUrlMapping implements UrlMapping {
	private Map<String, ActionInvoker> urlmap;
	private List<Pattern> patterns;
	private List<ActionInvoker> actionis;

	public RegexUrlMapping() {
		urlmap = new HashMap<String, ActionInvoker>();
		patterns = new ArrayList<Pattern>();
		actionis = new ArrayList<ActionInvoker>();
	}

	@Override
	protected void addInvoker(String path, ActionInvoker invoker) {
		if (Strings.endsWithChar(path, '$')) {
			Pattern pattern = Pattern.compile(path);
			patterns.add(pattern);
			actionis.add(invoker);
		}
		else {
			urlmap.put(path, invoker);
		}
	}
	
	@Override
	protected ActionInvoker getInvoker(String path, List<String> args) {
		ActionInvoker ai = urlmap.get(path);
		if (ai != null) {
			return ai;
		}
		
		for (int i = 0; i < patterns.size(); i++) {
			Pattern p = patterns.get(i);
			Matcher m = p.matcher(path);
			if (m.matches()) {
				if (args != null) {
					m.reset();
					while (m.find()) {
						for (int a = 1; a <= m.groupCount(); a++) {
							args.add(m.group(a));
						}
					}
				}
				return actionis.get(i);
			}
		}

		return null;
	}
}
