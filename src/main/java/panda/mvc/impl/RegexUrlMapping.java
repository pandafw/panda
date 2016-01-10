package panda.mvc.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import panda.ioc.annotation.IocBean;
import panda.mvc.UrlMapping;

@IocBean(type=UrlMapping.class)
public class RegexUrlMapping extends AbstractUrlMapping implements UrlMapping {
	private Map<Pattern, ActionInvoker> map;
	
	public RegexUrlMapping() {
		map = new HashMap<Pattern, ActionInvoker>();
	}

	@Override
	protected void addInvoker(String path, ActionInvoker invoker) {
		Pattern regx = Pattern.compile(path);
		map.put(regx, invoker);
	}
	
	@Override
	protected ActionInvoker getInvoker(String path, List<String> args) {
		for (Entry<Pattern, ActionInvoker> en : map.entrySet()) {
			Pattern p = en.getKey();
			Matcher m = p.matcher(path);
			if (m.matches()) {
				if (args != null) {
					m.reset();
					while(m.find()) {
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