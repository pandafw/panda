package panda.dao;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import panda.bean.Beans;
import panda.castor.Castors;
import panda.dao.entity.AnnotationEntityMaker;
import panda.dao.entity.Entity;
import panda.dao.entity.EntityDao;
import panda.dao.entity.EntityMaker;
import panda.lang.Collections;


/**
 * !! thread-safe !!
 * @author yf.frank.wang@gmail.com
 */
public abstract class DaoClient {
	protected String name;
	protected Map<Class<?>, Entity> entities;
	protected Beans beans;
	protected Castors castors;
	protected EntityMaker entityMaker;

	public DaoClient() {
		entities = new ConcurrentHashMap<Class<?>, Entity>();
		castors = Castors.me();
		beans = castors.getBeans();
		entityMaker = new AnnotationEntityMaker(this);
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
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
	 * @return the castors
	 */
	public Castors getCastors() {
		return castors;
	}

	/**
	 * @param castors the castors to set
	 */
	public void setCastors(Castors castors) {
		this.castors = castors;
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
					entity = entityMaker.make(type);
					entities.put(type, entity);
				}
			}
		}
		return entity;
	}

	/**
	 * @param type record type
	 * @return the entity
	 */
	public <T> EntityDao<T> getEntityDao(Class<T> type) {
		return getDao().getEntityDao(type);
	}

	/**
	 * @param type record type
	 * @param param argument used for dynamic table
	 * @return the entity
	 */
	public <T> EntityDao<T> getEntityDao(Class<T> type, Object param) {
		return getDao().getEntityDao(type, param);
	}

	/**
	 * @return datebase meta
	 */
	public abstract DatabaseMeta getDatabaseMeta();

	/**
	 * @return a Dao instance.
	 */
	public abstract Dao getDao();
}
