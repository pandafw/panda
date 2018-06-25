package panda.ex.wordpress;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import panda.lang.Arrays;
import panda.lang.Randoms;

public class UserTest extends AbstractWordpressTest {

	@Test
	public void testGetUsersBlogs() throws Exception {
		List<Blog> result = WP.getUsersBlogs();

//		System.out.print(result);
		Assert.assertNotNull(result);
		Assert.assertEquals(1, result.size());
		Assert.assertEquals("WordPress", result.get(0).blogName);
	}

	@Test
	public void testGetUser() throws Exception {
		User result = WP.getUser(1);

//		System.out.print(result);
		Assert.assertNotNull(result);
		Assert.assertEquals("admin", result.username);
		Assert.assertEquals(Arrays.asList(new String[] { "administrator" }), result.roles);
	}
	
	@Test
	public void testGetUsers() throws Exception {
		List<User> result = WP.getUsers();

//		System.out.print(result);
		Assert.assertNotNull(result);
		Assert.assertEquals(1, result.size());
		Assert.assertEquals("admin", result.get(0).display_name);
	}

	@Test
	public void testGetProfile() throws Exception {
		User result = WP.getProfile();

//		System.out.print(result);
		Assert.assertNotNull(result);
		Assert.assertEquals("admin", result.username);
		Assert.assertEquals(Arrays.asList(new String[] { "administrator" }), result.roles);
	}

	@Test
	public void testEditProfile() throws Exception {
		User user = WP.getProfile();

//		System.out.print(result);
		Assert.assertNotNull(user);
		Assert.assertEquals("admin", user.username);
		
		String url = "http://www.foolite.com/" + Randoms.randInt();
		
		user.url = url;
		boolean r = WP.editProfile(user);
		Assert.assertTrue(r);

		user = WP.getProfile();

//		System.out.print(result);
		Assert.assertNotNull(user);
		Assert.assertEquals("admin", user.username);
		Assert.assertEquals(url, user.url);
	}

	@Test
	public void testGetAuthors() throws Exception {
		List<User> result = WP.getAuthors();

//		System.out.print(result);
		Assert.assertNotNull(result);
		Assert.assertEquals(1, result.size());
		Assert.assertEquals("admin", result.get(0).display_name);
	}
}
