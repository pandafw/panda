package panda.dao.entity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import panda.dao.entity.annotation.FK;
import panda.dao.entity.annotation.Index;
import panda.dao.entity.annotation.PK;
import panda.lang.Objects;

public class Klass {
	@PK
	String name;

	@Index(unique=true)
	String alias;
	
	@FK(target=Teacher.class)
	String teacher;

	BigDecimal price;
	
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

	public String getTeacher() {
		return teacher;
	}

	public void setTeacher(String teacher) {
		this.teacher = teacher;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}


	@Override
	public String toString() {
		return Objects.toStringBuilder(this)
				.append("name", name)
				.append("alias", alias)
				.append("teacher", teacher)
				.append("price", price)
				.toString();
	}

	@Override
	public int hashCode() {
		return Objects.hashCodeBuilder()
				.append(name)
				.append(alias)
				.append(teacher)
				.append(price)
				.toHashCode();
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
				.append(teacher, rhs.teacher)
				.append(price, rhs.price)
				.isEquals();
	}

	public Klass() {
	}

	public Klass(int i) {
		this.name = "K" + i;
		this.alias = "A" + i;
		this.teacher = "T" + i;
		this.price = new BigDecimal(i * 100101 / 100).setScale(2);
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
