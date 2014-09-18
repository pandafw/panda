package panda.dao.entity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import panda.dao.entity.annotation.Column;
import panda.dao.entity.annotation.FK;
import panda.dao.entity.annotation.Index;
import panda.dao.entity.annotation.PK;
import panda.lang.Objects;

public class Klass {
	@PK
	String name;

	@Index(unique=true)
	@Column
	String alias;
	
	@FK(target=Teacher.class)
	@Column
	String teacherName;

	@Column(size=10, scale=2)
	BigDecimal price;
	
	@Column
	Boolean closed;
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	public String getTeacherName() {
		return teacherName;
	}

	public void setTeacherName(String teacher) {
		this.teacherName = teacher;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public Boolean getClosed() {
		return closed;
	}

	public void setClosed(Boolean closed) {
		this.closed = closed;
	}

	@Override
	public String toString() {
		return Objects.toStringBuilder()
				.append("name", name)
				.append("alias", alias)
				.append("teacherName", teacherName)
				.append("price", price)
				.append("closed", closed)
				.toString();
	}

	@Override
	public int hashCode() {
		return Objects.hashCodes(name, alias, teacherName, price, closed);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;

		Klass rhs = (Klass)obj;
		return Objects.equalsBuilder()
				.append(name, rhs.name)
				.append(alias, rhs.alias)
				.append(teacherName, rhs.teacherName)
				.append(price.intValue(), rhs.price.intValue())
				.append(closed, rhs.closed)
				.isEquals();
	}

	public Klass() {
	}

	public Klass(int i) {
		this.name = "K" + i;
		this.alias = "A" + i;
		this.teacherName = "T" + i;
		this.price = new BigDecimal(i * 100101 / 100).setScale(2);
		this.closed = (i % 2 == 0);
	}

	public static Klass create(int i) {
		return new Klass(i);
	}

	public static List<Klass> creates(int f, int t) {
		List<Klass> l = new ArrayList<Klass>();
		for (int i = f; i <= t; i++) {
			l.add(new Klass(i));
		}
		return l;
	}
}
