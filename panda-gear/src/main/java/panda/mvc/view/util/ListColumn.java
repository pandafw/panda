package panda.mvc.view.util;

import java.util.ArrayList;
import java.util.List;

import panda.lang.Arrays;
import panda.lang.Collections;
import panda.mvc.view.tag.ui.ListView.ItemLink;


public class ListColumn {
	public static class Format {
		public String type;
		public String expr;
		public Object codemap;
		public String pattern;
		public String escape;
	}

	public String name;
	public String type;
	public String header;
	public String tooltip;
	public String cssClass;

	public boolean display = true;
	public boolean sortable = false;

	public boolean fixed;
	public boolean group;
	public boolean pkey;
	public boolean value;
	public boolean hidden;

	public Format format;
	public ListFilter filter;
	
	public Object link;
	public List<ItemLink> actions;

	public ListColumn() {
	}

	public ListColumn(String name) {
		this.name = name;
	}
	
	@SuppressWarnings("unchecked")
	public static List<ListColumn> toColumns(String...names) {
		if (Arrays.isEmpty(names)) {
			return Collections.EMPTY_LIST;
		}
		
		List<ListColumn> cs = new ArrayList<ListColumn>();
		for (String s : names) {
			cs.add(new ListColumn(s));
		}
		return cs;
	}
}
