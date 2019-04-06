package panda.mvc.view.redirect;

import panda.mvc.annotation.At;
import panda.mvc.annotation.To;
import panda.mvc.annotation.param.Param;

@At
public class MyModule {
	@At("/register")
	@To("redirect:/jsp/user/information.do?id=${r.id}")
	public User register() {
		User user = new User();
		user.setId(373);
		return user;
	}

	@At("/login")
	@To("redirect:/jsp/user/${params.name}")
	public boolean login(@Param("name") String name, @Param("password") String password) {
		return "panda".equals(name) && "123456".equals(password);
	}
}
