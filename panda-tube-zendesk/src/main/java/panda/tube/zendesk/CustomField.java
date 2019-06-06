package panda.tube.zendesk;

import panda.bind.json.Jsons;

public class CustomField {
	private Long id;
	private String value;

	public CustomField() {
	}

	public CustomField(Long id, String value) {
		this.id = id;
		this.value = value;
	}

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return the value
	 */
	public String getValue() {
		return value;
	}

	/**
	 * @param value the value to set
	 */
	public void setValue(String value) {
		this.value = value;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return Jsons.toJson(this, true);
	}
}
