package panda.mvc.view.taglib;

import java.util.List;

import panda.mvc.ActionContext;
import panda.mvc.view.tag.ftl.TagModels;

public class DefaultTagLibrary implements TagLibraryModelProvider, TagLibraryDirectiveProvider {
	@Override
	public Object getModels(ActionContext context) {
		return new TagModels(context);
	}

	@Override
	public List<Class> getDirectiveClasses() {
		return null;
//		return PandaDirectives.getVelocityDirectiveClasses();
	}
}
