package panda.mvc.view;

import panda.ioc.annotation.IocBean;

/**
 * serialize json object to output
 */
@IocBean(singleton=false)
public class SitemeshJsonView extends JsonView {
	
	public SitemeshJsonView() {
		setSitemesh(true);
	}
}
