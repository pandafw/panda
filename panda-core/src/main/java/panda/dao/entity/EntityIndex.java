package panda.dao.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * 封装了实体的索引
 */
public class EntityIndex {
	private String name;
	private boolean unique;
	private boolean real;
	private List<EntityField> fields = new ArrayList<EntityField>();

	/**
	 * constructor
	 */
	protected EntityIndex() {
		super();
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
	protected void setName(String name) {
		this.name = name;
	}
	/**
	 * @return the unique
	 */
	public boolean isUnique() {
		return unique;
	}
	/**
	 * @param unique the unique to set
	 */
	protected void setUnique(boolean unique) {
		this.unique = unique;
	}
	/**
	 * @return the real
	 */
	public boolean isReal() {
		return real;
	}
	/**
	 * @param real the real to set
	 */
	protected void setReal(boolean real) {
		this.real = real;
	}

	/**
	 * @return the fields
	 */
	public List<EntityField> getFields() {
		return fields;
	}

	/**
	 * @param field the field to add
	 */
	protected void addField(EntityField field) {
		fields.add(field);
	}
}
