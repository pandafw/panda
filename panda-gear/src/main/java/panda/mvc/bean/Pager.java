package panda.mvc.bean;

import java.io.Serializable;

import panda.lang.Objects;
import panda.mvc.validation.Validators;
import panda.mvc.validation.annotation.Validate;
import panda.mvc.validation.annotation.Validates;

/**
 * Pager bean object
 */
public class Pager implements Cloneable, Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * constructor
	 */
	public Pager() {
	}

	/** page no */
	private Long page;
	
	/** start offset */
	private Long start;
	
	/** page limit */
	private Long limit;
	
	/** the item count of the page */
	private Long count;
	
	/** total count */
	private Long total;

	//--------------------------------------------------------------
	public void normalize() {
		// total is unknown
		if (total == null) {
			if (page != null) {
				start = (page - 1) * (limit == null ? 0 : limit);
			}
			if (start != null) {
				if (limit == null || limit == 0) {
					page = start > 0 ? 2L : 1L;
				}
				else {
					page = calcPage();
				}
			}
		}
		// total is known
		else {
			if (start != null) {
				// fix start & calculate page
				if (start >= total) {
					if (limit == null || limit < 1) {
						start = 0L;
						page = 1L;
					}
					else {
						start = total - limit;
						if (start < 0) {
							start = 0L;
							page = 1L;
						}
						else {
							page = calcPage();
						}
					}
				}
				else {
					page = calcPage();
				}
			}
			else if (page != null) {
				// fix page & calculate start
				if (limit == null || limit < 1) {
					page = 1L;
					start = 0L;
				}
				else {
					long p = total / limit;
					if (total % limit != 0) {
						p++;
					}
					if (page > p) {
						page = p;
					}
					start = (page - 1) * limit;
				}
			}
			else {
				page = 1L;
				start = 0L;
			}

			// fix count
			if (total > 0) {
				long ic = (limit == null || limit < 1 ? total : limit);
				long is = getStart();
				if (is + ic > total) {
					ic = total - is;
				}
				if (count == null || count <= 0 || count > ic) {
					count = ic;
				}
			}
		}
	}

	/**
	 * normalize() must be called before.
	 * @return the pages according to the start, total, count, limit
	 */
	public long getPages() {
		long page = getPage();
		long pages = 0;
		if (total != null) {
			if (total > 0) {
				pages = (limit != null && limit > 0) ? ((total - 1) / limit) + 1 : 1;
				if (page > pages) {
					pages = page;
				}
			}
		}
		else if (count != null) {
			if (limit == null || limit < 1) {
				pages = page;
			}
			else if (count < limit) {
				pages = page;
			}
			else {
				pages = page + 1;
			}
		}
		return pages;
	}

	private long calcPage() {
		return (long)Math.ceil(start.doubleValue() / limit) + 1;
	}
	
	//--------------------------------------------------------------
	/**
	 * @return the begin page no
	 */
	public Long getBegin() {
		return start == null ? null : start + 1;
	}
	
	/**
	 * @return the end page no
	 */
	public Long getEnd() {
		return start == null || count == null ? null : start + count;
	}
	
	/**
	 * @return page
	 */
	public Long getPage() {
		return page == null ? 1 : page;
	}
	
	/**
	 * @param page the page to set
	 */
	public void setPage(int page) {
		setPage((long)page);
	}
	
	/**
	 * @param page the page to set
	 */
	public void setPage(Long page) {
		if (page == null || page > 0) {
			this.page = page;
		}
	}

	/**
	 * @return the start
	 */
	public Long getStart() {
		return start == null ? 0 : start;
	}

	/**
	 * @param start the start to set
	 */
	public void setStart(int start) {
		setStart((long)start);
	}

	/**
	 * @param start the start to set
	 */
	public void setStart(Long start) {
		if (start == null || start >= 0) {
			this.start = start;
		}
	}
	
	/**
	 * @return the count
	 */
	public Long getCount() {
		return count;
	}

	/**
	 * @param count the count to set
	 */
	public void setCount(int count) {
		if (count >= 0) {
			this.count = (long)count;
		}
	}

	/**
	 * @param count the count to set
	 */
	public void setCount(Long count) {
		if (count == null || count >= 0) {
			this.count = count;
		}
	}

	public boolean hasLimit() {
		return limit != null && limit > 0;
	}

	/**
	 * @return the limit
	 */
	public Long getLimit() {
		return limit == null ? 0 : limit;
	}
	
	/**
	 * @param limit the limit to set
	 */
	public void setLimit(int limit) {
		setLimit((long)limit);
	}
	
	/**
	 * @param limit the limit to set
	 */
	public void setLimit(Long limit) {
		if (limit == null || limit > 0) {
			this.limit = limit;
		}
	}
	
	/**
	 * @return the total
	 */
	public Long getTotal() {
		return total;
	}
	
	/**
	 * @param total the total to set
	 */
	public void setTotal(int total) {
		setTotal((long)total);
	}
	
	/**
	 * @param total the total to set
	 */
	public void setTotal(Long total) {
		if (total == null || total >= 0) {
			this.total = total;
		}
	}

	//------------------------------------------------
	// short name
	//------------------------------------------------
	/**
	 * @return the page
	 */
	public Long getP() {
		return getPage();
	}
	
	/**
	 * @param page the page to set
	 */
	public void setP(Long page) {
		setPage(page);
	}
	
	/**
	 * @return the start
	 */
	@Validates({
		@Validate(value=Validators.CAST, msgId=Validators.MSGID_CAST_NUMBER),
		@Validate(value=Validators.NUMBER, params="{min: 0}", msgId=Validators.MSGID_NUMBER_RANGE)
	})
	public Long getS() {
		return getStart();
	}
	
	/**
	 * @param start the start to set
	 */
	public void setS(Long start) {
		setStart(start);
	}
	
	/**
	 * @return the count
	 */
	public Long getC() {
		return count;
	}

	/**
	 * @param count the count to set
	 */
	public void setC(Long count) {
		setCount(count);
	}
	
	/**
	 * @return the limit
	 */
	@Validates({
		@Validate(value=Validators.CAST, msgId=Validators.MSGID_CAST_NUMBER),
		@Validate(value=Validators.NUMBER, params="{min: 0}", msgId=Validators.MSGID_NUMBER_RANGE)
	})
	public Long getL() {
		return getLimit();
	}
	
	/**
	 * @param limit the limit to set
	 */
	public void setL(Long limit) {
		setLimit(limit);
	}
	
	/**
	 * @return the total
	 */
	@Validates({
		@Validate(value=Validators.CAST, msgId=Validators.MSGID_CAST_NUMBER),
		@Validate(value=Validators.NUMBER, params="{min: 0}", msgId=Validators.MSGID_NUMBER_RANGE)
	})
	public Long getT() {
		return getTotal();
	}
	
	/**
	 * @param total the total to set
	 */
	public void setT(Long total) {
		setTotal(total);
	}
	
	/**
	 * @return a string representation of the object.
	 */
	@Override
	public String toString() {
		return Objects.toStringBuilder()
				.append("start", getStart())
				.append("limit", getLimit())
				.append("count", getCount())
				.append("total", getTotal())
				.append("page", getPage())
				.append("pages", getPages())
				.toString();
	}

	/**
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return Objects.hashCodes(start, limit, count, total);
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

		Pager rhs = (Pager)obj;
		return Objects.equalsBuilder()
				.append(start, rhs.start)
				.append(limit, rhs.limit)
				.append(count, rhs.count)
				.append(total, rhs.total)
				.isEquals();
	}

	/**
	 * Clone
	 * @return Clone Object
	 */
	public Pager clone() {
		Pager clone = new Pager();

		clone.page = this.page;
		clone.start = this.start;
		clone.limit = this.limit;
		clone.count = this.count;
		clone.total = this.total;
		
		return clone;
	}

}
