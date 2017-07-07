package panda.log;

import java.util.Map;

public interface LogAdapter {

	void init(Logs logs, String name, Map<String, String> props);
	
	Log getLog(String name);

}
