package panda.wing.action.crud;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import panda.dao.Dao;
import panda.dao.entity.Entity;
import panda.dao.entity.EntityField;
import panda.lang.Arrays;
import panda.lang.Collections;
import panda.lang.Strings;
import panda.lang.Systems;
import panda.mvc.View;
import panda.mvc.view.SitemeshFreemarkerView;
import panda.wing.action.AbstractAction;


/**
 * @param <T> data type
 */
public abstract class GenericBaseAction<T> extends AbstractAction {
	/**
	 * METHOD_SEPARATOR = "_";
	 */
	protected final static String METHOD_SEPARATOR = "_";

	//------------------------------------------------------------
	// scenario & result
	//------------------------------------------------------------
	protected View resultView;
	protected String actionScenario;

	//------------------------------------------------------------
	// entity properties
	//------------------------------------------------------------
	protected Class<T> type;
	protected Entity<T> entity;
	protected Dao dao;
	
	//------------------------------------------------------------
	// display fields (display & update)
	//------------------------------------------------------------
	protected Set<String> displayFields;
	protected Map<String, String> mappingFields;
	
	/**
	 * Constructor 
	 */
	public GenericBaseAction() {
	}

	//------------------------------------------------------------
	// display fields
	//------------------------------------------------------------
	/**
	 * @return the fields
	 */
	protected Set<String> getDisplayFields() {
		return displayFields;
	}

	/**
	 * @param fields the fields to set
	 */
	protected void setDisplayFields(Set<String> fields) {
		displayFields = fields;
	}

	protected void addDisplayFields(String... fields) {
		if (Arrays.isEmpty(fields)) {
			return;
		}
		if (displayFields == null) {
			displayFields = new HashSet<String>();
		}
		displayFields.addAll(Arrays.asList(fields));
	}

	protected void removeDisplayFields(String... fields) {
		if (Arrays.isEmpty(fields)) {
			return;
		}
		if (Collections.isEmpty(displayFields)) {
			return;
		}
		displayFields.removeAll(Arrays.asList(fields));
	}

	/**
	 * used by view
	 * @param field field name
	 * @return true if the field should be display
	 */
	public boolean displayField(String field) {
		Collection<String> fs = getDisplayFields();
		if (Collections.isEmpty(fs)) {
			return true;
		}
		return Collections.contains(fs, field);
	}

	//------------------------------------------------------------
	// mapping fields
	//------------------------------------------------------------
	/**
	 * @return the fields
	 */
	protected Map<String, String> getMappingFields() {
		return mappingFields;
	}

	/**
	 * @param fields the fields to set
	 */
	protected void setMappingFields(Map<String, String> fields) {
		mappingFields = fields;
	}

	protected void addMappingFields(String... fields) {
		if (Arrays.isEmpty(fields)) {
			return;
		}
		if (fields.length % 2 != 0) {
			throw new IllegalArgumentException("The mapping fields is incorrect: " + Strings.join(fields, ','));
		}
		if (mappingFields == null) {
			mappingFields = new HashMap<String, String>();
		}
		
		for (int i = 0; i < fields.length; i = i + 2) {
			mappingFields.put(fields[i], fields[i + 1]);
		}
	}

	protected void removeMappingFields(String... fields) {
		if (Arrays.isEmpty(fields)) {
			return;
		}
		if (Collections.isEmpty(mappingFields)) {
			return;
		}
		
		for (String f : fields) {
			mappingFields.remove(f);
		}
	}

	/**
	 * @param field field name
	 * @return the mapped field
	 */
	protected String mappedField(String field) {
		Map<String, String> fmap = getMappingFields();
		if (Collections.isEmpty(fmap)) {
			return field;
		}
		String mf = fmap.get(field);
		return mf == null ? field : mf;
	}

	/**
	 * @param field field name
	 * @return the mapped field
	 */
	protected EntityField mappedEntityField(EntityField field) {
		Map<String, String> fmap = getMappingFields();
		if (Collections.isEmpty(fmap)) {
			return field;
		}
		String fn = field.getName();
		String mf = fmap.get(fn);
		if (mf == null || mf.equals(fn)) {
			return field;
		}
		
		return getEntity().getField(mf);
	}

	//------------------------------------------------------------
	// entity
	//------------------------------------------------------------
	/**
	 * public for getText(...)
	 * @return the type
	 */
	public Class<T> getT() {
		return type;
	}

	/**
	 * public for getText(...)
	 * @return the type
	 */
	public Class<T> getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	protected void setType(Class<T> type) {
		this.type = type;
	}

	protected Entity<T> getEntity() {
		if (entity == null) {
			entity = getDaoClient().getEntity(type);
		}
		return entity;
	}
	
	protected Dao getDao() {
		if (dao == null) {
			dao = getDaoClient().getDao();
		}
		return dao;
	}
	
	//------------------------------------------------------------
	// scenario
	//------------------------------------------------------------
	/**
	 * @return the actionScenario
	 */
	protected String getActionScenario() {
		if (actionScenario == null) {
			actionScenario = Strings.substringBefore(context.getMethodName(), METHOD_SEPARATOR);
		}
		return actionScenario;
	}

	/**
	 * @param actionScenario the actionScenario to set
	 */
	protected void setActionScenario(String actionScenario) {
		this.actionScenario = actionScenario;
	}

	protected void setResultView(View result) {
		this.resultView = result;
	}

	/**
	 * set result by action scenario
	 */
	protected void setScenarioResult() {
		resultView = new SitemeshFreemarkerView("~" + getActionScenario());
	}

	/**
	 * set result by action scenario
	 * @param step scenario step
	 */
	protected void setScenarioResult(String step) {
		resultView = new SitemeshFreemarkerView("~" + getActionScenario() + "_" + step);
	}

	//------------------------------------------------------------
	// result
	//------------------------------------------------------------
	protected Object doResult(Object r) {
		if (resultView == null) {
			return r;
		}
		getContext().setResult(r);
		return resultView;
	}

	//------------------------------------------------------------
	// message
	//------------------------------------------------------------
	/**
	 * getMessage
	 * @param msg msg id
	 * @return message string
	 */
	protected String getMessage(String msg) {
		return getText(msg, msg);
	}
	
	/**
	 * getMessage
	 * @param msg msg id
	 * @param param parameters
	 * @return message string
	 */
	protected String getMessage(String msg, Object param) {
		return getText(msg, msg, param);
	}

	/**
	 * getScenarioMessage
	 * @param prefix message id prefix
	 * @return message string
	 */
	protected String getScenarioMessage(String prefix) {
		String msg = prefix + getActionScenario();
		return getText(msg, msg);
	}
	
	/**
	 * getMessage
	 * @param prefix message id prefix
	 * @param param parameters
	 * @return message string
	 */
	protected String getScenarioMessage(String prefix, String param) {
		String msg = prefix + getActionScenario();
		return getText(msg, msg, param);
	}

	/**
	 * add a system error to action
	 * @param prefix
	 * @param e
	 */
	protected void addSystemError(String prefix, Throwable e) {
		addActionError(getScenarioMessage(prefix, 
			assist().isSuperUser() ? Systems.LINE_SEPARATOR + e.getMessage() : Strings.EMPTY));
	}
	
	/**
	 * @param pn property name
	 * @return getText("a.t." + pn)
	 */
	public String getFieldLabel(String pn) {
		return getText("a.t." + pn, pn);
	}
	
	/**
	 * @param pn property name
	 * @return getText("a.t." + pn + "-tip")
	 */
	public String getFieldTooltip(String pn) {
		return getText("a.t." + pn + "-tip", "");
	}
}
