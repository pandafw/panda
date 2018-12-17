package panda.mvc.util;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map.Entry;

import panda.dao.entity.Entity;
import panda.dao.entity.EntityField;
import panda.dao.query.DataQuery;
import panda.dao.query.Join;
import panda.dao.query.Query;
import panda.lang.Collections;
import panda.lang.Strings;
import panda.lang.time.DateTimes;
import panda.log.Log;
import panda.log.Logs;
import panda.mvc.bean.Filter;
import panda.mvc.bean.Queryer;
import panda.mvc.bean.Sorter;

public abstract class DataQueryHelper {
	private final static Log log = Logs.getLog(DataQueryHelper.class);

	/**
	 * add query columns
	 * @param dq DataQuery
	 * @param names column/field names
	 */
	public static void addQueryColumns(DataQuery<?> dq, Collection<String> names) {
		if (Collections.isNotEmpty(names)) {
			dq.excludeAll();
			dq.includePrimayKeys();
			dq.include(names);
		}
	}

	/**
	 * @param entity entity
	 * @param dq DataQuery
	 * @param qr queryer
	 */
	@SuppressWarnings("unchecked")
	public static void addQueryFilters(Entity<?> entity, DataQuery<?> dq, Queryer qr) {
		qr.normalize();
		if (Collections.isEmpty(qr.getFilters())) {
			return;
		}
		
		if (qr.isOr()) {
			dq.or();
		}
		else {
			dq.and();
		}
		
		for (Entry<String, Filter> e : qr.getFilters().entrySet()) {
			Filter f = e.getValue();
			if (f == null) {
				continue;
			}

			List<?> values = f.getValues();
			if (values == null || values.isEmpty()) {
				continue;
			}

			Object value = values.get(0);
			if (value == null
					&& !Filter.IN.equals(f.getComparator())
					&& !Filter.BETWEEN.equals(f.getComparator())) {
				continue;
			}

			String name = Strings.isEmpty(f.getName()) ? e.getKey() : f.getName();
			EntityField ef = entity.getField(name);
			if (ef == null) {
				if (log.isWarnEnabled()) {
					log.warn("Invalid filter field [" + name + "] of entity " + entity);
				}
				continue;
			}
			if (!ef.isPersistent()) {
				if (qr.isOr()) {
					if (log.isDebugEnabled()) {
						log.debug("SKIP filter(OR) of non persistent field [" + name + "] of entity " + entity);
					}
					continue;
				}
				
				if (ef.isJoinField()) {
					Join join = dq.getJoin(ef.getJoinName());
					if (join == null) {
						if (log.isDebugEnabled()) {
							log.debug("SKIP filter of join field [" + name + "] of entity " + entity);
						}
						continue;
					}
					
					join.setType(Join.INNER);
					Query<?> jq = join.getQuery();
					DataQuery jgq;
					if (jq instanceof DataQuery) {
						jgq = (DataQuery)jq;
					}
					else {
						jgq = new DataQuery(jq);
						join.setQuery(jgq);
					}

					addQueryFilter(jgq, f, ef.getJoinField(), value, values);
					continue;
				}

				if (log.isDebugEnabled()) {
					log.debug("SKIP filter of non persistent field [" + name + "] of entity " + entity);
				}
				continue;
			}
			
			addQueryFilter(dq, f, name, value, values);
		}

		dq.end();
	}

	/**
	 * add filter to queryer
	 * @param dq DataQuery
	 * @param f filter
	 * @param name field name
	 * @param value field value
	 * @param values field values
	 */
	protected static void addQueryFilter(DataQuery<?> dq, Filter f, String name, Object value, List<?> values) {
		if (Filter.EQUAL.equals(f.getComparator())) {
			if (value instanceof Date && Filter.VT_DATE.equals(f.getType())) {
				Date value2 = DateTimes.addMilliseconds(DateTimes.zeroCeiling((Date)value), -1);
				dq.between(name, value, value2);
			}
			else {
				dq.eq(name, value);
			}
		}
		else if (Filter.NOT_EQUAL.equals(f.getComparator())) {
			if (value instanceof Date && Filter.VT_DATE.equals(f.getType())) {
				Date value2 = DateTimes.addMilliseconds(DateTimes.zeroCeiling((Date)value), -1);
				dq.nbetween(name, value, value2);
			}
			else {
				dq.ne(name, value);
			}
		}
		else if (Filter.LESS_THAN.equals(f.getComparator())) {
			dq.lt(name, value);
		}
		else if (Filter.LESS_EQUAL.equals(f.getComparator())) {
			dq.le(name, value);
		}
		else if (Filter.GREATER_THAN.equals(f.getComparator())) {
			dq.gt(name, value);
		}
		else if (Filter.GREATER_EQUAL.equals(f.getComparator())) {
			dq.ge(name, value);
		}
		else if (Filter.LIKE.equals(f.getComparator())) {
			dq.like(name, value.toString());
		}
		else if (Filter.NOT_LIKE.equals(f.getComparator())) {
			dq.nlike(name, value.toString());
		}
		else if (Filter.MATCH.equals(f.getComparator())) {
			dq.match(name, value.toString());
		}
		else if (Filter.NOT_MATCH.equals(f.getComparator())) {
			dq.nmatch(name, value.toString());
		}
		else if (Filter.LEFT_MATCH.equals(f.getComparator())) {
			dq.lmatch(name, value.toString());
		}
		else if (Filter.NOT_LEFT_MATCH.equals(f.getComparator())) {
			dq.nlmatch(name, value.toString());
		}
		else if (Filter.RIGHT_MATCH.equals(f.getComparator())) {
			dq.rmatch(name, value.toString());
		}
		else if (Filter.NOT_RIGHT_MATCH.equals(f.getComparator())) {
			dq.nrmatch(name, value.toString());
		}
		else if (Filter.IN.equals(f.getComparator())) {
			dq.in(name, values);
		}
		else if (Filter.NOT_IN.equals(f.getComparator())) {
			dq.nin(name, values);
		}
		else if (Filter.BETWEEN.equals(f.getComparator())) {
			Object v1 = values.get(0);
			Object v2 = values.size() > 1 ? values.get(1) : null;

			if (v1 == null && v2 == null) {
			}
			else if (v1 == null) {
				dq.le(name, v2);
			}
			else if (v2 == null) {
				dq.ge(name, v1);
			}
			else {
				if (v1 instanceof Date && v2 instanceof Date && Filter.VT_DATE.equals(f.getType())) {
					v2 = DateTimes.addMilliseconds(DateTimes.zeroCeiling((Date)v2), -1);
				}
				dq.between(name, v1, v2);
			}
		}
	}

	/**
	 * @param dq query
	 * @param sorter sorter
	 */
	public void addQueryOrders(DataQuery<?> dq, Sorter sorter) {
		addQueryOrders(dq, sorter, null);
	}
	
	/**
	 * @param dq query
	 * @param sorter sorter
	 * @param fields fields
	 */
	public static void addQueryOrders(DataQuery<?> dq, Sorter sorter, Collection<String> fields) {
		if (Strings.isNotEmpty(sorter.getColumn())) {
			if (Collections.isEmpty(fields) || Collections.contains(fields, sorter.getColumn())) {
				if (Strings.isEmpty(sorter.getDirection())) {
					sorter.setDirection(Sorter.ASC);
				}
				dq.orderBy(sorter.getColumn(), sorter.getDirection());
			}
		}
	}

}
