package panda.dao.entity;

/**
 * Entity Maker
 */
public interface EntityMaker {

	/**
	 * Create a Entity instance
	 * 
	 * @param type Entity class
	 * @return Entity instance
	 */
	<T> Entity<T> create(Class<T> type);

	/**
	 * Initialize the Entity instance
	 * 
	 * @param entity Entity instance
	 */
	void initialize(Entity<?> entity);
}
