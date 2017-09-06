package panda.dao.sql.expert;

import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Pattern;

import panda.bind.json.JsonObject;
import panda.lang.Charsets;
import panda.lang.ClassLoaders;
import panda.lang.Classes;
import panda.log.Log;
import panda.log.Logs;

public class SqlExpertConfig {
	private static Log log = Logs.getLog(SqlExpertConfig.class);
	
	private Map<Pattern, Class<? extends SqlExpert>> experts;

	private Map<String, Object> options;

	public SqlExpertConfig() {
		InputStream is = ClassLoaders.getResourceAsStream("sql-experts.json");
		if (is == null) {
			is = ClassLoaders.getResourceAsStream(
				SqlExpertConfig.class.getPackage().getName().replace('.', '/') + "/sql-experts.json");
		}
		if (is == null) {
			throw new RuntimeException("Failed to find sql-experts.json");
		}
		
		JsonObject jo = JsonObject.fromJson(is, Charsets.UTF_8);
		
		setExperts(jo.getJsonObject("experts"));
		setOptions(jo.optJsonObject("options"));
	}

	public Map<String, Object> getOptions() {
		return options;
	}

	public void setOptions(Map<String, Object> options) {
		this.options = options;
	}

	public SqlExpert matchExpert(String name) {
		for (Entry<Pattern, Class<? extends SqlExpert>> entry : experts.entrySet()) {
			if (entry.getKey().matcher(name).find()) {
				try {
					return entry.getValue().newInstance();
				}
				catch (Exception e) {
					throw new RuntimeException(e);
				}
			}
		}
		return null;
	}

	public Map<Pattern, Class<? extends SqlExpert>> getExperts() {
		return experts;
	}

	@SuppressWarnings("unchecked")
	public void setExperts(Map<String, Object> exps) {
		experts = new LinkedHashMap<Pattern, Class<? extends SqlExpert>>();
		for (Entry<String, Object> entry : exps.entrySet()) {
			Class<? extends SqlExpert> clazz;
			try {
				clazz = (Class<? extends SqlExpert>)Classes.getClass(entry.getValue().toString());
			}
			catch (ClassNotFoundException e) {
				log.warn("Failed to get class " + entry.getValue() + " for " + entry.getKey());
				continue;
			}

			// case insensitive
			Pattern pattern = Pattern.compile(entry.getKey(), Pattern.DOTALL & Pattern.CASE_INSENSITIVE);
			experts.put(pattern, clazz);
		}
	}
}
