package panda.cast.castor;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.sql.Blob;
import java.sql.SQLException;

import panda.cast.CastContext;
import panda.io.Streams;
import panda.lang.Exceptions;
import panda.lang.codec.binary.Base64;


/**
 * 
 * @author yf.frank.wang@gmail.com
 */
public class ByteArrayCastor extends AnySingleCastor<byte[]> {
	public ByteArrayCastor() {
		super(byte[].class);
	}

	@Override
	protected byte[] castValue(Object value, CastContext context) {
		try {
			if (value instanceof Blob) {
				return ((Blob)value).getBytes(1, (int)((Blob)value).length());
			}
			if (value instanceof InputStream) {
				return Streams.toByteArray((InputStream)value);
			}
			if (value instanceof Reader) {
				return Base64.decodeBase64((Reader)value);
			}
			if (value instanceof CharSequence) {
				return Base64.decodeBase64(((CharSequence)value).toString());
			}
			if (value instanceof char[]) {
				return Base64.decodeBase64(new String((char[])value));
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
