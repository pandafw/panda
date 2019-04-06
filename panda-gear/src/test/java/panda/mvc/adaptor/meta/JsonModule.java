package panda.mvc.adaptor.meta;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;

import panda.mvc.annotation.At;
import panda.mvc.annotation.To;
import panda.mvc.annotation.param.Param;
import panda.mvc.view.Views;

@At("/json")
@To(Views.SJSON)
public class JsonModule {

	@At("hello")
	public String hello(@Param("pet") Pet pet) throws UnsupportedEncodingException {
		return "!!" + pet.getName() + "!!";
	}

	@At("map")
	@To(Views.RAW)
	public int jsonMap(@Param Map map) {
		return map.size();
	}

	@At("list")
	@To(Views.RAW)
	public int jsonList(@Param List<Pet> pets) {
		return pets.size();
	}

	@At("array")
	@To(Views.RAW)
	public int jsonArray(@Param Pet[] pets) {
		return pets.length;
	}

	@At("map/obj")
	@To(Views.RAW)
	public int mapPet(@Param Pet pet) {
		return pet.map.size();
	}

}
