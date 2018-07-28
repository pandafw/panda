package panda.dao.entity;

import java.util.ArrayList;
import java.util.List;

import panda.dao.entity.annotation.Column;
import panda.dao.entity.annotation.Id;
import panda.dao.entity.annotation.Index;
import panda.dao.entity.annotation.Indexes;
import panda.lang.Objects;

@Indexes({@Index(fields={"name"})})
public class Student {
	@Id(start=6)
	@Column("student_id")
	int id;
	
	@Column("STUDENT_NAME")
	String name;
	
	String dummy;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDummy() {
		return dummy;
	}

	public void setDummy(String dummy) {
		this.dummy = dummy;
	}
	

	@Override
	public String toString() {
		return Objects.toStringBuilder()
				.append("id", id)
				.append("name", name)
				.append("dummy", dummy)
				.toString();
	}

	@Override
	public int hashCode() {
		return Objects.hashCodes(id, name, dummy);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;

		Student rhs = (Student)obj;
		return Objects.equalsBuilder()
				.append(id, rhs.id)
				.append(name, rhs.name)
				.append(dummy, rhs.dummy)
				.isEquals();
	}

	public Student() {
	}

	public Student(int i) {
		this.id = i;
		this.name = "S" + i;
	}

	public static Student create(int i) {
		return new Student(i);
	}

	public static List<Student> creates(int f, int t) {
		List<Student> l = new ArrayList<Student>();
		for (int i = f; i <= t; i++) {
			l.add(new Student(i));
		}
		return l;
	}
}
