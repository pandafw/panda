package panda.bean;

import java.util.List;

public class TestG<T> {
	T obj;
	List<T> lst;
	
	/**
	 * @return the obj
	 */
	public T getObj() {
		return obj;
	}
	/**
	 * @param obj the obj to set
	 */
	public void setObj(T obj) {
		this.obj = obj;
	}
	/**
	 * @return the lst
	 */
	public List<T> getLst() {
		return lst;
	}
	/**
	 * @param lst the lst to set
	 */
	public void setLst(List<T> lst) {
		this.lst = lst;
	}
	
}
