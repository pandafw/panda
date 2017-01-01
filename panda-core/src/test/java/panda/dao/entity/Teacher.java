package panda.dao.entity;

import java.util.ArrayList;
import java.util.List;

import panda.dao.entity.annotation.Comment;
import panda.dao.entity.annotation.PK;
import panda.lang.Objects;
import panda.lang.Strings;


@Comment("先生")
public class Teacher {
	@PK
	String name;

	@Comment("TEACHER_MEMO")
	String memo;

	String all;
	
	String key;
	
	byte[] data;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}
	
	public String getAll() {
		return all;
	}

	public void setAll(String all) {
		this.all = all;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public byte[] getData() {
		return data;
	}

	public void setData(byte[] data) {
		this.data = data;
	}

	@Override
	public String toString() {
		return Objects.toStringBuilder()
				.append("name", name)
				.append("memo", memo)
				.append("all", all)
				.append("key", key)
				.append("data", Strings.newStringUtf8(data))
				.toString();
	}
	
	@Override
	public int hashCode() {
		return Objects.hashCodes(name, memo, all, key, data);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;

		Teacher rhs = (Teacher)obj;
		return Objects.equalsBuilder()
				.append(name, rhs.name)
				.append(memo, rhs.memo)
				.append(all, rhs.all)
				.append(key, rhs.key)
				.append(data, rhs.data)
				.isEquals();
	}

	public Teacher() {
	}

	public Teacher(int i) {
		this.name = "T" + i;
		this.memo = "memo-" + i;
		this.data = ("data-" + i).getBytes();
	}

	public static Teacher create(int i) {
		return new Teacher(i);
	}

	public static List<Teacher> creates(int f, int t) {
		List<Teacher> l = new ArrayList<Teacher>();
		for (int i = f; i <= t; i++) {
			l.add(new Teacher(i));
		}
		return l;
	}
}
