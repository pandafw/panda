package panda.tube.wordpress;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import panda.bean.Beans;
import panda.cast.Castors;
import panda.lang.Collections;
import panda.lang.reflect.Types;
import panda.net.xmlrpc.XmlRpcClient;
import panda.net.xmlrpc.XmlRpcFaultException;



/**
 * The utility class that links xmlrpc calls to Java functions.
 */
public class WordPress {

	private XmlRpcClient client;
	private int blogId;
	private String username;
	private String password;

	/**
	 * @param username User name
	 * @param password Password
	 * @param xmlRpcUrl xmlrpc communication point, usually blogurl/xmlrpc.php
	 */
	public WordPress(String xmlRpcUrl, String username, String password) {
		this(xmlRpcUrl, username, password, 0);
	}

	/**
	 * @param blogId the blog id
	 * @param username User name
	 * @param password Password
	 * @param xmlRpcUrl xmlrpc communication point, usually blogurl/xmlrpc.php
	 */
	public WordPress(String xmlRpcUrl, String username, String password, int blogId) {
		this.client = new XmlRpcClient(xmlRpcUrl);
		this.username = username;
		this.password = password;
	}

	/**
	 * @return the blogId
	 */
	public int getBlogId() {
		return blogId;
	}

	/**
	 * @param blogId the blogId to set
	 */
	public void setBlogId(int blogId) {
		this.blogId = blogId;
	}

	/**
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * @param username the username to set
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @param password the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * for jdk6 compiler error (type parameters of <T>T cannot be determined)
	 * @param method wordpress method
	 * @param params parameters
	 * @return result
	 * @throws XmlRpcFaultException if a XmlRpcFault error occurred
	 * @throws IOException if an IO error occurred
	 */
	protected boolean callb(String method, Object... params) throws XmlRpcFaultException, IOException {
		Boolean b = call(method, Boolean.class, params);
		return b == null ? false : b.booleanValue();
	}

	/**
	 * for jdk6 compiler error (type parameters of <T>T cannot be determined)
	 * @param method wordpress method
	 * @param params parameters
	 * @return result
	 * @throws XmlRpcFaultException if a XmlRpcFault error occurred
	 * @throws IOException if an IO error occurred
	 */
	protected int calli(String method, Object... params) throws XmlRpcFaultException, IOException {
		Integer i = call(method, Integer.class, params);
		return i == null ? 0 : i.intValue();
	}

