package panda.mvc.impl;

import java.util.List;

import panda.mvc.UrlMapping;

public class NodeUrlMapping extends AbstractUrlMapping implements UrlMapping {
	private MappingNode<ActionInvoker> root;
	
	public NodeUrlMapping() {
		root = new MappingNode<ActionInvoker>();
	}

	@Override
	protected void addInvoker(String path, ActionInvoker invoker) {
		root.add(path, invoker);
	}
	
	@Override
	protected ActionInvoker getInvoker(String path, List<String> args) {
		return root.get(path, args);
	}
}
