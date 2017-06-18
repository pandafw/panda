package panda.ex.wordpress;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import panda.bean.Beans;
import panda.cast.Castors;
import panda.ex.wordpress.bean.Blog;
import panda.ex.wordpress.bean.Category;
import panda.ex.wordpress.bean.Comment;
import panda.ex.wordpress.bean.CommentCount;
import panda.ex.wordpress.bean.CommentFilter;
import panda.ex.wordpress.bean.CommentStatusList;
import panda.ex.wordpress.bean.MediaFile;
import panda.ex.wordpress.bean.MediaFilter;
import panda.ex.wordpress.bean.MediaItem;
import panda.ex.wordpress.bean.MediaObject;
import panda.ex.wordpress.bean.Option;
import panda.ex.wordpress.bean.Post;
import panda.ex.wordpress.bean.PostFilter;
import panda.ex.wordpress.bean.PostType;
import panda.ex.wordpress.bean.Profile;
import panda.ex.wordpress.bean.Tag;
import panda.ex.wordpress.bean.Taxonomy;
import panda.ex.wordpress.bean.Term;
import panda.ex.wordpress.bean.TermFilter;
import panda.ex.wordpress.bean.User;
import panda.lang.Collections;
import panda.lang.reflect.Types;
import panda.net.xmlrpc.XmlRpcClient;
import panda.net.xmlrpc.XmlRpcFaultException;




/**
 * The utility class that links xmlrpc calls to Java functions.
 */
public class Wordpress {

	private String username;

	private String password;

	private XmlRpcClient client;

	/**
	 * @param username User name
	 * @param password Password
	 * @param xmlRpcUrl xmlrpc communication point, usually blogurl/xmlrpc.php
	 * @throws MalformedURLException If the URL is faulty
	 */
	public Wordpress(String username, String password, String xmlRpcUrl) throws MalformedURLException {
		this.username = username;
		this.password = password;
		this.client = new XmlRpcClient(xmlRpcUrl);
	}

	/**
	 * for jdk6 compiler error (type parameters of <T>T cannot be determined)
	 */
	protected boolean callb(String method, Object... params) throws XmlRpcFaultException, IOException {
		Boolean b = call(method, Boolean.class, params);
		return b == null ? false : b.booleanValue();
	}

	/**
	 * for jdk6 compiler error (type parameters of <T>T cannot be determined)
	 */
	protected int calli(String method, Object... params) throws XmlRpcFaultException, IOException {
		Integer i = call(method, Integer.class, params);
		return i == null ? 0 : i.intValue();
	}
	
	protected <T> T call(String method, Type type, Object... params) throws XmlRpcFaultException, IOException {
		List<Object> ress = client.call(method, List.class, params);
		
		Object o = null;
		if (Collections.isNotEmpty(ress)) {
			o = ress.get(0);
		}
		
		return Castors.scast(o, type);
	}
	
	//--------------------------------------------------------
	// Posts
	//
	public Post getPost(int post_id) throws XmlRpcFaultException, IOException {
		return getPost(0, post_id);
	}

	public Post getPost(int blogid, int post_id) throws XmlRpcFaultException, IOException {
		return call("wp.getPost", Post.class, blogid, username, password, post_id);
	}

	public List<Post> getPosts() throws XmlRpcFaultException, IOException {
		return getPosts(0, null);
	}

	public List<Post> getPosts(PostFilter filter) throws XmlRpcFaultException, IOException {
		return getPosts(0, filter);
	}

	public List<Post> getPosts(int blogid, PostFilter filter) throws XmlRpcFaultException, IOException {
		return call("wp.getPosts", Types.paramTypeOf(List.class, Post.class), blogid, username, password, filter);
	}

	public String newPost(Post post) throws XmlRpcFaultException, IOException {
		return newPost(0, post);
	}
	
