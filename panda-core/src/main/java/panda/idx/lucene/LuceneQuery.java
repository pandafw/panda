package panda.idx.lucene;

import java.io.IOException;
import java.io.StringReader;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.NumericRangeQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.SortField;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocsCollector;
import org.apache.lucene.search.TopFieldCollector;
import org.apache.lucene.search.TopScoreDocCollector;

import panda.idx.IQuery;
import panda.idx.IndexException;
import panda.lang.Asserts;
import panda.lang.Collections;
import panda.lang.Strings;
import panda.log.Log;
import panda.log.Logs;

public class LuceneQuery implements IQuery {
	private static final Log log = Logs.getLog(LuceneQuery.class);
	
	private Analyzer analyzer;
	private String field;
	private long start;
	private long limit;
	private List<SortField> sorts;
	private Object query;
	private Occur occur = Occur.MUST;

	public LuceneQuery(Analyzer analyzer) {
		this.analyzer = analyzer;
		sorts = new ArrayList<SortField>();
	}
	
	@Override
	public IQuery clear() {
		start = 0L;
		limit = 0L;
		query = null;
		sorts.clear();
		return this;
	}

	@Override
	public IQuery and() {
		occur = Occur.MUST;
		return this;
	}

	@Override
	public IQuery or() {
		occur = Occur.SHOULD;
		return this;
	}
	
	@Override
	public IQuery field(String field) {
		this.field = field;
		return this;
	}

	@Override
	public IQuery match(String text) {
		String[] ss;
		try {
			Set<String> tokens = new LinkedHashSet<String>();
			TokenStream stream = analyzer.tokenStream(null, new StringReader(text));
			CharTermAttribute cattr = stream.addAttribute(CharTermAttribute.class);
			stream.reset();
			while (stream.incrementToken()) {
				tokens.add(cattr.toString());
			}
			stream.end();
			stream.close();
			ss = tokens.toArray(new String[tokens.size()]);
		}
		catch (IOException e) {
			log.warn("Failed to use " + analyzer.getClass().getName() + " to find token: ", e);
			ss = Strings.split(Strings.lowerCase(text));
		}

		return eq(ss);
	}
	
	private void addQuery(Query q) {
		if (query == null) {
			query = q;
		}
		else if (query instanceof Query) {
			BooleanQuery.Builder bqb = new BooleanQuery.Builder();
			bqb.add((Query)query, occur);
			bqb.add(q, occur);
			query = bqb;
		}
		else {
			((BooleanQuery.Builder)query).add(q, occur);
		}
	}

	private Query numEqQuery(String field, Number num) {
		Query q;

		if (num instanceof Float) {
			q = NumericRangeQuery.newFloatRange(field, num.floatValue(), num.floatValue(), true, true);
		}
		else if (num instanceof Double || num instanceof BigDecimal) {
			q = NumericRangeQuery.newDoubleRange(field, num.doubleValue(), num.doubleValue(), true, true);
		}
		else if (num instanceof Long || num instanceof BigInteger) {
			q = NumericRangeQuery.newLongRange(field, num.longValue(), num.longValue(), true, true);
		}
		else {
			q = NumericRangeQuery.newIntRange(field, num.intValue(), num.intValue(), true, true);
		}

		return q;
	}

	@Override
	public IQuery eq(String... values) {
		if (values.length == 1) {
			addQuery(new TermQuery(new Term(field, values[0])));
		}
		else {
			BooleanQuery.Builder bqb = new BooleanQuery.Builder();
			for (String v : values) {
				bqb.add(new TermQuery(new Term(field, v)), Occur.MUST);
			}
			addQuery(bqb.build());
		}
		return this;
	}

	@Override
	public IQuery in(Number... values) {
		if (values.length == 1) {
			addQuery(numEqQuery(field, values[0]));
		}
		else {
			BooleanQuery.Builder bqb = new BooleanQuery.Builder();
			for (Number v : values) {
				bqb.add(numEqQuery(field, v), Occur.SHOULD);
			}
			addQuery(bqb.build());
		}
		return this;
	}

	@Override
	public IQuery in(Date... values) {
		if (values.length == 1) {
			addQuery(numEqQuery(field, values[0].getTime()));
		}
		else {
			BooleanQuery.Builder bqb = new BooleanQuery.Builder();
			for (Date v : values) {
				bqb.add(numEqQuery(field, v.getTime()), Occur.SHOULD);
			}
			addQuery(bqb.build());
		}
		return this;
	}

	@Override
	public IQuery ne(String... values) {
		BooleanQuery.Builder bqb = new BooleanQuery.Builder();
		for (String v : values) {
			bqb.add(new TermQuery(new Term(field, v)), Occur.MUST_NOT);
		}
		addQuery(bqb.build());
		return this;
	}

	@Override
	public IQuery nin(Number... values) {
		BooleanQuery.Builder bqb = new BooleanQuery.Builder();
		for (Number v : values) {
			bqb.add(numEqQuery(field, v), Occur.MUST_NOT);
		}
		addQuery(bqb.build());
		return this;
	}

	@Override
	public IQuery nin(Date... values) {
		BooleanQuery.Builder bqb = new BooleanQuery.Builder();
		for (Date v : values) {
			bqb.add(numEqQuery(field, v.getTime()), Occur.MUST_NOT);
		}
		addQuery(bqb.build());
		return this;
	}

