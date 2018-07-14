package panda.mvc.init.module;

import java.lang.reflect.Type;
import java.util.List;

import panda.mvc.View;
import panda.mvc.annotation.At;
import panda.mvc.annotation.To;
import panda.mvc.annotation.param.Param;

@At("/simple")
@To(all=View.SJSON)
public class SimpleTestModule {

	@At
	public void testArrayArgs(@Param("names") List<String>[] names) {
	}

	@At
	public void testArrayArgs2(@Param("names") String[] names) {
	}

	@At
	public void testArrayArgs3(@Param("names") List<? extends Type>[] names) {
	}

	@At
	public void testArrayArgs4(@Param("names") List<? super Type>[] names) {
	}

	@At
	public void testArrayArgs5(@Param("names") List<?>[] names) {
	}

}
