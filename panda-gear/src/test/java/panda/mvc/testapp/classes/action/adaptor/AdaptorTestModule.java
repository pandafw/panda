package panda.mvc.testapp.classes.action.adaptor;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.util.Date;
import java.util.List;
import java.util.Map;

import panda.io.Streams;
import panda.mvc.View;
import panda.mvc.adaptor.meta.Pet;
import panda.mvc.annotation.At;
import panda.mvc.annotation.To;
import panda.mvc.annotation.param.Param;
import panda.mvc.testapp.BaseWebappTest;

import junit.framework.TestCase;

@At("/adaptor")
@To(value=View.RAW, fatal="http:500")
public class AdaptorTestModule extends BaseWebappTest {

	@At("edate")
	public String getDate(@Param(value="d", format="yyyyMMdd") Date d) throws IOException {
		return String.valueOf(d.getTime());
	}

	@At("reader")
	public String getInputStream(Reader reader) throws IOException {
		return Streams.toString(reader);
	}

	@At("ins")
	public String getInputStream2(InputStream ins) throws IOException {
		return new String(Streams.toByteArray(ins));
	}

	@At("json/pet/array")
	public String getJsonPetArray(@Param("pets") Pet[] pets) {
		return String.format("pets(%d) %s", pets.length, "array");
	}

	@At("json/pet/list")
	public String getJsonPetList(@Param("pets") List<Pet> lst) {
		StringBuilder sb = new StringBuilder();
		for (Pet pet : lst)
			sb.append(',').append(pet.getName());
		return String.format("pets(%d) %s", lst.size(), "list");
	}

	@At("json/type")
	public void jsonMapType(@Param Map<String, Double> map) {
		TestCase.assertNotNull(map);
		TestCase.assertEquals(1, map.size());
		TestCase.assertEquals(123456.0, map.get("abc").doubleValue());
		System.out.println(map.get("abc"));
	}
}
