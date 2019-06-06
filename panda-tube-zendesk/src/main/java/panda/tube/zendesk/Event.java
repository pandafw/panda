package panda.tube.zendesk;

import panda.bind.json.Jsons;

public class Event {
	/** Automatically assigned when the event is created */
	public Long id;
	
	/** Has the value Create */
	public String type;
	
	/** The name of the field that was set */
	public String field_name;

	/** The value of the field that was set */
	public Object value;

	/** The previous value of the field that was changed */
	public Object previous_value;

	public Long author_id;
	
	public Object metadata;


	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return Jsons.toJson(this, true);
	}
}
