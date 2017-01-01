package panda.dao.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * Join entity
 */
public class EntityJoin {
	private String name;
	private String type;
	private Entity<?> target;
	private List<EntityField> keys = new ArrayList<EntityField>();
	private List<EntityField> refs = new ArrayList<EntityField>();
	
	/**
	 * constructor
	 */
	protected EntityJoin() {
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
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * @return the target
	 */
	public Entity<?> getTarget() {
		return target;
	}

	/**
	 * @param target the target to set
	 */
	public void setTarget(Entity<?> target) {
		this.target = target;
	}

	/**
	 * @return the keys
	 */
	public List<EntityField> getKeys() {
		return keys;
	}
	/**
	 * @param field the field to add
	 */
	protected void addKeyField(EntityField field) {
		keys.add(field);
	}

	/**
	 * @return the refs
	 */
	public List<EntityField> getRefs() {
		return refs;
	}
	/**
	 * @param field the field to add
	 */
	protected void addRefField(EntityField field) {
		refs.add(field);
	}

}
