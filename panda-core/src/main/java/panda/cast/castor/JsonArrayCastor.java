package panda.cast.castor;

import java.util.Iterator;

import panda.bind.json.JsonArray;
import panda.bind.json.JsonException;
import panda.cast.AbstractCastor;
import panda.cast.CastContext;
import panda.lang.Iterators;
import panda.lang.Strings;


public class JsonArrayCastor extends AbstractCastor<Object, JsonArray> {
	public JsonArrayCastor() {
		super(Object.class, JsonArray.class);
	}

	@Override
	protected JsonArray castValue(Object value, CastContext cc) {
		if (value instanceof CharSequence) {
			String s = value.toString();
			if (Strings.isEmpty(s)) {
				return defaultValue();
			}

			try {
				return JsonArray.fromJson(s);
			}
			catch (JsonException e) {
				return castError(value, cc, e);
			}
		}
		else if (Iterators.isIterable(value)) {
			JsonArray ja = new JsonArray();
			
			Iterator it = Iterators.asIterator(value);
			while (it.hasNext()) {
				Object v = it.next();
				ja.add(v);
			}
			
			return ja;
		}
		
		return castError(value, cc);
	}
}
