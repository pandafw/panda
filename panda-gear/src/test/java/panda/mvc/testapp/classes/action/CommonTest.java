package panda.mvc.testapp.classes.action;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import panda.ioc.annotation.IocInject;
import panda.mvc.annotation.At;
import panda.mvc.annotation.To;
import panda.mvc.annotation.param.Param;
import panda.mvc.annotation.param.PathArg;
import panda.mvc.testapp.BaseWebappTest;
import panda.mvc.testapp.classes.bean.UserT;
import panda.mvc.view.Views;

@At("/common")
@To(Views.RAW)
public class CommonTest extends BaseWebappTest {

	// 最最基本的测试
	@At("pathArgs/(.*)$")
	public String test_base_pathargs(@PathArg String name) {
		return name;
	}

	// 基本测试1
	@At("pathArgs2/(.+?)/(.+?)/(.+?)/(.+?)/(.+?)/(.+?)/(.+?)/(.*)$")
	public String test_base_pathargs2(@PathArg String name, 
			@PathArg int id, 
			@PathArg long pid, 
			@PathArg short fid, 
			@PathArg double xid, 
			@PathArg float yid, 
			@PathArg boolean z, 
			@PathArg char p,
			@PathArg(0) String name2) {
		return name + id + pid + fid + (int)xid + (int)yid + z + p + name2;
	}

	// 含?和*
	@At("pathArgs3/(.+?)/blog/(.*)$")
	public String test_base_pathargs3(@PathArg String type, @PathArg long id) {
		return type + "&" + id;
	}

	// 含? 与方法test_base_pathargs3比对,
	@At("pathArgs32/(.*)$")
	public String test_base_pathargs3_2(@PathArg String type) {
		return type + "&Z";
	}

	// 与Parms混用
	@At("pathArgs4/(.*)$")
	public String test_base_pathargs4(@PathArg String key, @Param UserT userT) {
		return key + "&" + userT.getName();
	}

	// 与Parms混用
	@At("pathArgs5/(.*)$")
	public String test_base_pathargs5(@PathArg String key, 
			@Param("user.*") UserT user1, 
			@Param("user2.*") UserT user2) {
		return key + "&" + user1.getName() + "&" + user2.getName();
	}

	// Parms混用
	@At("param")
	public String test_param(@Param("id") long id) {
		return "" + id;
	}

	// Parms混用
	@At("path")
	@To(">>:/${reqParams.key}.jsp")
	public void test_req_param() {
	}

	// Test EL
	@At("path2")
	@To("->:/${reqParams.key.length() == 1 ? 'base' : 'false'}.jsp")
	public void test_req_param2() {
	}

	// Test get Servlet object
	@At("servlet_obj")
	public void test_servlet_obj(@IocInject HttpServletRequest req, 
			@IocInject HttpServletResponse resp, 
			@IocInject ServletContext context,
			@IocInject(value="test.not.required", required=false) String mustNull) throws Throwable {

		req.getInputStream();
		req.getContentLength();

		// resp.getOutputStream();
		resp.getWriter();

		context.getAttributeNames();
		
		if (mustNull != null) {
			throw new IllegalArgumentException("iocInject('test.not.required') not work: " + mustNull);
		}
	}
}
