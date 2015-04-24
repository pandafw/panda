package panda.lang.codec.binary;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PushbackInputStream;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import panda.io.Streams;
import panda.io.stream.ByteArrayOutputStream;
import panda.lang.Charsets;
import panda.lang.Strings;
import panda.lang.codec.Decoder;
import panda.lang.codec.DecoderException;
import panda.lang.codec.Encoder;
import panda.lang.codec.EncoderException;

public class Bencode implements Encoder, Decoder {
	/**
	 * This creates a new instance of Bencode class
	 */
	public Bencode() {
	}

	@Override
	public Object decode(Object source) throws DecoderException {
		try {
			if (source instanceof byte[]) {
				return parse((byte[])source);
			}
			if (source instanceof InputStream) {
				return parse((InputStream)source);
			}
		}
		catch (IOException e) {
			throw new DecoderException(e);
		}
		throw new DecoderException("Parameter supplied to Bencode decode is not a byte[] or a InputStream");
	}

	/**
	 * This method encode the object to byte[]
	 */
	@Override
	public Object encode(Object source) throws EncoderException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try {
			write(source, baos);
		}
		catch (IOException e) {
			throw new EncoderException(e);
		}
		return baos.toByteArray();
	}

	public static void write(Object object, OutputStream os) throws IOException {
		if (object instanceof Long) {
			os.write('i');
			os.write(((Long)object).toString().getBytes());
			os.write('e');
		}
		else if (object instanceof byte[]) {
			byte[] bs = ((byte[])object);
			os.write(Integer.toString(bs.length).getBytes());
			os.write(':');
			os.write(bs);
		}
		else if (object instanceof List) {
			List list = (List)object;
			os.write('l');
			for (Object elem : list) {
				write(elem, os);
			}
			os.write('e');
		}
		else if (object instanceof Map) {
			Map map = (Map)object;

			os.write('d');
			for (Object elem : map.entrySet()) {
				Map.Entry entry = (Map.Entry)elem;
				write(entry.getKey().toString().getBytes(), os);
				write(entry.getValue(), os);
			}
			os.write('e');
		}
		else {
			throw new IllegalStateException("Unexcepted object: " + object);
		}
	}

	public static Object parse(byte[] bs) throws IOException {
		return parse(new ByteArrayInputStream(bs));
	}
	
	public static Object parse(InputStream is) throws IOException {
		if (is instanceof PushbackInputStream) {
			return _parse((PushbackInputStream)is);
		}
		return _parse(new PushbackInputStream(is, 1));
	}
	
	public static Object _parse(PushbackInputStream is) throws IOException {
		int ch = is.read();
		switch (ch) {
		case 'i':
			return parseInteger(is);
		case 'l':
			return parseList(is);
		case 'd':
			return parseDictionary(is);
		case '0':
		case '1':
		case '2':
		case '3':
		case '4':
		case '5':
		case '6':
		case '7':
		case '8':
		case '9':
			is.unread(ch);
			return parseBytes(is);
		default:
			throw new IOException("Problem parsing bencoded file: unexpected char " + (char)ch);
		}
	}

	private static Long parseInteger(InputStream is) throws IOException {
		StringBuilder sb = new StringBuilder();

		Streams.copyUntil(is, sb, 'e');

		// System.out.println("Loaded int: " + buff);
		return Long.parseLong(sb.toString());
	}

	private static List<Object> parseList(PushbackInputStream is) throws IOException {
		List<Object> list = new ArrayList<Object>();

		int ch = is.read();
		while (ch != 'e') {
			if (ch < 0) {
				throw new IOException("Unexpected EOF found");
			}

			is.unread(ch);
			list.add(_parse(is));
			
			ch = is.read();
		}

		return list;
	}

	private static Map<String, Object> parseDictionary(PushbackInputStream is) throws IOException {
		Map<String, Object> map = new LinkedHashMap<String, Object>();

		int ch = is.read();
		while (ch != 'e') {
			if (ch < 0) {
				throw new IOException("Unexpected EOF found");
			}

			is.unread(ch);
			map.put(parseString(is), _parse(is));

			ch = is.read();
		}

		return map;
	}

	private static String parseString(InputStream is) throws IOException {
		StringBuilder sb = new StringBuilder();

		Streams.copyUntil(is, sb, ':');
		int len = Integer.parseInt(sb.toString());

		byte[] bs = new byte[len];
		
		Streams.readFully(is, bs);
		
		return Strings.toString(bs, Charsets.UTF_8);
	}

	private static byte[] parseBytes(InputStream is) throws IOException {
		StringBuilder sb = new StringBuilder();

		Streams.copyUntil(is, sb, ':');
		int len = Integer.parseInt(sb.toString());

		byte[] bs = new byte[len];
		
		Streams.readFully(is, bs);
		
		return bs;
	}
}

