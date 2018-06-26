package panda.ex.zendesk;

import java.util.List;

import panda.bind.json.Jsons;

public class Attachment {
	/** Automatically assigned when created */
	public Long id;
	
	/** The name of the image file */
	public String file_name;
	
	/** A full URL where the attachment image file can be downloaded */
	public String content_url;
	
	/** The content type of the image. Example value: image/png */
	public String content_type;

	/** The size of the image file in bytes */
	public Long size;
	
	/** An array of Photo objects. Note that thumbnails do not have thumbnails. */
	public List<Attachment> thumbnails;

	/** If true, the attachment is excluded from the attachment list and the attachment's URL can be referenced within the comment of a ticket. Default is false */
	public Boolean inline;
	

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return Jsons.toJson(this, true);
	}
}
