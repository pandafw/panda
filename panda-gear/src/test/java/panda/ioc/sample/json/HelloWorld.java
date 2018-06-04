package panda.ioc.sample.json;

import panda.ioc.Ioc;
import panda.ioc.impl.DefaultIoc;
import panda.ioc.loader.JsonIocLoader;

public class HelloWorld {
	public static void main(String[] args) {
		Ioc ioc = new DefaultIoc(new JsonIocLoader("panda/ioc/sample/json/Pets.json"));
		Pet tom = ioc.get(Pet.class, "tom");
		System.out.println(tom);
		
		// because the type is declared in the configuration file, it's not need to pass the type parameter
		Pet jerry = ioc.get(null, "jerry");
		System.out.println(jerry);

		// by declaring singleton: false, every time you fetch it, a new instance is created
		Pet jerry2 = ioc.get(null, "jerry");
		System.out.println("jerry == jerry2: " + (jerry == jerry2));
		
		ioc.depose();
	}
}
