package panda.util;

import java.util.ArrayList;
import java.util.List;

/**
 * TreeNode
 *
 * @param <T> the value type
 * @author yf.frank.wang@gmail.com
 */
public class TreeNode<T> {
	private int level = 0;
	private TreeNode<T> parent;
	private List<TreeNode<T>> children;
	private T value;

	/**
	 * Constructor
	 */
	public TreeNode() {
		this(null, null);
	}

	/**
	 * Constructor
	 * @param parent parent
	 */
	public TreeNode(TreeNode<T> parent) {
		this(parent, null);
	}
	
	/**
	 * Constructor
	 * @param value value
	 */
	public TreeNode(T value) {
		this(null, value);
	}
	
	/**
	 * Constructor
	 * @param parent parent
	 * @param value value
	 */
	public TreeNode(TreeNode<T> parent, T value) {
		this.value = value;
		if (parent != null) {
			this.parent = parent;
			parent.addChild(this);
		}
	}
	
	/**
	 * @return the level
	 */
	public int getLevel() {
		return level;
	}

	/**
	 * @return the parent
	 */
	public TreeNode<T> getParent() {
		return parent;
	}

	/**
	 * @return the children
	 */
	public List<TreeNode<T>> getChildren() {
		if (children == null) {
			children = new ArrayList<TreeNode<T>>();
		}
		return children;
	}

	/**
	 * @return true if has child
	 */
	public boolean hasChild() {
		return children != null && !children.isEmpty();
	}
	
	/**
	 * @return the value
	 */
	public T getValue() {
		return value;
	}

	/**
	 * @param value the value to set
	 */
	public void setValue(T value) {
		this.value = value;
	}

	/**
	 * @return true if this is the root node of tree
	 */
	public boolean isRoot() {
		return this.parent == null;
	}
	
	/**
	 * @return the root node of tree 
	 */
	public TreeNode<T> getRoot() {
		TreeNode<T> n = this;
		while (n.parent != null) {
			n = n.parent;
		}
		return n;
	}

	/**
	 * @return true if this is a leaf node
	 */
	public boolean isLeaf() {
		return !hasChild();
	}
	
	/**
	 * add a child node
	 * @param node child node
	 */
	public void addChild(TreeNode<T> node) {
		if (node == null) {
			throw new IllegalArgumentException("child node can not be null.");
		}
		node.level = this.level + 1;
		getChildren().add(node);
	}

	/**
	 * create child
	 * @param value the value to set
	 * @return child node
	 */
	public TreeNode<T> createChild(T value) {
		return new TreeNode<T>(this, value);
	}
	
	/**
	 * remove the child from tree
	 * @param node child node
	 */
	public void removeChild(TreeNode<T> node) {
		getChildren().remove(node);
	}
	
	/**
	 * remove this from tree
	 */
	public void remove() {
		if (this.parent != null) {
			this.parent.removeChild(this);
			this.parent = null;
		}
	}

	/**
	 * traveler for traverse tree
	 *
	 * @param <T> value type
	 */
	public static interface Traveler<T> {
		/**
		 * visit 
		 * @param node node
		 * @return false to stop the traversal
		 */
		boolean visit(TreeNode<T> node); 
	}
	
	private void traverseTree(TreeNode<T> node, Traveler<T> traveler) {
		if (traveler.visit(node)) {
			if (node.hasChild()) {
				for (TreeNode<T> n : node.children) {
					traverseTree(n, traveler);
				}
			}
		}
	}

	/**
	 * traverse tree
	 * @param traveler traveler
	 */
	public void traverseTree(Traveler<T> traveler) {
		TreeNode<T> root = getRoot();
		traverseTree(root, traveler);
	}
	
	/**
	 * @return the leaf list
	 */
	public List<TreeNode<T>> getLeaves() {
		final List<TreeNode<T>> leaves = new ArrayList<TreeNode<T>>();
		
		traverseTree(new Traveler<T>() {
			public boolean visit(TreeNode<T> node) {
				if (node.isLeaf()) {
					leaves.add(node);
				}
				return true;
			}
		});
		
		return leaves;
	}

	/**
	 * find node by value, use equals to compare
	 * @param value the node value
	 * @return node
	 */
	public TreeNode<T> findNode(T value) {
		return findNode(value, true);
	}
	
	/**
	 * find node by value
	 * @param value the node value
	 * @param equals compare with equals method
	 * @return node
	 */
	public TreeNode<T> findNode(T value, boolean equals) {
		List<TreeNode<T>> nodes = findNodes(value, equals, false);
		return nodes.isEmpty() ? null : nodes.get(0);
	}

	/**
	 * find nodes by value, use equals to compare
	 * @param value the node value
	 * @return node list
	 */
	public List<TreeNode<T>> findNodes(T value) {
		return findNodes(value, true, true);
	}
	
	/**
	 * find nodes by value
	 * @param value the node value
	 * @param equals compare with equals method
	 * @return node list
	 */
	public List<TreeNode<T>> findNodes(T value, boolean equals) {
		return findNodes(value, equals, true);
	}
	
	/**
	 * find nodes by value
	 * @param value the node value
	 * @param equals compare with equals method
	 * @param all find all nodes with the same value or not
	 * @return node list
	 */
	private List<TreeNode<T>> findNodes(T value, boolean equals, boolean all) {
		List<TreeNode<T>> nodes = new ArrayList<TreeNode<T>>();
		
		traverseTree(new FindNodeTraveler<T>(nodes, value, equals, all));
		
		return nodes;
	}
	
	private static class FindNodeTraveler<T> implements Traveler<T> {
		List<TreeNode<T>> nodes;
		T value;
		boolean equals;
		boolean all;
		
		/**
		 * @param nodes node list
		 * @param value the node value
		 * @param equals compare with equals method
		 * @param all find all nodes with the same value or not
		 */
		public FindNodeTraveler(List<TreeNode<T>> nodes, T value, boolean equals, boolean all) {
			this.nodes = nodes;
			this.value = value;
			this.equals = equals;
			this.all = all;
		}

		public boolean visit(TreeNode<T> node) {
			if (node.value == value) {
				nodes.add(node);
				return all;
			}
			if (equals && node.value != null && value != null && node.value.equals(value)) {
				nodes.add(node);
				return all;
			}
			return true;
		}
	}
}
