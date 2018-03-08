package panda.mvc.view.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import panda.io.Streams;
import panda.ioc.annotation.IocBean;
import panda.lang.Strings;
import panda.log.Log;
import panda.log.Logs;

@IocBean
public class HttpRangeDownloader {
	private static final Log log = Logs.getLog(HttpRangeDownloader.class);
	
	public void download(HttpServletRequest req, HttpServletResponse res, File file) throws IOException {
		long fileSize = file.length();

		String range = req.getHeader("Range");
		if (log.isDebugEnabled()) {
			log.debug("Range Download : " + range);
		}
		
		OutputStream out = res.getOutputStream();
		List<RangeRange> rs = new ArrayList<RangeRange>();
		if (!parseRange(range, rs, fileSize)) {
			res.setStatus(416);
			return;
		}

		// 暂时只实现了单range
		if (rs.size() != 1) {
			// TODO 完成多range的下载
			log.info("multipart/byteranges is NOT support yet");
			res.setStatus(416);
			return;
		}

		long totolSize = 0;
		for (RangeRange rangeRange : rs) {
			totolSize += (rangeRange.end - rangeRange.start);
		}
		res.setStatus(206);
		res.setHeader("Content-Length", "" + totolSize);
		res.setHeader("Accept-Ranges", "bytes");

		// 暂时只有单range,so,简单起见吧
		RangeRange rangeRange = rs.get(0);
		res.setHeader("Content-Range",
			String.format("bytes %d-%d/%d", rangeRange.start, rangeRange.end - 1, fileSize));
		writeFileRange(file, out, rangeRange);

		out.flush();
	}
	
	public static class RangeRange {
		public RangeRange(long start, long end) {
			this.start = start;
			this.end = end;
		}

		public long start;
		public long end = -1;
		
		public long length() {
			return end - start;
		}
	}

	protected boolean parseRange(String rangeStr, List<RangeRange> rs, long maxSize) {
		rangeStr = rangeStr.substring("bytes=".length());
		String[] ranges = rangeStr.split(",");
		for (String range : ranges) {
			if (range == null || Strings.isBlank(range)) {
				log.debug("Bad Range -->    " + rangeStr);
				return false;
			}

			range = range.trim();
			try {
				// 首先是从后往前算的 bytes=-100 取最后100个字节
				if (range.startsWith("-")) {
					// 注意,这里是负数
					long end = Long.parseLong(range);
					long start = maxSize + end;
					if (start < 0) {
						log.debug("Bad Range -->    " + rangeStr);
						return false;
					}
					rs.add(new RangeRange(start, maxSize));
					continue;
				}

				// 然后就是从开头到最后 bytes=1024-
				if (range.endsWith("-")) {
					// 注意,这里是负数
					long start = Long.parseLong(range.substring(0, range.length() - 1));
					if (start < 0) {
						log.debug("Bad Range -->    " + rangeStr);
						return false;
					}
					rs.add(new RangeRange(start, maxSize));
					continue;
				}

				// 哦也,是最标准的有头有尾?
				if (range.contains("-")) {
					String[] tmp = range.split("-");
					long start = Long.parseLong(tmp[0]);
					long end = Long.parseLong(tmp[1]);
					if (start > end) {
						log.debug("Bad Range -->    " + rangeStr);
						return false;
					}
					rs.add(new RangeRange(start, end + 1)); // 这里需要调查一下
				}
				else {
					// 单个字节?!!
					long start = Long.parseLong(range);
					rs.add(new RangeRange(start, start + 1));
				}
			}
			catch (Throwable e) {
				log.debug("Bad Range -->    " + rangeStr, e);
				return false;
			}
		}
		return !rs.isEmpty();
	}

	protected void writeFileRange(File file, OutputStream out, RangeRange rangeRange) throws IOException {
		FileInputStream fin = new FileInputStream(file);
		try {
			Streams.copyLarge(fin, out, rangeRange.start, rangeRange.length());
		}
		finally {
			Streams.safeClose(fin);
		}
	}
}
