package panda.wing.auth;

import java.util.List;

public interface IUser {
	public Long getId();

	public void setId(Long id);

	public String getName();

	public String getPassword();

	public List<String> getPermits();
	
	public Long getLoginTime();
}

