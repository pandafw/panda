package panda.mvc;

import panda.mvc.config.AbstractMvcConfig;

public interface Loading {

	ActionMapping load(AbstractMvcConfig config);

	void depose(MvcConfig config);

}
