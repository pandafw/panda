package panda.mvc.processor;

import panda.mvc.ActionContext;
import panda.mvc.ActionInfo;
import panda.mvc.MvcConfig;

public class EncodingProcessor extends AbstractProcessor {

	private String input;
	private String output;

	@Override
	public void init(MvcConfig config, ActionInfo ai) throws Throwable {
		input = ai.getInputEncoding();
		output = ai.getOutputEncoding();
	}

	public void process(ActionContext ac) throws Throwable {
		ac.getRequest().setCharacterEncoding(input);
		ac.getResponse().setCharacterEncoding(output);
		doNext(ac);
	}

}
