package panda.mvc;

import java.io.InputStream;
import java.io.Reader;
import java.util.Properties;

import org.junit.Test;

import panda.el.ElTemplate;
import panda.io.Streams;
import panda.lang.Charsets;
import panda.lang.reflect.Fields;
import panda.mvc.validation.validator.ImageValidator;

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
		testResourceTemplate("Resource.txt");
	}
	
	@Test
	public void testResourceTemplateJA() throws Exception {
		testResourceTemplate("Resource_ja.txt");
	}
	
	@Test
	public void testResourceTemplateZH() throws Exception {
		testResourceTemplate("Resource_zh.txt");
	}
	
	@Test
	public void testValidationImageJA() throws Exception {
		testValidationImage("Resource_ja.txt");
	}
	
	@Test
	public void testValidationImageZH() throws Exception {
		testValidationImage("Resource_zh.txt");
	}
	
	@Test
	public void testValidationImageEN() throws Exception {
		testValidationImage("Resource.txt");
	}
	
	private void testValidationImage(String name) throws Exception {
		InputStream is = getClass().getResourceAsStream(name);
		Reader r = Streams.toReader(is, Charsets.UTF_8);
		Properties ps = new Properties();
		ps.load(r);
		
		String s = ps.getProperty("validation-image");
		
		ImageValidator iv = new ImageValidator();
		top = iv;

		Fields.writeField(iv, "image", false, true);
		String actual = ElTemplate.evaluate(s, this, true);
		System.out.println(actual);
		
		////
		Fields.writeField(iv, "image", true, true);
		Fields.writeField(iv, "width", 100, true);
		Fields.writeField(iv, "height", 200, true);

		Fields.writeField(iv, "maxWidth", 30, true);
		Fields.writeField(iv, "minWidth", null, true);
		actual = ElTemplate.evaluate(s, this, true);
		System.out.println(actual);

		Fields.writeField(iv, "maxWidth", null, true);
		Fields.writeField(iv, "minWidth", 20, true);
		actual = ElTemplate.evaluate(s, this, true);
		System.out.println(actual);

		Fields.writeField(iv, "maxWidth", 30, true);
		Fields.writeField(iv, "minWidth", 20, true);
		actual = ElTemplate.evaluate(s, this, true);
		System.out.println(actual);

		////
		Fields.writeField(iv, "maxWidth", null, true);
		Fields.writeField(iv, "minWidth", null, true);

		Fields.writeField(iv, "maxHeight", 30, true);
		Fields.writeField(iv, "minHeight", null, true);
		actual = ElTemplate.evaluate(s, this, true);
		System.out.println(actual);

		Fields.writeField(iv, "maxHeight", null, true);
		Fields.writeField(iv, "minHeight", 20, true);
		actual = ElTemplate.evaluate(s, this, true);
		System.out.println(actual);

		Fields.writeField(iv, "maxHeight", 30, true);
		Fields.writeField(iv, "minHeight", 20, true);
		actual = ElTemplate.evaluate(s, this, true);
		System.out.println(actual);

		////
		Fields.writeField(iv, "maxWidth", 30, true);
		Fields.writeField(iv, "minWidth", 20, true);
		Fields.writeField(iv, "maxHeight", 30, true);
		Fields.writeField(iv, "minHeight", 20, true);
		actual = ElTemplate.evaluate(s, this, true);
		System.out.println(actual);

	}
}
