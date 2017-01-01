package panda.ioc;

import java.util.Map;

import panda.bind.json.Jsons;
import panda.ioc.aop.MirrorFactory;
import panda.ioc.meta.IocValue;
import panda.lang.Exceptions;

public class IocMaking {

	private String name;

	private ObjectMaker maker;

	private Ioc ioc;

	private ValueProxyMaker vpm;

	private MirrorFactory mirrors;

	private Map<String, ObjectWeaver> weavers;
	
	public IocMaking(String objName, Ioc ioc, MirrorFactory mirrors, ObjectMaker maker, 
			ValueProxyMaker vpm,
			Map<String, ObjectWeaver> weavers) {
		this.name = objName;
		this.maker = maker;
		this.ioc = ioc;
		this.vpm = vpm;
		this.mirrors = mirrors;
		this.weavers = weavers;
	}

	public Ioc getIoc() {
		return ioc;
	}

	public String getName() {
		return name;
	}

	public ObjectMaker getMaker() {
		return maker;
	}

	public MirrorFactory getMirrors() {
		return mirrors;
	}

	public Map<String, ObjectWeaver> getWeavers() {
		return weavers;
	}

	public IocMaking clone(String objectName) {
		return new IocMaking(objectName, ioc, mirrors, maker, vpm, weavers);
	}

	public ValueProxy makeValueProxy(IocValue iv) {
		ValueProxy vp = vpm.make(this, iv);
		if (vp != null) {
			return vp;
		}
		
		throw Exceptions.makeThrow("Unknown value {'%s':%s} for object [%s]", 
			iv.getType(), Jsons.toJson(iv.getValue()), name);
	}

}
