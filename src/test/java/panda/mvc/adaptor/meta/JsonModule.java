package panda.mvc.adaptor.meta;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;

import panda.mvc.annotation.At;
import panda.mvc.annotation.param.Param;
import panda.mvc.annotation.view.Fatal;
import panda.mvc.annotation.view.Ok;

@At("/json")
@Ok("json")
@Fatal("json")
public class JsonModule {

	@At("hello")
	public String hello(@Param("pet") Pet pet) throws UnsupportedEncodingException {
		return "!!" + pet.getName() + "!!";
	}

	@At("map")
	@Ok("raw")
	public int jsonMap(Map map) {
		return map.size();
	}

	@At("list")
	@Ok("raw")
	public int jsonList(@Param List<Pet> pets) {
		return pets.size();
	}

	@At("array")
	@Ok("raw")
	public int jsonArray(@Param Pet[] pets) {
		return pets.length;
	}

	@At("map/obj")
	@Ok("raw")
	public int mapPet(@Param Pet pet) {
		return pet.map.size();
	}

}
