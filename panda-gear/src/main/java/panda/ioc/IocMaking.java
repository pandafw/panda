package panda.ioc;

import java.util.Map;

import panda.bean.Beans;
import panda.cast.Castors;
import panda.ioc.aop.Mirrors;
import panda.ioc.meta.IocValue;

public interface IocMaking {

	public Ioc getIoc();

	public String getName();

	public Mirrors getMirrors();

	public ObjectMaker getObjectMaker();

	public Map<String, ObjectWeaver> getWeavers();

	public Beans getBeans();

	public Castors getCastors();

	public ValueProxy makeValueProxy(IocValue iv);

	public ValueProxy makeValueProxy(IocValue[] ivs);

	public IocMaking clone(String name);
}
