package panda.ex.wordpress;

import panda.bind.json.Jsons;

public class MediaFile {
	public String name;
	
	public String type;

	public byte[] bits;

	public Boolean overwrite;
	
	public String post_id;


	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return Jsons.toJson(this, true);
	}
}
