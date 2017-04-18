package panda.wordpress;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

import panda.lang.Arrays;
import panda.lang.Collections;
import panda.wordpress.bean.Post;
import panda.wordpress.bean.PostType;
import panda.wordpress.bean.Taxonomy;

public class PostTest extends AbstractWordpressTest {

	@Test
	public void testGetPost() throws Exception {
		Post p = WP.getPost(1);

//		System.out.println(p);
		Assert.assertNotNull(p);
		Assert.assertEquals("hello-world", p.post_name);
	}

	@Test
	public void testGetPosts() throws Exception {
		List<Post> ps = WP.getPosts();

//		System.out.println(ps);
		Assert.assertNotNull(ps);
		Assert.assertEquals(2, ps.size());

		List<String> ecs = Arrays.toList( "Hello world!", "あなたは何位？" );
		List<String> acs = new ArrayList<String>();
		for (Post p : ps) {
			acs.add(p.post_title);
		}
		Collections.sort(ecs);
		Collections.sort(acs);
		Assert.assertEquals(ecs, acs);
	}

	@Test
	public void testEditPosts() throws Exception {
		Post post = new Post();
		
		post.post_author = "1";
		post.post_status = Post.POST_STATUS_DRAFT;
		post.post_title = "New Post";
		post.post_content = "New Post Content";

		String pid = WP.newPost(post);
		
		Assert.assertNotNull(pid);
		
		post.post_title = "Edit Post";
		post.post_content = "Edit Post Content";
		post.post_status = Post.POST_STATUS_PUBLISH;
		post.terms_names = new HashMap<String, List<String>>();
		post.terms_names.put(Taxonomy.CATEGORY, Arrays.asList("Anime"));
		post.terms_names.put(Taxonomy.POST_TAG, Arrays.asList("top"));
		boolean r = WP.editPost(post);
		Assert.assertTrue(r);
		
		r = WP.deletePost(pid);
		Assert.assertTrue(r);
	}

	@Test
	public void testGetPostType() throws Exception {
		PostType p = WP.getPostType(Post.POST_TYPE_POST);

//		System.out.println(p);
		Assert.assertNotNull(p);
		Assert.assertEquals("Posts", p.label);
	}

	@Test
	public void testGetPostTypes() throws Exception {
		Map<String, PostType> ps = WP.getPostTypes();

//		System.out.println(ps);
		
		Assert.assertNotNull(ps);
		List<String> ecs = Arrays.toList(Post.POST_TYPE_POST, Post.POST_TYPE_PAGE, Post.POST_TYPE_ATTACHMENT );
		List<String> acs = new ArrayList<String>(ps.keySet());
		Collections.sort(ecs);
		Collections.sort(acs);
		Assert.assertEquals(ecs, acs);
	}

	@Test
	public void testGetPostFormats() throws Exception {
		Map<String, String> ps = WP.getPostFormats();

		System.out.println(ps);
		
		Assert.assertNotNull(ps);
		Assert.assertEquals("Standard", ps.get("standard"));
	}

	@Test
	public void testGetPostStatusList() throws Exception {
		Map<String, String> ps = WP.getPostStatusList();

		System.out.println(ps);
		
		Assert.assertNotNull(ps);
		Assert.assertEquals("Draft", ps.get("draft"));
	}
}
