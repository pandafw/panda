package panda.dao;

import java.util.Map;

import panda.bean.Beans;
import panda.cast.Castors;
import panda.dao.entity.Entities;
import panda.dao.entity.Entity;
import panda.dao.entity.EntityDao;
import panda.dao.query.Query;
import panda.lang.Strings;


/**
 * !! thread-safe !!
 */
public abstract class DaoClient {
	protected Beans beans;
	protected Castors castors;
	protected Entities entities;

	protected String name;
	protected String prefix;
	protected int timeout;

	public DaoClient() {
		entities = Entities.i();
		castors = Castors.i();
		beans = castors.getBeans();
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return the prefix
	 */
	public String getPrefix() {
		return prefix;
	}

	/**
	 * @param prefix the prefix to set
	 */
	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	/**
	 * @return the timeout (seconds)
	 */
	public int getTimeout() {
		return timeout;
	}

	/**
	 * @param timeout the timeout (seconds) to set
	 */
	public void setTimeout(int timeout) {
		this.timeout = timeout;
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
	public Map<Class<?>, Entity<?>> getEntities() {
		return entities.getEntities();
	}

	/**
	 * @param type record type
	 * @return the entity
	 */
	public <T> Entity<T> getEntity(Class<T> type) {
		return entities.getEntity(type);
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


	/**
	 * @param query query
	 * @return the table name of query
	 */
	public String getTableName(Query<?> query) {
		if (Strings.isEmpty(prefix)) {
			return query.getTable();
		}
		return prefix + query.getTable();
	}

	/**
	 * @param entity entity
	 * @return the table name of entity
	 */
	public String getTableName(Entity<?> entity) {
		if (Strings.isEmpty(prefix)) {
			return entity.getTable();
		}
		return prefix + entity.getTable();
	}

	/**
	 * @param entity entity
	 * @return the view name of entity
	 */
	public String getViewName(Entity<?> entity) {
		if (Strings.isEmpty(prefix)) {
			return entity.getView();
		}
		return prefix + entity.getView();
	}
}
