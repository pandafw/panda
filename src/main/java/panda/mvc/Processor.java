package panda.mvc;

public interface Processor {

	void process(ActionContext ac) throws Throwable;

	void setNext(Processor proc);

	Processor getNext();
}
