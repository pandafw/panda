package panda.dao.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * Foreign key of entity
 */
public class EntityFKey {
	private String name;
	private Entity<?> reference;
	private List<EntityField> fields = new ArrayList<EntityField>();
	private String onUpdate;
	private String onDelete;
	
	/**
	 * constructor
	 */
	protected EntityFKey() {
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
	 * @return the reference
	 */
	public Entity<?> getReference() {
		return reference;
	}

	/**
	 * @param reference the reference to set
	 */
	protected void setReference(Entity<?> reference) {
		this.reference = reference;
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

	/**
	 * @return the onUpdate
	 */
	public String getOnUpdate() {
		return onUpdate;
	}

	/**
	 * @param onUpdate the onUpdate to set
	 */
	protected void setOnUpdate(String onUpdate) {
		this.onUpdate = onUpdate;
	}

	/**
	 * @return the onDelete
	 */
	public String getOnDelete() {
		return onDelete;
	}

	/**
	 * @param onDelete the onDelete to set
	 */
	protected void setOnDelete(String onDelete) {
		this.onDelete = onDelete;
	}
}
