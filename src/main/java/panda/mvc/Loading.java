package panda.mvc;

import panda.mvc.config.AbstractMvcConfig;

public interface Loading {

	UrlMapping load(AbstractMvcConfig config);

	void depose(MvcConfig config);

}
