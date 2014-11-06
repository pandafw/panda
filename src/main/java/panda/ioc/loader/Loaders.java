package panda.ioc.loader;

import java.util.Map.Entry;

import panda.ioc.meta.IocEventSet;
import panda.ioc.meta.IocObject;
import panda.ioc.meta.IocValue;
import panda.lang.Strings;

public abstract class Loaders {
	/**
	 * 查看一下 me 中有没有缺少的属性，没有的话，从 it 补充
	 */
	public static IocObject mergeWith(IocObject me, IocObject it) {
		// merge type
		if (me.getType() == null)
			me.setType(it.getType());

		// don't need merge signleon

		// merge events
		if (me.getEvents() == null) {
			me.setEvents(it.getEvents());
		}
		else if (it.getEvents() != null) {
			IocEventSet eventSet = it.getEvents();
			IocEventSet myEventSet = me.getEvents();
			if (Strings.isBlank(myEventSet.getCreate())) {
				myEventSet.setCreate(eventSet.getCreate());
			}
			
			if (Strings.isBlank(myEventSet.getDepose())) {
				myEventSet.setDepose(eventSet.getDepose());
			}
			
			if (Strings.isBlank(myEventSet.getFetch())) {
				myEventSet.setFetch(eventSet.getFetch());
			}
		}

		// merge scope
		if (Strings.isBlank(me.getScope())) {
			me.setScope(it.getScope());
		}

		// merge arguments
		if (!me.hasArgs()) {
			me.copyArgs(it.getArgs());
		}
		
		// merge fields
		for (Entry<String, IocValue> en : it.getFields().entrySet()) {
			if (!me.hasField(en.getKey())) {
				me.addField(en.getKey(), en.getValue());
			}
		}

		return me;
	}
}
