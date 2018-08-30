package panda.net.p2p;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

import panda.cast.Castors;
import panda.codec.binary.Base32;
import panda.io.stream.ByteArrayOutputStream;
import panda.lang.Charsets;
import panda.lang.Collections;
import panda.lang.Strings;
import panda.lang.reflect.Types;
import panda.util.crypto.Digests;

public class Torrent {
	public static class Item {
		public long length;
		public String path;
	}

	public static String getBtih(byte[] bs) throws IOException {
		return new Torrent(bs).getBtih();
	}
	
	public static String getBtih(InputStream is) throws IOException {
		return new Torrent(is).getBtih();
	}
	
	private String encoding = Charsets.UTF_8;
	private String name;
	private String btih;
	private List<Item> files;
	private long length;

	private Map<String, Object> root;
	
	public Torrent() {
	}
	
	public Torrent(InputStream is) throws IOException {
		parse(is);
	}
	
	public Torrent(byte[] bs) throws IOException {
		parse(bs);
	}

	/**
	 * @return the encoding
	 */
	public String getEncoding() {
		return encoding;
	}


	/**
	 * @param encoding the encoding to set
	 */
	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}

	/**
	 * @return the btih
	 */
	public String getBtih() {
		return btih;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}


	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the length
	 */
	public long getLength() {
		return length;
	}

	/**
	 * @param length the length to set
	 */
	public void setLength(long length) {
		this.length = length;
	}

	/**
	 * @return the files
	 */
	public List<Item> getFiles() {
		return files;
	}

	/**
	 * @param files the files to set
	 */
	public void setFiles(List<Item> files) {
		this.files = files;
	}

	/**
	 * @return single file or not
	 */
	public boolean isSingle() {
		return files == null;
	}

	/**
	 * @return the root
	 */
	public Map<String, Object> root() {
		return root;
	}
	
	private String getPropertyAsString(Map<String, Object> map, String key) {
		return getPropertyAsString(map, key, null);
	}
	
	private String getPropertyAsString(Map<String, Object> map, String key, String def) {
		Object v = map.get(key);
		if (v instanceof byte[]) {
			return Strings.newString((byte[])v, encoding);
		}
		return v == null ? def : v.toString();
	}

	private void parse(byte[] bs) throws IOException {
		InputStream is = new ByteArrayInputStream(bs);
		parse(is);
	}
	
	@SuppressWarnings("unchecked")
	private void parse(InputStream is) throws IOException {
		root = (Map<String, Object>)Bencode.parse(is);
		
		Map info = (Map)(root.get("info"));
		if (Collections.isEmpty(info)) {
			throw new IOException("Invalid torrent file: missing <info>");
		}

		encoding = getPropertyAsString(info, "encoding", encoding);
		name = getPropertyAsString(info, "name");
		if (info.containsKey("length")) {
			length = (Long)info.get("length");
		}
		else {
			List files = (List)(info.get("files"));
			if (Collections.isEmpty(files)) {
				throw new IOException("Invalid torrent file: missing <length> or <info.files>");
			}
			this.files = Castors.scast(files, Types.paramTypeOf(List.class, Item.class), false);

			length = 0;
			for (Item i : this.files) {
				length += i.length;
			}
		}

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		Bencode.write(info, baos);
		byte[] sha1 = Digests.sha1(baos.toByteArray());
		btih = Base32.encodeBase32String(sha1);
	}
	
	public String toMagnetURL() {
		Magnet mag = new Magnet();
		mag.setDn(name);
		mag.setBtih(btih);

		return mag.toURL();
	}
}
