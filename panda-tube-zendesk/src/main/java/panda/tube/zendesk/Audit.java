package panda.tube.zendesk;

import java.util.Date;
import java.util.List;

import panda.bind.json.Jsons;

public class Audit {
	/** Automatically assigned when creating audits */
	public Long id;
	
	/** The ID of the associated ticket */
	public Long ticket_id;

	/** Metadata for the audit, custom and system data */
	public Object metadata;

	/** This object explains how this audit was created */
	public Via via;
	
	/** The time the audit was created */
	public Date created_at;

	/** The user who created the audit */
	public Long author_id;

	/** An array of the events that happened in this audit. See Audit Events */
	public List<Event> events;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return Jsons.toJson(this, true);
	}
}
