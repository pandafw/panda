package panda.mvc.adaptor.ejector;

import java.io.InputStream;

import panda.bind.json.JsonObject;
import panda.bind.xml.Xmls;
import panda.ioc.annotation.IocBean;

/**
 * XML Ejector
 */
@IocBean(singleton=false)
public class XmlParamEjector extends JsonParamEjector {
	public XmlParamEjector() {
	}

	@Override
	protected JsonObject toJson(InputStream is, String encoding) {
		return Xmls.fromXml(is, encoding, JsonObject.class);
	}
	
}
