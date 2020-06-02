package panda.ioc.loader;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import panda.bind.json.Jsons;
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

	@Override
	public Set<String> getNames() {
		return beans.keySet();
	}

	@Override
	public boolean has(String name) {
		return beans.containsKey(name);
	}

	@Override
	public IocObject load(String name) {
		return beans.get(name);
	}

	@Override
	public String toString() {
		return "/* " + getClass().getName() + " */\n" + Jsons.toJson(beans);
	}
}
