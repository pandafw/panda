package panda.wing.action.tool;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import panda.bind.json.Jsons;
import panda.io.Streams;
import panda.lang.Charsets;
import panda.lang.reflect.Types;
import panda.mvc.annotation.At;
import panda.mvc.annotation.view.Ok;
import panda.wing.action.AbstractAction;
import panda.wing.task.CronEntry;

/**
 */
@At("/admin")
public class CronJobsAction extends AbstractAction {
	/**
	 * execute
	 * 
	 * @return SUCCESS
	 * @throws Exception if an error occurs
	 */
	@At("cronjobs")
	@Ok("ftl")
	public List<CronEntry> execute() throws Exception {
		String webinf = getContext().getServlet().getRealPath("/WEB-INF");
		
		File json = new File(webinf, "cron.json");
		if (json.exists()) {
			return loadJson(json);
		}
		File xml = new File(webinf, "cron.xml");
		if (xml.exists()) {
			return loadXml(xml);
		}
		return null;
	}
	
	private List<CronEntry> loadJson(File path) throws IOException {
		InputStream is = null;
		try {
			is = new FileInputStream(path);
			return Jsons.fromJson(is, Charsets.UTF_8, Types.paramTypeOf(List.class, CronEntry.class));
		}
		finally {
			Streams.safeClose(is);
		}
	}

	private List<CronEntry> loadXml(File path) throws Exception {
		InputStream is = null;
		try {
			is = new FileInputStream(path);
			DocumentBuilderFactory dbfactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = dbfactory.newDocumentBuilder();
			Document doc = builder.parse(is);
			Element root = doc.getDocumentElement();
			NodeList nl = root.getChildNodes();
			
			List<CronEntry> cronEntries = new ArrayList<CronEntry>(nl.getLength());
			
			for (int i = 0; i < nl.getLength(); i++) {
				Node n = nl.item(i);
				if (n.getNodeName().equals("cron")) {
					CronEntry ce = new CronEntry();
					
					NodeList cnl = n.getChildNodes();
					for (int j = 0; j < cnl.getLength(); j++) {
						Node c = cnl.item(j);
						if (c.getNodeName().equals("url")) {
							ce.setUrl(c.getTextContent());
						}
						else if (c.getNodeName().equals("description")) {
							ce.setDescription(c.getTextContent());
						}
						else if (c.getNodeName().equals("schedule")) {
							ce.setCron(c.getTextContent());
						}
					}
					cronEntries.add(ce);
				}
			}
			
			return cronEntries;
		}
		finally {
			Streams.safeClose(is);
		}
	}
}
