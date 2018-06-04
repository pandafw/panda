package panda.ioc.sample.json;

import java.util.Date;

public class Pet {
	private String name;

	private Date birthday;

	private Pet friend;

	public Pet() {
	}

	public Pet(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getBirthday() {
		return birthday;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

	public Pet getFriend() {
		return friend;
	}

	public void setFriend(Pet friend) {
		this.friend = friend;
	}

	@Override
	public String toString() {
		return "Pet [name=" + name + ", birthday=" + birthday + ", friend=" + friend + "]";
	}
}
