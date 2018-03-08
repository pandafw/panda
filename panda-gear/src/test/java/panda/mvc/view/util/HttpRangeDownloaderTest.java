package panda.mvc.view.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import panda.bind.json.Jsons;
import panda.io.Files;
import panda.io.Streams;
import panda.io.stream.ByteArrayOutputStream;
import panda.mvc.view.util.HttpRangeDownloader.RangeRange;

public class HttpRangeDownloaderTest {
	private FileOutputStream makeTemp(File file) throws IOException {
		FileOutputStream out = new FileOutputStream(file);
		for (int i = 0; i < 256; i++) {
			out.write(i);
		}
		out.flush();
		out.close();
		return out;
	}

	@Test
	public void test2() throws Throwable {
		HttpRangeDownloader hrd = new HttpRangeDownloader();
		
		File src = File.createTempFile("panda", "raw");
		FileOutputStream out = null;
		
		try {
			out = makeTemp(src);

			List<RangeRange> rs = new ArrayList<HttpRangeDownloader.RangeRange>();
			hrd.parseRange("bytes=0-,-1000000,22222-22222222222", rs, Long.MAX_VALUE);
			System.out.println(Jsons.toJson(rs));

			rs = new ArrayList<HttpRangeDownloader.RangeRange>();
			hrd.parseRange("bytes=0-127", rs, 256);
			
			ByteArrayOutputStream dst = new ByteArrayOutputStream();
			hrd.writeFileRange(src, dst, rs.get(0));
			Assert.assertEquals(128, dst.size());
	
			InputStream in = dst.toInputStream();
			for (int i = 0; i < 128; i++) {
				Assert.assertEquals(i, in.read());
			}
		}
		finally {
			Streams.safeClose(out);
			Files.safeDelete(src);
		}
	}
	
	@Test
	public void test3() throws Throwable {
		HttpRangeDownloader hrd = new HttpRangeDownloader();

		File src = File.createTempFile("panda", "raw");
		FileOutputStream out = null;
		
		try {
			out = makeTemp(src);

			List<RangeRange> rs = new ArrayList<HttpRangeDownloader.RangeRange>();
			hrd.parseRange("bytes=128-", rs, 256);
			System.out.println(Jsons.toJson(rs));
		
			ByteArrayOutputStream dst = new ByteArrayOutputStream();
			hrd.writeFileRange(src, dst, rs.get(0));
			Assert.assertEquals(128, dst.size());

			InputStream in = dst.toInputStream();
			for (int i = 0; i < 128; i++) {
				Assert.assertEquals(i + 128, in.read());
			}
		}
		finally {
			Streams.safeClose(out);
			Files.safeDelete(src);
		}
	}
	
	@Test
	public void test4() throws Throwable {
		HttpRangeDownloader hrd = new HttpRangeDownloader();

		File src = File.createTempFile("panda", "raw");
		FileOutputStream out = null;
		
		try {
			out = makeTemp(src);

			List<RangeRange> rs = new ArrayList<HttpRangeDownloader.RangeRange>();
			hrd.parseRange("bytes=-64", rs, 256);
			System.out.println(Jsons.toJson(rs));

			ByteArrayOutputStream dst = new ByteArrayOutputStream();
			hrd.writeFileRange(src, dst, rs.get(0));
			Assert.assertEquals(64, dst.size());
			
			InputStream in = dst.toInputStream();
			for (int i = 0; i < 64; i++) {
				Assert.assertEquals(i + 128 + 64, in.read());
			}
		}
		finally {
			Streams.safeClose(out);
			Files.safeDelete(src);
		}
	}
}
