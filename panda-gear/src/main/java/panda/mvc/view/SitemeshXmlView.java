package panda.mvc.view;

import panda.ioc.annotation.IocBean;

/**
 * serialize XML object to output
 */
@IocBean(singleton=false)
public class SitemeshXmlView extends XmlView {
	/**
	 * Constructor.
	 */
	public SitemeshXmlView() {
		setSitemesh(true);
	}

}
