package panda.mvc.validator;

import java.util.Map;

import panda.lang.Strings;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.conversion.impl.XWorkConverter;
import com.opensymphony.xwork2.validator.ValidationException;
import com.opensymphony.xwork2.validator.validators.RepopulateConversionErrorFieldValidatorSupport;

/**
 * <!-- START SNIPPET: javadoc --> Field Validator that checks if a conversion error occured for
 * this field. <!-- END SNIPPET: javadoc -->
 * <p/>
 * <!-- START SNIPPET: parameters -->
 * <ul>
 * <li>fieldName - The field name this validator is validating. Required if using Plain-Validator
 * Syntax otherwise not required</li>
 * </ul>
 * <!-- END SNIPPET: parameters --> <!-- START SNIPPET: example -->
 * 
 * <pre>
 *     &lt;!-- Plain Validator Syntax --&gt;
 *     &lt;validator type="conversion"&gt;
 *     		&lt;param name="fieldName"&gt;myField&lt;/param&gt;
 *          &lt;message&gt;Conversion Error Occurred&lt;/message&gt;
 *     &lt;/validator&gt;
 *      
 *     &lt;!-- Field Validator Syntax --&gt;
 *     &lt;field name="myField"&gt;
 *        &lt;field-validator type="conversion"&gt;
 *           &lt;message&gt;Conversion Error Occurred&lt;/message&gt;
 *        &lt;/field-validator&gt;
 *     &lt;/field&gt;
 * </pre>
 * 
 * <!-- END SNIPPET: example -->
 */
public class ConversionErrorFieldValidator extends RepopulateConversionErrorFieldValidatorSupport {

	private Boolean clearErrors = true;

	/**
	 * @return the clearErrors
	 */
	public String getClearErrors() {
		return clearErrors.toString();
	}

	/**
	 * @param clearErrors the clearErrors to set
	 */
	public void setClearErrors(String clearErrors) {
		this.clearErrors = Boolean.parseBoolean(clearErrors);
	}

	/**
	 * The validation implementation must guarantee that setValidatorContext will be called with a
	 * non-null ValidatorContext before validate is called.
	 * 
	 * @param object validate object
	 * @throws ValidationException if an validation error occurs
	 */
	@Override
	public void doValidate(Object object) throws ValidationException {
		String fieldName = getFieldName();
		String fullFieldName = getValidatorContext().getFullFieldName(fieldName);
		ActionContext context = ActionContext.getContext();
		Map<String, Object> conversionErrors = context.getConversionErrors();

		if (conversionErrors.containsKey(fullFieldName)) {
			if (Strings.isBlank(defaultMessage)) {
				defaultMessage = XWorkConverter.getConversionErrorMessage(fullFieldName, context.getValueStack());
			}

			if (clearErrors) {
				getValidatorContext().getFieldErrors().remove(fullFieldName);
			}
			addFieldError(fieldName, object);
		}
	}

}
