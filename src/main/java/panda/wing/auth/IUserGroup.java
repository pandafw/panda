package panda.wing.auth;

import java.util.List;

public interface IUserGroup {

	public Long getId();

	public String getName();

	public Integer getLevel();

	public List<String> getPermits();
}
