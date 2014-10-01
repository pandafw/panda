package panda.tpl;


public interface Template {
	public String evaluate();
	
	public String evaluate(Object context);

	public void evaluate(Appendable out) throws TemplateException;

	public void evaluate(Appendable out, Object context) throws TemplateException;
}
