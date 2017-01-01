package panda.wing.auth;

public interface ILogin {

	public Long getLoginTime();
	
	public void setLoginTime(Long time);
	
	public Boolean getAutoLogin();

}
