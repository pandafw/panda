package panda.util.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import panda.lang.Collections;
import panda.lang.Objects;
import panda.lang.Strings;

/**
 * @author yf.frank.wang@gmail.com
 */
public class SimpleQuery implements Cloneable, Serializable {
	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * constructor
	 */
	public SimpleQuery() {
	}

	private String key;

	private List<String> targets;

	/**
	 * @return the key
	 */
	public String getKey() {
		return key;
	}

	/**
	 * @param key the key to set
	 */
	public void setKey(String key) {
		this.key = Strings.stripToNull(key);
	}

	/**
	 * @return the target
	 */
	public String getTarget() {
		return (targets == null || targets.isEmpty()) ? null : targets.get(0);
	}

	/**
	 * @param target the target to set
	 */
	public void setTarget(String target) {
		target = Strings.stripToNull(target);
		if (target != null) {
			if (targets == null) {
				targets = new ArrayList<String>();
			}
			this.targets.add(0, target);
		}
	}

	/**
	 * @return the targets
	 */
	public List<String> getTargets() {
		return targets;
	}

	/**
	 * @param targets the targets to set
	 * @throws Exception if an error occurs
	 */
	@SuppressWarnings("unchecked")
	public void setTargets(List<String> targets) throws Exception {
		this.targets = (List<String>)Collections.copyNotNull(targets);
	}

	//-------------------------------------------------------------
	// short name
	//-------------------------------------------------------------
	/**
	 * @return the key
	 */
	public String getK() {
		return getKey();
	}

	/**
	 * @param key the key to set
	 */
	public void setK(String key) {
		setKey(key);
	}

	/**
	 * @return the target
	 */
	public String getT() {
		return getTarget();
	}

	/**
	 * @param target the target to set
	 */
	public void setT(String target) {
		setTarget(target);
	}

	/**
	 * @return the targets
	 */
	public List<String> getTs() {
		return getTargets();
	}

	/**
	 * @param targets the targets to set
	 * @throws Exception if an error occurs
	 */
	public void setTs(List<String> targets) throws Exception {
		setTargets(targets);
	}

	/**
	 * @return a string representation of the object.
	 */
	@Override
	public String toString() {
		return Objects.toStringBuilder()
				.append("key", key)
				.append("targets", targets)
				.toString();
	}

	/**
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return Objects.hashCodes(key, targets);
	}

	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;

		SimpleQuery rhs = (SimpleQuery) obj;
		return Objects.equalsBuilder()
				.append(key, rhs.key)
				.append(targets, rhs.targets)
				.isEquals();
	}

	/**
	 * Clone
	 * @return Clone Object
	 */
	public Object clone() {
		SimpleQuery clone = new SimpleQuery();
		
		clone.key = this.key;
		clone.targets = new ArrayList<String>(this.targets);
		
		return clone;
	}

}
