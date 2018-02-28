package panda.mvc.validator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import panda.ioc.annotation.IocBean;
import panda.lang.Strings;

@IocBean(singleton=false)
public class RegexValidator extends AbstractStringValidator {

	private String regex;
	private boolean caseSensitive = true;

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
	 * @param caseSensitive the caseSensitive to set
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

	protected Pattern getPattern() {
		if (Strings.isEmpty(regex)) {
			throw new IllegalArgumentException("Empty regex expression of field '" + getName() + "': " + regex);
		}
		
		// match against expression
		Pattern pattern;
		try {
			if (isCaseSensitive()) {
				pattern = Pattern.compile(regex);
			}
			else {
				pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
			}
		}
		catch (PatternSyntaxException e) {
			throw new IllegalArgumentException("Invalid regex expression of field '" + getName() + "': " + regex, e);
		}
		
		return pattern;
	}
	
	@Override
	protected boolean validateString(String value) {
		Pattern pattern = getPattern();
		Matcher matcher = pattern.matcher(value);
		return matcher.matches();
	}
}
