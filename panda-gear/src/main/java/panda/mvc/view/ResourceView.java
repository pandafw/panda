package panda.mvc.view;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import panda.io.Settings;
import panda.ioc.annotation.IocBean;
import panda.lang.Charsets;
import panda.lang.ClassLoaders;
import panda.lang.Strings;
import panda.lang.time.DateTimes;
import panda.log.Log;
import panda.log.Logs;
import panda.mvc.ActionContext;
import panda.mvc.SetConstants;
import panda.net.http.HttpStatus;
import panda.servlet.HttpServletResponser;
import panda.vfs.FileItem;

/**
 * static resource view
 * <p/>
 * <ul>
 * <li>'@To("res:/abc/cbc")' => /abc/cbc
 * <li>'@To("res:/abc/cbc.txt")' => /abc/cbc.txt
 * </ul>
 */
@IocBean(singleton=false)
public class ResourceView extends AbstractView {
	private static final Log log = Logs.getLog(ResourceView.class);

	public ResourceView() {
	}
	
	public ResourceView(String location) {
		setArgument(location);
	}
	
	public void render(ActionContext ac) {
		String path = argument;

		Object file = ac.getResult();
		if (file != null) {
			if (file instanceof FileItem
					|| file instanceof File
					|| file instanceof URL
					|| file instanceof byte[]
					|| file instanceof InputStream) {
				sendFile(ac, file);
				return;
			}
			
			if (file instanceof CharSequence) {
				path = file.toString();
			}
			else {
				throw new RuntimeException("Invalid result for " + getClass().getName() + ": " + file);
			}
		}

		if (Strings.isEmpty(path)) {
			ac.getResponse().setStatus(HttpStatus.SC_NOT_FOUND);
			return;
		}

		file = findResource(ac, path);
		sendFile(ac, file);
	}

	protected File findFile(ActionContext ac, String path) {
		String rpath = ac.getServlet().getRealPath(path);
		File file = new File(rpath);
		return file.exists() ? file : null;
	}
	
	protected Object findResource(ActionContext ac, String path) {
		if (log.isDebugEnabled()) {
			log.debug("Find Resource: " + path);
		}

		File file = findFile(ac, path);
		if (file != null) {
			return file;
		}
		
		return ClassLoaders.getResourceAsURL(path);
	}

	protected void sendFile(ActionContext ac, Object file) {
		if (file == null 
				|| (file instanceof File && !((File)file).exists())
				|| (file instanceof FileItem && !((FileItem)file).isExists())) {
			if (log.isDebugEnabled()) {
				log.warn("[NOT FOUND]: " + file);
			}
			ac.getResponse().setStatus(HttpStatus.SC_NOT_FOUND);
			return;
		}
		
		forward(ac, file);
	}

	protected void forward(ActionContext ac, Object file) {
		forward(ac.getSettings(), ac.getRequest(), ac.getResponse(), file);
	}
	
	protected void forward(Settings settings, HttpServletRequest request, HttpServletResponse response, Object file) {
		HttpServletResponser hsr = new HttpServletResponser(request, response);

		hsr.setMaxAge(settings.getPropertyAsInt(SetConstants.MVC_STATIC_MAXAGE, 30 * DateTimes.SEC_DAY));
		hsr.setCharset(settings.getProperty(SetConstants.MVC_STATIC_CHARSET, Charsets.UTF_8));
		hsr.setFile(file);
		
		try {
			hsr.forward();
		}
		catch (IOException e) {
			log.warn("Failed to send resource: " + e.getMessage());
		}
	}
}
