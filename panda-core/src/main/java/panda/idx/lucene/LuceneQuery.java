package panda.idx.lucene;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.SortField;
import org.apache.lucene.search.TopDocsCollector;
import org.apache.lucene.search.TopFieldCollector;
import org.apache.lucene.search.TopScoreDocCollector;

import panda.idx.IQuery;
import panda.idx.IndexException;
import panda.lang.Collections;

public class LuceneQuery extends IQuery {
	protected Analyzer analyzer;
	protected QueryParser parser;
	protected List<SortField> sorts;

	public LuceneQuery(Analyzer analyzer) {
		this.analyzer = analyzer;
	}
	
	@Override
	public IQuery field(String field) {
		if (parser == null) {
			parser = new QueryParser(field, analyzer);
		}
		return super.field(field);
	}
	
	@Override
	public IQuery value(String value) {
		addSpace();
		query.append('"').append(Lucenes.escapeText(value)).append('"');
		return this;
	}
	
	@Override
	public IQuery sort(String field, SortType type, boolean desc) {
		if (sorts == null) {
			sorts = new ArrayList<SortField>();
		}
		
		SortField sf = new SortField(field, toSortFieldType(type), desc);
		sorts.add(sf);
		return null;
	}

	protected SortField.Type toSortFieldType(SortType type) {
		switch (type) {
		case SCORE:
			return SortField.Type.SCORE;
		case DOC:
			return SortField.Type.DOC;
		case STRING:
			return SortField.Type.STRING;
		case INT:
			return SortField.Type.INT;
		case FLOAT:
			return SortField.Type.FLOAT;
		case DATE:
		case LONG:
			return SortField.Type.LONG;
		case DOUBLE:
			return SortField.Type.DOUBLE;
		default:
			throw new IndexException("Invalid sort type: " + type);
		}
	}

	/**
	 * @return query
	 */
	protected Query buildQuery() {
		try {
			return parser.parse(getQuery());
		}
		catch (ParseException e) {
			throw new IndexException("Failed to parse query: " + getQuery(), e);
		}
	}
	
	protected TopDocsCollector buildCollector() {
		TopDocsCollector collector;
		if (Collections.isEmpty(sorts)) {
			collector = TopScoreDocCollector.create((int)(getLimit()));
		}
		else {
			Sort sort = new Sort(sorts.toArray(new SortField[0]));
			try {
				collector = TopFieldCollector.create(sort, (int)(getLimit()), true, false, false);
			}
			catch (IOException e) {
				throw new IndexException("Failed to create TopFieldCollector: " + sorts);
			}
		}
		return collector;
	}
}
