package panda.mvc.view.tag;

import java.util.List;
import java.util.Map;


public class ListColumn {
	public static class Format {
		public String type;
		public String expression;
		public Object codemap;
		public String escape;
	}
	
	public static class Filter {
		public String type;
		public boolean fixed;
		public String label;
		public String tooltip;
		public boolean display = true;
		public Object list;
	}

	public static class Action {
		public String href;
		public String action;
		public String icon;
		public String label;
		public String tooltip;
		public String target;
		public String onclick;
		public Map<String, Object> params;
	}

	public String name;
	public String type;
	public String header;
	public int width;
	public String tooltip;
	public boolean hidden;
	public boolean display = true;

	public boolean fixed;
	public boolean nowrap;
	public boolean group;
	public boolean pkey;
	public boolean enabled;
	public boolean value = true;

	public Format format;
	public Filter filter;
	
	public Object link;
	public List<Action> actions;
	
	public boolean sortable = false;
	public boolean filterable = true;
}
