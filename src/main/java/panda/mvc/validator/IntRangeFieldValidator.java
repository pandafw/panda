package panda.mvc.validator;

/**
 * <!-- START SNIPPET: javadoc -->
 * Field Validator that checks if the integer specified is within a certain range.
 * <!-- END SNIPPET: javadoc -->
 *
 *
 * <!-- START SNIPPET: parameters -->
 * <ul>
 * 		<li>fieldName - The field name this validator is validating. Required if using Plain-Validator Syntax otherwise not required</li>
 * 		<li>min - the minimum value (if none is specified, it will not be checked) </li>
 * 		<li>max - the maximum value (if none is specified, it will not be checked) </li>
 * </ul>
 * <!-- END SNIPPET: parameters -->
 *
 *
 * <pre>
 * <!-- START SNIPPET: examples -->
 * 		&lt;validators>
 *           &lt;!-- Plain Validator Syntax --&gt;
 *           &lt;validator type="int">
 *               &lt;param name="fieldName"&gt;age&lt;/param&gt;
 *               &lt;param name="min"&gt;20&lt;/param&gt;
 *               &lt;param name="max"&gt;50&lt;/param&gt;
 *               &lt;message&gt;Age needs to be between ${min} and ${max}&lt;/message&gt;
 *           &lt;/validator&gt;
 *
 *           &lt;!-- Field Validator Syntax --&gt;
 *           &lt;field name="age"&gt;
 *               &lt;field-validator type="int"&gt;
 *                   &lt;param name="min"&gt;20&lt;/param&gt;
 *                   &lt;param name="max"&gt;50&lt;/param&gt;
 *                   &lt;message&gt;Age needs to be between ${min} and ${max}&lt;/message&gt;
 *               &lt;/field-validator&gt;
 *           &lt;/field&gt;
 *      &lt;/validators&gt;
 * <!-- END SNIPPET: examples -->
 * </pre>
 *
 */
public class IntRangeFieldValidator extends AbstractRangeValidator {

	private Integer max = null;
	private Integer min = null;

	/**
	 * @return the max
	 */
	public Integer getMax() {
		return max;
	}

	/**
	 * @param max the max to set
	 */
	public void setMax(Integer max) {
		this.max = max;
	}

	/**
	 * @return the min
	 */
	public Integer getMin() {
		return min;
	}

	/**
	 * @param min the min to set
	 */
	public void setMin(Integer min) {
		this.min = min;
	}

	protected Comparable getMaxComparatorValue() {
		return max;
	}

	protected Comparable getMinComparatorValue() {
		return min;
	}
}
