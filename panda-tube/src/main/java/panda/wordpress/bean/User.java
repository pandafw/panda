package panda.wordpress.bean;

import java.util.Date;
import java.util.List;

public class User extends Profile {
	public String user_id;
	public String username;
	public String email;
	public Date registered;
	public List<String> roles;
}

