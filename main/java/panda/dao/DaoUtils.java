package panda.dao;

/**
 * @author yf.frank.wang@gmail.com
 */
public class DaoUtils {
	
	/**
	 * close session with out throw exception
	 * @param session session
	 */
	public static void safeClose(DaoSession session) {
		try {
			if (session != null) {
				session.close();
			}
		} catch (Throwable e) {
		}
	}
}