	public String newPost(int blogid, Post post) throws XmlRpcFaultException, IOException {
		Map<String, Object> params = new HashMap<String, Object>();

		Beans.i().copyNotNullProperties(params, post, 
			"post_type", "post_status", "post_title", "post_author", "post_excerpt",
			"post_content", "post_date_gmt", "post_format", "post_name", "post_password", 
			"comment_status", "ping_status", "post_parent", "custom_fields", "terms_names", "enclosure");
		
		if (post.post_thumbnail_id != null) {
			params.put("post_thumbnail", post.post_thumbnail_id);
		}

		String pid = call("wp.newPost", String.class, blogid, username, password, params);
		
		post.post_id = pid;
		return pid;
	}


	public boolean editPost(Post post) throws XmlRpcFaultException, IOException {
		return editPost(0, post);
	}
	
	public boolean editPost(int blogid, Post post) throws XmlRpcFaultException, IOException {
		Map<String, Object> params = new HashMap<String, Object>();

		Beans.i().copyNotNullProperties(params, post, 
			"post_type", "post_status", "post_title", "post_author", "post_excerpt",
			"post_content", "post_date_gmt", "post_format", "post_name", "post_password", 
			"comment_status", "ping_status", "post_parent", "custom_fields", "terms_names", "enclosure");
		
		if (post.post_thumbnail_id != null) {
			params.put("post_thumbnail", post.post_thumbnail_id);
		}
		if (post.terms_ids != null) {
			params.put("terms",post.terms_ids);
		}

		return callb("wp.editPost", blogid, username, password, post.post_id, params);
	}

	public boolean deletePost(String post_id) throws XmlRpcFaultException, IOException {
		return deletePost(0, post_id);
	}
	
	public boolean deletePost(int blogid, String post_id) throws XmlRpcFaultException, IOException {
		return callb("wp.deletePost", blogid, username, password, post_id);
	}

	public PostType getPostType(String post_type_name) throws XmlRpcFaultException, IOException {
		return getPostType(0, post_type_name);
	}

	public PostType getPostType(int blogid, String post_type_name) throws XmlRpcFaultException, IOException {
		return call("wp.getPostType", PostType.class, blogid, username, password, post_type_name);
	}

	public Map<String, PostType> getPostTypes() throws XmlRpcFaultException, IOException {
		return getPostTypes(0);
	}

	public Map<String, PostType> getPostTypes(int blogid) throws XmlRpcFaultException, IOException {
		return call("wp.getPostTypes", Types.paramTypeOf(Map.class, String.class, PostType.class), blogid, username, password);
	}

	public Map<String, String> getPostFormats() throws XmlRpcFaultException, IOException {
		return getPostFormats(0);
	}

	public Map<String, String> getPostFormats(int blogid) throws XmlRpcFaultException, IOException {
		return call("wp.getPostFormats", Types.paramTypeOf(Map.class, String.class, String.class), blogid, username, password);
	}

	public Map<String, String> getPostStatusList() throws XmlRpcFaultException, IOException {
		return getPostStatusList(0);
	}

	public Map<String, String> getPostStatusList(int blogid) throws XmlRpcFaultException, IOException {
		return call("wp.getPostStatusList", Types.paramTypeOf(Map.class, String.class, String.class), blogid, username, password);
	}
	//--------------------------------------------------------
	// Taxonomies
	//
	public Taxonomy getTaxonomy(String taxonomy) throws XmlRpcFaultException, IOException {
		return getTaxonomy(0, taxonomy);
	}

	public Taxonomy getTaxonomy(int blogid, String taxonomy) throws XmlRpcFaultException, IOException {
		return call("wp.getTaxonomy", Taxonomy.class, blogid, username, password, taxonomy);
	}

	public List<Taxonomy> getTaxonomies() throws XmlRpcFaultException, IOException {
		return getTaxonomies(0);
	}

	public List<Taxonomy> getTaxonomies(int blogid) throws XmlRpcFaultException, IOException {
		return call("wp.getTaxonomies", Types.paramTypeOf(List.class, Taxonomy.class), blogid, username, password);
	}

	public Term getTerm(String taxonomy, int term_id) throws XmlRpcFaultException, IOException {
		return getTerm(0, taxonomy, term_id);
	}

