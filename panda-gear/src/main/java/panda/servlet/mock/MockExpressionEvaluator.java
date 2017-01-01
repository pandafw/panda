package panda.servlet.mock;

import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.el.ELException;
import javax.servlet.jsp.el.Expression;
import javax.servlet.jsp.el.ExpressionEvaluator;
import javax.servlet.jsp.el.FunctionMapper;
import javax.servlet.jsp.el.VariableResolver;

/**
 * Mock implementation of the JSP 2.0
 * {@link javax.servlet.jsp.el.ExpressionEvaluator} interface, delegating to the
 * Jakarta JSTL ExpressionEvaluatorManager.
 * <p>
 * Used for testing the web framework; only necessary for testing applications
 * when testing custom JSP tags.
 * <p>
 * Note that the Jakarta JSTL implementation (jstl.jar, standard.jar) has to be
 * available on the class path to use this expression evaluator.
 */
@SuppressWarnings("deprecation")
public class MockExpressionEvaluator extends ExpressionEvaluator {

	private final PageContext pageContext;


	/**
	 * Create a new MockExpressionEvaluator for the given PageContext.
	 * 
	 * @param pageContext the JSP PageContext to run in
	 */
	public MockExpressionEvaluator(PageContext pageContext) {
		this.pageContext = pageContext;
	}

	@SuppressWarnings("rawtypes")
	public Expression parseExpression(final String expression, final Class expectedType,
			final FunctionMapper functionMapper) throws ELException {

		return new Expression() {

			public Object evaluate(VariableResolver variableResolver) throws ELException {
				return doEvaluate(expression, expectedType, functionMapper);
			}
		};
	}

	@SuppressWarnings("rawtypes")
	public Object evaluate(String expression, Class expectedType, VariableResolver variableResolver,
			FunctionMapper functionMapper) throws ELException {

		if (variableResolver != null) {
			throw new IllegalArgumentException("Custom VariableResolver not supported");
		}
		return doEvaluate(expression, expectedType, functionMapper);
	}

	@SuppressWarnings("rawtypes")
	protected Object doEvaluate(String expression, Class expectedType, FunctionMapper functionMapper)
			throws ELException {

		if (functionMapper != null) {
			throw new IllegalArgumentException("Custom FunctionMapper not supported");
		}

		return this.pageContext.getAttribute(expression);
//		try {
//			return ExpressionEvaluatorManager.evaluate("JSP EL expression", expression, expectedType, this.pageContext);
//		}
//		catch (JspException ex) {
//			throw new ELException("Parsing of JSP EL expression \"" + expression + "\" failed", ex);
//		}
	}

}
