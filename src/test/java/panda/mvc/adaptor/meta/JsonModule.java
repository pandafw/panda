package panda.mvc.adaptor.meta;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;

import panda.mvc.annotation.At;
import panda.mvc.annotation.Fail;
import panda.mvc.annotation.Ok;
import panda.mvc.annotation.param.Param;

@At("/json")
@Ok("json")
@Fail("json")
public class JsonModule {

	@At("/hello")
	public String hello(@Param("pet") Pet pet) throws UnsupportedEncodingException {
		return "!!" + pet.getName() + "!!";
	}

	@At("/map")
	public int jsonMap(Map map) {
		return map.size();
	}

	@At("/list")
	public int jsonList(List<Pet> pets) {
		return pets.size();
	}

	@At("/array")
	public int jsonArray(Pet[] pets) {
		return pets.length;
	}

	@At("/map/obj")
	public int mapPet(Pet pet) {
		return pet.map.size();
	}

}
