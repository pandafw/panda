package panda.dao.entity;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import panda.bean.Beans;
import panda.lang.Collections;


/**
 * !! thread-safe !!
 */
public class Entities {
	private static Entities i = new Entities();
	
	public static Entities i() {
		return i;
	}

	protected Map<Class<?>, Entity> entities;
	protected Beans beans;
	protected EntityMaker entityMaker;
	
	public Entities() {
		entities = new ConcurrentHashMap<Class<?>, Entity>();
		beans = Beans.i();
		entityMaker = new AnnotationEntityMaker(this);
	}

	/**
	 * @return the entityMaker
	 */
	public EntityMaker getEntityMaker() {
		return entityMaker;
	}

	/**
	 * @param entityMaker the entityMaker to set
	 */
	public void setEntityMaker(EntityMaker entityMaker) {
		this.entityMaker = entityMaker;
	}
	
	/**
	 * @return the beans
	 */
	public Beans getBeans() {
		return beans;
	}

	/**
	 * @param beans the beans to set
	 */
	public void setBeans(Beans beans) {
		this.beans = beans;
	}

	/**
	 * @return the entities
	 */
	public Map<Class<?>, Entity> getEntities() {
		return Collections.unmodifiableMap(entities);
	}

	/**
	 * @param type record type
	 * @return the entity
	 */
	@SuppressWarnings("unchecked")
	public <T> Entity<T> getEntity(Class<T> type) {
		Entity<T> entity = entities.get(type);
		if (entity == null) {
			synchronized(this) {
				entity = entities.get(type);
				if (entity == null) {
					entity = entityMaker.create(type);
					entities.put(type, entity);
					entityMaker.initialize(entity);
				}
			}
		}
		return entity;
	}
}
