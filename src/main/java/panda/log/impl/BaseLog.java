package panda.log.impl;




public abstract class BaseLog extends AbstractLog {
	protected BaseLogAdapter adapter;
	protected String name;

	protected BaseLog(BaseLogAdapter adapter, String name) {
		super(name);
		this.adapter = adapter;
		this.name = name;
	}
}
