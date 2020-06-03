package panda.ioc.impl;

import java.util.Map;

import panda.bean.Beans;
import panda.bind.json.Jsons;
import panda.cast.Castors;
import panda.ioc.Ioc;
import panda.ioc.IocException;
import panda.ioc.IocMaking;
import panda.ioc.ObjectMaker;
import panda.ioc.ObjectWeaver;
import panda.ioc.ValueProxy;
import panda.ioc.aop.Mirrors;
import panda.ioc.meta.IocValue;

public class DefaultIocMaking implements IocMaking {

	private String name;

	private DefaultIoc ioc;

	public DefaultIocMaking(String name, DefaultIoc ioc) {
		this.name = name;
		this.ioc = ioc;
	}

	@Override
	public Ioc getIoc() {
		return ioc;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public ObjectMaker getObjectMaker() {
		return ioc.getObjectMaker();
	}

	@Override
	public Mirrors getMirrors() {
		return ioc.getMirrors();
	}

	@Override
	public Map<String, ObjectWeaver> getWeavers() {
		return ioc.getWeavers();
	}

	@Override
	public Beans getBeans() {
		return ioc.getBeans();
	}

	@Override
	public Castors getCastors() {
		return ioc.getCastors();
	}
	
	@Override
	public ValueProxy makeValueProxy(IocValue[] ivs) {
		ValueProxy vp = ioc.getValueMaker().make(this, ivs);
		if (vp == null) {
			throw new IocException(String.format(
				"Failed to create IocValue[] ValueProxy for object '%s'", name));
		}
		return vp;
	}

	@Override
	public ValueProxy makeValueProxy(IocValue iv) {
		ValueProxy vp = ioc.getValueMaker().make(this, iv);
		if (vp == null) {
			throw new IocException(String.format(
				"Failed to create {'%s':%s} ValueProxy for object [%s]", 
				iv.getKind(), Jsons.toJson(iv.getValue()), name));
		}
		
		return vp;
	}

	@Override
	public DefaultIocMaking clone(String name) {
		return new DefaultIocMaking(name, ioc);
	}
}
