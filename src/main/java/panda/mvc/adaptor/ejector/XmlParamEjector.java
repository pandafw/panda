package panda.mvc.adaptor.ejector;

import java.io.InputStream;

import panda.bind.json.JsonObject;
import panda.bind.xml.Xmls;
import panda.mvc.ActionContext;

/**
 * XML提取器
 */
public class XmlParamEjector extends JsonParamEjector {
	public XmlParamEjector(ActionContext ac) {
		super(ac);
	}

	@Override
	protected JsonObject toJson(InputStream is, String encoding) {
		return Xmls.fromXml(is, encoding, JsonObject.class);
	}
	
}
