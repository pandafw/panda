package panda.ioc.sample.xml;

import java.util.Arrays;
import java.util.Date;

import panda.ioc.Ioc;
import panda.ioc.IocContext;

public class Pet {
	private String name;
	private String alias;
	private Date birthday;
	private int age;
	private boolean dead;
	private String[] favorites;
	private Pet friend;
	private Ioc ioc;
	private IocContext ctx;
	private String beanName;

	private Object elsm;
	private Object elsp;
	private Object elbo;
	private Object elbp;
	
	public Pet() {
	}

	public Pet(String name) {
		this.name = name;
	}
	public Pet(String name, String alias) {
		this.name = name;
		this.alias = alias;
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
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
	public String[] getFavorites() {
		return favorites;
	}
	public void setFavorites(String[] favorites) {
		this.favorites = favorites;
	}
	public Pet getFriend() {
		return friend;
	}
	public void setFriend(Pet friend) {
		this.friend = friend;
	}
	public boolean isDead() {
		return dead;
	}
	public void setDead(boolean dead) {
		this.dead = dead;
	}
	public Ioc getIoc() {
		return ioc;
	}
	public void setIoc(Ioc ioc) {
		this.ioc = ioc;
	}
	public IocContext getCtx() {
		return ctx;
	}
	public void setCtx(IocContext iocContext) {
		this.ctx = iocContext;
	}
	public String getBeanName() {
		return beanName;
	}
	public void setBeanName(String beanName) {
		this.beanName = beanName;
	}

	public Object getElsm() {
		return elsm;
	}

	public void setElsm(Object elsm) {
		this.elsm = elsm;
	}

	public Object getElsp() {
		return elsp;
	}

	public void setElsp(Object elsp) {
		this.elsp = elsp;
	}

	public Object getElbo() {
		return elbo;
	}

	public void setElbo(Object elbo) {
		this.elbo = elbo;
	}

	public Object getElbp() {
		return elbp;
	}

	public void setElbp(Object elbp) {
		this.elbp = elbp;
	}

	@Override
	public String toString() {
		return "Pet [name=" + name + ", alias=" + alias 
				+ ", birthday=" + birthday + ", age=" + age + ", dead=" + dead + ", favorites="
				+ Arrays.toString(favorites)
				+ ", beanName=" + beanName
				+ ", elsp=" + elsp
				+ ", elsm=" + elsm
				+ ", elbo=" + elbo
				+ ", elbp=" + elbp
				+ ", ioc=" + (ioc == null ? null : ioc.hashCode())
				+ ", ctx=" + (ctx == null ? null : ctx.hashCode())
				+ ", friend=" + friend + "]";
	}
}
