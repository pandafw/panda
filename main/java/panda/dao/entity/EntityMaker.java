package panda.dao.entity;

/**
 * Entity Maker
 * @author yf.frank.wang@gmail.com
 */
public interface EntityMaker {

	/**
	 * 根据一个配置信息，生成一个新的 Entity 的实例
	 * 
	 * @param type Entity 的配置信息
	 * @return Entity 实例
	 */
	<T> Entity<T> make(Class<T> type);

}
