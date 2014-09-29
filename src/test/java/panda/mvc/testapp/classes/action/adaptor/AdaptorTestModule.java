package panda.mvc.testapp.classes.action.adaptor;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.util.List;
import java.util.Map;

import junit.framework.TestCase;

import panda.io.Streams;
import panda.mvc.adaptor.meta.Pet;
import panda.mvc.annotation.At;
import panda.mvc.annotation.Fail;
import panda.mvc.annotation.Ok;
import panda.mvc.annotation.param.Param;
import panda.mvc.testapp.BaseWebappTest;

@At("/adaptor")
@Ok("raw")
@Fail("http:500")
public class AdaptorTestModule extends BaseWebappTest {

	/*
	 * Githut : #352
	 */
	@At("/reader")
	public String getInputStream(Reader reader) throws IOException {
		return Streams.toString(reader);
	}

	/*
	 * Githut : #352
	 */
	@At("/ins")
	public String getInputStream(InputStream ins) throws IOException {
		return new String(Streams.toByteArray(ins));
	}

	@At("/json/pet/array")
	public String getJsonPetArray(@Param("pets") Pet[] pets) {
		return String.format("pets(%d) %s", pets.length, "array");
	}

	@At("/json/pet/list")
	public String getJsonPetList(@Param("pets") List<Pet> lst) {
		StringBuilder sb = new StringBuilder();
		for (Pet pet : lst)
			sb.append(',').append(pet.getName());
		return String.format("pets(%d) %s", lst.size(), "list");
	}

	// 传入的id,会是一个非法的字符串!!
	//TODO
//	@At({ "/err/param", "/err/param/?" })
//	public void errParam(@Param("id") long id, AdaptorErrorContext errCtx) {
//		TestCase.assertNotNull(errCtx);
//		TestCase.assertNotNull(errCtx.getErrors()[0]);
//	}

	@At("/json/type")
	public void jsonMapType(@Param Map<String, Double> map) {
		TestCase.assertNotNull(map);
		TestCase.assertEquals(1, map.size());
		TestCase.assertEquals(123456.0, map.get("abc").doubleValue());
		System.out.println(map.get("abc"));
	}
}
