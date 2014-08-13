package panda.io.resource;

import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * @author yf.frank.wang@gmail.com
 */
public interface ResourceBundleMaker {
	public ResourceBundle getBundle(String baseName, Locale locale, ClassLoader loader,
			boolean reload) throws IllegalAccessException, InstantiationException, IOException;
}