	public Term getTerm(int blogid, String taxonomy, int term_id) throws XmlRpcFaultException, IOException {
		return call("wp.getTerm", Term.class, blogid, username, password, taxonomy, term_id);
	}

	public List<Term> getTerms(String taxonomy) throws XmlRpcFaultException, IOException {
		return getTerms(0, taxonomy);
	}

	public List<Term> getTerms(int blogid, String taxonomy) throws XmlRpcFaultException, IOException {
		return getTerms(0, taxonomy, null);
	}

	public List<Term> getTerms(int blogid, String taxonomy, TermFilter filter) throws XmlRpcFaultException, IOException {
		return call("wp.getTerms", Types.paramTypeOf(List.class, Term.class), blogid, username, password, taxonomy, filter);
	}

	public String newTerm(Term term) throws XmlRpcFaultException, IOException {
		return newTerm(0, term);
	}
	
	public String newTerm(int blogid, Term term) throws XmlRpcFaultException, IOException {
		Map<String, Object> params = new HashMap<String, Object>();
		
		Beans.i().copyNotNullProperties(params, term, "name", "taxonomy", "slug", "description", "parent");

		String tid = call("wp.newTerm", String.class, blogid, username, password, params);
		
		term.term_id = tid;

		return tid;
	}

	public boolean editTerm(Term term) throws XmlRpcFaultException, IOException {
		return editTerm(0, term);
	}
	
	public boolean editTerm(int blogid, Term term) throws XmlRpcFaultException, IOException {
		Map<String, Object> params = new HashMap<String, Object>();
		
		Beans.i().copyNotNullProperties(params, term, "name", "taxonomy", "slug", "description", "parent");

		return callb("wp.editTerm", blogid, username, password, term.term_id, params);
	}

	public boolean deleteTerm(String taxonomy, String term_id) throws XmlRpcFaultException, IOException {
		return deleteTerm(0, taxonomy, term_id);
	}

	public boolean deleteTerm(int blogid, String taxonomy, String term_id) throws XmlRpcFaultException, IOException {
		return callb("wp.deleteTerm", blogid, username, password, taxonomy, term_id);
	}

	//--------------------------------------------------------
	// Media
	//
	public MediaItem getMediaItem(int attachment_id) throws XmlRpcFaultException, IOException {
		return getMediaItem(0, attachment_id);
	}

	public MediaItem getMediaItem(int blogid, int attachment_id) throws XmlRpcFaultException, IOException {
		return call("wp.getMediaItem", MediaItem.class, blogid, username, password, attachment_id);
	}

	public List<MediaItem> getMediaLibrary() throws XmlRpcFaultException, IOException {
		return getMediaLibrary(0, null);
	}

	public List<MediaItem> getMediaLibrary(MediaFilter filter) throws XmlRpcFaultException, IOException {
		return getMediaLibrary(0, filter);
	}

	public List<MediaItem> getMediaLibrary(int blogid, MediaFilter filter) throws XmlRpcFaultException, IOException {
		return call("wp.getMediaLibrary", Types.paramTypeOf(List.class, MediaItem.class), blogid, username, password, filter);
	}

	public MediaObject uploadFile(MediaFile file) throws XmlRpcFaultException, IOException {
		return uploadFile(0, file);
	}

	public MediaObject uploadFile(int blogid, MediaFile file) throws XmlRpcFaultException, IOException {
		return call("wp.uploadFile", MediaObject.class, blogid, username, password, file);
	}
	
	//--------------------------------------------------------
	// Comments
	//
	public CommentCount getCommentCount(String post_id) throws XmlRpcFaultException, IOException {
		return getCommentCount(0, post_id);
	}
	
	public CommentCount getCommentCount(int blogid, String post_id) throws XmlRpcFaultException, IOException {
		return call("wp.getCommentCount", CommentCount.class, blogid, username, password, post_id);
	}
	
	public Comment getComment(int comment_id) throws XmlRpcFaultException, IOException {
		return getComment(0, comment_id);
	}

