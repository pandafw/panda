package panda.tube.zendesk;

import panda.bind.json.Jsons;

public class ListResult {
	private int count;
	private String next_page;
	private String previous_page;

	/**
	 * @return the count
	 */
	public int getCount() {
		return count;
	}

	/**
	 * @param count the count to set
	 */
	public void setCount(int count) {
		this.count = count;
	}

	/**
	 * @return the next_page
	 */
	public String getNext_page() {
		return next_page;
	}

	/**
	 * @param next_page the next_page to set
	 */
	public void setNext_page(String next_page) {
		this.next_page = next_page;
	}

	/**
	 * @return the previous_page
	 */
	public String getPrevious_page() {
		return previous_page;
	}

	/**
	 * @param previous_page the previous_page to set
	 */
	public void setPrevious_page(String previous_page) {
		this.previous_page = previous_page;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return Jsons.toJson(this, true);
	}
}
