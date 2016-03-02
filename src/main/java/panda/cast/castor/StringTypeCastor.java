package panda.cast.castor;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.Reader;
import java.lang.reflect.Type;
import java.sql.Clob;
import java.util.Calendar;
import java.util.Date;

import panda.cast.CastContext;
import panda.cast.CastException;
import panda.cast.castor.DateTypeCastor.DateCastor;
import panda.io.Streams;


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

	protected boolean write(Object value, Appendable a, CastContext cc) {
		try {
			if (value instanceof Class) {
				a.append(((Class)value).getName().toString());
			}
			else if (value instanceof Date) {
				a.append(dateCastor.getDateFormat(cc.getFormat(), cc.getLocale()).format((Date)value));
			}
			else if (value instanceof Calendar) {
				a.append(dateCastor.getDateFormat(cc.getFormat(), cc.getLocale()).format(((Calendar)value).getTime()));
			}
			else if (value instanceof char[]) {
				a.append(new String((char[])value));
			}
			else if (value instanceof Reader) {
				Streams.copy((Reader)value, a);
			}
			else if (value instanceof Clob) {
				Streams.copy(((Clob)value).getCharacterStream(), a);
			}
			else if (value instanceof byte[]) {
				InputStream bis = new ByteArrayInputStream((byte[])value);
				Streams.copy(bis, a, cc.getEncoding());
			}
			else if (value instanceof InputStream) {
				Streams.copy((InputStream)value, a, cc.getEncoding());
			}
			else if (value instanceof Number || value instanceof Boolean) {
				a.append(value.toString());
			}
			else if (value instanceof Character) {
				a.append((Character)value);
			}
			else {
				castError(value, cc);
				return false;
			}
			return true;
		}
		catch (CastException e) {
			throw e;
		}
		catch (Exception e) {
			castError(value, cc, e);
			return false;
		}
	}

	public static class StringCastor extends StringTypeCastor<String> {
		public StringCastor(DateCastor dateCastor) {
			super(String.class, dateCastor);
		}
		
		@Override
		protected String castValue(Object value, CastContext context) {
			StringBuilder sb = new StringBuilder();
			if (write(value, sb, context)) {
				return sb.toString();
			}
			return null;
		}
	}

	public static class StringBufferCastor extends StringTypeCastor<StringBuffer> {
		public StringBufferCastor(DateCastor dateCastor) {
			super(StringBuffer.class, dateCastor);
		}
		
		@Override
		protected StringBuffer castValue(Object value, CastContext context) {
			StringBuffer sb = new StringBuffer();
			if (write(value, sb, context)) {
				return sb;
			}
			return null;
		}
		
		@Override
		protected StringBuffer castValueTo(Object value, StringBuffer sb, CastContext context) {
			sb.setLength(0);
			write(value, sb, context);
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
			if (write(value, sb, context)) {
				return sb;
			}
			return null;
		}
		
		@Override
		protected StringBuilder castValueTo(Object value, StringBuilder sb, CastContext context) {
			sb.setLength(0);
			write(value, sb, context);
			return sb;
		}
	}
}
