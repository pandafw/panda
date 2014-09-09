package panda.mvc.view.redirect;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import panda.mvc.annotation.At;
import panda.mvc.annotation.Ok;
import panda.mvc.annotation.param.Param;

public class MyModule {

	@At("/register")
	@Ok("redirect:/jsp/user/information.nut?id=${obj.id}")
	public User register(HttpServletRequest request, HttpSession session) {
		User user = new User();
		user.setId(373);
		return user;
	}

	@At("/login")
	@Ok("redirect:/jsp/user/${p.name}")
	public boolean login(@Param("name") String name, @Param("password") String password) {
		return "wendal".equals(name) && "123456".equals(password);
	}
}
