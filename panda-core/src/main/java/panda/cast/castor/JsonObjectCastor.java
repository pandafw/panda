package panda.cast.castor;

import panda.bind.json.JsonException;
import panda.bind.json.JsonObject;
import panda.cast.CastContext;
import panda.lang.Strings;


public class JsonObjectCastor extends AnySingleCastor<JsonObject> {
	public JsonObjectCastor() {
		super(JsonObject.class);
	}

	@Override
	protected JsonObject castValue(Object value, CastContext cc) {
		if (value instanceof CharSequence) {
			String s = value.toString();
			if (Strings.isEmpty(s)) {
				return defaultValue();
			}

			try {
				return JsonObject.fromJson(s);
			}
			catch (JsonException e) {
				return castError(value, cc, e);
			}
		}
		
		return castError(value, cc);
	}
}
