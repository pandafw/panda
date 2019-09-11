package panda.idx.lucene;

import java.io.IOException;
import java.io.StringReader;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.document.DoublePoint;
import org.apache.lucene.document.FloatPoint;
import org.apache.lucene.document.IntPoint;
import org.apache.lucene.document.LongPoint;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.search.BooleanQuery;
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
		if (num instanceof Float) {
			return FloatPoint.newExactQuery(field, num.floatValue());
		}
		
		if (num instanceof Double || num instanceof BigDecimal) {
			return DoublePoint.newExactQuery(field, num.doubleValue());
		}

		if (num instanceof Long || num instanceof BigInteger) {
			return LongPoint.newExactQuery(field, num.longValue());
		}

		return IntPoint.newExactQuery(field, num.intValue());
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
		Query q;
		if (values.length == 1) {
			q = numEqQuery(field, values[0]);
		}
		else {
			Number num = values[0];
			if (num instanceof Float) {
				q = FloatPoint.newSetQuery(field, Arrays.asList((Float[])values));
			}
			else if (num instanceof Double || num instanceof BigDecimal) {
				q = DoublePoint.newSetQuery(field, Arrays.asList((Double[])values));
			}
			else if (num instanceof Long || num instanceof BigInteger) {
				q = LongPoint.newSetQuery(field, Arrays.asList((Long[])values));
			}
			else {
				q = IntPoint.newSetQuery(field, Arrays.asList((Integer[])values));
			}
		}
		addQuery(q);
		return this;
	}

	@Override
	public IQuery in(Date... values) {
		Query q;
		if (values.length == 1) {
			q = numEqQuery(field, values[0].getTime());
		}
		else {
			List<Long> nums = new ArrayList<Long>();
			for (Date d : values) {
				nums.add(d.getTime());
			}
			q = LongPoint.newSetQuery(field, nums);
		}
		addQuery(q);
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
		Query q;
		Number num = values[0];
		if (num instanceof Float) {
			q = FloatPoint.newSetQuery(field, Arrays.asList((Float[])values));
		}
		else if (num instanceof Double || num instanceof BigDecimal) {
			q = DoublePoint.newSetQuery(field, Arrays.asList((Double[])values));
		}
		else if (num instanceof Long || num instanceof BigInteger) {
			q = LongPoint.newSetQuery(field, Arrays.asList((Long[])values));
		}
		else {
			q = IntPoint.newSetQuery(field, Arrays.asList((Integer[])values));
		}

		BooleanQuery.Builder bqb = new BooleanQuery.Builder();
		bqb.add(q, Occur.MUST_NOT);
		addQuery(bqb.build());
		return this;
	}

	@Override
	public IQuery nin(Date... values) {
		List<Long> nums = new ArrayList<Long>();
		for (Date d : values) {
			nums.add(d.getTime());
		}
		Query q = LongPoint.newSetQuery(field, nums);

		BooleanQuery.Builder bqb = new BooleanQuery.Builder();
		bqb.add(q, Occur.MUST_NOT);
		addQuery(bqb.build());
		return this;
	}

	@Override
	public IQuery lt(Number value) {
		Query q;
		Number num = (Number)value;

		if (num instanceof Float) {
			q = FloatPoint.newRangeQuery(field, Float.NEGATIVE_INFINITY, num.floatValue() - Float.MIN_NORMAL);
		}
		else if (num instanceof Double || num instanceof BigDecimal) {
			q = DoublePoint.newRangeQuery(field, Double.NEGATIVE_INFINITY, num.doubleValue() - Double.MIN_NORMAL);
		}
		else if (num instanceof Long || num instanceof BigInteger) {
			q = LongPoint.newRangeQuery(field, Long.MIN_VALUE, num.longValue() + 1);
		}
		else {
			q = IntPoint.newRangeQuery(field, Integer.MIN_VALUE, num.intValue() + 1);
		}

		addQuery(q);
		return this;
	}

	@Override
	public IQuery lt(Date value) {
		addQuery(LongPoint.newRangeQuery(field, Long.MIN_VALUE, value.getTime() - 1));
		return this;
	}

	@Override
	public IQuery le(Number value) {
		Query q;
		Number num = (Number)value;

		if (num instanceof Float) {
			q = FloatPoint.newRangeQuery(field, Float.NEGATIVE_INFINITY, num.floatValue());
		}
		else if (num instanceof Double || num instanceof BigDecimal) {
			q = DoublePoint.newRangeQuery(field, Double.NEGATIVE_INFINITY, num.doubleValue());
		}
		else if (num instanceof Long || num instanceof BigInteger) {
			q = LongPoint.newRangeQuery(field, Long.MIN_VALUE, num.longValue());
		}
		else {
			q = IntPoint.newRangeQuery(field, Integer.MIN_VALUE, num.intValue());
		}

		addQuery(q);
		return this;
	}

	@Override
	public IQuery le(Date value) {
		addQuery(LongPoint.newRangeQuery(field, Long.MIN_VALUE, value.getTime()));
		return this;
	}

	@Override
	public IQuery gt(Number value) {
		Query q;
		Number num = (Number)value;

		if (num instanceof Float) {
			q = FloatPoint.newRangeQuery(field, num.floatValue() + Float.MIN_NORMAL, Float.POSITIVE_INFINITY);
		}
		else if (num instanceof Double || num instanceof BigDecimal) {
			q = DoublePoint.newRangeQuery(field, num.doubleValue() + Double.MIN_NORMAL, Double.POSITIVE_INFINITY);
		}
		else if (num instanceof Long || num instanceof BigInteger) {
			q = LongPoint.newRangeQuery(field, num.longValue() + 1, Long.MAX_VALUE);
		}
		else {
			q = IntPoint.newRangeQuery(field, num.intValue() + 1, Integer.MAX_VALUE);
		}

		addQuery(q);
		return this;
	}

	@Override
	public IQuery gt(Date value) {
		addQuery(LongPoint.newRangeQuery(field, value.getTime() + 1, Long.MAX_VALUE));
		return this;
	}

	@Override
	public IQuery ge(Number value) {
		Query q;
		Number num = (Number)value;

		if (num instanceof Float) {
			q = FloatPoint.newRangeQuery(field, num.floatValue(), Float.POSITIVE_INFINITY);
		}
		else if (num instanceof Double || num instanceof BigDecimal) {
			q = DoublePoint.newRangeQuery(field, num.doubleValue(), Double.POSITIVE_INFINITY);
		}
		else if (num instanceof Long || num instanceof BigInteger) {
			q = LongPoint.newRangeQuery(field, num.longValue(), Long.MAX_VALUE);
		}
		else {
			q = IntPoint.newRangeQuery(field, num.intValue(), Integer.MAX_VALUE);
		}

		addQuery(q);
		return this;
	}

	@Override
	public IQuery ge(Date value) {
		addQuery(LongPoint.newRangeQuery(field, value.getTime(), Long.MAX_VALUE));
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
				collector = TopFieldCollector.create(sort, (int)(start + limit), true, false, false, false);
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