	public Comment getComment(int blogid, int comment_id) throws XmlRpcFaultException, IOException {
		return call("wp.getComment", Comment.class, blogid, username, password, comment_id);
	}

	public List<Comment> getComments() throws XmlRpcFaultException, IOException {
		return getComments(null);
	}

	public List<Comment> getComments(CommentFilter filter) throws XmlRpcFaultException, IOException {
		return getComments(0, filter);
	}

	public List<Comment> getComments(int blogid, CommentFilter filter) throws XmlRpcFaultException, IOException {
		return call("wp.getComments", Types.paramTypeOf(List.class, Comment.class), blogid, username, password, filter);
	}

	public int newComment(int post_id, Comment comment) throws XmlRpcFaultException, IOException {
		return newComment(0, post_id, comment);
	}
	
	public int newComment(int blogid, int post_id, Comment comment) throws XmlRpcFaultException, IOException {
		Map<String, Object> cm = new HashMap<String, Object>();
		
		Beans.i().copyNotNullProperties(cm, comment, "comment_parent", "content", "author", "author_url", "author_email");

		int cid = calli("wp.newComment", blogid, username, password, post_id, cm);
		
		comment.comment_id = String.valueOf(cid);

		return cid;
	}

	public boolean editComment(Comment comment) throws XmlRpcFaultException, IOException {
		return editComment(0, comment);
	}
	
	public boolean editComment(int blogid, Comment comment) throws XmlRpcFaultException, IOException {
		Map<String, Object> cm = new HashMap<String, Object>();
		
		Beans.i().copyNotNullProperties(cm, comment, "status", "date_created_gmt", "content", "author", "author_url", "author_email");

		return callb("wp.editComment", blogid, username, password, comment.comment_id, cm);
	}

	public boolean deleteComment(int comment_id) throws XmlRpcFaultException, IOException {
		return deleteComment(0, comment_id);
	}

	public boolean deleteComment(int blogid, int comment_id) throws XmlRpcFaultException, IOException {
		return callb("wp.deleteComment", blogid, username, password, comment_id);
	}

	public CommentStatusList getCommentStatusList() throws XmlRpcFaultException, IOException {
		return getCommentStatusList(0);
	}

	public CommentStatusList getCommentStatusList(int blogid) throws XmlRpcFaultException, IOException {
		return call("wp.getCommentStatusList", CommentStatusList.class, blogid, username, password);
	}

	//--------------------------------------------------------
	// Options
	//
	public Map<String, Option> getOptions(String... options) throws XmlRpcFaultException, IOException {
		return getOptions(0, options);
	}

	public Map<String, Option> getOptions(int blogid, String... options) throws XmlRpcFaultException, IOException {
		return call("wp.getOptions", Types.paramTypeOf(Map.class, String.class, Option.class), blogid, username, password, options);
	}
	
	public Map<String, Option> setOptions(Map<String, Option> options) throws XmlRpcFaultException, IOException {
		return setOptions(0, options);
	}
	
	public Map<String, Option> setOptions(int blogid, Map<String, Option> options) throws XmlRpcFaultException, IOException {
		return call("wp.setOptions", Types.paramTypeOf(Map.class, String.class, Option.class), blogid, username, password, options);
	}
	
	//--------------------------------------------------------
	// Users
	//
	public List<Blog> getUsersBlogs() throws XmlRpcFaultException, IOException {
		return call("wp.getUsersBlogs", Types.paramTypeOf(List.class, Blog.class), username, password);
	}

	public User getUser(int user_id) throws XmlRpcFaultException, IOException {
		return getUser(0, user_id);
	}
	
	public User getUser(int blogid, int user_id) throws XmlRpcFaultException, IOException {
		return call("wp.getUser", User.class, blogid, username, password, user_id);
	}

	public List<User> getUsers() throws XmlRpcFaultException, IOException {
		return getUsers(0);
	}
	
	public List<User> getUsers(int blogid) throws XmlRpcFaultException, IOException {
		return call("wp.getUsers", Types.paramTypeOf(List.class, User.class), blogid, username, password);
	}

