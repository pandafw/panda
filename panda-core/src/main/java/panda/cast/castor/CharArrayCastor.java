package panda.cast.castor;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.SQLException;

import panda.cast.CastContext;
import panda.codec.binary.Base64;
import panda.io.Streams;
import panda.lang.Exceptions;


/**
 * 
 */
public class CharArrayCastor extends AnySingleCastor<char[]> {
	public CharArrayCastor() {
		super(char[].class);
	}

	@Override
	protected char[] castValue(Object value, CastContext context) {
		try {
			if (value instanceof CharSequence) {
				return value.toString().toCharArray();
			}
			if (value instanceof Reader) {
				return Streams.toCharArray((Reader)value);
			}
			if (value instanceof Clob) {
				String s = ((Clob)value).getSubString(1L, (int)((Clob)value).length());
				return s.toCharArray();
			}
			if (value instanceof byte[]) {
				return Base64.encodeBase64String((byte[])value).toCharArray();
			}
			if (value instanceof InputStream) {
				return Base64.encodeBase64String((InputStream)value).toCharArray();
			}
			if (value instanceof Blob) {
				byte[] bs = ((Blob)value).getBytes(1, (int)((Blob)value).length());
				return Base64.encodeBase64String(bs).toCharArray();
			}
			return castError(value, context);
		}
		catch (SQLException e) {
			throw Exceptions.wrapThrow(e);
		}
		catch (IOException e) {
			throw Exceptions.wrapThrow(e);
		}
	}

}
