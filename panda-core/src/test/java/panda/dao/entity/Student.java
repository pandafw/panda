package panda.dao.entity;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import panda.dao.entity.annotation.Column;
import panda.dao.entity.annotation.Id;
import panda.dao.entity.annotation.Index;
import panda.dao.entity.annotation.Indexes;
import panda.lang.Exceptions;
import panda.lang.Objects;
import panda.lang.time.DateTimes;

@Indexes({@Index(fields={"name"})})
public class Student {
	@Id(start=6)
	@Column("student_id")
	int id;
	
	@Column("STUDENT_NAME")
	String name;

	@Column
	Date birthday;
	
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

	/**
	 * @return the birthday
	 */
	public Date getBirthday() {
		return birthday;
	}

	/**
	 * @param birthday the birthday to set
	 */
	public void setBirthday(Date birthday) {
		this.birthday = birthday;
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
				.append("birthday", birthday)
				.append("dummy", dummy)
				.toString();
	}

	@Override
	public int hashCode() {
		return Objects.hashCodes(id, name, birthday, dummy);
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
				.append(birthday, rhs.birthday)
				.append(dummy, rhs.dummy)
				.isEquals();
	}

	public Student() {
	}

	public Student(int i) {
		this.id = i;
		this.name = "S" + i;
		try {
			this.birthday = DateTimes.isoDateFormat().parse(String.format("%d-01-01", 2000 + i));
		}
		catch (ParseException e) {
			throw Exceptions.wrapThrow(e);
		}
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