	@Override
	public IQuery lt(Number value) {
		Query q;
		Number num = (Number)value;

		if (num instanceof Float) {
			q = NumericRangeQuery.newFloatRange(field, Float.NEGATIVE_INFINITY, num.floatValue(), true, false);
		}
		else if (num instanceof Double || num instanceof BigDecimal) {
			q = NumericRangeQuery.newDoubleRange(field, Double.NEGATIVE_INFINITY, num.doubleValue(), true, false);
		}
		else if (num instanceof Long || num instanceof BigInteger) {
			q = NumericRangeQuery.newLongRange(field, Long.MIN_VALUE, num.longValue(), true, false);
		}
		else {
			q = NumericRangeQuery.newIntRange(field, Integer.MIN_VALUE, num.intValue(), true, false);
		}

		addQuery(q);
		return this;
	}

	@Override
	public IQuery lt(Date value) {
		addQuery(NumericRangeQuery.newLongRange(field, Long.MIN_VALUE, value.getTime(), true, false));
		return this;
	}

	@Override
	public IQuery le(Number value) {
		Query q;
		Number num = (Number)value;

		if (num instanceof Float) {
			q = NumericRangeQuery.newFloatRange(field, Float.NEGATIVE_INFINITY, num.floatValue(), true, true);
		}
		else if (num instanceof Double || num instanceof BigDecimal) {
			q = NumericRangeQuery.newDoubleRange(field, Double.NEGATIVE_INFINITY, num.doubleValue(), true, true);
		}
		else if (num instanceof Long || num instanceof BigInteger) {
			q = NumericRangeQuery.newLongRange(field, Long.MIN_VALUE, num.longValue(), true, true);
		}
		else {
			q = NumericRangeQuery.newIntRange(field, Integer.MIN_VALUE, num.intValue(), true, true);
		}

		addQuery(q);
		return this;
	}

	@Override
	public IQuery le(Date value) {
		addQuery(NumericRangeQuery.newLongRange(field, Long.MIN_VALUE, value.getTime(), true, true));
		return this;
	}

	@Override
	public IQuery gt(Number value) {
		Query q;
		Number num = (Number)value;

		if (num instanceof Float) {
			q = NumericRangeQuery.newFloatRange(field, num.floatValue(), Float.POSITIVE_INFINITY, false, true);
		}
		else if (num instanceof Double || num instanceof BigDecimal) {
			q = NumericRangeQuery.newDoubleRange(field, num.doubleValue(), Double.POSITIVE_INFINITY, false, true);
		}
		else if (num instanceof Long || num instanceof BigInteger) {
			q = NumericRangeQuery.newLongRange(field, num.longValue(), Long.MAX_VALUE, false, true);
		}
		else {
			q = NumericRangeQuery.newIntRange(field, num.intValue(), Integer.MAX_VALUE, false, true);
		}

		addQuery(q);
		return this;
	}

	@Override
	public IQuery gt(Date value) {
		addQuery(NumericRangeQuery.newLongRange(field, value.getTime(), Long.MAX_VALUE, false, true));
		return this;
	}

	@Override
	public IQuery ge(Number value) {
		Query q;
		Number num = (Number)value;

		if (num instanceof Float) {
			q = NumericRangeQuery.newFloatRange(field, num.floatValue(), Float.POSITIVE_INFINITY, true, true);
		}
		else if (num instanceof Double || num instanceof BigDecimal) {
			q = NumericRangeQuery.newDoubleRange(field, num.doubleValue(), Double.POSITIVE_INFINITY, true, true);
		}
		else if (num instanceof Long || num instanceof BigInteger) {
			q = NumericRangeQuery.newLongRange(field, num.longValue(), Long.MAX_VALUE, true, true);
		}
		else {
			q = NumericRangeQuery.newIntRange(field, num.intValue(), Integer.MAX_VALUE, true, true);
		}

		addQuery(q);
		return this;
	}

	@Override
	public IQuery ge(Date value) {
		addQuery(NumericRangeQuery.newLongRange(field, value.getTime(), Long.MAX_VALUE, true, true));
		return this;
	}

	//---------------------------------------------------------------
	// start & limit
	//
	protected long start() {
		return start;
	}
	
	protected long limit() {
		return limit;
	}
	
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
		SortField sf = new SortField(field, toSortFieldType(type), desc);
		sorts.add(sf);
		return this;
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
		if (query instanceof BooleanQuery.Builder) {
			return ((BooleanQuery.Builder)query).build();
		}
		return (Query)query;
	}
	
	protected TopDocsCollector buildCollector() {
		TopDocsCollector collector;
		if (Collections.isEmpty(sorts)) {
			collector = TopScoreDocCollector.create((int)start);
		}
		else {
			Sort sort = new Sort(sorts.toArray(new SortField[0]));
			try {
				collector = TopFieldCollector.create(sort, (int)(start + limit), true, false, false);
			}
			catch (Exception e) {
				throw new IndexException("Failed to create TopFieldCollector: " + sorts);
			}
		}
		return collector;
	}

	@Override
	public String toString() {
		return "LuceneQuery(" + analyzer.getClass().getName() + ") [query=" + query + ", start=" + start + ", limit=" + limit + "]";
	}
}
