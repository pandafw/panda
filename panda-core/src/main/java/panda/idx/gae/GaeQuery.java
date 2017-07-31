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
import panda.lang.Strings;

public class GaeQuery extends IQuery {
	private List<SortExpression> sorts;

	public GaeQuery() {
		sorts = new ArrayList<SortExpression>();
	}

	@Override
	public IQuery value(String value) {
		addSpace();
		query.append('"').append(Strings.escapeChars(value, "\"")).append('"');
		return this;
	}

	@Override
	public IQuery sort(String field, SortType type, boolean desc) {
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
		sob.setLimit((int)getLimit());
		
		SortOptions so = sob.build();

		// Build the QueryOptions
		QueryOptions.Builder oob = QueryOptions.newBuilder();
		oob.setOffset((int)getStart());
		oob.setLimit((int)getLimit());
		oob.setSortOptions(so);
		QueryOptions oo = oob.build();
		
		Query query = Query.newBuilder().setOptions(oo).build(getQuery());
		return query;
	}
}
