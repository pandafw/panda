package panda.mvc.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import panda.lang.Objects;
import panda.lang.Strings;
import panda.mvc.validation.Validators;
import panda.mvc.validation.annotation.Validate;
import panda.mvc.validation.annotation.Validates;

/**
 * Filter bean object
 * @author yf.frank.wang@gmail.com
 */
public class Filter implements Cloneable, Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * constructor
	 */
	public Filter() {
	}

	/**
	 * EQUAL = "eq";
	 */
	public final static String EQUAL = "eq";

	/**
	 * NOT_EQUAL = "ne";
	 */
	public final static String NOT_EQUAL = "ne";

	/**
	 * LESS_THAN = "lt";
	 */
	public final static String LESS_THAN = "lt";

	/**
	 * LESS_EQUAL = "le";
	 */
	public final static String LESS_EQUAL = "le";

	/**
	 * GREATER_THAN = "gt";
	 */
	public final static String GREATER_THAN = "gt";

	/**
	 * GREATER_EQUAL = "ge";
	 */
	public final static String GREATER_EQUAL = "ge";

	/**
	 * LIKE = "lk";
	 */
	public final static String LIKE = "lk";

	/**
	 * NOT_LIKE = "nk";
	 */
	public final static String NOT_LIKE = "nk";

	/**
	 * MATCH = "mt";
	 */
	public final static String MATCH = "mt";

	/**
	 * NOT_MATCH = "nmt";
	 */
	public final static String NOT_MATCH = "nmt";

	/**
	 * LEFT_MATCH = "lm";
	 */
	public final static String LEFT_MATCH = "lm";

	/**
	 * NOT_LEFT_MATCH = "nlm";
	 */
	public final static String NOT_LEFT_MATCH = "nlm";

	/**
	 * RIGHT_MATCH = "rm";
	 */
	public final static String RIGHT_MATCH = "rm";

	/**
	 * NOT_RIGHT_MATCH = "nrm";
	 */
	public final static String NOT_RIGHT_MATCH = "nrm";

	/**
	 * IN = "in";
	 */
	public final static String IN = "in";

	/**
	 * NOT_IN = "nn";
	 */
	public final static String NOT_IN = "nn";

	/**
	 * BETWEEN = "bt";
	 */
	public final static String BETWEEN = "bt";

	/**
	 * COMPARATORS
	 */
	public final static String COMPARATORS = "{list: [ 'eq', 'ne', 'lt', 'le', 'gt', 'ge', 'lk', 'nk', 'mt', 'nmt', 'lm', 'nlm', 'rm', 'nrm', 'in', 'nn', 'bt' ]}";
	
	/**
	 * VT_BOOLEAN = "b";
	 */
	public final static String VT_BOOLEAN = "b";
	
	/**
	 * VT_DATETIME = "e";
	 */
	public final static String VT_DATETIME = "e";
		
	/**
	 * VT_DATE = "d";
	 */
	public final static String VT_DATE = "d";
	
	/**
	 * VT_TIME = "t";
	 */
	public final static String VT_TIME = "t";
	
	/**
	 * VT_NUMBER = "n";
	 */
	public final static String VT_NUMBER = "n";
	
	/**
	 * VT_STRING = "s";
	 */
	public final static String VT_STRING = "s";

	private String name;
	private String comparator;
	private List values;
	private String type;

	@SuppressWarnings("unchecked")
	private <T> T getObject(int idx, Class<T> cls) {
		if (values == null || values.size() <= idx) {
			return null;
		}

		Object v = values.get(idx);
		if (v == null) {
			return null;
		}
		return cls.isAssignableFrom(v.getClass()) ? (T)v : null;
	}
	
	private void setObject(int idx, Object o) {
		setObject(idx, o, null);
	}
	
	@SuppressWarnings("unchecked")
	private void setObject(int idx, Object o, String type) {
		if (o == null) {
			return;
		}

		if (type == null) {
			if (o instanceof Boolean) {
				type = VT_BOOLEAN;
			}
			else if (o instanceof Date) {
				type = VT_DATE;
			}
			else if (o instanceof Number) {
				type = VT_NUMBER;
			}
			else if (o instanceof String) {
				o = Strings.stripToNull((String)o);
				if (o == null) {
					return;
				}
				type = VT_STRING;
			}
		}

		this.type = type;
		if (this.values == null) {
			this.values = new ArrayList();
		}
		for (int i = this.values.size(); i <= idx; i++) {
			this.values.add(null);
		}
		this.values.set(idx, o);
	}

	private Boolean getBoolean(int idx) {
		return getObject(idx, Boolean.class);
	}

	private Date getDate(int idx) {
		return getObject(idx, Date.class);
	}

	private Number getNumber(int idx) {
		return getObject(idx, Number.class);
	}

	private String getString(int idx) {
		return getObject(idx, String.class);
	}

	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = Strings.stripToNull(name);
	}

	/**
	 * @return comparator
	 */
	public String getComparator() {
		return comparator;
	}

	/**
	 * @param comparator the comparator to set
	 */
	public void setComparator(String comparator) {
		this.comparator = Strings.lowerCase(Strings.stripToNull(comparator));
	}

	/**
	 * @return the values
	 */
	public List getValues() {
		return values;
	}

	/**
	 * @return the value
	 */
	public Object getValue() {
		return getObject(0, Object.class);
	}

	/**
	 * @param value the value to set
	 */
	public void setValue(Object value) {
		setObject(0, value);
	}

	/**
	 * @return the value2
	 */
	public Object getValue2() {
		return getObject(1, Object.class);
	}

	/**
	 * @param value2 the value2 to set
	 */
	public void setValue2(Object value2) {
		setObject(1, value2);
	}

	/**
	 * @return the bvs
	 */
	@SuppressWarnings("unchecked")
	public List<Boolean> getBooleanValues() {
		return (List<Boolean>)values;
	}

	/**
	 * @param bvs the bvs to set
	 */
	public void setBooleanValues(List<Boolean> bvs) {
		this.type = VT_BOOLEAN;
		this.values = bvs;
	}

	/**
	 * @return the bv
	 */
	public Boolean getBooleanValue() {
		return getBoolean(0);
	}

	/**
	 * @param bv the bv to set
	 */
	public void setBooleanValue(Boolean bv) {
		setObject(0, bv, VT_BOOLEAN);
	}

	/**
	 * @return the bv2
	 */
	public Boolean getBooleanValue2() {
		return getBoolean(1);
	}

	/**
	 * @param bv2 the bv2 to set
	 */
	public void setBooleanValue2(Boolean bv2) {
		setObject(1, bv2, VT_BOOLEAN);
	}

	/**
	 * @return the dtvs
	 */
	@SuppressWarnings("unchecked")
	public List<Date> getDateTimeValues() {
		return (List<Date>)values;
	}

	/**
	 * @param dtvs the dtvs to set
	 */
	public void setDateTimeValues(List<Date> dtvs) {
		this.type = VT_DATETIME;
		this.values = dtvs;
	}

	/**
	 * @return the dv
	 */
	public Date getDateTimeValue() {
		return getDate(0);
	}

	/**
	 * @param dtv the dtv to set
	 */
	public void setDateTimeValue(Date dtv) {
		setObject(0, dtv, VT_DATETIME);
	}

	/**
	 * @return the dtv2
	 */
	public Date getDateTimeValue2() {
		return getDate(1);
	}

	/**
	 * @param dtv2 the dtv2 to set
	 */
	public void setDateTimeValue2(Date dtv2) {
		setObject(1, dtv2, VT_DATETIME);
	}

	/**
	 * @return the dvs
	 */
	@SuppressWarnings("unchecked")
	public List<Date> getDateValues() {
		return (List<Date>)values;
	}

	/**
	 * @param dvs the dvs to set
	 */
	public void setDateValues(List<Date> dvs) {
		this.type = VT_DATE;
		this.values = dvs;
	}

	/**
	 * @return the dv
	 */
	public Date getDateValue() {
		return getDate(0);
	}

	/**
	 * @param dv the dv to set
	 */
	public void setDateValue(Date dv) {
		setObject(0, dv, VT_DATE);
	}

	/**
	 * @return the dv2
	 */
	public Date getDateValue2() {
		return getDate(1);
	}

	/**
	 * @param dv2 the dv2 to set
	 */
	public void setDateValue2(Date dv2) {
		setObject(1, dv2, VT_DATE);
	}

	/**
	 * @return the tvs
	 */
	@SuppressWarnings("unchecked")
	public List<Date> getTimeValues() {
		return (List<Date>)values;
	}

	/**
	 * @param tvs the tvs to set
	 */
	public void setTimeValues(List<Date> tvs) {
		this.type = VT_TIME;
		this.values = tvs;
	}

	/**
	 * @return the tv
	 */
	public Date getTimeValue() {
		return getDate(0);
	}

	/**
	 * @param tv the tv to set
	 */
	public void setTimeValue(Date tv) {
		setObject(0, tv, VT_TIME);
	}

	/**
	 * @return the tv2
	 */
	public Date getTimeValue2() {
		return getDate(1);
	}

	/**
	 * @param tv2 the tv2 to set
	 */
	public void setTimeValue2(Date tv2) {
		setObject(1, tv2, VT_TIME);
	}

	/**
	 * @return the numberValues
	 */
	@SuppressWarnings("unchecked")
	public List<Number> getNumberValues() {
		return (List<Number>)values;
	}

	/**
	 * @param numberValues the numberValues to set
	 */
	public void setNumberValues(List<Number> numberValues) {
		this.type = VT_NUMBER;
		this.values = numberValues;
	}

	/**
	 * @return the numberValue
	 */
	public Number getNumberValue() {
		return getNumber(0);
	}

	/**
	 * @param numberValue the numberValue to set
	 */
	public void setNumberValue(Number numberValue) {
		setObject(0, numberValue, VT_NUMBER);
	}

	/**
	 * @return the numberValue2
	 */
	public Number getNumberValue2() {
		return getNumber(1);
	}

	/**
	 * @param numberValue2 the numberValue2 to set
	 */
	public void setNumberValue2(Number numberValue2) {
		setObject(1, numberValue2, VT_NUMBER);
	}

	/**
	 * @return the stringValues
	 */
	@SuppressWarnings("unchecked")
	public List<String> getStringValues() {
		return (List<String>)values;
	}

	/**
	 * @param stringValues the stringValues to set
	 */
	public void setStringValues(List<String> stringValues) {
		this.type = VT_STRING;
		this.values = stringValues;
	}

	/**
	 * @return the stringValue
	 */
	public String getStringValue() {
		return getString(0);
	}

	/**
	 * @param stringValue the stringValue to set
	 */
	public void setStringValue(String stringValue) {
		setObject(0, stringValue, VT_STRING);
	}

	/**
	 * @return the stringValue2
	 */
	public String getStringValue2() {
		return getString(1);
	}

	/**
	 * @param stringValue2 the stringValue2 to set
	 */
	public void setStringValue2(String stringValue2) {
		setObject(1, stringValue2, VT_STRING);
	}

	//////////////////////////////////////////////////////////
	// short name
	//////////////////////////////////////////////////////////
	/**
	 * @return the type
	 */
	public String getT() {
		return getType();
	}

	/**
	 * @return the N
	 */
	public String getN() {
		return getName();
	}

	/**
	 * @param n the name to set
	 */
	public void setN(String n) {
		setName(n);
	}

	/**
	 * @return the comparison
	 */
	@Validates(@Validate(value=Validators.CONSTANT, params=COMPARATORS, msgId=Validators.MSGID_CONSTANT))
	public String getC() {
		return getComparator();
	}

	/**
	 * @param c the c to set
	 */
	public void setC(String c) {
		setComparator(c);
	}

	/**
	 * @return the values
	 */
	public List getVs() {
		return getValues();
	}

	/**
	 * @return the value
	 */
	public Object getV() {
		return getValue();
	}

	/**
	 * @param value the value to set
	 */
	public void setV(Object value) {
		setValue(value);
	}

	/**
	 * @return the value2
	 */
	public Object getV2() {
		return getValue2();
	}

	/**
	 * @param value2 the value2 to set
	 */
	public void setV2(Object value2) {
		setValue2(value2);
	}

	/**
	 * @return the bvs
	 */
	@Validates(@Validate(value=Validators.CAST, msgId=Validators.MSGID_CAST_BOOLEAN))
	public List<Boolean> getBvs() {
		return getBooleanValues();
	}

	/**
	 * @param bvs the bvs to set
	 */
	public void setBvs(List<Boolean> bvs) {
		setBooleanValues(bvs);
	}

	/**
	 * @return the bv
	 */
	@Validates(@Validate(value=Validators.CAST, msgId=Validators.MSGID_CAST_BOOLEAN))
	public Boolean getBv() {
		return getBooleanValue();
	}

	/**
	 * @param bv the bv to set
	 */
	public void setBv(Boolean bv) {
		setBooleanValue(bv);
	}

	/**
	 * @return the bv2
	 */
	@Validates(@Validate(value=Validators.CAST, msgId=Validators.MSGID_CAST_BOOLEAN))
	public Boolean getBv2() {
		return getBooleanValue2();
	}

	/**
	 * @param bv2 the bv2 to set
	 */
	public void setBv2(Boolean bv2) {
		setBooleanValue2(bv2);
	}

	/**
	 * @return the evs
	 */
	@Validates(@Validate(value=Validators.CAST, msgId=Validators.MSGID_CAST_DATETIME))
	public List<Date> getEvs() {
		return getDateTimeValues();
	}

	/**
	 * @param evs the evs to set
	 */
	public void setEvs(List<Date> evs) {
		setDateValues(evs);
	}

	/**
	 * @return the ev
	 */
	@Validates(@Validate(value=Validators.CAST, msgId=Validators.MSGID_CAST_DATETIME))
	public Date getEv() {
		return getDateTimeValue();
	}

	/**
	 * @param ev the ev to set
	 */
	public void setEv(Date ev) {
		setDateTimeValue(ev);
	}

	/**
	 * @return the ev2
	 */
	@Validates({
		@Validate(value=Validators.CAST, msgId=Validators.MSGID_CAST_DATETIME),
		@Validate(value=Validators.EL, params="{ el: 'top.value > top.parent.value.ev' }", msgId=Validators.MSGID_DATETIME_TO)
	})
	public Date getEv2() {
		return getDateTimeValue2();
	}

	/**
	 * @param ev2 the ev to set
	 */
	public void setEv2(Date ev2) {
		setDateTimeValue2(ev2);
	}

	/**
	 * @return the dvs
	 */
	@Validates(@Validate(value=Validators.CAST, msgId=Validators.MSGID_CAST_DATE))
	public List<Date> getDvs() {
		return getDateValues();
	}

	/**
	 * @param dvs the dvs to set
	 */
	public void setDvs(List<Date> dvs) {
		setDateValues(dvs);
	}

	/**
	 * @return the dv
	 */
	@Validates(@Validate(value=Validators.CAST, msgId=Validators.MSGID_CAST_DATE))
	public Date getDv() {
		return getDateValue();
	}

	/**
	 * @param dv the dv to set
	 */
	public void setDv(Date dv) {
		setDateValue(dv);
	}

	/**
	 * @return the dv2
	 */
	@Validates({
		@Validate(value=Validators.CAST, msgId=Validators.MSGID_CAST_DATE),
		@Validate(value=Validators.EL, params="{ el: 'top.value > top.parent.value.dv' }", msgId=Validators.MSGID_DATE_TO)
	})
	public Date getDv2() {
		return getDateValue2();
	}

	/**
	 * @param dv2 the dv to set
	 */
	public void setDv2(Date dv2) {
		setDateValue2(dv2);
	}

	/**
	 * @return the tvs
	 */
	@Validates(@Validate(value=Validators.CAST, msgId=Validators.MSGID_CAST_TIME))
	public List<Date> getTvs() {
		return getTimeValues();
	}

	/**
	 * @param tvs the tvs to set
	 */
	public void setTvs(List<Date> tvs) {
		setTimeValues(tvs);
	}

	/**
	 * @return the tv
	 */
	@Validates(@Validate(value=Validators.CAST, msgId=Validators.MSGID_CAST_TIME))
	public Date getTv() {
		return getTimeValue();
	}

	/**
	 * @param tv the tv to set
	 */
	public void setTv(Date tv) {
		setTimeValue(tv);
	}

	/**
	 * @return the tv2
	 */
	@Validates({
		@Validate(value=Validators.CAST, msgId=Validators.MSGID_CAST_TIME),
		@Validate(value=Validators.EL, params="{ el: 'top.value > top.parent.value.tv' }", msgId=Validators.MSGID_TIME_TO)
	})
	public Date getTv2() {
		return getTimeValue2();
	}

	/**
	 * @param tv2 the tv to set
	 */
	public void setTv2(Date tv2) {
		setTimeValue2(tv2);
	}

	/**
	 * @return the nvs
	 */
	@Validates(@Validate(value=Validators.CAST, msgId=Validators.MSGID_CAST_NUMBER))
	public List<Number> getNvs() {
		return getNumberValues();
	}

	/**
	 * @param nvs the nvs to set
	 */
	public void setNvs(List<Number> nvs) {
		setNumberValues(nvs);
	}

	/**
	 * @return the nv
	 */
	@Validates(@Validate(value=Validators.CAST, msgId=Validators.MSGID_CAST_NUMBER))
	public Number getNv() {
		return getNumberValue();
	}

	/**
	 * @param nv the nv to set
	 */
	public void setNv(Number nv) {
		setNumberValue(nv);
	}

	/**
	 * @return the nv2
	 */
	@Validates({
		@Validate(value=Validators.CAST, msgId=Validators.MSGID_CAST_NUMBER),
		@Validate(value=Validators.EL, params="{ el: 'top.value > top.parent.value.nv' }", msgId=Validators.MSGID_NUMBER_TO)
	})
	public Number getNv2() {
		return getNumberValue2();
	}

	/**
	 * @param nv2 the nv2 to set
	 */
	public void setNv2(Number nv2) {
		setNumberValue2(nv2);
	}

	/**
	 * @return the svs
	 */
	public List<String> getSvs() {
		return getStringValues();
	}

	/**
	 * @param svs the svs to set
	 */
	public void setSvs(List<String> svs) {
		setStringValues(svs);
	}

	/**
	 * @return the sv
	 */
	public String getSv() {
		return getStringValue();
	}

	/**
	 * @param sv the sv to set
	 */
	public void setSv(String sv) {
		setStringValue(sv);
	}

	/**
	 * @return the sv2
	 */
	public String getSv2() {
		return getStringValue2();
	}

	/**
	 * @param sv2 the sv2 to set
	 */
	public void setSv2(String sv2) {
		setStringValue(sv2);
	}

	/**
	 * @return a string representation of the object.
	 */
	@Override
	public String toString() {
		return Objects.toStringBuilder()
				.append("name", name)
				.append("comparator", comparator)
				.append("values", values)
				.append("type", type)
				.toString();
	}

	/**
	 * @return a hash code value for this object.
	 */
	@Override
	public int hashCode() {
		return Objects.hashCodes(name, comparator, values, type);
	}

	/**
	 * @return <code>true</code> if this object is the same as the obj argument; <code>false</code>
	 *         otherwise.
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		
		Filter rhs = (Filter) obj;
		return Objects.equalsBuilder()
				.append(name, rhs.name)
				.append(comparator, rhs.comparator)
				.append(values, rhs.values)
				.append(type, rhs.type)
				.isEquals();
	}

	/**
	 * Clone
	 * @return Clone Object
	 */
	@SuppressWarnings("unchecked")
	public Object clone() {
		Filter clone = new Filter();
		
		clone.name = this.name;
		clone.comparator = this.comparator;
		clone.type = this.type;
		clone.values = new ArrayList(this.values);

		return clone;
	}

	/**
	 * normalize
	 */
	public static void normalize(Map<String, Filter> filters) {
		if (filters != null) {
			List<String> ks = new ArrayList<String>();
			for (Entry<String, Filter> e : filters.entrySet()) {
				String n = e.getKey();
				Filter f = e.getValue();
				if (Strings.isBlank(n) || f == null) {
					ks.add(n);
				}
			}
			for (String k : ks) {
				filters.remove(k);
			}
		}
	}
}
