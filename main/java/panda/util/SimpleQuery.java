package panda.util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import panda.lang.Collections;
import panda.lang.Strings;

/**
 * @author yf.frank.wang@gmail.com
 */
@SuppressWarnings("serial")
public class SimpleQuery implements Cloneable, Serializable {

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
     * @return  a string representation of the object.
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();

		sb.append("{ ");
		sb.append("key: ").append(key);
		sb.append(", ");
		sb.append("targets: ").append(targets);
		sb.append(" }");
		
		return sb.toString();
	}

	/**
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((key == null) ? 0 : key.hashCode());
		result = prime * result + ((targets == null) ? 0 : targets.hashCode());
		return result;
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
		SimpleQuery other = (SimpleQuery) obj;
		if (key == null) {
			if (other.key != null)
				return false;
		}
		else if (!key.equals(other.key))
			return false;
		if (targets == null) {
			if (other.targets != null)
				return false;
		}
		else if (!targets.equals(other.targets))
			return false;
		return true;
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
