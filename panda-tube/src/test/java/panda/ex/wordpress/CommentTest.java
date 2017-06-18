package panda.ex.wordpress;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import panda.ex.wordpress.bean.Comment;
import panda.ex.wordpress.bean.CommentCount;
import panda.ex.wordpress.bean.CommentStatusList;

public class CommentTest extends AbstractWordpressTest {

	@Test
	public void testGetCommentCount() throws Exception {
		CommentCount cc = WP.getCommentCount("1");

//		System.out.println(cc);
		Assert.assertNotNull(cc);
		Assert.assertEquals(new Integer(1), cc.total_comments);
	}

	@Test
	public void testGetComment() throws Exception {
		Comment c = WP.getComment(2);

//		System.out.println(c);
		Assert.assertNotNull(c);
		Assert.assertEquals("Nice work!", c.content);
	}

	@Test
	public void testGetComments() throws Exception {
		List<Comment> comments = WP.getComments();

//		System.out.println(comments);
		Assert.assertNotNull(comments);
		Assert.assertEquals(1, comments.size());
		Assert.assertEquals("Nice work!", comments.get(0).content);
	}

	@Test
	public void testEditComment() throws Exception {
		Comment c = new Comment();
		
		c.content = "New Comment !";
		c.author = "Test Author";
		c.author_email = "test@test.com";
		c.author_url = "http://author.test.com";
		
		int cid = WP.newComment(1, c);
		
		Assert.assertTrue(cid > 0);

		c.content = "Edit Comment !";
		boolean re = WP.editComment(c);
		Assert.assertTrue(re);

		boolean rd = WP.deleteComment(cid);
		Assert.assertTrue(rd);
	}

	@Test
	public void testGetCommentStatusList() throws Exception {
		CommentStatusList csl = WP.getCommentStatusList();

//		System.out.println(csl);
		Assert.assertNotNull(csl);
		Assert.assertEquals("Spam", csl.spam);
	}
}
