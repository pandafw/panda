package panda.castor.castors;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.lang.reflect.Type;
import java.sql.Clob;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;

import panda.castor.AbstractCastor;
import panda.castor.CastContext;
import panda.castor.CastException;
import panda.castor.castors.DateTypeCastor.DateCastor;
import panda.io.Streams;
import panda.lang.Charsets;


/**
 * 
 * @author yf.frank.wang@gmail.com
 *
 * @param <S> source type
 * @param <T> target type
 */
public abstract class StringTypeCastor<S, T> extends AbstractCastor<S, T> {
	protected DateCastor dateCastor;
	
	public StringTypeCastor(Type fromType, Type toType, DateCastor dateCastor) {
		super(fromType, toType);
		this.dateCastor = dateCastor;
	}

	protected <E extends Appendable> E convertValue(Object value, Class<E> type) {
		try {
			E a = type.newInstance();
			if (value instanceof InputStream) {
				Streams.copy((InputStream)value, a, Charsets.UTF_8);
			}
			else if (value instanceof Reader) {
				Streams.copy((Reader)value, a);
			}
			else if (value instanceof Clob) {
				Streams.copy(((Clob)value).getCharacterStream(), a);
			}
			else {
				a.append(convertToString(value));
			}
			return a;
		}
		catch (CastException e) {
			throw e;
		}
		catch (Exception e) {
			throw new CastException(e);
		}
	}

	protected String convertToString(Object value) {
		try {
			if (value instanceof Date) {
				return dateCastor.getDefaultDateFormat().format((Date)value);
			}
			if (value instanceof Calendar) {
				return dateCastor.getDefaultDateFormat().format(((Calendar)value).getTime());
			}
			if (value instanceof char[]) {
				return new String((char[])value);
			}
			if (value instanceof byte[]) {
				return new String((byte[])value, Charsets.CS_UTF_8);
			}
			if (value instanceof InputStream) {
				return Streams.toString((InputStream)value, Charsets.UTF_8);
			}
			if (value instanceof Reader) {
				return Streams.toString((Reader)value);
			}
			if (value instanceof Clob) {
				return ((Clob)value).getSubString(0, (int)((Clob)value).length());
			}
			return value.toString();
		}
		catch (SQLException e) {
			throw new CastException(e);
		}
		catch (IOException e) {
			throw new CastException(e);
		}
	}
	
	public static class StringCastor extends StringTypeCastor<Object, String> {
		public StringCastor(DateCastor dateCastor) {
			super(Object.class, String.class, dateCastor);
		}
		
		@Override
		protected String convertValue(Object value, CastContext context) {
			return convertToString(value);
		}
	}

	public static class StringBufferCastor extends StringTypeCastor<Object, StringBuffer> {
		public StringBufferCastor(DateCastor dateCastor) {
			super(Object.class, StringBuffer.class, dateCastor);
		}
		
		@Override
		protected StringBuffer convertValue(Object value, CastContext context) {
			return convertValue(value, StringBuffer.class);
		}
	}

	public static class StringBuilderCastor extends StringTypeCastor<Object, StringBuilder> {
		public StringBuilderCastor(DateCastor dateCastor) {
			super(Object.class, StringBuilder.class, dateCastor);
		}
		
		@Override
		protected StringBuilder convertValue(Object value, CastContext context) {
			return convertValue(value, StringBuilder.class);
		}
	}
}
