package panda.log;

import java.util.Properties;

/**
 * @author yf.frank.wang@gmail.com
 */
public interface LogAdapter {

	void init(Properties props);
	
	Log getLogger(String name);

}