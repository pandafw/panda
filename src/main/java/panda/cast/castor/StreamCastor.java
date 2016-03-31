package panda.cast.castor;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.lang.reflect.Type;

import panda.cast.CastContext;
import panda.io.Streams;
import panda.io.stream.ReaderInputStream;
import panda.lang.Charsets;
import panda.lang.Exceptions;
import panda.vfs.FileItem;

/**
 * 
 * @author yf.frank.wang@gmail.com
 *
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
					ByteArrayInputStream bais = new ByteArrayInputStream((byte[])value);
					return bais;
				}
				if (value instanceof char[]) {
					return Streams.toInputStream(new String((char[])value), Charsets.UTF_8);
				}
				
				if (value instanceof Reader) {
					return new ReaderInputStream((Reader)value, Charsets.CS_UTF_8);
				}
				if (value instanceof CharSequence) {
					return Streams.toInputStream((CharSequence)value, Charsets.UTF_8);
				}
				if (value instanceof FileItem) {
					FileItem fi = (FileItem)value;
					return fi.isExists() ? fi.getInputStream() : null;
				}
			}
			catch (IOException e) {
				throw Exceptions.wrapThrow(e);
			}

			return castError(value, context);
		}
	}

	public static class ReaderCastor extends StreamCastor<Reader> {
		public ReaderCastor() {
			super(Reader.class);
		}
		
		@Override
		protected Reader castValue(Object value, CastContext context) {
			if (value instanceof char[]) {
				return new StringReader(new String((char[])value));
			}
			if (value instanceof byte[]) {
				ByteArrayInputStream bais = new ByteArrayInputStream((byte[])value);
				return new InputStreamReader(bais, Charsets.CS_UTF_8);
			}
			
			if (value instanceof InputStream) {
				return new InputStreamReader((InputStream)value, Charsets.CS_UTF_8);
			}
			
			String sv = value.toString();
			return new StringReader(sv);
		}
	}
}	
