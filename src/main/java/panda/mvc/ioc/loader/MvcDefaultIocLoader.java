package panda.mvc.ioc.loader;

import panda.filepool.local.LocalFilePool;
import panda.io.resource.ResourceBundleLoader;
import panda.mvc.aware.ActionAwareSupport;
import panda.mvc.aware.ApplicationAwareSupport;
import panda.mvc.aware.ParamAwareSupport;
import panda.mvc.aware.SessionAwareSupport;
import panda.mvc.impl.DefaultActionChainMaker;
import panda.mvc.impl.DefaultUrlMapping;
import panda.mvc.util.CookieStateProvider;
import panda.mvc.util.DefaultTextProvider;
import panda.mvc.validation.DefaultValidators;
import panda.mvc.validation.validator.AbstractValidator;

public class MvcDefaultIocLoader extends MvcAnnotationIocLoader {
	protected static final Object[] DEFAULTS = {
		DefaultUrlMapping.class,
		DefaultActionChainMaker.class,
		
		// file pool used by Upload
		LocalFilePool.class,
		
		ResourceBundleLoader.class,
		CookieStateProvider.class,
		DefaultTextProvider.class,
		
		// validation
		ActionAwareSupport.class,
		ParamAwareSupport.class,
		SessionAwareSupport.class,
		ApplicationAwareSupport.class,
		
		// validators
		DefaultValidators.class,
		AbstractValidator.class.getPackage().getName()
	};
	
	public MvcDefaultIocLoader() {
		super(DEFAULTS);
	}
}
