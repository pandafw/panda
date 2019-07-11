package panda.app.action.crud;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import panda.app.action.BaseAction;
import panda.dao.Dao;
import panda.dao.entity.Entity;
import panda.dao.entity.EntityField;
import panda.dao.query.DataQuery;
import panda.lang.Arrays;
import panda.lang.Collections;
import panda.lang.Strings;
import panda.lang.Systems;
import panda.mvc.View;
import panda.mvc.view.SitemeshFreemarkerView;
import panda.mvc.view.Views;


/**
 * @param <T> data type
 */
public abstract class GenericBaseAction<T> extends BaseAction {
	//------------------------------------------------------------
	// scenario & apimode
	//------------------------------------------------------------
	private String scenario;
	private boolean apimode;

	//------------------------------------------------------------
	// entity properties
	//------------------------------------------------------------
	private Class<? extends T> type;
	private Entity<T> entity;
	private Dao dao;
	
	//------------------------------------------------------------
	// display fields (display & update)
	//------------------------------------------------------------
	private Set<String> displayFields;
	private Map<String, String> mappingFields;
	
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

	/**
	 * @param fields the fields to add
	 */
	protected void setDisplayFields(String... fields) {
		displayFields = Arrays.toSet(fields);
	}

	/**
	 * @param fields the fields to add
	 */
	protected void addDisplayFields(String... fields) {
		if (Arrays.isEmpty(fields)) {
			return;
		}
		if (displayFields == null) {
			displayFields = new HashSet<String>();
		}
		displayFields.addAll(Arrays.asList(fields));
	}

	/**
	 * @param fields the fields to remove
	 */
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
		return fs.contains(field);
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

	/**
	 * @param fields the fields to set
	 */
	protected void setMappingFields(String... fields) {
		if (Arrays.isEmpty(fields)) {
			mappingFields = null;
			return;
		}
		if (fields.length % 2 != 0) {
			throw new IllegalArgumentException("The mapping fields is incorrect: " + Strings.join(fields, ','));
		}

		mappingFields = Arrays.toMap(fields);
	}

	/**
	 * @param fields the fields to add
	 */
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

	/**
	 * @param fields the fields to remove
	 */
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
	public Class<? extends T> getT() {
		return type;
	}

	/**
	 * public for getText(...)
	 * @return the type
	 */
	public Class<? extends T> getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	protected void setType(Class<? extends T> type) {
		this.type = type;
	}

	@SuppressWarnings("unchecked")
	protected Entity<T> getEntity() {
		if (entity == null) {
			entity = (Entity<T>)getDaoClient().getEntity(getType());
		}
		return entity;
	}
	
	protected DataQuery<T> getDataQuery() {
		return new DataQuery<T>(getEntity());
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
	 * @return the scenario
	 */
	protected String getScenario() {
		if (scenario == null) {
			scenario = Strings.substringBefore(context.getMethodName(), '_');
		}
		return scenario;
	}

	/**
	 * @param scenario the scenario to set
	 */
	protected void setScenario(String scenario) {
		this.scenario = scenario;
	}

	/**
	 * set view by scenario
	 */
	protected void setScenarioView() {
		setScenarioView(null);
	}

	/**
	 * set view by scenario
	 * @param step scenario step
	 */
	protected void setScenarioView(String step) {
		if (apimode) {
			// do not set view on api mode
			return;
		}
		
		String v = getScenario();
		if (Strings.isNotEmpty(step)) {
			v += '_' + step;
		}
		
		View w = newScenarioView(v);
		context.setView(w);
	}

	/**
	 * create a scenario view (default: sftl:~xxx)
	 * @param sn the scenario step name
	 * @return the view
	 */
	protected View newScenarioView(String sn) {
		return Views.sftl(context, SitemeshFreemarkerView.ALT_PREFIX + sn);
	}

	/**
	 * @return the apimode
	 */
	protected boolean isApimode() {
		return apimode;
	}

	/**
	 * @param apimode the apimode to set
	 */
	protected void setApimode(boolean apimode) {
		this.apimode = apimode;
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
		String msg = prefix + getScenario();
		return getText(msg, msg);
	}
	
	/**
	 * getMessage
	 * @param prefix message id prefix
	 * @param param parameters
	 * @return message string
	 */
	protected String getScenarioMessage(String prefix, String param) {
		String msg = prefix + getScenario();
		return getText(msg, msg, param);
	}

	/**
	 * add a system error to action
	 * @param prefix
	 * @param e
	 */
	protected void addSystemError(String prefix, Throwable e) {
		addActionError(getScenarioMessage(prefix, 
			assist().hasSuperRole() ? Systems.LINE_SEPARATOR + e.getMessage() : Strings.EMPTY));
	}
	
	/**
	 * @param pn property name
	 * @return getText("a.t." + pn)
	 */
	public String getFieldLabel(String pn) {
		if (Strings.isEmpty(pn)) {
			return pn;
		}
		if (Character.isLetter(pn.charAt(0))) {
			return getText("a.t." + pn, pn);
		}
		return getText(pn, pn);
	}
	
	/**
	 * @param pn property name
	 * @return getText("a.t." + pn + "-tip")
	 */
	public String getFieldTooltip(String pn) {
		if (Strings.isEmpty(pn)) {
			return pn;
		}
		if (Character.isLetter(pn.charAt(0))) {
			return getText("a.t." + pn + "-tip", "");
		}
		return getText(pn + "-tip", "");
	}
}
