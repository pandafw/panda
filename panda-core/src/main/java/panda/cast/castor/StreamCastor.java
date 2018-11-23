package panda.cast.castor;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.StringReader;
import java.lang.reflect.Type;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.SQLException;

import panda.cast.CastContext;
import panda.codec.binary.Base64;
import panda.codec.binary.Base64InputStream;
import panda.io.Streams;
import panda.lang.Charsets;
import panda.lang.Exceptions;
import panda.vfs.FileItem;

/**
 *
 * @param <T> target type
 */
public abstract class StreamCastor<T> extends AnySingleCastor<T> {
	public StreamCastor(Type toType) {
		super(toType);
	}

	public static class InputStreamCastor extends StreamCastor<InputStream> {
		public InputStreamCastor() {
			super(InputStream.class);
		}
		
		@Override
		protected InputStream castValue(Object value, CastContext context) {
			try {
				if (value instanceof byte[]) {
					return new ByteArrayInputStream((byte[])value);
				}
				if (value instanceof FileItem) {
					FileItem fi = (FileItem)value;
					return fi.isExists() ? fi.open() : null;
				}
				if (value instanceof Blob) {
					return ((Blob)value).getBinaryStream();
				}

				if (value instanceof char[]) {
					InputStream in = Streams.toInputStream(new String((char[])value), context.getEncoding());
					Base64InputStream b64i = new Base64InputStream(in);
					return b64i;
				}
				if (value instanceof CharSequence) {
					InputStream in = Streams.toInputStream((CharSequence)value, Charsets.UTF_8);
					Base64InputStream b64i = new Base64InputStream(in);
					return b64i;
				}
				if (value instanceof Reader) {
					return Streams.toInputStream((Reader)value, context.getEncoding());
				}
				if (value instanceof Clob) {
					return Streams.toInputStream(((Clob)value).getCharacterStream(), context.getEncoding());
				}

				return castError(value, context);
			}
			catch (IOException e) {
				throw Exceptions.wrapThrow(e);
			}
			catch (SQLException e) {
				throw Exceptions.wrapThrow(e);
			}
		}
	}

	public static class ReaderCastor extends StreamCastor<Reader> {
		public ReaderCastor() {
			super(Reader.class);
		}
		
		@Override
		protected Reader castValue(Object value, CastContext context) {
			try {
				if (value instanceof char[]) {
					return new StringReader(new String((char[])value));
				}
				if (value instanceof CharSequence) {
					return new StringReader(value.toString());
				}
				if (value instanceof Clob) {
					return ((Clob)value).getCharacterStream();
				}

				if (value instanceof FileItem) {
					FileItem fi = (FileItem)value;
					return fi.isExists() ? Streams.toReader(fi.open(), context.getEncoding()) : null;
				}

				if (value instanceof byte[]) {
					String s = Base64.decodeBase64String((byte[])value);
					return new StringReader(s);
				}
				if (value instanceof InputStream) {
					return Streams.toReader((InputStream)value, context.getEncoding());
				}
				if (value instanceof Blob) {
					return Streams.toReader(((Blob)value).getBinaryStream(), context.getEncoding());
				}
				
				return castError(value, context);
			}
			catch (IOException e) {
				throw Exceptions.wrapThrow(e);
			}
			catch (SQLException e) {
				throw Exceptions.wrapThrow(e);
			}
		}
	}
}	
