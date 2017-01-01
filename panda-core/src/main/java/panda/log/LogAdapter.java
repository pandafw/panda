package panda.log;

import java.util.Properties;

/**
 */
public interface LogAdapter {

	void init(Properties props);
	
	Log getLogger(String name);

}