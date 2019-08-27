package panda.idx.gae;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.google.appengine.api.search.Query;
import com.google.appengine.api.search.QueryOptions;
import com.google.appengine.api.search.SortExpression;
import com.google.appengine.api.search.SortExpression.SortDirection;
import com.google.appengine.api.search.SortOptions;

import panda.idx.IQuery;
import panda.idx.IndexException;
import panda.lang.Asserts;
import panda.lang.StringEscapes;
import panda.lang.Strings;
import panda.lang.time.DateTimes;

public class GaeQuery implements IQuery {
	private List<SortExpression> sorts;
	private StringBuilder query;
	private String field;
	private long start;
	private long limit;
	private String andor = " AND ";

	public GaeQuery() {
		sorts = new ArrayList<SortExpression>();
		query = new StringBuilder();
	}

	@Override
	public IQuery clear() {
		start = 0L;
		limit = 0L;
		query.setLength(0);
		sorts.clear();
		return this;
	}

	@Override
	public IQuery and() {
		andor = " AND ";
		return this;
	}

	@Override
	public IQuery or() {
		andor = " OR ";
		return this;
	}

	@Override
	public IQuery field(String field) {
		this.field = field;
		return this;
	}

	@Override
	public IQuery match(String text) {
		String[] ss = Strings.split(text);
		return eq(ss);
	}

	private void _andor() {
		if (query.length() > 0) {
			query.append(andor);
		}
	}

	private void _not() {
		query.append("NOT ");
	}
	
	private void _name() {
		query.append(field);
	}
	
	private void _add(String s) {
		query.append(s);
	}

	private void _add(char c) {
		query.append(c);
	}

	private void _val(Object value) {
		if (value instanceof CharSequence) {
			CharSequence cs = (CharSequence)value;
			if (Strings.contains(cs, '"')) {
				query.append('"').append(StringEscapes.escapeChars(cs, "\"")).append('"');
			}
			else {
				query.append(cs);
			}
		}
		else if (value instanceof Number) {
			query.append(value);
		}
		else if (value instanceof Date) {
			query.append(DateTimes.isoDateFormat().format(value));
		}
		else {
			throw new IllegalArgumentException("Invalid value: " + value);
		}
	}

	private IQuery _eq(Object[] values) {
		_andor();
		_name();
		_add("=(");
		boolean first = true;
		for (Object v : values) {
			if (!first) {
				_add(' ');
				first = true;
			}
			_val(v);
		}
		query.append(')');
		return this;
	}
	
	@Override
	public IQuery eq(String... values) {
		return _eq(values);
	}

	@Override
	public IQuery eq(Number... values) {
		return _eq(values);
	}

	@Override
	public IQuery eq(Date... values) {
		return _eq(values);
	}

	private IQuery _ne(Object[] values) {
		_andor();
		_not();
		_name();
		_add("=(");
		boolean first = true;
		for (Object v : values) {
			if (!first) {
				_add(' ');
				first = true;
			}
			_val(v);
		}
		_add(')');
		return this;
	}
	
	@Override
	public IQuery ne(String... values) {
		return _ne(values);
	}

	@Override
	public IQuery ne(Number... values) {
		return _ne(values);
	}

	@Override
	public IQuery ne(Date... values) {
		return _ne(values);
	}

	private IQuery _lt(Object value) {
		_andor();
		_name();
		_add('<');
		_val(value);
		return this;
	}
	
	@Override
	public IQuery lt(Number value) {
		return _lt(value);
	}

	@Override
	public IQuery lt(Date value) {
		return _lt(value);
	}

	private IQuery _le(Object value) {
		_andor();
		_name();
		_add("<=");
		_val(value);
		return this;
	}

	@Override
	public IQuery le(Number value) {
		return _le(value);
	}

	@Override
	public IQuery le(Date value) {
		return _le(value);
	}

	private IQuery _gt(Object value) {
		_andor();
		_name();
		_add('>');
		_val(value);
		return this;
	}

	@Override
	public IQuery gt(Number value) {
		return _gt(value);
	}

	@Override
	public IQuery gt(Date value) {
		return _gt(value);
	}

	private IQuery _ge(Object value) {
		_andor();
		_name();
		_add(">=");
		_val(value);
		return this;
	}

	@Override
	public IQuery ge(Number value) {
		return _ge(value);
	}

	@Override
	public IQuery ge(Date value) {
		return _ge(value);
	}

	//---------------------------------------------------------------
	// start & limit
	//
	/**
	 * @param start the start to set
	 * @return this
	 */
	public IQuery start(long start) {
		Asserts.isTrue(start >= 0, "The start must >= 0");
		this.start = start;
		return this;
	}

	/**
	 * @param limit the limit to set
	 * @return this
	 */
	public IQuery limit(long limit) {
		Asserts.isTrue(limit >= 0, "The limit must >= 0");
		this.limit = limit;
		return this;
	}

	@Override
	public IQuery sort(SortType type, boolean desc) {
		SortExpression.Builder seb = SortExpression.newBuilder();
		seb.setExpression(field);
		seb.setDirection(desc ? SortDirection.DESCENDING : SortDirection.ASCENDING);
		switch (type) {
		case SCORE:
		case DOC:
		case STRING:
			seb.setDefaultValue("");
			break;
		case INT:
		case FLOAT:
		case LONG:
		case DOUBLE:
			seb.setDefaultValueNumeric(0);
			break;
		case DATE:
			seb.setDefaultValueDate(new Date(0));
			break;
		default:
			throw new IndexException("Invalid sort type: " + type);
		}

		SortExpression se = seb.build();
		sorts.add(se);
		return this;
	}

	/**
	 * @return query
	 */
	protected Query buildQuery() {
		SortOptions.Builder sob = SortOptions.newBuilder();
		for (SortExpression se : sorts) {
			sob.addSortExpression(se);
		}
		sob.setLimit((int)limit);
		
		SortOptions so = sob.build();

		// Build the QueryOptions
		QueryOptions.Builder oob = QueryOptions.newBuilder();
		oob.setOffset((int)start);
		oob.setLimit((int)limit);
		oob.setSortOptions(so);
		QueryOptions oo = oob.build();
		
		return Query.newBuilder().setOptions(oo).build(query.toString());
	}

	@Override
	public String toString() {
		return "GaeQuery [query=" + query + ", start=" + start + ", limit=" + limit + "]";
	}
}
