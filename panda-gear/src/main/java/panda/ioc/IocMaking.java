package panda.ioc;

import java.util.Map;

import panda.bind.json.Jsons;
import panda.ioc.aop.MirrorFactory;
import panda.ioc.meta.IocValue;

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

	public ValueProxy makeValueProxy(IocValue[] ivs) {
		ValueProxy vp = vpm.make(this, ivs);
		if (vp == null) {
			throw new IocException(String.format(
				"Failed to create IocValue[] ValueProxy for object '%s'", name));
		}
		return vp;
	}

	public ValueProxy makeValueProxy(IocValue iv) {
		ValueProxy vp = vpm.make(this, iv);
		if (vp == null) {
			throw new IocException(String.format(
				"Failed to create {'%s':%s} ValueProxy for object [%s]", 
				iv.getKind(), Jsons.toJson(iv.getValue()), name));
		}
		
		return vp;
	}

}
