package panda.mvc;

public interface Processor {

	void init(MvcConfig config, ActionInfo ai) throws Throwable;

	void process(ActionContext ac) throws Throwable;

	void setNext(Processor p);

	Processor getNext();
}
