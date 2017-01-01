package panda.ioc.loader.xml.meta;

import java.sql.Timestamp;

public class Pet {
	private int id;

	private String name;

	private String nickName;

	private int age;

	private int masterId;

	private Timestamp birthday;

	public int getId() {
		return id;
	}

	public Pet setId(int id) {
		this.id = id;
		return this;
	}

	public int getMasterId() {
		return masterId;
	}

	public void setMasterId(int masterId) {
		this.masterId = masterId;
	}

	public String getName() {
		return name;
	}

	public Pet setName(String name) {
		this.name = name;
		return this;
	}

	public String getNickName() {
		return nickName;
	}

	public Pet setNickName(String nickName) {
		this.nickName = nickName;
		return this;
	}

	public int getAge() {
		return age;
	}

	public Pet setAge(int age) {
		this.age = age;
		return this;
	}

	public Timestamp getBirthday() {
		return birthday;
	}

	public void setBirthday(Timestamp birthday) {
		this.birthday = birthday;
	}

	public String toString() {
		return name;
	}
}
