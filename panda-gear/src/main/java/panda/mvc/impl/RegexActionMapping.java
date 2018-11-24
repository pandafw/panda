package panda.mvc.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import panda.ioc.annotation.IocBean;
import panda.lang.Strings;
import panda.lang.collection.CaseInsensitiveMap;
import panda.mvc.ActionMapping;

/**
 * Regular expression action mapping
 * 
 * The regular expression must end with $.
 *
 */
@IocBean(type=ActionMapping.class, create="initialize")
public class RegexActionMapping extends AbstractActionMapping {
	private Map<String, ActionDispatcher> urlmap;
	private List<Pattern> patterns;
	private List<ActionDispatcher> dispatchers;

	@Override
	public void initialize() {
		super.initialize();
		
		urlmap = caseSensitive ? new HashMap<String, ActionDispatcher>() : new CaseInsensitiveMap<String, ActionDispatcher>();
		patterns = new ArrayList<Pattern>();
		dispatchers = new ArrayList<ActionDispatcher>();
	}

	@Override
	protected void addDispatcher(String path, ActionDispatcher dispatcher) {
		if (Strings.endsWithChar(path, '$')) {
			Pattern pattern = caseSensitive ? Pattern.compile(path) : Pattern.compile(path, Pattern.CASE_INSENSITIVE);
			patterns.add(pattern);
			dispatchers.add(dispatcher);
		}
		else {
			urlmap.put(path, dispatcher);
		}
	}
	
	@Override
	protected ActionDispatcher getDispatcher(String path, List<String> args) {
		ActionDispatcher ai = urlmap.get(path);
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
				return dispatchers.get(i);
			}
		}

		return null;
	}
}
