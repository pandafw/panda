package panda.mvc.annotation;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import org.junit.Assert;
import org.junit.Test;

import panda.mvc.annotation.validate.BinaryValidate;
import panda.mvc.annotation.validate.EmailValidate;
import panda.mvc.annotation.validate.ImailValidate;
import panda.mvc.annotation.validate.VisitValidate;

public class AnnotationOrderTest {

	@VisitValidate
	@ImailValidate
	@EmailValidate
	public void some(
			@BinaryValidate
			@VisitValidate
			@ImailValidate
			@EmailValidate
			String arg) {
	}
	
	@Test
	public void testOrder() throws Exception {
		Method m = this.getClass().getMethod("some", String.class);
		Annotation[] ans = m.getAnnotations();
		
		Assert.assertEquals(3, ans.length);
		Assert.assertEquals(VisitValidate.class, ans[0].annotationType());
		Assert.assertEquals(ImailValidate.class, ans[1].annotationType());
		Assert.assertEquals(EmailValidate.class, ans[2].annotationType());
		
		Annotation[][] panss = m.getParameterAnnotations();
		Assert.assertEquals(1, panss.length);

		Annotation[] pans = panss[0];
		Assert.assertEquals(4, pans.length);
		Assert.assertEquals(BinaryValidate.class, pans[0].annotationType());
		Assert.assertEquals(VisitValidate.class, pans[1].annotationType());
		Assert.assertEquals(ImailValidate.class, pans[2].annotationType());
		Assert.assertEquals(EmailValidate.class, pans[3].annotationType());
	}
}
