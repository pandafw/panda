package panda.mvc.adaptor.meta;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;

import panda.mvc.View;
import panda.mvc.annotation.At;
import panda.mvc.annotation.To;
import panda.mvc.annotation.param.Param;

@At("/json")
@To(all=View.SJSON)
public class JsonModule {

	@At("hello")
	public String hello(@Param("pet") Pet pet) throws UnsupportedEncodingException {
		return "!!" + pet.getName() + "!!";
	}

	@At("map")
	@To(View.RAW)
	public int jsonMap(Map map) {
		return map.size();
	}

	@At("list")
	@To(View.RAW)
	public int jsonList(@Param List<Pet> pets) {
		return pets.size();
	}

	@At("array")
	@To(View.RAW)
	public int jsonArray(@Param Pet[] pets) {
		return pets.length;
	}

	@At("map/obj")
	@To(View.RAW)
	public int mapPet(@Param Pet pet) {
		return pet.map.size();
	}

}
