package panda.idx.lucene;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.DoubleField;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.FieldType;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.document.FloatField;
import org.apache.lucene.document.IntField;
import org.apache.lucene.document.LongField;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DocValuesType;
import org.apache.lucene.index.IndexableField;
import org.apache.lucene.index.Term;

import panda.idx.IDocument;
import panda.idx.IndexException;

public class LuceneDocument implements IDocument {
	public static final FieldType INT_FIELD_TYPE_STORED = new FieldType(IntField.TYPE_STORED);
	public static final FieldType LONG_FIELD_TYPE_STORED = new FieldType(LongField.TYPE_STORED);
	public static final FieldType FLOAT_FIELD_TYPE_STORED = new FieldType(FloatField.TYPE_STORED);
	public static final FieldType DOUBLE_FIELD_TYPE_STORED = new FieldType(DoubleField.TYPE_STORED);
	
	public static final FieldType INT_FIELD_TYPE_NOT_SORTED = new FieldType(IntField.TYPE_NOT_STORED);
	public static final FieldType LONG_FIELD_TYPE_NOT_SORTED = new FieldType(LongField.TYPE_NOT_STORED);
	public static final FieldType FLOAT_FIELD_TYPE_NOT_SORTED = new FieldType(FloatField.TYPE_NOT_STORED);
	public static final FieldType DOUBLE_FIELD_TYPE_NOT_SORTED = new FieldType(DoubleField.TYPE_NOT_STORED);

	static {
		INT_FIELD_TYPE_STORED.setDocValuesType(DocValuesType.NUMERIC);
		INT_FIELD_TYPE_STORED.freeze();
		LONG_FIELD_TYPE_STORED.setDocValuesType(DocValuesType.NUMERIC);
		LONG_FIELD_TYPE_STORED.freeze();
		FLOAT_FIELD_TYPE_STORED.setDocValuesType(DocValuesType.NUMERIC);
		FLOAT_FIELD_TYPE_STORED.freeze();
		DOUBLE_FIELD_TYPE_STORED.setDocValuesType(DocValuesType.NUMERIC);
		DOUBLE_FIELD_TYPE_STORED.freeze();

		INT_FIELD_TYPE_NOT_SORTED.setDocValuesType(DocValuesType.NUMERIC);
		INT_FIELD_TYPE_NOT_SORTED.freeze();
		LONG_FIELD_TYPE_NOT_SORTED.setDocValuesType(DocValuesType.NUMERIC);
		LONG_FIELD_TYPE_NOT_SORTED.freeze();
		FLOAT_FIELD_TYPE_NOT_SORTED.setDocValuesType(DocValuesType.NUMERIC);
		FLOAT_FIELD_TYPE_NOT_SORTED.freeze();
		DOUBLE_FIELD_TYPE_NOT_SORTED.setDocValuesType(DocValuesType.NUMERIC);
		DOUBLE_FIELD_TYPE_NOT_SORTED.freeze();
	}

	protected static final String ID = "_id_";
	
	protected Document doc;

	public LuceneDocument() {
		this(new Document());
	}

	public LuceneDocument(Document doc) {
		this.doc = doc;
	}

	/**
	 * @return the id
	 */
	@Override
	public String getId() {
		return getAtomField(ID);
	}

	/**
	 * @param id the id to set
	 */
	@Override
	public void setId(String id) {
		addAtomField(ID, id);
	}

	/**
	 * @return the doc
	 */
	public Document getDocument() {
		return doc;
	}

	/**
	 * @param doc the doc to set
	 */
	public void setDocoment(Document doc) {
		this.doc = doc;
	}

	@Override
	public String getTextField(String name) {
		IndexableField f = doc.getField(name);
		return f == null ? null : f.stringValue();
	}

	@Override
	public List<String> getTextFields(String name) {
		IndexableField[] fs = doc.getFields(name);
		if (fs == null) {
			return null;
		}
		List<String> ss = new ArrayList<String>();
		for (IndexableField f : fs) {
			ss.add(f.stringValue());
		}
		return ss;
	}

	@Override
	public void addTextField(String name, String text) {
		if (text == null) {
			return;
		}
		doc.add(new TextField(name, text, Field.Store.YES));
	}

	@Override
	public Date getDateField(String name) {
		Number n = getNumberField(name);
		return n == null ? null : new Date(n.longValue());
	}

	@Override
	public List<Date> getDateFields(String name) {
		IndexableField[] fs = doc.getFields(name);
		if (fs == null) {
			return null;
		}
		List<Date> ds = new ArrayList<Date>();
		for (IndexableField f : fs) {
			Number n = f.numericValue();
			ds.add(new Date(n == null ? 0L : n.longValue()));
		}
		return ds;
	}

	@Override
	public void addDateField(String name, Date date) {
		if (date == null) {
			return;
		}
		addNumberField(name, date.getTime());
	}

	@Override
	public Number getNumberField(String name) {
		IndexableField f = doc.getField(name);
		return f == null ? null : f.numericValue();
	}

	@Override
	public List<Number> getNumberFields(String name) {
		IndexableField[] fs = doc.getFields(name);
		if (fs == null) {
			return null;
		}
		
		List<Number> ns = new ArrayList<Number>();
		for (IndexableField f : fs) {
			ns.add(f.numericValue());
		}
		return ns;
	}

	@Override
	public void addNumberField(String name, Number num) {
		if (num == null) {
			return;
		}
		
		if (num instanceof Float) {
			doc.add(new FloatField(name, num.floatValue(), FLOAT_FIELD_TYPE_STORED));
		}
		else if (num instanceof Double || num instanceof BigDecimal) {
			doc.add(new DoubleField(name, num.floatValue(), DOUBLE_FIELD_TYPE_STORED));
		}
		else if (num instanceof Byte || num instanceof Short || num instanceof Integer) {
			doc.add(new IntField(name, num.intValue(), INT_FIELD_TYPE_STORED));
		}
		else if (num instanceof Long || num instanceof BigInteger) {
			doc.add(new LongField(name, num.longValue(), LONG_FIELD_TYPE_STORED));
		}
		else {
			throw new IndexException("Invalid number: " + num.getClass());
		}
	}

	@Override
	public String getAtomField(String name) {
		IndexableField f = doc.getField(name);
		return f == null ? null : f.stringValue();
	}

	@Override
	public List<String> getAtomFields(String name) {
		IndexableField[] fs = doc.getFields(name);
		if (fs == null) {
			return null;
		}
		
		List<String> ss = new ArrayList<String>();
		for (IndexableField f : fs) {
			ss.add(f.stringValue());
		}
		return ss;
	}
	
	@Override
	public void addAtomField(String name, String str) {
		if (str == null) {
			return;
		}
		doc.add(new StringField(name, str, Store.YES));
	}

	public Term getIdTerm() {
		return getIdTerm(getId());
	}

	public static Term getIdTerm(String id) {
		return new Term(ID, id);
	}

	@Override
	public String toString() {
		return "LuceneDocument [doc=" + doc + "]";
	}
}
