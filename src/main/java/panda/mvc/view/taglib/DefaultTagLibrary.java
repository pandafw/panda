package panda.mvc.view.taglib;

import java.util.List;

import panda.mvc.ActionContext;
import panda.mvc.view.tag.io.ftl.TagModels;

public class DefaultTagLibrary implements TagLibrary {
	@Override
	public Object getModels(ActionContext context) {
		return new TagModels(context);
	}

	@Override
	public List<Class> getDirectiveClasses() {
		return null;
	}
}
