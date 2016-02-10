package panda.ioc.json;

import panda.ioc.Ioc;
import panda.ioc.impl.DefaultIoc;
import panda.ioc.json.pojo.Animal;
import panda.ioc.loader.MapIocLoader;
import panda.lang.Strings;

class Utils {

	static DefaultIoc I(String... ss) {
		String json = "{";
		json += Strings.join(ss, ',');
		json += "}";
		return new DefaultIoc(new MapIocLoader(json));
	}

	static String J(String name, String s) {
		return name + " : {" + s + "}";
	}

	static Animal A(String s) {
		Ioc ioc = I(J("obj", s));
		return ioc.get(Animal.class, "obj");
	}

}
