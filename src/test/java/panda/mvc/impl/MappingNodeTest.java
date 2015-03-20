package panda.mvc.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class MappingNodeTest {

	@Test
	public void test_simple_mapping() {
		MappingNode<String> root = new MappingNode<String>();
		root.add("/a/b/c", "A");
		root.add("/a/c/", "B");
		root.add("/a/f", "C");
		root.add("/a", "D");

		List<String> args = new ArrayList<String>();
		assertEquals("A", root.get("/a/b/c", args));

		assertEquals("B", root.get("/a/c", args));

		assertEquals("C", root.get("/a/f/", args));

		assertEquals("D", root.get("/a/", args));

		assertNull(root.get("/a/x", args));
	}

	@Test
	public void test_single_path_arg() {
		MappingNode<String> root = new MappingNode<String>();
		root.add("/a/?/c", "A");

		List<String> args = new ArrayList<String>();
		assertEquals("A", root.get("/a/b/c", args));
		assertEquals(1, args.size());
		assertEquals("b", args.get(0));
	}

	@Test
	public void test_multi_path_arg() {
		MappingNode<String> root = new MappingNode<String>();
		root.add("/a/*", "A");

		List<String> args = new ArrayList<String>();
		assertEquals("A", root.get("/a", args));
		assertEquals(0, args.size());

		assertEquals("A", root.get("/a/b/c", args));
		assertEquals(2, args.size());
		assertEquals("b", args.get(0));
		assertEquals("c", args.get(1));
	}

	@Test
	public void test_single_and_multi_path_arg() {
		MappingNode<String> root = new MappingNode<String>();
		root.add("/a/?/c/*", "A");

		List<String> args = new ArrayList<String>();

		assertEquals("A", root.get("/a/b/c", args));
		assertEquals(1, args.size());
		assertEquals("b", args.get(0));

		assertEquals("A", root.get("/a/b/c/d", args));
		assertEquals(2, args.size());
		assertEquals("b", args.get(0));
		assertEquals("d", args.get(1));

		assertEquals("A", root.get("/a/b/c/d/e", args));
		assertEquals(3, args.size());
		assertEquals("b", args.get(0));
		assertEquals("d", args.get(1));
		assertEquals("e", args.get(2));

	}

}
