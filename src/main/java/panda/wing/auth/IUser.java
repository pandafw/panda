package panda.wing.auth;

import java.util.List;

public interface IUser {
	public Long getId();

	public List<String> getPermits();
	
	public Long getLoginTime();
}

