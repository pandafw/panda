package panda.ioc.loader;

import java.util.Map.Entry;

import panda.ioc.meta.IocEventSet;
import panda.ioc.meta.IocObject;
import panda.ioc.meta.IocValue;
import panda.lang.Chars;
import panda.lang.Strings;

public abstract class Loaders {
	/**
	 * convert inject description to IocValue
	 * 
	 * <pre>
	 * ${...}: el expression
	 * %{...}: el expression
	 * !{...}: json object
	 * ![...]: json array
	 * '...  : normal: value.substring(1)
	 * #...  : reference ioc bean
	 * </pre>
	 * 
	 * @param value inject object description
	 * @param defType default ioc type
	 * @return the IocValue
	 */
	public static IocValue convert(String value, char defType) {
		if (value == null) {
			return new IocValue(IocValue.TYPE_NULL);
		}
		if (value.isEmpty()) {
			return new IocValue(IocValue.TYPE_RAW, value);
		}

		int c0 = value.charAt(0);
		if (c0 == Chars.SINGLE_QUOTE) {
			return new IocValue(IocValue.TYPE_RAW, value.substring(1));
		}
		if (c0 == Chars.SHARP && value.length() > 1) {
			return new IocValue(IocValue.TYPE_REF, value.substring(1));
		}
		if (value.length() > 3) {
			int c1 = value.charAt(1);
			int cx = value.charAt(value.length() - 1);
			
			if ((c0 == Chars.DOLLAR || c0 == Chars.PERCENT) && c1 == Chars.BRACES_LEFT && cx == Chars.BRACES_RIGHT) {
				return new IocValue(IocValue.TYPE_EL, value.substring(2, value.length() - 1));
			}
			if (c0 == Chars.EXCLAMATION && c1 == Chars.BRACES_LEFT && cx == Chars.BRACES_RIGHT) {
				return new IocValue(IocValue.TYPE_JSON, value.substring(1));
			}
			if (c0 == Chars.EXCLAMATION && c1 == Chars.BRACKETS_LEFT && cx == Chars.BRACKETS_RIGHT) {
				return new IocValue(IocValue.TYPE_JSON, value.substring(1));
			}
		}

		return new IocValue(defType, value);
	}

	/**
	 * merge IocObject from it to me.
	 * 
	 * @param me the IocObject to be merged
	 * @param it the IocObject to merge from
	 * @return the merged IocObject (me)
	 */
	public static IocObject mergeWith(IocObject me, IocObject it) {
		// merge type
		if (me.getType() == null) {
			me.setType(it.getType());
		}

		// don't need merge singleton

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
