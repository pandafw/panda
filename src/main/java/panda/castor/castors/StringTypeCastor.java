package panda.castor.castors;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.lang.reflect.Type;
import java.sql.Clob;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;

import panda.castor.CastContext;
import panda.castor.CastException;
import panda.castor.Castor;
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
public abstract class StringTypeCastor<S, T> extends Castor<S, T> {
	protected DateCastor dateCastor;
	
	public StringTypeCastor(Type fromType, Type toType, DateCastor dateCastor) {
		super(fromType, toType);
		this.dateCastor = dateCastor;
	}

	protected void write(Object value, Appendable a) {
		try {
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
			if (value instanceof Class) {
				return ((Class)value).getName().toString();
			}
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
				return ((Clob)value).getSubString(1, (int)((Clob)value).length());
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
		protected String castValue(Object value, CastContext context) {
			return convertToString(value);
		}
	}

	public static class StringBufferCastor extends StringTypeCastor<Object, StringBuffer> {
		public StringBufferCastor(DateCastor dateCastor) {
			super(Object.class, StringBuffer.class, dateCastor);
		}
		
		@Override
		protected StringBuffer castValue(Object value, CastContext context) {
			StringBuffer sb = new StringBuffer();
			write(value, sb);
			return sb;
		}
		
		@Override
		protected StringBuffer castValueTo(Object value, StringBuffer sb, CastContext context) {
			sb.setLength(0);
			write(value, sb);
			return sb;
		}
	}

	public static class StringBuilderCastor extends StringTypeCastor<Object, StringBuilder> {
		public StringBuilderCastor(DateCastor dateCastor) {
			super(Object.class, StringBuilder.class, dateCastor);
		}
		
		@Override
		protected StringBuilder castValue(Object value, CastContext context) {
			StringBuilder sb = new StringBuilder();
			write(value, sb);
			return sb;
		}
		
		@Override
		protected StringBuilder castValueTo(Object value, StringBuilder sb, CastContext context) {
			sb.setLength(0);
			write(value, sb);
			return sb;
		}
	}
}
