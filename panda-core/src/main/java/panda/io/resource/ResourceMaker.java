package panda.io.resource;

import java.io.IOException;
import java.util.Locale;

/**
 */
public interface ResourceMaker {
	Resource getResource(Resource parent, String base, Locale locale, ClassLoader classLoader) throws IOException;
}
