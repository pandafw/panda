package panda.wing.util.pdf;

import java.io.File;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import panda.io.Files;
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
import panda.wing.AppConstants;

@IocBean(type=Html2Pdf.class)
public class WkHtml2Pdf implements Html2Pdf {
	private static final Log log = Logs.getLog(WkHtml2Pdf.class);
	
	@IocInject(value=AppConstants.PANDA_WKHTML2PDF_PATH)
	protected String path;
	
	@IocInject(value=AppConstants.PANDA_WKHTML2PDF_TIMEOUT, required=false)
	protected int timeout = 300;
	
	public void process(OutputStream os, String url, String charset, Map<String, Object> headers) throws Exception {
		if (!Files.isFile(path)) {
			throw new RuntimeException("wkhtmltopdf not exists: " + path);
		}

		int wait = timeout * 1000;

		File of = File.createTempFile("wkhtmltopdf-", ".pdf");
		List<String> cmds = new ArrayList<String>();
		cmds.add(path);
		
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
			log.debug(Strings.join(cmds, ' '));
		}
		
		Process p = null;
		StopWatch watch = new StopWatch();
		try {
			ProcessBuilder pb = new ProcessBuilder(cmds);
			p = pb.start();

			Waiter w = new Waiter(p);
			w.start();
			
			while (w.isAlive()) {
				w.join(100);

				if (watch.getTime() > wait) {
					throw new RuntimeException("Process [" + Processors.getPid(p) + "] of Command [ " + Strings.join(cmds, ' ') + " ] is timeout for " + timeout + " seconds.");
				}
			}

			Files.copyFile(of, os);
		}
		catch (Exception e) {
			if (p != null) {
				p.destroy();
			}
		}
		finally {
			of.delete();
		}
	}
}
