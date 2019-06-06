package panda.tube.wordpress;

import panda.bind.json.Jsons;
import panda.lang.Objects;


public class Option {
	public String desc;

	public String value;

	public Boolean readonly;
	

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}

		Option rhs = (Option)obj;
		return Objects.equalsBuilder()
				.append(desc, rhs.desc)
				.append(value, rhs.value)
				.append(readonly, rhs.readonly)
				.isEquals();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return Jsons.toJson(this, true);
	}
}
