package panda.dao.sql;

import java.util.ArrayList;
import java.util.List;

import panda.lang.Objects;

/**
 * @author yf.frank.wang@gmail.com
 */
public class Sql {
	private StringBuilder sql = new StringBuilder();
	private List<Object> params;

	public Sql insert(int i, CharSequence s) {
		sql.insert(i, s);
		return this;
	}

	public Sql insert(int i, char c) {
		sql.insert(i, c);
		return this;
	}

	public Sql append(Sql rhs) {
		sql.append(rhs.getSql());
		if (rhs.params != null) {
			if (params == null) {
				params = new ArrayList<Object>();
			}
			params.addAll(rhs.params);
		}
		return this;
	}
	
	public Sql append(CharSequence s) {
		sql.append(s);
		return this;
	}
	
	public Sql append(Object o) {
		sql.append(o);
		return this;
	}
	
	public Sql append(char c) {
		sql.append(c);
		return this;
	}
	
	public Sql setLength(int i) {
		sql.setLength(i);
		return this;
	}

	public Sql setCharAt(int i, char c) {
		sql.setCharAt(i, c);
		return this;
	}
	
	public int length() {
		return sql.length();
	}
	
	/**
	 * @return the sql
	 */
	public String getSql() {
		return sql.toString();
	}
	
	/**
	 * @return the sql
	 */
	public StringBuilder getSqlBuilder() {
		return sql;
	}
	
	/**
	 * @return the params
	 */
	public List<?> getParams() {
		return params;
	}
	
	/**
	 * @param param the parameter to add
	 */
	public Sql addParam(Object param) {
		if (params == null) {
			params = new ArrayList<Object>();
		}
		params.add(param);
		return this;
	}
	
	@Override
	public String toString() {
		return Objects.toStringBuilder()
				.append(sql)
				.append(params)
				.toString();
	}
}
