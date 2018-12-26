package panda.vfs.gcs;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

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

import panda.io.FileNames;
import panda.io.Streams;
import panda.lang.Randoms;
import panda.lang.Strings;
import panda.lang.time.DateTimes;
import panda.log.Log;
import panda.log.Logs;
import panda.vfs.FileItem;
import panda.vfs.FilePool;

public class GcsFilePool implements FilePool {
	private final static Log log = Logs.getLog(GcsFilePool.class);
	
	/** Used below to determine the size of chucks to read in. Should be > 1kb and < 10MB */
	private static final int BUFFER_SIZE = 2 * 1024 * 1024;
	
	protected GcsService gcsService;
	
	protected String bucket;

	protected String prefix;

	/**
	 * milliseconds since last modified. (default: 1 day) 
	 */
	protected long expires = DateTimes.MS_DAY;

	public GcsFilePool() {
	}

	public void initialize() {
		if (Strings.isEmpty(prefix)) {
			prefix = "temp/";
		}
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

	/**
	 * @return the maxAge
	 */
	public int getMaxAge() {
		return (int)(expires / 1000);
	}

	/**
	 * @param maxAge the maxAge to set
	 */
	public void setMaxAge(int maxAge) {
		this.expires = maxAge * 1000L;
	}

	@Override
	public Class<? extends FileItem> getItemType() {
		return GcsFileItem.class;
	}
	
	@Override
	public FileItem saveFile(String name, final byte[] data) throws IOException {
		name = FileNames.trimFileName(name);
		if (Strings.isEmpty(name)) {
			name = "noname";
		}

		String id = null;
		while (true) {
			id = Randoms.randUUID32();
			FileItem fi = findFile(id);
			if (fi == null || !fi.isExists()) {
				break;
			}
		}

		GcsFileItem gfi = new GcsFileItem(this);
		gfi.setId(id);
		gfi.setName(name);
		
		saveFile(gfi, data);
		return gfi;
	}
	
	@Override
	public FileItem saveFile(String name, final InputStream data) throws IOException {
		return saveFile(name, Streams.toByteArray(data));
	}
	
	@Override
	public FileItem findFile(String id) throws IOException {
		ListOptions.Builder lob = new ListOptions.Builder();
		ListResult lr = gcsService.list(bucket, lob.setPrefix(prefix + id + '/').setRecursive(true).build());
		if (lr.hasNext()) {
			return new GcsFileItem(this, id, lr.next());
		}

		GcsFileItem gfi = new GcsFileItem(this);
		gfi.setId(id);
		gfi.setName("noname");
		gfi.setSize(0);
		gfi.setExists(false);
		return gfi;
	}

	protected void saveFile(GcsFileItem gfi, byte[] data) throws IOException {
		GcsFilename gfn = toGcsFilename(gfi);
		GcsFileOptions gfo = GcsFileOptions.getDefaultInstance();
		GcsOutputChannel goc = gcsService.createOrReplace(gfn, gfo);

		goc.write(ByteBuffer.wrap(data));
		goc.close();
		
		gfi.setSize(data.length);
		gfi.setDate(DateTimes.getDate());
		gfi.setExists(true);
	}
	
	protected void saveFile(GcsFileItem gfi, InputStream data) throws IOException {
		saveFile(gfi, Streams.toByteArray(data));
	}
	
	protected InputStream openFile(GcsFileItem gfi) throws IOException {
		GcsFilename gfn = toGcsFilename(gfi);
		GcsInputChannel gic = gcsService.openPrefetchingReadChannel(gfn, 0, BUFFER_SIZE);
		return Channels.newInputStream(gic);
	}

	protected boolean removeFile(GcsFileItem gfi) throws IOException {
		GcsFilename gfn = toGcsFilename(gfi);
		return gcsService.delete(gfn);
	}

	protected GcsFilename toGcsFilename(GcsFileItem gfi) {
		return new GcsFilename(bucket, prefix + gfi.getId() + '/' + gfi.getName());
	}
	
	@Override
	public synchronized int clean() throws IOException {
		final long time = System.currentTimeMillis() - expires;
		Collection<FileItem> fs = listFiles();

		int cnt = 0;
		for (FileItem f : fs) {
			GcsFilename gfn = toGcsFilename((GcsFileItem)f);
			if (f.getDate().getTime() < time) {
				if (gcsService.delete(gfn)) {
					cnt++;
					if (log.isDebugEnabled()) {
						log.debug("Remove temporary file: " + gfn);
					}
				}
			}
		}
		
		return cnt;
	}

	@Override
	public List<FileItem> listFiles() throws IOException {
		ListOptions.Builder lob = new ListOptions.Builder();
		ListResult lr = gcsService.list(bucket, lob.setPrefix(prefix).setRecursive(true).build());

		List<FileItem> fis = new ArrayList<FileItem>();
		while (lr.hasNext()) {
			ListItem i = lr.next();
			String id = FileNames.getParent(Strings.removeStart(i.getName(), prefix));
			fis.add(new GcsFileItem(this, id, i));
		}

		return fis;
	}
}
