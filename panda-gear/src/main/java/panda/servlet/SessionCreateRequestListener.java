package panda.servlet;

import javax.servlet.ServletRequestEvent;
import javax.servlet.ServletRequestListener;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import panda.log.Log;
import panda.log.Logs;

public class SessionCreateRequestListener implements ServletRequestListener {
	private Log log = Logs.getLog(SessionCreateRequestListener.class);

	public void requestInitialized(ServletRequestEvent sre) {
		HttpServletRequest request = (HttpServletRequest)sre.getServletRequest();

		HttpSession hs = request.getSession(false);
		if (hs == null) {
			hs = request.getSession(true);
			if (log.isDebugEnabled()) {
				log.debug("Create HttpSession: " + hs.getId());
			}
		}
	}

	public void requestDestroyed(ServletRequestEvent sre) {
	}
}
