package panda.app.util.pdf;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import panda.app.constant.MVC;
import panda.app.constant.SET;
import panda.app.util.AppSettings;
import panda.io.Files;
import panda.io.Streams;
import panda.ioc.annotation.IocBean;
import panda.ioc.annotation.IocInject;
import panda.lang.Collections;
import panda.lang.Iterators;
import panda.lang.Processors;
import panda.lang.Processors.Waiter;
import panda.lang.Strings;
import panda.lang.time.StopWatch;
import panda.log.Log;
import panda.log.Logs;
import panda.net.http.HttpHeader;

@IocBean(type=Html2Pdf.class, singleton=false, create="initialize")
public class WkHtml2Pdf extends Html2Pdf {
	private static final Log log = Logs.getLog(WkHtml2Pdf.class);

	@IocInject
	protected AppSettings settings;
	
	@IocInject(value=MVC.WKHTML2PDF_PATH, required=false)
	protected String path = "wkhtmltopdf";
	
	@IocInject(value=MVC.WKHTML2PDF_TIMEOUT, required=false)
	protected int timeout = 120;

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public int getTimeout() {
		return timeout;
	}

	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}

	public void initialize() {
		path = settings.getPropertyAsPath(SET.WKHTML2PDF_PATH, path);
		timeout = settings.getPropertyAsInt(SET.WKHTML2PDF_TIMEOUT, timeout);
	}
	
	@Override
	public void process(OutputStream os) throws Exception {
		if (!Files.isFile(path)) {
			throw new RuntimeException("wkhtmltopdf not exists: " + path);
		}

		int wait = timeout * 1000;

		File of = File.createTempFile("wkhtmltopdf-", ".pdf");

		List<String> cmds = new ArrayList<String>();
		cmds.add(path);
		if (!log.isDebugEnabled()) {
			cmds.add("-q"); // quiet
		}
		if (Strings.isNotEmpty(charset)) {
			cmds.add("--encoding");
			cmds.add(charset);
		}
		cmds.add("--print-media-type");
		cmds.add("--margin-top");
		cmds.add("10");
		cmds.add("--margin-right");
		cmds.add("10");
		cmds.add("--margin-bottom");
		cmds.add("10");
		cmds.add("--margin-left");
		cmds.add("10");
		cmds.add("--footer-center");
		cmds.add("[page] / [topage]");
		cmds.add("--footer-spacing");
		cmds.add("3");
		cmds.add("--footer-font-name");
		cmds.add("Courier New");
		cmds.add("--footer-font-size");
		cmds.add("7");
		
		if (Collections.isNotEmpty(headers)) {
			HttpHeader hh = new HttpHeader();
			hh.putAll(headers);
			for (Entry<String, Object> en : hh.entrySet()) {
				String key = en.getKey();
				Object val = en.getValue();
				for (Object v : Iterators.asIterable(val)) {
					cmds.add("--custom-header");
					cmds.add(key);
					cmds.add(v.toString());
				}
			}
		}
		cmds.add(url);
		cmds.add(of.getAbsolutePath());

		if (log.isDebugEnabled()) {
			log.debug(Strings.join(cmds, " ", "\"", "\""));
		}
		
		Process p = null;
		StopWatch watch = new StopWatch();
		try {
			ProcessBuilder pb = new ProcessBuilder(cmds);
			pb.redirectErrorStream(true);
			
			p = pb.start();

			StringBuilder sb = new StringBuilder();
			InputStream stdout = p.getInputStream();

			Waiter w = new Waiter(p);
			w.start();
			
			while (w.isAlive()) {
				w.join(100);

				if (sb != null) {
					Streams.copy(stdout, sb);
				}

				if (watch.getTime() > wait) {
					throw new RuntimeException("Process [" + Processors.getPid(p) + "] of Command [ " + Strings.join(cmds, ' ') + " ] is timeout for " + timeout + " seconds.");
				}
			}

			Streams.copy(stdout, sb);
			if (log.isDebugEnabled() && Strings.isNotEmpty(sb)) {
				log.debug(sb.toString());
			}

			if (w.exitValue() != 0) {
				throw new IOException("Html2PDF failed with code (" + w.exitValue() + ")\n" + sb.toString());
			}
			Files.copyFile(of, os);
		}
		catch (Exception e) {
			if (p != null) {
				p.destroy();
			}
			throw e;
		}
		finally {
			of.delete();
		}
	}
}
