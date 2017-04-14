package panda.aop.interceptor;

import java.sql.Connection;

import panda.aop.InterceptorChain;
import panda.aop.MethodInterceptor;
import panda.dao.Dao;
import panda.lang.Exceptions;

/**
 * 可以插入事务的拦截器
 * <p/>
 * 默认事务登记为 Connection.TRANSACTION_READ_COMMITTED
 * <p/>
 * 可以在构建拦截器时设置
 * 
 * @author wendal(wendal1985@gmail.com)
 */
public class TransactionInterceptor implements MethodInterceptor {

	private Dao dao;
	private int level = Connection.TRANSACTION_NONE;

	public TransactionInterceptor(Dao dao) {
		this.dao = dao;
	}

	public TransactionInterceptor(Dao dao, int level) {
		this.dao = dao;
		this.level = level;
	}

	public void filter(final InterceptorChain chain) {
		dao.exec(new Runnable() {
			public void run() {
				try {
					chain.doChain();
				}
				catch (Throwable e) {
					throw Exceptions.wrapThrow(e);
				}
			}
		}, level);
	}

}
