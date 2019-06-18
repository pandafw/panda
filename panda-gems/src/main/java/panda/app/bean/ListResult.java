package panda.app.bean;

import java.util.List;

import panda.mvc.bean.Pager;

/**
 * List Result POJO
 */
public class ListResult<T> {
	private List<T> list;
	private Pager page;

	/**
	 * @return the list
	 */
	public List<T> getList() {
		return list;
	}

	/**
	 * @param list the list to set
	 */
	public void setList(List<T> list) {
		this.list = list;
	}

	/**
	 * @return the page
	 */
	public Pager getPage() {
		return page;
	}

	/**
	 * @param page the page to set
	 */
	public void setPage(Pager page) {
		this.page = page;
	}
}
