package panda.ex.wordpress.bean;

import panda.lang.Objects;


public class Option extends BaseBean {
	public String desc;

	public String value;

	public Boolean readonly;
	
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
}
