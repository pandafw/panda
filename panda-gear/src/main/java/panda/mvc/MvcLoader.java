package panda.mvc;

import panda.mvc.config.AbstractMvcConfig;

public interface MvcLoader {

	ActionMapping load(AbstractMvcConfig config);

	void depose(MvcConfig config);

}
