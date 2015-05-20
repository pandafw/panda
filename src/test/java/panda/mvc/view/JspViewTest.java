package panda.mvc.view;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import panda.mvc.mock.Mock;
import panda.mvc.mock.Mock.MockActionContext;

public class JspViewTest {

	@Test
	public void test_name() throws Exception {
		MockActionContext ac = Mock.actionContext();
		JspView fv = new JspView("abc/bcd");
		fv.render(ac);
		assertEquals("/WEB-INF/abc/bcd.jsp", ac.getMockResponse().getForwardedUrl());
	}

	@Test
	public void test_req_path() throws Exception {
		MockActionContext ac = Mock.actionContext();
		ac.setPath("/abc/bcd");
		JspView fv = new JspView(null);
		fv.render(ac);
		assertEquals("/WEB-INF/abc/bcd.jsp", ac.getMockResponse().getForwardedUrl());
	}

	@Test
	public void test_req_path2() throws Exception {
		MockActionContext ac = Mock.actionContext();
		ac.setPath("/abc/bcd.do");
		JspView fv = new JspView("");
		fv.render(ac);
		assertEquals("/WEB-INF/abc/bcd.jsp", ac.getMockResponse().getForwardedUrl());
	}

	@Test
	public void test_req_path3() throws Exception {
		MockActionContext ac = Mock.actionContext();
		ac.setPath("/abc/bcd");
		JspView fv = new JspView("  \r\n\t  ");
		fv.render(ac);
		assertEquals("/WEB-INF/abc/bcd.jsp", ac.getMockResponse().getForwardedUrl());
	}

	@Test
	public void test_dest_path() throws Exception {
		MockActionContext ac = Mock.actionContext();
		JspView fv = new JspView("/abc/bcd.jsp");
		fv.render(ac);
		assertEquals("/abc/bcd.jsp", ac.getMockResponse().getForwardedUrl());
	}
}
