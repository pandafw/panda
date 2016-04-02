package panda.mvc.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
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
	private Map<String, ActionInvoker> plains;
	private Map<Pattern, ActionInvoker> regexs;

	public RegexUrlMapping() {
		plains = new HashMap<String, ActionInvoker>();
		regexs = new HashMap<Pattern, ActionInvoker>();
	}

	@Override
	protected void addInvoker(String path, ActionInvoker invoker) {
		if (Strings.endsWithChar(path, '$')) {
			Pattern regx = Pattern.compile(path);
			regexs.put(regx, invoker);
		}
		else {
			plains.put(path, invoker);
		}
	}
	
	@Override
	protected ActionInvoker getInvoker(String path, List<String> args) {
		ActionInvoker ai = plains.get(path);
		if (ai != null) {
			return ai;
		}
		
		for (Entry<Pattern, ActionInvoker> en : regexs.entrySet()) {
			Pattern p = en.getKey();
			Matcher m = p.matcher(path);
			if (m.matches()) {
				if (args != null) {
					m.reset();
					while (m.find()) {
						for (int i = 1; i <= m.groupCount(); i++) {
							args.add(m.group(i));
						}
					}
				}
				return en.getValue();
			}
		}

		return null;
	}
}
