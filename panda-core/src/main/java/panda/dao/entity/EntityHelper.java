package panda.dao.entity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import panda.cast.Castors;
import panda.dao.Dao;
import panda.dao.query.DataQuery;
import panda.lang.Collections;
import panda.lang.Objects;

public abstract class EntityHelper {
	/**
	 * clear primary key value of data
	 * @param entity the Entity
	 * @param data the data
	 */
	public static <T> void clearPrimaryKeyValues(Entity<T> entity, T data) {
		if (data == null) {
			return;
		}
		
		List<EntityField> pks = entity.getPrimaryKeys();
		for (EntityField pk : pks) {
			Object value = Castors.scast(null, pk.getType());
			pk.setValue(data, value);
		}
	}

	/**
	 * clear identity value of data
	 * @param entity the Entity
	 * @param data the data
	 */
	public static <T> void clearIdentityValue(Entity<T> entity, T data) {
		if (data == null) {
			return;
		}
		
		EntityField eid = entity.getIdentity();
		if (eid != null) {
			Object value = Castors.scast(null, eid.getType());
			eid.setValue(data, value);
		}
	}

	/**
	 * copy identity value from source data to destination data
	 * @param entity entity
	 * @param src source data
	 * @param des destination data
	 */
	public static <T> void copyIdentityValue(Entity<T> entity, T src, T des) {
		if (src == null || des == null || src == des) {
			return;
		}

		EntityField eid = entity.getIdentity();
		if (eid != null) {
			eid.setValue(des, eid.getValue(src));
		}
	}

	/**
	 * copy primary key values from source data to destination data
	 * @param entity entity
	 * @param src source data
	 * @param des destination data
	 */
	public static <T> void copyPrimaryKeyValues(Entity<T> entity, T src, T des) {
		if (src == null || des == null || src == des) {
			return;
		}

		Collection<EntityField> efs = entity.getPrimaryKeys();
		for (EntityField ef : efs) {
			ef.setValue(des, ef.getValue(src));
		}
	}

