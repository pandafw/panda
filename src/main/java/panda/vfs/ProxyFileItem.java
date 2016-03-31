package panda.vfs;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;

import panda.io.FileNames;
import panda.io.Streams;

/**
 */
public class ProxyFileItem implements FileItem {
	
	private Long id;

	private String name;

	private int size;

	private Date date;

	private String contentType;

	private byte[] data;

	private InputStream stream;
	
	/**
	 * Constructor
	 */
	public ProxyFileItem() {
	}

	/**
	 * @return the id
	 */
	@Override
	public Long getId() {
		return id;
	}

	/**
	 * @return the name
	 */
	@Override
	public String getName() {
		return name;
	}

	/**
	 * @return the size
	 */
	@Override
	public int getSize() {
		return size;
	}

	/**
	 * @return the date
	 */
	@Override
	public Date getDate() {
		return date;
	}

	/**
	 * @return the contentType
	 */
	@Override
	public String getContentType() {
		return contentType == null ? FileNames.getContentTypeFor(name) : contentType;
	}

	/**
	 * @return true if this is a temporary file
	 */
	@Override
	public boolean isTemporary() {
		return true;
	}

	/**
	 * @return false
	 */
	@Override
	public boolean isExists() {
		return true;
	}

	/**
	 * @return the data
	 */
	@Override
	public byte[] getData() throws IOException {
		if (data != null) {
			return data;
		}
		if (stream != null) {
			return Streams.toByteArray(stream);
		}
		return null;
	}

	/**
	 * @return the input stream
	 */
	@Override
	public InputStream getInputStream() throws IOException {
		if (stream != null) {
			return stream;
		}
		if (data != null) {
			return Streams.toInputStream(data);
		}
		return null;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @param size the size to set
	 */
	public void setSize(int size) {
		this.size = size;
	}

	/**
	 * @param date the date to set
	 */
	public void setDate(Date date) {
		this.date = date;
	}

	/**
	 * @param contentType the contentType to set
	 */
	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	/**
	 * @param data the data to set
	 */
	public void setData(byte[] data) {
		this.data = data;
	}

	/**
	 * @param stream the stream to set
	 */
	public void setInputStream(InputStream stream) {
		this.stream = stream;
	}

	/**
	 * save data
	 */
	@Override
	public void save(byte[] data) throws IOException {
		error();
	}
	
	/**
	 * save data
	 */
	@Override
	public void save(InputStream data) throws IOException {
		error();
	}

	@Override
	public void delete() throws IOException {
		throw error();
	}

	private IOException error() {
		return new FileNotFoundException(toString());
	}
	
	/**
	 * @return a string representation of the object.
	 */
	@Override
	public String toString() {
		return String.format("ProxyFileItem(id: %s, name: %s)", String.valueOf(id), String.valueOf(name));
	}

	/**
	 * @return a hash code value for this object.
	 */
	@Override
	public int hashCode() {
		return ((id == null) ? 0 : id.hashCode());
	}

	/**
	 * Creates and returns a copy of this object.
	 * @return the copy object
	 */
	@Override
	public ProxyFileItem clone() {
		ProxyFileItem copy = new ProxyFileItem();
		copy.id = id;
		copy.name = name;
		copy.size = size;
		copy.date = date;
		copy.contentType = contentType;
		copy.data = data;
		copy.stream = stream;
		return copy;
	}
}

