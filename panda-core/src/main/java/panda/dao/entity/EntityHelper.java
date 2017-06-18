package panda.dao.entity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import panda.cast.Castors;
import panda.dao.Dao;
import panda.dao.query.GenericQuery;
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
		if (src == null || des == null) {
			return;
		}

		EntityField eid = entity.getIdentity();
		if (eid != null) {
			eid.setValue(des, eid.getValue(src));
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
	 * checkPrimaryKeys
	 * 
	 * @param dao the DAO
	 * @param entity the Entity
	 * @param data the data
	 * @return true if check successfully
	 */
	public static <T> boolean checkPrimaryKeys(Dao dao, Entity<T> entity, T data) {
		EntityField eid = entity.getIdentity(); 
		if (eid == null) {
			List<EntityField> pks = entity.getPrimaryKeys();

			GenericQuery<T> q = new GenericQuery<T>(entity);
			for (EntityField pk : pks) {
				Object dv = pk.getValue(data);
				if (dv == null) {
					return false;
				}

				q.equalTo(pk.getName(), dv);
			}

			if (dao.exists(entity, data)) {
				return false;
			}
		}
		else {
			Object id = eid.getValue(data);
			if (dao.isValidIdentity(id) && dao.exists(entity, data)) {
				return false;
			}
		}
		return true;
	}

	public static <T> boolean checkUniqueIndex(Dao dao, Entity<T> entity, T data, EntityIndex ei) {
		if (!ei.isUnique()) {
			return true;
		}

		boolean allNull = true;

		GenericQuery<T> q = new GenericQuery<T>(entity);
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
		
		GenericQuery<?> q = new GenericQuery(efk.getReference());

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
	 * Check Foreign Keys
	 * 
	 * @param dao DAO object
	 * @param entity the Entity
	 * @param data the data to check
	 * @return true if check successfully
	 */
	public static <T> List<EntityFKey> checkForeignKeys(Dao dao, Entity<T> entity, T data) {
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
}
