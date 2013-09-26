package panda.dao.sql;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.List;

/**
 * TestA
 */
public class TestA implements Cloneable {
	protected int id;
	protected char kind;
	protected String kinds;
	protected String name;
	protected BigDecimal price;
	protected Calendar updateTime;

	protected int[] idArray;
	protected List<Integer> idList;
	protected List<String> nameList;
	
	protected String orderCol;
	protected String orderDir;
	
	protected TestA a;

	/**
	 * @return true if id > 0
	 */
	public boolean isValidId() {
		return id > 0;
	}
	

	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}


	/**
	 * @param id the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}


	/**
	 * @return the kind
	 */
	public char getKind() {
		return kind;
	}


	/**
	 * @param kind the kind to set
	 */
	public void setKind(char kind) {
		this.kind = kind;
	}


	/**
	 * @return the kinds
	 */
	public String getKinds() {
		return kinds;
	}


	/**
	 * @param kinds the kinds to set
	 */
	public void setKinds(String kinds) {
		this.kinds = kinds;
	}


	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}


	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}


	/**
	 * @return the price
	 */
	public BigDecimal getPrice() {
		return price;
	}


	/**
	 * @param price the price to set
	 */
	public void setPrice(BigDecimal price) {
		this.price = price;
	}


	/**
	 * @return the updateTime
	 */
	public Calendar getUpdateTime() {
		return updateTime;
	}


	/**
	 * @param updateTime the updateTime to set
	 */
	public void setUpdateTime(Calendar updateTime) {
		this.updateTime = updateTime;
	}


	/**
	 * @return the idArray
	 */
	public int[] getIdArray() {
		return idArray;
	}


	/**
	 * @param idArray the idArray to set
	 */
	public void setIdArray(int[] idArray) {
		this.idArray = idArray;
	}


	/**
	 * @return the idList
	 */
	public List<Integer> getIdList() {
		return idList;
	}


	/**
	 * @param idList the idList to set
	 */
	public void setIdList(List<Integer> idList) {
		this.idList = idList;
	}


	/**
	 * @return the nameList
	 */
	public List<String> getNameList() {
		return nameList;
	}


	/**
	 * @param nameList the nameList to set
	 */
	public void setNameList(List<String> nameList) {
		this.nameList = nameList;
	}


	/**
	 * @return the orderCol
	 */
	public String getOrderCol() {
		return orderCol;
	}


	/**
	 * @param orderCol the orderCol to set
	 */
	public void setOrderCol(String orderCol) {
		this.orderCol = orderCol;
	}


	/**
	 * @return the orderDir
	 */
	public String getOrderDir() {
		return orderDir;
	}


	/**
	 * @param orderDir the orderDir to set
	 */
	public void setOrderDir(String orderDir) {
		this.orderDir = orderDir;
	}


	/**
	 * @return the a
	 */
	public TestA getA() {
		return a;
	}


	/**
	 * @param a the a to set
	 */
	public void setA(TestA a) {
		this.a = a;
	}


	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();

		sb.append("{ ");
		sb.append("id: ").append(id);
		sb.append(", ");
		sb.append("kind: ").append(kind);
		sb.append(", ");
		sb.append("name: ").append(name);
		sb.append(", ");
		sb.append("price: ").append(price);
		sb.append(", ");
		sb.append("updateTime: ").append(updateTime);
		sb.append(", ");
		sb.append("a: ").append(a);
		sb.append(" }");
		
		return sb.toString();
	}


	/**
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int PRIME = 31;
		int result = 1;
		result = PRIME * result + id;
		result = PRIME * result + kind;
		result = PRIME * result + ((name == null) ? 0 : name.hashCode());
		result = PRIME * result + ((price == null) ? 0 : price.hashCode());
		result = PRIME * result + ((updateTime == null) ? 0 : updateTime.hashCode());
		result = PRIME * result + ((a == null) ? 0 : a.hashCode());
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
		final TestA other = (TestA) obj;
		if (a == null) {
			if (other.a != null)
				return false;
		} else if (!a.equals(other.a))
			return false;
		if (id != other.id)
			return false;
		if (kind != other.kind)
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (price == null) {
			if (other.price != null)
				return false;
		} else if (!price.equals(other.price))
			return false;
		if (updateTime == null) {
			if (other.updateTime != null)
				return false;
		} else if (!updateTime.equals(other.updateTime))
			return false;
		return true;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#clone()
	 */
	public TestA clone() {
		TestA a = new TestA();
		
		a.id = this.id;
		a.kind = this.kind;
		a.kinds = this.kinds;
		a.name = this.name;
		a.price = this.price;
		a.updateTime = this.updateTime;

		a.idArray = this.idArray;
		a.idList = this.idList;
		a.nameList = this.nameList;
		
		a.orderCol = this.orderCol;
		a.orderDir = this.orderDir;
		
		a.a = this.a;
		return a;
	}

	
}
