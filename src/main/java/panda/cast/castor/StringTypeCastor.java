package panda.cast.castor;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.lang.reflect.Type;
import java.sql.Clob;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;

import panda.cast.CastContext;
import panda.cast.CastException;
import panda.cast.castor.DateTypeCastor.DateCastor;
import panda.io.Streams;
import panda.lang.Charsets;
import panda.lang.codec.binary.Base64;


/**
 * 
 * @author yf.frank.wang@gmail.com
 *
 * @param <T> target type
 */
public abstract class StringTypeCastor<T> extends AnySingleCastor<T> {
	protected DateCastor dateCastor;
	
	public StringTypeCastor(Type toType, DateCastor dateCastor) {
		super(toType);
		this.dateCastor = dateCastor;
	}

	@Override
	protected Object prepare(Object value) {
		if (value instanceof byte[] || value instanceof char[]) {
			return value;
		}
		return super.prepare(value);
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
			if (value instanceof Reader) {
				return Streams.toString((Reader)value);
			}
			if (value instanceof Clob) {
				return ((Clob)value).getSubString(1, (int)((Clob)value).length());
			}
			if (value instanceof byte[]) {
				return Base64.encodeBase64String((byte[])value);
			}
			if (value instanceof InputStream) {
				return Base64.encodeBase64String((InputStream)value);
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
	
	public static class StringCastor extends StringTypeCastor<String> {
		public StringCastor(DateCastor dateCastor) {
			super(String.class, dateCastor);
		}
		
		@Override
		protected String castValue(Object value, CastContext context) {
			return convertToString(value);
		}
	}

	public static class StringBufferCastor extends StringTypeCastor<StringBuffer> {
		public StringBufferCastor(DateCastor dateCastor) {
			super(StringBuffer.class, dateCastor);
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

	public static class StringBuilderCastor extends StringTypeCastor<StringBuilder> {
		public StringBuilderCastor(DateCastor dateCastor) {
			super(StringBuilder.class, dateCastor);
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
