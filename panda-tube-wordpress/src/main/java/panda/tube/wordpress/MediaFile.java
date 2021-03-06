package panda.tube.wordpress;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import panda.bind.json.Jsons;
import panda.io.Files;
import panda.io.MimeTypes;

public class MediaFile {
	public String name;
	
	public String type;

	public byte[] bits;

	public Boolean overwrite;
	
	public String post_id;

	public static MediaFile fromFile(File file) throws FileNotFoundException, IOException {
		MediaFile mf = new MediaFile();
		
		mf.name = file.getName();
		mf.bits = Files.readToBytes(file);
		mf.type = MimeTypes.getMimeType(file.getName());
		
		return mf;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return Jsons.toJson(this, true);
	}
}
