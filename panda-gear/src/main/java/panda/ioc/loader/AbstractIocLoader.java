package panda.ioc.loader;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import panda.bind.json.Jsons;
import panda.ioc.IocLoadException;
import panda.ioc.IocLoader;
import panda.ioc.meta.IocObject;

public abstract class AbstractIocLoader implements IocLoader {

	protected Map<String, IocObject> beans;

	protected AbstractIocLoader() {
		beans = new HashMap<String, IocObject>();
	}

	public AbstractIocLoader(Map<String, IocObject> beans) {
		this.beans = beans;
	}

	public Set<String> getNames() {
		return beans.keySet();
	}

	public boolean has(String name) {
		return beans.containsKey(name);
	}

	public IocObject load(String name) throws IocLoadException {
		return beans.get(name);
	}

	@Override
	public String toString() {
		return "/* " + getClass().getName() + " */\n" + Jsons.toJson(beans);
	}
}
