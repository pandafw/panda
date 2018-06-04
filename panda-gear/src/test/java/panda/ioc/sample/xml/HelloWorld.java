package panda.ioc.sample.xml;

import panda.ioc.Ioc;
import panda.ioc.impl.DefaultIoc;
import panda.ioc.loader.XmlIocLoader;

public class HelloWorld {
	public static void main(String[] args) throws Exception {
		Ioc ioc = new DefaultIoc(new XmlIocLoader("panda/ioc/sample/xml/Pets.xml"));
		Pet tom = ioc.get(Pet.class, "tom");
		System.out.println(tom);

		ioc.depose();
	}
}
