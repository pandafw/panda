package panda.app;

import java.io.InputStream;
import java.io.Reader;
import java.util.Properties;

import org.junit.Test;

import panda.el.ElTemplate;
import panda.io.Streams;
import panda.lang.Charsets;

public class ResourceTest {

	private Object top;
	
	/**
	 * @return the top
	 */
	public Object getTop() {
		return top;
	}

	/**
	 * @param top the top to set
	 */
	public void setTop(Object top) {
		this.top = top;
	}

	private void testResourceTemplate(String name) throws Exception {
		InputStream is = getClass().getResourceAsStream(name);
		Reader r = Streams.toReader(is, Charsets.UTF_8);
		Properties ps = new Properties();
		ps.load(r);
		
		for (Object v : ps.values()) {
			new ElTemplate((String)v);
		}
	}
	
	@Test
	public void testResourceTemplateEN() throws Exception {
		testResourceTemplate("App.txt");
	}
	
	@Test
	public void testResourceTemplateJA() throws Exception {
		testResourceTemplate("App_ja.txt");
	}
	
	@Test
	public void testResourceTemplateZH() throws Exception {
		testResourceTemplate("App_zh.txt");
	}
}
