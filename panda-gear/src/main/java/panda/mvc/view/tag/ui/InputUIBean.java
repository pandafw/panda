package panda.mvc.view.tag.ui;

import panda.lang.Strings;


public abstract class InputUIBean extends UIBean {
	// parent form
	protected Form form;
	
	// shortcut, sets label, name, and value
	protected String key;

	protected String label;
	protected String labelPosition;
	protected String labelSeparator;

	protected String labelClass;
	protected String inputClass;
	
	protected boolean required;
	protected boolean readonly;
	protected String requiredString;
	protected String requiredPosition;
	protected String errorPosition;
	
	protected String description;

	protected Object value;

	protected void evaluateLabelParam() {
		if (label == null) {
			// lookup the label from a TextProvider (default value is the key)
			label = context.getText().getText("p." + key, null);
			if (label == null) {
				label = context.getText().getText(key, key);
			}
		}
	}
	
	protected void evaluateDescriptionParam() {
		if (description == null) {
			// lookup the placeholder from a TextProvider (default value is the key)
			description = context.getText().getText("p." + key + "-dip", null);
			if (description == null) {
				description = context.getText().getText(key + "-dip", null);
			}
		}
	}
	
	protected void evaluateTooltipParam() {
		if (tooltip == null) {
			// lookup the tooltip from a TextProvider (default value is the key)
			tooltip = context.getText().getText("p." + key + "-tip", null);
			if (tooltip == null) {
				tooltip = context.getText().getText(key + "-tip", null);
			}
		}
	}

	@Override
	protected void evaluateParams() {
		super.evaluateParams();

		if (key != null) {
			if (name == null) {
				name = key;
			}

			evaluateLabelParam();
			evaluateDescriptionParam();
			evaluateTooltipParam();
		}

		// see if the value was specified as a parameter already
		evaluateNameValue();

		Form form = findForm();
		if (form != null) {
			if (id == null) {
				// create HTML id element
				populateComponentHtmlId(form);
			}

			if (labelClass == null) {
				labelClass = form.getLabelClass();
			}
		
			if (labelSeparator == null) {
				labelSeparator = form.getLabelSeparator();
			}
			
			if (labelPosition == null) {
				labelPosition = form.getLabelPosition();
			}
			
			if (inputClass == null) {
				inputClass = form.getInputClass();
			}
		}
	}

	public Form findForm() {
		if (form == null) {
			form = (Form)findAncestor(Form.class);
		}
		return form;
	}

	protected void evaluateNameValue() {
		if (value != null || Strings.isEmpty(name)) {
			return;
		}

		value = context.getParameter(name);
		if (value == null) {
			value = context.getCastErrors().get(name);
		}
	}

	/**
	 * Create HTML id element for the component and populate this component parmaeter map.
	 * Additionally, a parameter named escapedId is populated which contains the found id value
	 * filtered by {@link #escape(String)}, needed eg. for naming Javascript identifiers based on
	 * the id value. The order is as follows :-
	 * <ol>
	 * <li>This component id attribute</li>
	 * <li>[containing_form_id]_[this_component_name]</li>
	 * <li>[this_component_name]</li>
	 * </ol>
	 * 
	 * @param form
	 */
	protected void populateComponentHtmlId(Form form) {
		if (form == null) {
			return;
		}
		
		if (id == null) {
			id = Strings.defaultString(form.getId()) 
					+ '_' + (name == null ? form.getSequence() : escape(name));
		}
	}

	/**
	 * @return the key
	 */
	public String getKey() {
		return key;
	}

	/**
	 * @return the label
	 */
	public String getLabel() {
		return label;
	}

	/**
	 * @return the labelSeparator
	 */
	public String getLabelSeparator() {
		return labelSeparator;
	}

	/**
	 * @return the labelPosition
	 */
	public String getLabelPosition() {
		return labelPosition;
	}

	/**
	 * @param labelPosition the labelPosition to set
	 */
	public void setLabelPosition(String labelPosition) {
		this.labelPosition = labelPosition;
	}

	/**
	 * @return the requiredPosition
	 */
	public String getRequiredPosition() {
		return requiredPosition;
	}

	/**
	 * @return the errorPosition
	 */
	public String getErrorPosition() {
		return errorPosition;
	}

	/**
	 * @return the requiredString
	 */
	public String getRequiredString() {
		return requiredString;
	}

	/**
	 * @return the value
	 */
	public Object getValue() {
		return value;
	}

	/** 
	 * Label expression used for rendering an element specific label 
	 * @param label the label to set
	 */
	public void setLabel(String label) {
		this.label = label;
	}

	/** 
	 * String that will be appended to the label 
	 * @param labelseparator the labelseparator to set
	 */
	public void setLabelSeparator(String labelseparator) {
		this.labelSeparator = labelseparator;
	}

	/** 
	 * Define required position of required form element (left|right) 
	 * @param requiredPosition the requiredPosition to set
	 */
	public void setRequiredPosition(String requiredPosition) {
		this.requiredPosition = requiredPosition;
	}

	/**
	 * Define error position of form element (top|bottom) 
	 * @param errorPosition the errorPosition to set
	 */
	public void setErrorPosition(String errorPosition) {
		this.errorPosition = errorPosition;
	}
	
	/**
	 * @param requiredString the requiredstring to set
	 */
	public void setRequiredString(String requiredString) {
		this.requiredString = requiredString;
	}

	/**
	 * Set the key (name, value, label) for this particular component 
	 * @param key the key to set
	 */
	public void setKey(String key) {
		this.key = key;
	}

	/**
	 * @return the required
	 */
	public boolean isRequired() {
		return required;
	}

	/**
	 * @param required the required to set
	 */
	public void setRequired(boolean required) {
		this.required = required;
	}

	/**
	 * @param value the value to set
	 */
	public void setValue(Object value) {
		this.value = value;
	}

	/**
	 * @return the readonly
	 */
	public boolean isReadonly() {
		return readonly;
	}

	/**
	 * @param readonly the readonly to set
	 */
	public void setReadonly(boolean readonly) {
		this.readonly = readonly;
	}

	/**
	 * @return the labelClass
	 */
	public String getLabelClass() {
		return labelClass;
	}

	/**
	 * @param labelClass the labelClass to set
	 */
	public void setLabelClass(String labelClass) {
		this.labelClass = labelClass;
	}

	/**
	 * @return the inputClass
	 */
	public String getInputClass() {
		return inputClass;
	}

	/**
	 * @param inputClass the inputClass to set
	 */
	public void setInputClass(String inputClass) {
		this.inputClass = inputClass;
	}
	
	
	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}
}
