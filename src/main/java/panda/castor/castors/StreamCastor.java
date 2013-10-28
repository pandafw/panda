package panda.castor.castors;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;

import panda.castor.CastContext;
import panda.castor.Castor;
import panda.io.Streams;
import panda.io.stream.ReaderInputStream;
import panda.lang.Charsets;

/**
 * 
 * @author yf.frank.wang@gmail.com
 *
 */
public class StreamCastor {
	public static class InputStreamCastor extends Castor<Object, InputStream> {
		public InputStreamCastor() {
			super(Object.class, InputStream.class);
		}
		
		@Override
		protected InputStream castValue(Object value, CastContext context) {
			if (value instanceof byte[]) {
				ByteArrayInputStream bais = new ByteArrayInputStream((byte[])value);
				return bais;
			}
			if (value instanceof Reader) {
				return new ReaderInputStream((Reader)value, Charsets.CS_UTF_8);
			}
			if (value instanceof CharSequence) {
				String sv = value.toString();
				try {
					return Streams.toInputStream(sv, Charsets.UTF_8);
				}
				catch (IOException e) {
					throw wrapError(e, context);
				}
			}
			throw castError(value, context);
		}
	}

	public static class ReaderCastor extends Castor<Object, Reader> {
		public ReaderCastor() {
			super(Object.class, Reader.class);
		}
		
		@Override
		protected Reader castValue(Object value, CastContext context) {
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
