package panda.mvc.validator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import panda.lang.Strings;

import com.opensymphony.xwork2.validator.ValidationException;

/**
 * <!-- START SNIPPET: javadoc -->
 * Validates a string field using a regular expression.
 * <!-- END SNIPPET: javadoc -->
 * <p/>
 * 
 * 
 * <!-- START SNIPPET: parameters -->
 * <ul>
 * 	  <li>fieldName - The field name this validator is validating. Required if using Plain-Validator Syntax otherwise not required</li>
 *    <li>expression - The RegExp expression  REQUIRED</li>
 *    <li>caseSensitive - Boolean (Optional). Sets whether the expression should be matched against in a case-sensitive way. Default is <code>true</code>.</li>
 *    <li>trim - Boolean (Optional). Sets whether the expression should be trimed before matching. Default is <code>true</code>.</li>
 * </ul>
 * <!-- END SNIPPET: parameters -->
 * 
 * 
 * <pre>
 * <!-- START SNIPPET: example -->
 *    &lt;validators&gt;
 *        &lt;!-- Plain Validator Syntax --&gt;
 *        &lt;validator type="regex"&gt;
 *            &lt;param name="fieldName"&gt;myStrangePostcode&lt;/param&gt;
 *            &lt;param name="expression"&gt;&lt;![CDATA[([aAbBcCdD][123][eEfFgG][456])]]&lt;&gt;/param&gt;
 *        &lt;/validator&gt;
 *    
 *        &lt;!-- Field Validator Syntax --&gt;
 *        &lt;field name="myStrangePostcode"&gt;
 *            &lt;field-validator type="regex"&gt;
 *               &lt;param name="expression"&gt;&lt;![CDATA[([aAbBcCdD][123][eEfFgG][456])]]&gt;&lt;/param&gt;
 *            &lt;/field-validator&gt;
 *        &lt;/field&gt;
 *    &lt;/validators&gt;
 * <!-- END SNIPPET: example -->
 * </pre>
 */
public class RegexFieldValidator extends AbstractStringFieldValidator {

	private String regex;
	private String regexKey;
	private String regexVar;
	private boolean caseSensitive = true;

	@Override
	protected void validateString(Object object, String value) throws ValidationException {
		if (Strings.isEmpty(regex)) {
			if (Strings.isNotEmpty(regexKey)) {
				regex = getText(object, regexKey);
			}
			else if (Strings.isNotEmpty(regexVar)) {
				regex = (String)parse(regexVar, String.class);
			}
		}
		
		if (Strings.isEmpty(regex)) {
			return;
		}
		
		// match against expression
		Pattern pattern;
		if (isCaseSensitive()) {
			pattern = Pattern.compile(regex);
		}
		else {
			pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
		}

		Matcher matcher = pattern.matcher(value);

		if (!matcher.matches()) {
			addFieldError(ac, object, getName());
		}
	}

	/**
	 * @return Returns whether the expression should be matched against in a
	 *         case-sensitive way. Default is <code>true</code>.
	 */
	public boolean isCaseSensitive() {
		return caseSensitive;
	}

	/**
	 * Sets whether the expression should be matched against in a case-sensitive
	 * way. Default is <code>true</code>.
	 */
	public void setCaseSensitive(boolean caseSensitive) {
		this.caseSensitive = caseSensitive;
	}

	/**
	 * @return the regex
	 */
	public String getRegex() {
		return regex;
	}

	/**
	 * @param regex the regex to set
	 */
	public void setRegex(String regex) {
		this.regex = regex;
	}

	/**
	 * @return the regexKey
	 */
	public String getRegexKey() {
		return regexKey;
	}

	/**
	 * @param regexKey the regexKey to set
	 */
	public void setRegexKey(String regexKey) {
		this.regexKey = regexKey;
	}

	/**
	 * @return the regexVar
	 */
	public String getRegexVar() {
		return regexVar;
	}

	/**
	 * @param regexVar the regexVar to set
	 */
	public void setRegexVar(String regexVar) {
		this.regexVar = regexVar;
	}
	
	/**
	 * Backwards compatible
	 * @return Returns the regular expression to be matched.
	 */
	public String getExpression() {
		return regex;
	}

	/**
	 * Backwards compatible
	 * Sets the regular expression to be matched.
	 */
	public void setExpression(String expression) {
		this.regex = expression;
	}

	/**
	 * Backwards compatible
	 * @return the expressionKey
	 */
	public String getExpressionKey() {
		return regexKey;
	}

	/**
	 * Backwards compatible
	 * @param expressionKey the expressionKey to set
	 */
	public void setExpressionKey(String expressionKey) {
		this.regexKey = expressionKey;
	}
}