	/**
	 * @param efs the EntityField list
	 * @param lhs the left object
	 * @param rhs the right object
	 * @return true if lhs is different with rhs
	 */
	public static boolean isDifferent(Collection<EntityField> efs, Object lhs, Object rhs) {
		for (EntityField ef : efs) {
			Object lv = ef.getValue(lhs);
			Object rv = ef.getValue(rhs);
			if (!Objects.equals(lv, rv)) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * @param entity the Entity
	 * @param data the data
	 * @return true if check successfully
	 */
	public static <T> boolean hasPrimaryKeyValues(Entity<T> entity, T data) {
		for (EntityField ef : entity.getPrimaryKeys()) {
			Object dv = ef.getValue(data);
			if (dv == null) {
				return false;
			}
		}

		return true;
	}

	/**
	 * find duplicate unique index
	 * @param dao dao
	 * @param entity entity
	 * @param data data
	 * @param sdat source data, check unique index data modified or not if sdat is supplied 
	 * @return duplicate unique index
	 */
	public static <T> EntityIndex findDuplicateUniqueIndex(Dao dao, Entity<T> entity, T data, T sdat) {
		Collection<EntityIndex> eis = entity.getIndexes();
		if (Collections.isEmpty(eis)) {
			return null;
		}
		
		for (EntityIndex ei : eis) {
			if (!ei.isUnique()) {
				continue;
			}

			if (sdat != null) {
				if (EntityHelper.isDifferent(ei.getFields(), data, sdat)) {
					if (!checkUniqueIndex(dao, entity, data, ei)) {
						return ei;
					}
				}
			}
			else {
				if (!checkUniqueIndex(dao, entity, data, ei)) {
					return ei;
				}
			}
		}
		return null;
	}

	public static <T> boolean checkUniqueIndex(Dao dao, Entity<T> entity, T data, EntityIndex ei) {
		if (!ei.isUnique()) {
			return true;
		}

		boolean allNull = true;

		DataQuery<T> q = new DataQuery<T>(entity);
		for (EntityField ef : ei.getFields()) {
			Object dv = ef.getValue(data);
			if (dv == null) {
				q.isNull(ef.getName());
			}
			else {
				allNull = false;
				q.equalTo(ef.getName(), dv);
			}
		}

		if (!allNull) {
			if (dao.count(q) > 0) {
				return false;
			}
		}
		
		return true;
	}

	/**
	 * findIncorrectForeignKey
	 * @param data
	 * @return true if check successfully
	 */
	public static <T> EntityFKey findIncorrectForeignKey(Dao dao, Entity<T> entity, T data) {
		Collection<EntityFKey> efks = entity.getForeignKeys();
		if (Collections.isEmpty(efks)) {
			return null;
		}
		
		for (EntityFKey efk : efks) {
			if (!checkForeignKey(dao, entity, data, efk)) {
				return efk;
			}
		}
		return null;
	}
	
	/**
	 * findIncorrectForeignKeys
	 * 
	 * @param dao DAO object
	 * @param entity the Entity
	 * @param data the data to check
	 * @return true if check successfully
	 */
	public static <T> List<EntityFKey> findIncorrectForeignKeys(Dao dao, Entity<T> entity, T data) {
		Collection<EntityFKey> efks = entity.getForeignKeys();
		if (Collections.isEmpty(efks)) {
			return null;
		}

		List<EntityFKey> eefks = new ArrayList<EntityFKey>();
		for (EntityFKey efk : efks) {
			if (!checkForeignKey(dao, entity, data, efk)) {
				eefks.add(efk);
			}
		}
		return eefks.isEmpty() ? null : eefks;
	}

	/**
	 * checkForeignKey
	 * 
	 * @param dao the DAO
	 * @param entity the Entity
	 * @param data the data
	 * @param efk the EntityFKey
	 * @return true if check successfully
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static <T> boolean checkForeignKey(Dao dao, Entity<T> entity, T data, EntityFKey efk) {
		boolean allNull = true;
		
		DataQuery<?> q = new DataQuery(efk.getReference());

		int i = 0;
		for (EntityField rf : efk.getReference().getPrimaryKeys()) {
			EntityField ef = efk.getFields().get(i);
			Object dv = ef.getValue(data);
			if (dv == null) {
				q.isNull(rf.getName());
			}
			else {
				allNull = false;
				q.equalTo(rf.getName(), dv);
			}
			i++;
		}

		if (!allNull) {
			q.setLimit(1);
			if (dao.count(q) < 1) {
				return false;
			}
		}
		
		return true;
	}
	
	/**
	 * check not null fields
	 * 
	 * @param entity the Entity
	 * @param data the data to check
	 * @return null fields
	 */
	public static <T> List<EntityField> checkNotNulls(Entity<T> entity, T data) {
		List<EntityField> nulls = new ArrayList<EntityField>();
		for (EntityField ef : entity.getFields()) {
			if (ef.isNotNull() && !ef.isAutoGenerate()) {
				Object o = ef.getValue(data);
				if (o == null) {
					nulls.add(ef);
				}
			}
		}
		return nulls.isEmpty() ? null : nulls;
	}
	
	/**
	 * check not null fields
	 * 
	 * @param efs the EntityField list
	 * @param data the data to check
	 * @return null fields
	 */
	public static <T> List<EntityField> checkNotNulls(List<EntityField> efs, T data) {
		List<EntityField> nulls = new ArrayList<EntityField>();
		for (EntityField ef : efs) {
			Object o = ef.getValue(data);
			if (o == null) {
				nulls.add(ef);
			}
		}
		return nulls.isEmpty() ? null : nulls;
	}

	/**
	 * fetch data by the entity field key.
	 * if key is null, then null will be returned.
	 * @param dao dao
	 * @param entity entity
	 * @param ef entity field
	 * @param data the data contains the query key
	 * @return the fetched data (first 1)
	 */
	public static <T> T fetchDataByField(Dao dao, Entity<T> entity, EntityField ef, T data) {
		Object dv = ef.getValue(data);
		if (dv == null) {
			return null;
		}
		
		DataQuery<T> q = new DataQuery<T>(entity);
		q.equalTo(ef.getName(), dv).limit(1);
		return dao.fetch(q);
	}
	
	/**
	 * fetch data by the keys of entity fields.
	 * if keys is null, then null will be returned.
	 * @param dao dao
	 * @param entity entity
	 * @param fs entity fields
	 * @param data the data contains the query keys
	 * @return the fetched data (first 1)
	 */
	public static <T> T fetchDataByFields(Dao dao, Entity<T> entity, String[] fs, T data) {
		Collection<EntityField> efs = entity.getFields(fs);
		return fetchDataByFields(dao, entity, efs, data);
	}
	
	/**
	 * fetch data by the keys of entity fields.
	 * if keys is null, then null will be returned.
	 * @param dao dao
	 * @param entity entity
	 * @param efs entity fields
	 * @param data the data contains the query keys
	 * @return the fetched data (first 1)
	 */
	public static <T> T fetchDataByFields(Dao dao, Entity<T> entity, Collection<?> efs, T data) {
		boolean allNull = true;

		DataQuery<T> q = new DataQuery<T>(entity);
		for (Object o : efs) {
			EntityField ef = null;
			if (o instanceof EntityField) {
				ef = (EntityField)o;
			}
			else if (o instanceof String) {
				ef = entity.getField((String)o);
			}
			else {
				throw new IllegalArgumentException("Invalid entity field: " + (o == null ? null : o.getClass()));
			}

			Object dv = ef.getValue(data);
			if (dv == null) {
				q.isNull(ef.getName());
			}
			else {
				allNull = false;
				q.equalTo(ef.getName(), dv);
			}
		}

		if (allNull) {
			return null;
		}
		
		q.limit(1);
		return dao.fetch(q);
	}
}
