package panda.app.lucene;

import org.apache.lucene.document.DoubleField;
import org.apache.lucene.document.FieldType;
import org.apache.lucene.document.FloatField;
import org.apache.lucene.document.IntField;
import org.apache.lucene.document.LongField;
import org.apache.lucene.index.DocValuesType;

import panda.lang.Strings;

public abstract class Lucenes {

	private static final String SPECIAL_CHARS = "+-&|!(){}[]^\"~*?:\\/";
	
	public static final FieldType INT_FIELD_TYPE_STORED_SORTED = new FieldType(IntField.TYPE_STORED);
	public static final FieldType LONG_FIELD_TYPE_STORED_SORTED = new FieldType(LongField.TYPE_STORED);
	public static final FieldType FLOAT_FIELD_TYPE_STORED_SORTED = new FieldType(FloatField.TYPE_STORED);
	public static final FieldType DOUBLE_FIELD_TYPE_STORED_SORTED = new FieldType(DoubleField.TYPE_STORED);
	
	public static final FieldType INT_FIELD_TYPE_SORTED = new FieldType(IntField.TYPE_NOT_STORED);
	public static final FieldType LONG_FIELD_TYPE_SORTED = new FieldType(LongField.TYPE_NOT_STORED);
	public static final FieldType FLOAT_FIELD_TYPE_SORTED = new FieldType(FloatField.TYPE_NOT_STORED);
	public static final FieldType DOUBLE_FIELD_TYPE_SORTED = new FieldType(DoubleField.TYPE_NOT_STORED);

	static {
		INT_FIELD_TYPE_STORED_SORTED.setDocValuesType(DocValuesType.NUMERIC);
		INT_FIELD_TYPE_STORED_SORTED.freeze();
		LONG_FIELD_TYPE_STORED_SORTED.setDocValuesType(DocValuesType.NUMERIC);
		LONG_FIELD_TYPE_STORED_SORTED.freeze();
		FLOAT_FIELD_TYPE_STORED_SORTED.setDocValuesType(DocValuesType.NUMERIC);
		FLOAT_FIELD_TYPE_STORED_SORTED.freeze();
		DOUBLE_FIELD_TYPE_STORED_SORTED.setDocValuesType(DocValuesType.NUMERIC);
		DOUBLE_FIELD_TYPE_STORED_SORTED.freeze();

		INT_FIELD_TYPE_SORTED.setDocValuesType(DocValuesType.NUMERIC);
		INT_FIELD_TYPE_SORTED.freeze();
		LONG_FIELD_TYPE_SORTED.setDocValuesType(DocValuesType.NUMERIC);
		LONG_FIELD_TYPE_SORTED.freeze();
		FLOAT_FIELD_TYPE_SORTED.setDocValuesType(DocValuesType.NUMERIC);
		FLOAT_FIELD_TYPE_SORTED.freeze();
		DOUBLE_FIELD_TYPE_SORTED.setDocValuesType(DocValuesType.NUMERIC);
		DOUBLE_FIELD_TYPE_SORTED.freeze();
	}

	public static String escapeText(String str) {
		return Strings.escapeChars(str, SPECIAL_CHARS);
	}
}