	/**
	 * @param method wordpress method
	 * @param type result type
	 * @param params parameters
	 * @return result
	 * @throws XmlRpcFaultException if a XmlRpcFault error occurred
	 * @throws IOException if an IO error occurred
	 */
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
		return getPost(blogId, post_id);
	}

	public Post getPost(int blogid, int post_id) throws XmlRpcFaultException, IOException {
		return call("wp.getPost", Post.class, blogid, username, password, post_id);
	}

	public List<Post> getPosts() throws XmlRpcFaultException, IOException {
		return getPosts(blogId, null);
	}

	public List<Post> getPosts(PostFilter filter) throws XmlRpcFaultException, IOException {
		return getPosts(blogId, filter);
	}

	public List<Post> getPosts(int blogid, PostFilter filter) throws XmlRpcFaultException, IOException {
		return call("wp.getPosts", Types.paramTypeOf(List.class, Post.class), blogid, username, password, filter);
	}

	public String newPost(Post post) throws XmlRpcFaultException, IOException {
		return newPost(blogId, post);
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
		return editPost(blogId, post);
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
		return deletePost(blogId, post_id);
	}
	
	public boolean deletePost(int blogid, String post_id) throws XmlRpcFaultException, IOException {
		return callb("wp.deletePost", blogid, username, password, post_id);
	}

	public PostType getPostType(String post_type_name) throws XmlRpcFaultException, IOException {
		return getPostType(blogId, post_type_name);
	}

	public PostType getPostType(int blogid, String post_type_name) throws XmlRpcFaultException, IOException {
		return call("wp.getPostType", PostType.class, blogid, username, password, post_type_name);
	}

	public Map<String, PostType> getPostTypes() throws XmlRpcFaultException, IOException {
		return getPostTypes(blogId);
	}

	public Map<String, PostType> getPostTypes(int blogid) throws XmlRpcFaultException, IOException {
		return call("wp.getPostTypes", Types.paramTypeOf(Map.class, String.class, PostType.class), blogid, username, password);
	}

	public Map<String, String> getPostFormats() throws XmlRpcFaultException, IOException {
		return getPostFormats(blogId);
	}

	public Map<String, String> getPostFormats(int blogid) throws XmlRpcFaultException, IOException {
		return call("wp.getPostFormats", Types.paramTypeOf(Map.class, String.class, String.class), blogid, username, password);
	}

	public Map<String, String> getPostStatusList() throws XmlRpcFaultException, IOException {
		return getPostStatusList(blogId);
	}

	public Map<String, String> getPostStatusList(int blogid) throws XmlRpcFaultException, IOException {
		return call("wp.getPostStatusList", Types.paramTypeOf(Map.class, String.class, String.class), blogid, username, password);
	}
	//--------------------------------------------------------
	// Taxonomies
	//
	public Taxonomy getTaxonomy(String taxonomy) throws XmlRpcFaultException, IOException {
		return getTaxonomy(blogId, taxonomy);
	}

	public Taxonomy getTaxonomy(int blogid, String taxonomy) throws XmlRpcFaultException, IOException {
		return call("wp.getTaxonomy", Taxonomy.class, blogid, username, password, taxonomy);
	}

	public List<Taxonomy> getTaxonomies() throws XmlRpcFaultException, IOException {
		return getTaxonomies(blogId);
	}

	public List<Taxonomy> getTaxonomies(int blogid) throws XmlRpcFaultException, IOException {
		return call("wp.getTaxonomies", Types.paramTypeOf(List.class, Taxonomy.class), blogid, username, password);
	}

	public Term getTerm(String taxonomy, int term_id) throws XmlRpcFaultException, IOException {
		return getTerm(blogId, taxonomy, term_id);
	}

	public Term getTerm(int blogid, String taxonomy, int term_id) throws XmlRpcFaultException, IOException {
		return call("wp.getTerm", Term.class, blogid, username, password, taxonomy, term_id);
	}

	public List<Term> getTerms(String taxonomy) throws XmlRpcFaultException, IOException {
		return getTerms(blogId, taxonomy);
	}

	public List<Term> getTerms(String taxonomy, TermFilter filter) throws XmlRpcFaultException, IOException {
		return getTerms(blogId, taxonomy, filter);
	}

	public List<Term> getTerms(int blogid, String taxonomy) throws XmlRpcFaultException, IOException {
		return getTerms(blogId, taxonomy, null);
	}

	public List<Term> getTerms(int blogid, String taxonomy, TermFilter filter) throws XmlRpcFaultException, IOException {
		return call("wp.getTerms", Types.paramTypeOf(List.class, Term.class), blogid, username, password, taxonomy, filter);
	}

	public String newTerm(Term term) throws XmlRpcFaultException, IOException {
		return newTerm(blogId, term);
	}
	
	public String newTerm(int blogid, Term term) throws XmlRpcFaultException, IOException {
		Map<String, Object> params = new HashMap<String, Object>();
		
		Beans.i().copyNotNullProperties(params, term, "name", "taxonomy", "slug", "description", "parent");

		String tid = call("wp.newTerm", String.class, blogid, username, password, params);
		
		term.term_id = tid;

		return tid;
	}

	public boolean editTerm(Term term) throws XmlRpcFaultException, IOException {
		return editTerm(blogId, term);
	}
	
	public boolean editTerm(int blogid, Term term) throws XmlRpcFaultException, IOException {
		Map<String, Object> params = new HashMap<String, Object>();
		
		Beans.i().copyNotNullProperties(params, term, "name", "taxonomy", "slug", "description", "parent");

		return callb("wp.editTerm", blogid, username, password, term.term_id, params);
	}

	public boolean deleteTerm(String taxonomy, String term_id) throws XmlRpcFaultException, IOException {
		return deleteTerm(blogId, taxonomy, term_id);
	}

	public boolean deleteTerm(int blogid, String taxonomy, String term_id) throws XmlRpcFaultException, IOException {
		return callb("wp.deleteTerm", blogid, username, password, taxonomy, term_id);
	}

	//--------------------------------------------------------
	// Media
	//
	public MediaItem getMediaItem(int attachment_id) throws XmlRpcFaultException, IOException {
		return getMediaItem(blogId, attachment_id);
	}

	public MediaItem getMediaItem(int blogid, int attachment_id) throws XmlRpcFaultException, IOException {
		return call("wp.getMediaItem", MediaItem.class, blogid, username, password, attachment_id);
	}

	public List<MediaItem> getMediaLibrary() throws XmlRpcFaultException, IOException {
		return getMediaLibrary(blogId, null);
	}

	public List<MediaItem> getMediaLibrary(MediaFilter filter) throws XmlRpcFaultException, IOException {
		return getMediaLibrary(blogId, filter);
	}

	public List<MediaItem> getMediaLibrary(int blogid, MediaFilter filter) throws XmlRpcFaultException, IOException {
		return call("wp.getMediaLibrary", Types.paramTypeOf(List.class, MediaItem.class), blogid, username, password, filter);
	}

	public MediaObject uploadFile(MediaFile file) throws XmlRpcFaultException, IOException {
		return uploadFile(blogId, file);
	}

	public MediaObject uploadFile(int blogid, MediaFile file) throws XmlRpcFaultException, IOException {
		return call("wp.uploadFile", MediaObject.class, blogid, username, password, file);
	}
	
	//--------------------------------------------------------
	// Comments
	//
	public CommentCount getCommentCount(String post_id) throws XmlRpcFaultException, IOException {
		return getCommentCount(blogId, post_id);
	}
	
	public CommentCount getCommentCount(int blogid, String post_id) throws XmlRpcFaultException, IOException {
		return call("wp.getCommentCount", CommentCount.class, blogid, username, password, post_id);
	}
	
	public Comment getComment(int comment_id) throws XmlRpcFaultException, IOException {
		return getComment(blogId, comment_id);
	}

	public Comment getComment(int blogid, int comment_id) throws XmlRpcFaultException, IOException {
		return call("wp.getComment", Comment.class, blogid, username, password, comment_id);
	}

	public List<Comment> getComments() throws XmlRpcFaultException, IOException {
		return getComments(null);
	}

	public List<Comment> getComments(CommentFilter filter) throws XmlRpcFaultException, IOException {
		return getComments(blogId, filter);
	}

	public List<Comment> getComments(int blogid, CommentFilter filter) throws XmlRpcFaultException, IOException {
		return call("wp.getComments", Types.paramTypeOf(List.class, Comment.class), blogid, username, password, filter);
	}

	public int newComment(int post_id, Comment comment) throws XmlRpcFaultException, IOException {
		return newComment(blogId, post_id, comment);
	}
	
	public int newComment(int blogid, int post_id, Comment comment) throws XmlRpcFaultException, IOException {
		Map<String, Object> cm = new HashMap<String, Object>();
		
		Beans.i().copyNotNullProperties(cm, comment, "comment_parent", "content", "author", "author_url", "author_email");

		int cid = calli("wp.newComment", blogid, username, password, post_id, cm);
		
		comment.comment_id = String.valueOf(cid);

		return cid;
	}

	public boolean editComment(Comment comment) throws XmlRpcFaultException, IOException {
		return editComment(blogId, comment);
	}
	
	public boolean editComment(int blogid, Comment comment) throws XmlRpcFaultException, IOException {
		Map<String, Object> cm = new HashMap<String, Object>();
		
		Beans.i().copyNotNullProperties(cm, comment, "status", "date_created_gmt", "content", "author", "author_url", "author_email");

		return callb("wp.editComment", blogid, username, password, comment.comment_id, cm);
	}

	public boolean deleteComment(int comment_id) throws XmlRpcFaultException, IOException {
		return deleteComment(blogId, comment_id);
	}

	public boolean deleteComment(int blogid, int comment_id) throws XmlRpcFaultException, IOException {
		return callb("wp.deleteComment", blogid, username, password, comment_id);
	}

	public CommentStatusList getCommentStatusList() throws XmlRpcFaultException, IOException {
		return getCommentStatusList(blogId);
	}

	public CommentStatusList getCommentStatusList(int blogid) throws XmlRpcFaultException, IOException {
		return call("wp.getCommentStatusList", CommentStatusList.class, blogid, username, password);
	}

	//--------------------------------------------------------
	// Options
	//
	public Map<String, Option> getOptions(String... options) throws XmlRpcFaultException, IOException {
		return getOptions(blogId, options);
	}

	public Map<String, Option> getOptions(int blogid, String... options) throws XmlRpcFaultException, IOException {
		return call("wp.getOptions", Types.paramTypeOf(Map.class, String.class, Option.class), blogid, username, password, options);
	}
	
	public Map<String, Option> setOptions(Map<String, Option> options) throws XmlRpcFaultException, IOException {
		return setOptions(blogId, options);
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
		return getUser(blogId, user_id);
	}
	
	public User getUser(int blogid, int user_id) throws XmlRpcFaultException, IOException {
		return call("wp.getUser", User.class, blogid, username, password, user_id);
	}

	public List<User> getUsers() throws XmlRpcFaultException, IOException {
		return getUsers(blogId);
	}
	
	public List<User> getUsers(int blogid) throws XmlRpcFaultException, IOException {
		return call("wp.getUsers", Types.paramTypeOf(List.class, User.class), blogid, username, password);
	}

	public User getProfile() throws XmlRpcFaultException, IOException {
		return getProfile(blogId);
	}
	
	public User getProfile(int blogid) throws XmlRpcFaultException, IOException {
		return call("wp.getProfile", User.class, blogid, username, password);
	}

	public boolean editProfile(Profile profile) throws XmlRpcFaultException, IOException {
		return editProfile(blogId, profile);
	}
	
	public boolean editProfile(int blogid, Profile profile) throws XmlRpcFaultException, IOException {
		return callb("wp.editProfile", blogid, username, password, profile);
	}
	
	public List<User> getAuthors() throws XmlRpcFaultException, IOException {
		return getAuthors(blogId);
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
		return getCategories(blogId);
	}

	public List<Category> getCategories(int blogid) throws XmlRpcFaultException, IOException {
		return call("wp.getCategories", Types.paramTypeOf(List.class, Category.class), blogid, username, password);
	}

	public List<Category> suggestCategories(String prefix) throws XmlRpcFaultException, IOException {
		return suggestCategories(blogId, prefix, Integer.MAX_VALUE);
	}

	public List<Category> suggestCategories(int blogid, String prefix, int max_results) throws XmlRpcFaultException, IOException {
		return call("wp.suggestCategories", Types.paramTypeOf(List.class, Category.class), blogid, username, password, prefix, max_results);
	}

	public int newCategory(Category category) throws XmlRpcFaultException, IOException {
		return newCategory(blogId, category);
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
		return deleteCategory(blogId, category_id);
	}
	
	public boolean deleteCategory(int blogid, int category_id) throws XmlRpcFaultException, IOException {
		return callb("wp.deleteCategory", blogid, username, password, category_id);
	}


	//--------------------------------------------------------
	// Tags
	//
	public List<Tag> getTags() throws XmlRpcFaultException, IOException {
		return getTags(blogId);
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

