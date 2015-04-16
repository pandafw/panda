package panda.lang.reflect.testbed;

import java.util.List;

public class GenericTypeHolder {
	public GenericParent<String> stringParent;
	public GenericParent<Integer> integerParent;
	public List<Foo> foos;
	public GenericParent<Bar>[] barParents;
}
