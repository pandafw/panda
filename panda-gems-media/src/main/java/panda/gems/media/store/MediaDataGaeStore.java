package panda.gems.media.store;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;

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

import panda.dao.Dao;
import panda.gems.media.I;
import panda.gems.media.V;
import panda.gems.media.entity.Media;
import panda.gems.media.entity.MediaData;
import panda.image.ImageWrapper;
import panda.image.Images;
import panda.io.FileNames;
import panda.io.Streams;
import panda.ioc.annotation.IocInject;
import panda.lang.Arrays;
import panda.lang.Strings;
import panda.log.Log;
import panda.log.Logs;
import panda.vfs.FileStores;

public class MediaDataGaeStore extends AbstractMediaDataStore {
	private static final Log log = Logs.getLog(MediaDataGaeStore.class);

	/** Used below to determine the size of chucks to read in. Should be > 1kb and < 10MB */
	private static final int BUFFER_SIZE = 2 * 1024 * 1024;
	
	protected GcsService gcsService;
	
	@IocInject(value=I.MEDIA_GCS_BUCKET, required=false)
	protected String bucket;

	@IocInject(value=I.MEDIA_GCS_PREFIX, required=false)
	protected String prefix = "media/";

	public MediaDataGaeStore() {
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

	protected String getFilePath(Media m) {
		return prefix + Strings.leftPad(String.valueOf(m.getId() % 10000), 4, '0') + '/' + m.getSlug() + '/';
	}
	
	protected String getFileName(Media m, int sz) {
		StringBuilder name = new StringBuilder();
		
		name.append(getFilePath(m));
		if (sz == V.ORIGINAL) {
			name.append(m.getName());
		}
		else {
			name.append(FileNames.getBaseName(m.getName())).append('.').append(sz);
			String ext = FileNames.getExtension(m.getName());
			if (Strings.isNotEmpty(ext)) {
				name.append('.').append(ext);
			}
		}

		return name.toString();
	}
	
	protected GcsFilename toGcsFilename(Media m, int sz) {
		return new GcsFilename(bucket, getFileName(m, sz));
	}

	protected byte[] readData(GcsFilename gfn) throws IOException {
		GcsInputChannel gic = gcsService.openPrefetchingReadChannel(gfn, 0, BUFFER_SIZE);
		InputStream in = Channels.newInputStream(gic);
		try {
			return Streams.toByteArray(in);
		}
		finally {
			Streams.safeClose(in);
		}
	}

	protected boolean exists(GcsFilename gfn) {
		try {
			GcsFileMetadata gfm = gcsService.getMetadata(gfn);
			return gfm != null;
		}
		catch (IOException e) {
			log.error("Failed to get metadata of " + gfn.getBucketName() + ": " + gfn.getObjectName(), e);
			return false;
		}
	}

	@Override
	public MediaData find(Dao dao, Media m, int sz) {
		byte[] data = null;
		
		GcsFilename gfn = toGcsFilename(m, sz);
		if (exists(gfn)) {
			try {
				data = readData(gfn);
			}
			catch (IOException e) {
				log.error("Failed to read data of media [" + m.getId() + "] (" + sz + ")", e);
				return null;
			}
		}
		else if (sz != V.ORIGINAL) {
			GcsFilename org = toGcsFilename(m, V.ORIGINAL);
			if (!exists(org)) {
				return null;
			}

			try {
				// resize
				byte[] odata = readData(org);
				ImageWrapper iw = Images.i().read(odata);
				iw = iw.resize(sz);
				data = iw.getData();

				// save
				save(gfn, data);
			}
			catch (Exception e) {
				log.error("Failed to save data of media [" + m.getId() + "] (" + sz + ")", e);
				return null;
			}
		}
		else {
			return null;
		}

		MediaData md = new MediaData();
		md.setMid(m.getId());
		md.setMsz(sz);
		md.setSize(data.length);
		md.setData(data);

		return md;
	}

	protected void save(GcsFilename gfn, byte[] data) throws IOException {
		GcsFileOptions gfo = GcsFileOptions.getDefaultInstance();
		GcsOutputChannel goc = gcsService.createOrReplace(gfn, gfo);
		goc.write(ByteBuffer.wrap(data));
		goc.close();
	}
	
	@Override
	public void save(Dao dao, Media m) {
		GcsFilename gfn = toGcsFilename(m, V.ORIGINAL);

		try {
			byte[] data = FileStores.toByteArray(m.getFile());

			save(gfn, data);
		}
		catch (IOException e) {
			throw new RuntimeException("Failed to save media data of [" + m.getId() + "]", e);
		}
	}

	@Override
	public void delete(Dao dao, Media... ms) {
		if (Arrays.isEmpty(ms)) {
			return;
		}
		
		for (Media m : ms) {
			if (m == null) {
				continue;
			}
			
			try {
				ListOptions.Builder lob = new ListOptions.Builder();
				ListResult lr = gcsService.list(bucket, lob.setPrefix(getFilePath(m)).setRecursive(true).build());
				while (lr.hasNext()) {
					ListItem i = lr.next();

					GcsFilename gfn = new GcsFilename(bucket, i.getName());
					gcsService.delete(gfn);
				}
			}
			catch (Exception e) {
				log.error("Failed to delete media data for [" + m.getSlug() + "]", e);
			}
		}
	}
}