	public User getProfile() throws XmlRpcFaultException, IOException {
		return getProfile(0);
	}
	
	public User getProfile(int blogid) throws XmlRpcFaultException, IOException {
		return call("wp.getProfile", User.class, blogid, username, password);
	}

	public boolean editProfile(Profile profile) throws XmlRpcFaultException, IOException {
		return editProfile(0, profile);
	}
	
	public boolean editProfile(int blogid, Profile profile) throws XmlRpcFaultException, IOException {
		return callb("wp.editProfile", blogid, username, password, profile);
	}
	
	public List<User> getAuthors() throws XmlRpcFaultException, IOException {
		return getAuthors(0);
	}
	
	public List<User> getAuthors(int blogid) throws XmlRpcFaultException, IOException {
		return call("wp.getAuthors", Types.paramTypeOf(List.class, User.class), blogid, username, password);
	}

	//--------------------------------------------------------
	// Obsolete
	//

	//--------------------------------------------------------
	// Categories
	//
	public List<Category> getCategories() throws XmlRpcFaultException, IOException {
		return getCategories(0);
	}

	public List<Category> getCategories(int blogid) throws XmlRpcFaultException, IOException {
		return call("wp.getCategories", Types.paramTypeOf(List.class, Category.class), blogid, username, password);
	}

	public List<Category> suggestCategories(String prefix) throws XmlRpcFaultException, IOException {
		return suggestCategories(0, prefix, Integer.MAX_VALUE);
	}

	public List<Category> suggestCategories(int blogid, String prefix, int max_results) throws XmlRpcFaultException, IOException {
		return call("wp.suggestCategories", Types.paramTypeOf(List.class, Category.class), blogid, username, password, prefix, max_results);
	}

	public int newCategory(Category category) throws XmlRpcFaultException, IOException {
		return newCategory(0, category);
	}
	
	public int newCategory(int blogid, Category category) throws XmlRpcFaultException, IOException {
		Map<String, Object> c = new HashMap<String, Object>();
		c.put("name", category.categoryName);
		c.put("description", category.categoryDescription);
		c.put("parent_id", category.parentId);
		c.put("slug", category.slug);
		
		int id = calli("wp.newCategory", blogid, username, password, c);
		category.categoryId = String.valueOf(id);
		
		return id;
	}

	public boolean deleteCategory(int category_id) throws XmlRpcFaultException, IOException {
		return deleteCategory(0, category_id);
	}
	
	public boolean deleteCategory(int blogid, int category_id) throws XmlRpcFaultException, IOException {
		return callb("wp.deleteCategory", blogid, username, password, category_id);
	}


	//--------------------------------------------------------
	// Tags
	//
	public List<Tag> getTags() throws XmlRpcFaultException, IOException {
		return getTags(0);
	}

	public List<Tag> getTags(int blogid) throws XmlRpcFaultException, IOException {
		return call("wp.getTags", Types.paramTypeOf(List.class, Tag.class), blogid, username, password);
	}

	//--------------------------------------------------------
	// Pages
	//
//	XmlRpcStruct getPage(Integer blogid, Integer pageid, String username, String password) throws XmlRpcFaultException;
//
//	XmlRpcArray getPages(Integer blogid, String username, String password) throws XmlRpcFaultException;
//
//	XmlRpcArray getPageList(Integer blogid, String username, String password) throws XmlRpcFaultException;
//
//	String newPage(Integer blogid, String username, String password, XmlRpcStruct post, String publish)
//			throws XmlRpcFaultException;
//
//	Boolean editPage(Integer blogid, Integer post_ID, String username, String password, XmlRpcStruct post,
//			String publish) throws XmlRpcFaultException;
//
//	Boolean deletePage(Integer blogid, String username, String password, Integer post_ID, String publish)
//			throws XmlRpcFaultException;
//
//	XmlRpcStruct getPageStatusList(Integer blogid, String username, String password) throws XmlRpcFaultException;

}

