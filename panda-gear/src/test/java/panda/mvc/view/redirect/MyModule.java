package panda.mvc.view.redirect;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import panda.mvc.annotation.At;
import panda.mvc.annotation.param.Param;
import panda.mvc.annotation.view.Ok;

@At
public class MyModule {
	@At("/register")
	@Ok("redirect:/jsp/user/information.do?id=${r.id}")
	public User register(HttpServletRequest request, HttpSession session) {
		User user = new User();
		user.setId(373);
		return user;
	}

	@At("/login")
	@Ok("redirect:/jsp/user/${params.name}")
	public boolean login(@Param("name") String name, @Param("password") String password) {
		return "wendal".equals(name) && "123456".equals(password);
	}
}
