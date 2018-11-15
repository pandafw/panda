package panda.app.action.tool;


import java.io.Flushable;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeSet;

import panda.app.action.AbstractAction;
import panda.app.auth.Auth;
import panda.app.constant.AUTH;
import panda.bind.json.Jsons;
import panda.dao.Dao;
import panda.dao.DaoIterator;
import panda.dao.entity.Entity;
import panda.dao.entity.EntityField;
import panda.dao.query.EntityQuery;
import panda.io.MimeTypes;
import panda.io.stream.CsvWriter;
import panda.io.stream.ListWriter;
import panda.io.stream.TsvWriter;
import panda.io.stream.XlsWriter;
import panda.io.stream.XlsxWriter;
import panda.lang.Charsets;
import panda.lang.Exceptions;
import panda.lang.Strings;
import panda.lang.time.DateTimes;
import panda.log.Logs;
import panda.mvc.Mvcs;
import panda.mvc.annotation.At;
import panda.mvc.annotation.To;
import panda.mvc.view.Views;
import panda.servlet.HttpServletResponser;

@At("${super_path}/dataexp")
@Auth(AUTH.SUPER)
@To(Views.SFTL)
public class DataExportAction extends AbstractAction {
	protected static class DataType {
		String type;
		String format;
		
		public DataType(String type) {
			int i = type.indexOf(':');
			if (i >= 0) {
				this.type = type.substring(0, i);
				this.format = type.substring(i + 1);
			}
			else {
				this.type = type;
			}
		}
	}
	
	public static class Arg {
		public String target;
		public int start = 0;
		public int limit = 1000;
		public String format = "CSV";
	}

	protected Arg arg;
	protected Set<String> targetSet;
	
	/**
	 * @return the targetSet
	 */
	public Set<String> getTargets() {
		if (targetSet == null) {
			targetSet = new TreeSet<String>();
			for (Class<?> c : getDaoClient().getEntities().keySet()) {
				targetSet.add(c.getName());
			}
		}
		return targetSet;
	}

	protected Entity<?> resolveTargetEntity(String target) {
		for (Entry<Class<?>, Entity<?>> en : getDaoClient().getEntities().entrySet()) {
			if (en.getKey().getName().equalsIgnoreCase(target)) {
				return en.getValue();
			}
		}

		throw new IllegalArgumentException("Invalid target type: " + target);
	}

	/**
	 * @param arg the input arguments
	 * @return result list
	 * @throws Exception if an error occurs
	 */
	@At("")
	public Object execute(Arg arg) throws Exception {
		this.arg = arg;

		if (Strings.isEmpty(arg.target)) {
			return null;
		}

		ListWriter lw = null;
		
		if ("TSV".equalsIgnoreCase(arg.format)) {
			PrintWriter pw = getResponse().getWriter();
			lw = new TsvWriter(pw);
		}
		else if ("XLS".equalsIgnoreCase(arg.format)) {
			OutputStream os = getResponse().getOutputStream();
			lw = new XlsWriter(os, arg.target);
		}
		else if ("XLSX".equalsIgnoreCase(arg.format)) {
			OutputStream os = getResponse().getOutputStream();
			lw = new XlsxWriter(os, arg.target);
		}
		else {
			PrintWriter pw = getResponse().getWriter();
			lw = new CsvWriter(pw);
		}
		
		return export(lw);
	}

	@SuppressWarnings("unchecked")
	protected Object export(ListWriter lw) {
		try {
			HttpServletResponser hss = new HttpServletResponser(getRequest(), getResponse());

			hss.setCharset(Charsets.UTF_8);
			hss.setContentType(MimeTypes.getMimeType('.' + arg.format));
			hss.setFileName(arg.target + '-' + DateTimes.datetimeLogFormat().format(DateTimes.getDate()) + '.' + Strings.lowerCase(arg.format));
			hss.writeHeader();
			
			List<String> line = new ArrayList<String>();
			
			Entity<?> en = resolveTargetEntity(arg.target);
	
			// head
			for (EntityField ef : en.getFields()) {
				if (ef.isPersistent()) {
					line.add(ef.getName());
				}
			}
			lw.writeList(line);
	
			// data
			Dao dao = getDaoClient().getDao();
			EntityQuery eq = new EntityQuery(en);
			eq.start(arg.start).limit(arg.limit);
	
			try (DaoIterator it = dao.iterate(eq)) {
				while (it.hasNext()) {
					Object d = it.next();
	
					line.clear();
					for (EntityField ef : en.getFields()) {
						if (ef.isPersistent()) {
							String s;
							Object o = en.getFieldValue(d, ef.getName());
							if (o instanceof Collection) {
								s = Jsons.toJson(o);
							}
							else {
								s = Mvcs.castString(getContext(), o);
							}
							line.add(s);
						}
					}
					lw.writeList(line);
				}
			}
			
			if (lw instanceof Flushable) {
				((Flushable)lw).flush();
			}
			return Views.none(getContext());
		}
		catch (IOException e) {
			logException(arg.format, e);
			return null;
		}
	}
	
	protected void logException(String msg, Throwable e) {
		Logs.getLog(getClass()).warn(msg, e);
		if (getContext().isAppDebug()) {
			String s = Exceptions.getStackTrace(e);
			addActionError(e.getMessage() + "\n" + s);
		}
		else {
			addActionError(e.getMessage());
		}
	}
}
