package panda.mvc.view;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import panda.io.Files;
import panda.io.Streams;
import panda.lang.Charsets;
import panda.lang.Exceptions;
import panda.lang.Strings;
import panda.log.Log;
import panda.log.Logs;
import panda.mvc.ActionContext;
import panda.mvc.View;
import panda.net.http.HttpContentType;

/**
 * 将数据对象直接写入 HTTP 响应
 * <p>
 * <h2>数据对象可以是如下类型:</h2>
 * <ol>
 * <li><b>null</b> - 什么都不做
 * <li><b>File</b> - 文件,以下载方法返回,文件名将自动设置
 * <li><b>byte[]</b> - 按二进制方式写入HTTP响应流
 * <li><b>InputStream</b> - 按二进制方式写入响应流，并关闭 InputStream
 * <li><b>char[]</b> - 按文本方式写入HTTP响应流
 * <li><b>Reader</b> - 按文本方式写入HTTP响应流，并关闭 Reader
 * <li><b>默认的</b> - 直接将对象 toString() 后按文本方式写入HTTP响应流
 * </ol>
 * <p>
 * <h2>ContentType 支持几种缩写:</h2>
 * <ul>
 * <li><b>xml</b> - 表示 <b>text/xml</b>
 * <li><b>html</b> - 表示 <b>text/html</b>
 * <li><b>htm</b> - 表示 <b>text/html</b>
 * <li><b>stream</b> - 表示 <b>application/octet-stream</b>
 * <li><b>默认的</b>(即 '@Ok("raw")' ) - 将采用 <b>ContentType=text/plain</b>
 * </ul>
 */
public class RawView implements View {

	private static final Log log = Logs.getLog(RawView.class);

	public static final boolean DISABLE_RANGE_DOWNLOAD = false; // 禁用断点续传

	protected static final Map<String, String> contentTypeMap = new HashMap<String, String>();

	static {
		contentTypeMap.put("xml", "application/xml");
		contentTypeMap.put("html", "text/html");
		contentTypeMap.put("htm", "text/html");
		contentTypeMap.put("stream", "application/octet-stream");
		contentTypeMap.put("js", "application/javascript");
		contentTypeMap.put("json", "application/json");
		contentTypeMap.put("jpg", "image/jpeg");
		contentTypeMap.put("jpeg", "image/jpeg");
		contentTypeMap.put("png", "image/png");
		contentTypeMap.put("webp", "image/webp");
	}

	protected String contentType;

	public RawView(String contentType) {
		if (Strings.isBlank(contentType)) {
			contentType = HttpContentType.TEXT_PLAIN;
		}
		this.contentType = Strings.defaultString(contentTypeMap.get(contentType.toLowerCase()), contentType);
	}

	public void render(ActionContext ac) throws Throwable {
		HttpServletRequest req = ac.getRequest();
		HttpServletResponse res = ac.getResponse();
		Object obj = ac.getResult();

		// 如果用户自行设置了,那就不要再设置了!
		if (res.getContentType() == null) {
			if (obj != null && obj instanceof BufferedImage && HttpContentType.TEXT_PLAIN.equals(contentType)) {
				contentType = contentTypeMap.get("png");
			}
			res.setContentType(contentType);
		}

		if (obj == null) {
			return;
		}
		
		// 文件
		if (obj instanceof File) {
			File file = (File)obj;
			long fileSz = file.length();
			if (log.isDebugEnabled())
				log.debug("File downloading ... " + file.getAbsolutePath());
			if (!file.exists() || file.isDirectory()) {
				log.debug("File downloading ... Not Exist : " + file.getAbsolutePath());
				res.sendError(404);
				return;
			}
			if (!res.containsHeader("Content-Disposition")) {
				String filename = URLEncoder.encode(file.getName(), Charsets.UTF_8);
				res.setHeader("Content-Disposition", "attachment; filename=\"" + filename + "\"");
			}

			String rangeStr = req.getHeader("Range");
			OutputStream out = res.getOutputStream();
			if (DISABLE_RANGE_DOWNLOAD || fileSz == 0
					|| (rangeStr == null || !rangeStr.startsWith("bytes=") || rangeStr.length() < "bytes=1".length())) {
				res.setHeader("Content-Length", "" + fileSz);
				Files.copyFile(file, out);
			}
			else {
				// log.debug("Range Download : " + req.getHeader("Range"));
				List<RangeRange> rs = new ArrayList<RawView.RangeRange>();
				if (!parseRange(rangeStr, rs, fileSz)) {
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
					String.format("bytes %d-%d/%d", rangeRange.start, rangeRange.end - 1, fileSz));
				writeFileRange(file, out, rangeRange);
			}
		}
		// 字节数组
		else if (obj instanceof byte[]) {
			res.setHeader("Content-Length", "" + ((byte[])obj).length);
			OutputStream out = res.getOutputStream();
			Streams.write((byte[])obj, out);
		}
		// 字符数组
		else if (obj instanceof char[]) {
			Writer writer = res.getWriter();
			writer.write((char[])obj);
			writer.flush();
		}
		// 文本流
		else if (obj instanceof Reader) {
			Streams.copy((Reader)obj, res.getWriter());
		}
		// 二进制流
		else if (obj instanceof InputStream) {
			OutputStream out = res.getOutputStream();
			Streams.copy((InputStream)obj, out);
		}
		// 普通对象
		else {
			byte[] data = String.valueOf(obj).getBytes(Charsets.UTF_8);
			res.setHeader("Content-Length", "" + data.length);
			OutputStream out = res.getOutputStream();
			Streams.write(data, out);
		}
	}

	public static class RangeRange {
		public RangeRange(long start, long end) {
			this.start = start;
			this.end = end;
		}

		long start;
		long end = -1;
		
		long length() {
			return end - start;
		}

	}

	public static final boolean parseRange(String rangeStr, List<RangeRange> rs, long maxSize) {
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
					// 操!! 单个字节?!!
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

	public static void writeFileRange(File file, OutputStream out, RangeRange rangeRange) {
		FileInputStream fin = null;
		try {
			fin = new FileInputStream(file);
			Streams.copyLarge(fin, out, rangeRange.start, rangeRange.length());
		}
		catch (Throwable e) {
			throw Exceptions.wrapThrow(e);
		}
		finally {
			Streams.safeClose(fin);
		}
	}

	@Override
	public String toString() {
		return this.getClass().getName() + ": " + contentType;
	}
}
