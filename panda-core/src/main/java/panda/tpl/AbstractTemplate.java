package panda.tpl;


public abstract class AbstractTemplate implements Template {

	public String evaluate() throws TemplateException {
		return evaluate((Object)null);
	}
	
	public String evaluate(Object context) throws TemplateException {
		StringBuilder sb = new StringBuilder();
		evaluate(sb, context);
		return sb.toString();
	}

	public void evaluate(Appendable out) throws TemplateException {
		evaluate(out, null);
	}
}
