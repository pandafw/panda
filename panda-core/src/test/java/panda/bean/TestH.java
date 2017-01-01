package panda.bean;

import java.util.List;

public class TestH extends TestG<TestA> {
	/**
	 * Override is required for Generic Type
	 */
	@Override
	public TestA getObj() {
		return super.getObj();
	}

	/**
	 * Override is required for Generic Type
	 */
	@Override
	public List<TestA> getLst() {
		return super.getLst();
	}
	
}
