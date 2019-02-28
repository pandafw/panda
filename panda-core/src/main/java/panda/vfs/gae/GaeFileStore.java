package panda.vfs.gae;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.google.appengine.tools.cloudstorage.GcsFileMetadata;
import com.google.appengine.tools.cloudstorage.GcsFileOptions;
import com.google.appengine.tools.cloudstorage.GcsFilename;
import com.google.appengine.tools.cloudstorage.GcsInputChannel;
import com.google.appengine.tools.cloudstorage.GcsOutputChannel;
import com.google.appengine.tools.cloudstorage.GcsService;
import com.google.appengine.tools.cloudstorage.GcsServiceFactory;
import com.google.appengine.tools.cloudstorage.ListItem;
import com.google.appengine.tools.cloudstorage.ListOptions;
import com.google.appengine.tools.cloudstorage.ListResult;
import com.google.appengine.tools.cloudstorage.RetryParams;

import panda.io.Streams;
import panda.lang.Strings;
import panda.lang.time.DateTimes;
import panda.vfs.FileItem;
import panda.vfs.FileStore;

public class GaeFileStore implements FileStore {
	/** Used below to determine the size of chucks to read in. Should be > 1kb and < 10MB */
	private static final int BUFFER_SIZE = 2 * 1024 * 1024;
	
	protected GcsService gcsService;
	
	protected String bucket;

	protected String prefix = "";

	public GaeFileStore() {
		gcsService = GcsServiceFactory.createGcsService(new RetryParams.Builder()
			.initialRetryDelayMillis(10).retryMaxAttempts(10).totalRetryPeriodMillis(15000)
			.build());
	}

	/**
	 * @return the bucket
	 */
	public String getBucket() {
		return bucket;
	}

	/**
	 * @param bucket the bucket to set
	 */
	public void setBucket(String bucket) {
		this.bucket = bucket;
	}

	/**
	 * @return the prefix
	 */
	public String getPrefix() {
		return prefix;
	}

	/**
	 * @param prefix the prefix to set
	 */
	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	@Override
	public Class<? extends FileItem> getItemType() {
		return GaeFileItem.class;
	}
	
	@Override
	public FileItem getFile(String name) throws IOException {
		GaeFileItem gfi = new GaeFileItem(this);
		gfi.setName(name);

		GcsFilename gfn = toGcsFilename(gfi);

		GcsFileMetadata gfm = gcsService.getMetadata(gfn);
		if (gfm != null) {
			gfi.setExists(true);
			gfi.setSize((int)gfm.getLength());
			gfi.setDate(gfm.getLastModified());
		}

		return gfi;
	}

	protected void saveFile(GaeFileItem gfi, byte[] data) throws IOException {
		GcsFilename gfn = toGcsFilename(gfi);
		GcsFileOptions gfo = GcsFileOptions.getDefaultInstance();
		GcsOutputChannel goc = gcsService.createOrReplace(gfn, gfo);

		goc.write(ByteBuffer.wrap(data));
		goc.close();
		
		gfi.setSize(data.length);
		gfi.setDate(DateTimes.getDate());
		gfi.setExists(true);
	}
	
	protected void saveFile(GaeFileItem gfi, InputStream data) throws IOException {
		saveFile(gfi, Streams.toByteArray(data));
	}
	
	protected byte[] readFile(GaeFileItem gfi) throws IOException {
		GcsFilename gfn = toGcsFilename(gfi);
		GcsInputChannel gic = gcsService.openPrefetchingReadChannel(gfn, 0, BUFFER_SIZE);
		InputStream in = Channels.newInputStream(gic);
		try {
			return Streams.toByteArray(in);
		}
		finally {
			Streams.safeClose(in);
		}
	}

	protected boolean removeFile(GaeFileItem gfi) throws IOException {
		GcsFilename gfn = toGcsFilename(gfi);
		return gcsService.delete(gfn);
	}

	protected GcsFilename toGcsFilename(GaeFileItem gfi) {
		return new GcsFilename(bucket, prefix + gfi.getName());
	}

	@Override
	public List<FileItem> listFiles() throws IOException {
		return listFiles(null, null);
	}
	
	@Override
	public List<FileItem> listFiles(String prefix, Date before) throws IOException {
		prefix = prefix == null ? this.prefix : this.prefix + prefix;

		try {
			ListOptions.Builder lob = new ListOptions.Builder();

			ListResult lr = gcsService.list(bucket, lob.setPrefix(prefix).setRecursive(true).build());
	
			List<FileItem> fis = new ArrayList<FileItem>();
			while (lr.hasNext()) {
				ListItem i = lr.next();
	
				if (before != null && before.getTime() < i.getLastModified().getTime()) {
					continue;
				}
				
				GaeFileItem gfi = new GaeFileItem(this);
				gfi.setName(Strings.removeStart(i.getName(), this.prefix));
				gfi.setSize((int)i.getLength());
				gfi.setDate(i.getLastModified());
				gfi.setExists(true);
				fis.add(gfi);
			}
	
			return fis;
		}
		catch (Exception e) {
			throw new IOException("Failed to list files (" + prefix + ", " + before + ")", e);
		}
	}
}
