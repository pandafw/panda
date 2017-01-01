package panda.tpl;


public interface Template {
	public String evaluate() throws TemplateException ;
	
	public String evaluate(Object context) throws TemplateException;

	public void evaluate(Appendable out) throws TemplateException;

	public void evaluate(Appendable out, Object context) throws TemplateException;
}
