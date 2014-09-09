package panda.mvc.view;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import panda.mvc.Mvcs;
import panda.mvc.mock.Mock;
import panda.mvc.mock.Mock.MockActionContext;
import panda.mvc.view.JspView;

public class JspViewTest {

	@Before
	public void before() {
		Mvcs.setServletContext(Mock.servlet.context());
	}

	@Test
	public void test_name() throws Exception {
		MockActionContext ac = Mock.actionContext();
		JspView fv = new JspView("abc.bcd");
		fv.render(ac, null);
		assertEquals("/WEB-INF/abc/bcd.jsp", ac.getMockResponse().getForwardedUrl());
	}

	@Test
	public void test_req_path() throws Exception {
		MockActionContext ac = Mock.actionContext();
		ac.getMockRequest().setPathInfo("/abc/bcd.do");
		JspView fv = new JspView(null);
		fv.render(ac, null);
		assertEquals("/WEB-INF/abc/bcd.jsp", ac.getMockResponse().getForwardedUrl());
	}

	@Test
	public void test_req_path2() throws Exception {
		MockActionContext ac = Mock.actionContext();
		ac.getMockRequest().setPathInfo("/abc/bcd.do");
		JspView fv = new JspView("");
		fv.render(ac, null);
		assertEquals("/WEB-INF/abc/bcd.jsp", ac.getMockResponse().getForwardedUrl());
	}

	@Test
	public void test_req_path3() throws Exception {
		MockActionContext ac = Mock.actionContext();
		ac.getMockRequest().setPathInfo("/abc/bcd.do");
		JspView fv = new JspView("  \r\n\t  ");
		fv.render(ac, null);
		assertEquals("/WEB-INF/abc/bcd.jsp", ac.getMockResponse().getForwardedUrl());
	}

	@Test
	public void test_dest_path() throws Exception {
		MockActionContext ac = Mock.actionContext();
		JspView fv = new JspView("/abc/bcd.jsp");
		fv.render(ac, null);
		assertEquals("/abc/bcd.jsp", ac.getMockResponse().getForwardedUrl());
	}
}
