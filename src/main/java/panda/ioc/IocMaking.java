package panda.ioc;

import java.util.List;
import java.util.Map;

import panda.bind.json.Jsons;
import panda.ioc.aop.MirrorFactory;
import panda.ioc.meta.IocValue;
import panda.lang.Exceptions;

public class IocMaking {

	private String name;

	private ObjectMaker maker;

	private Ioc ioc;

	private List<ValueProxyMaker> vpms;

	private MirrorFactory mirrors;

	private Map<String, ObjectWeaver> weavers;
	
	public IocMaking(String objName, Ioc ioc, MirrorFactory mirrors, ObjectMaker maker, 
			List<ValueProxyMaker> vpms,
			Map<String, ObjectWeaver> weavers) {
		this.name = objName;
		this.maker = maker;
		this.ioc = ioc;
		this.vpms = vpms;
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
		return new IocMaking(objectName, ioc, mirrors, maker, vpms, weavers);
	}

	public ValueProxy makeValueProxy(IocValue iv) {
		for (ValueProxyMaker vpm : vpms) {
			ValueProxy vp = vpm.make(this, iv);
			if (null != vp) {
				return vp;
			}
		}
		
		throw Exceptions.makeThrow("Unknown value {'%s':%s} for object [%s]", 
			iv.getType(), Jsons.toJson(iv.getValue()), name);
	}

}
