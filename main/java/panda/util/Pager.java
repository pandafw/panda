package panda.util;

import java.io.Serializable;

/**
 * Pager bean object
 * @author yf.frank.wang@gmail.com
 */
@SuppressWarnings("serial")
public class Pager implements Cloneable, Serializable {

	/**
	 * constructor
	 */
	public Pager() {
	}

	private Integer start = 0;
	private Integer limit;
	private Integer count;
	private Integer total;

	/**
	 * correct the start property via total & limit
	 */
	public void normalize() {
		if (total != null) {
			if (start != null && start >= total) {
				if (limit == null) {
					start = 0;
				}
				else {
					start = total - limit;
				}
			}
			if (start == null || start < 0) {
				start = 0;
			}
		}
	}
	
	/**
	 * @return the start
	 */
	public Integer getStart() {
		return start;
	}
	
	/**
	 * @param start the start to set
	 */
	public void setStart(Integer start) {
		if (start == null || start >= 0) {
			this.start = start;
		}
	}
	
	/**
	 * @return the count
	 */
	public Integer getCount() {
		return count;
	}

	/**
	 * @param count the count to set
	 */
	public void setCount(Integer count) {
		if (count == null || count >= 0) {
			this.count = count;
		}
	}

	/**
	 * @return the limit
	 */
	public Integer getLimit() {
		return limit;
	}
	
	/**
	 * @param limit the limit to set
	 */
	public void setLimit(Integer limit) {
		if (limit == null || limit >= 1) {
			this.limit = limit;
		}
	}
	
	/**
	 * @return the total
	 */
	public Integer getTotal() {
		return total;
	}
	
	/**
	 * @param total the total to set
	 */
	public void setTotal(Integer total) {
		if (total == null || total >= 0) {
			this.total = total;
		}
	}

	/**
	 * @return page
	 */
	public Integer getPage() {
		int page = start / limit;
		if (start % limit == 0) {
			page++;
		}
		return page;
	}
	
	//------------------------------------------------
	// short name
	//------------------------------------------------
	/**
	 * @return the page
	 */
	public Integer getP() {
		return getPage();
	}
	
	/**
	 * @return the start
	 */
	public Integer getS() {
		return getStart();
	}
	
	/**
	 * @param start the start to set
	 */
	public void setS(Integer start) {
		setStart(start);
	}
	
	/**
	 * @return the count
	 */
	public Integer getC() {
		return count;
	}

	/**
	 * @param count the count to set
	 */
	public void setC(Integer count) {
		setCount(count);
	}
	
	/**
	 * @return the limit
	 */
	public Integer getL() {
		return getLimit();
	}
	
	/**
	 * @param limit the limit to set
	 */
	public void setL(Integer limit) {
		setLimit(limit);
	}
	
	/**
	 * @return the total
	 */
	public Integer getT() {
		return getTotal();
	}
	
	/**
	 * @param total the total to set
	 */
	public void setT(Integer total) {
		setTotal(total);
	}
	
	/**
     * @return  a string representation of the object.
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();

		sb.append("{ ");
		sb.append("start: ").append(start);
		sb.append(", ");
		sb.append("limit: ").append(limit);
		sb.append(", ");
		sb.append("count: ").append(count);
		sb.append(", ");
		sb.append("total: ").append(total);
		sb.append(" }");

		return sb.toString();
	}

	/**
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((start == null) ? 0 : start.hashCode());
		result = prime * result + ((limit == null) ? 0 : limit.hashCode());
		result = prime * result + ((count == null) ? 0 : count.hashCode());
		result = prime * result + ((total == null) ? 0 : total.hashCode());
		return result;
	}

	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Pager other = (Pager) obj;
		if (start == null) {
			if (other.start != null)
				return false;
		}
		else if (!start.equals(other.start))
			return false;
		if (limit == null) {
			if (other.limit != null)
				return false;
		}
		else if (!limit.equals(other.limit))
			return false;
		if (count == null) {
			if (other.count != null)
				return false;
		}
		else if (!count.equals(other.count))
			return false;
		if (total == null) {
			if (other.total != null)
				return false;
		}
		else if (!total.equals(other.total))
			return false;
		return true;
	}

	/**
	 * Clone
	 * @return Clone Object
	 */
	public Object clone() {
		Pager clone = new Pager();

		clone.start = this.start;
		clone.limit = this.limit;
		clone.count = this.count;
		clone.total = this.total;
		
		return clone;
	}

}
