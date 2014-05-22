package panda.ioc;

import java.util.List;

import panda.bind.json.Jsons;
import panda.ioc.aop.MirrorFactory;
import panda.ioc.meta.IocValue;
import panda.lang.Exceptions;

public class IocMaking {

	private String name;

	private ObjectMaker maker;

	private Ioc ioc;

	private IocContext context;

	private List<ValueProxyMaker> vpms;

	private MirrorFactory mirrors;

	public IocMaking(Ioc ioc, MirrorFactory mirrors, IocContext context, ObjectMaker maker, List<ValueProxyMaker> vpms,
			String objName) {
		this.name = objName;
		this.maker = maker;
		this.ioc = ioc;
		this.context = context;
		this.vpms = vpms;
		this.mirrors = mirrors;
	}

	public Ioc getIoc() {
		return ioc;
	}

	public IocContext getContext() {
		return context;
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

	public IocMaking clone(String objectName) {
		return new IocMaking(ioc, mirrors, context, maker, vpms, objectName);
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
