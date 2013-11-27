package panda.tool.codegen.bean;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;

/**
 * This object contains factory methods for each Java content interface and Java element interface
 * generated in the nuts.tools.codegen.bean package.
 * <p>
 * An ObjectFactory allows you to programatically construct new instances of the Java representation
 * for XML content. The Java representation of XML content can consist of schema derived interfaces
 * and classes representing the binding of schema type definitions, element declarations and model
 * groups. Factory methods for each of these are provided in this class.
 */
@XmlRegistry
public class ObjectFactory {

	private final static QName _Module_QNAME = new QName("panda.tool.codegen", "module");

	/**
	 * Create a new ObjectFactory that can be used to create new instances of schema derived classes
	 * for package: nuts.tools.codegen.bean
	 */
	public ObjectFactory() {
	}

	/**
	 * Create an instance of {@link Entity }
	 * 
	 * @return new Model
	 */
	public Entity createModel() {
		return new Entity();
	}

	/**
	 * Create an instance of {@link Filter }
	 * 
	 * @return new Filter
	 */
	public Filter createFilter() {
		return new Filter();
	}

	/**
	 * Create an instance of {@link Tag }
	 * 
	 * @return new Tag
	 */
	public Tag createTag() {
		return new Tag();
	}

	/**
	 * Create an instance of {@link EntityProperty }
	 * 
	 * @return new ModelProperty
	 */
	public EntityProperty createModelProperty() {
		return new EntityProperty();
	}

	/**
	 * Create an instance of {@link ActionProperty }
	 * 
	 * @return new ActionProperty
	 */
	public ActionProperty createActionProperty() {
		return new ActionProperty();
	}

	/**
	 * Create an instance of {@link Param }
	 * 
	 * @return new Param
	 */
	public Param createParam() {
		return new Param();
	}

	/**
	 * Create an instance of {@link Module }
	 * 
	 * @return new Module
	 */
	public Module createModule() {
		return new Module();
	}

	/**
	 * Create an instance of {@link Resource }
	 * 
	 * @return new Resource
	 */
	public Resource createResource() {
		return new Resource();
	}

	/**
	 * Create an instance of {@link Action }
	 * 
	 * @return new Action
	 */
	public Action createAction() {
		return new Action();
	}

	/**
	 * Create an instance of {@link InputField }
	 * 
	 * @return new InputField
	 */
	public InputField createInputField() {
		return new InputField();
	}

	/**
	 * Create an instance of {@link ListColumn }
	 * 
	 * @return new ListColumn
	 */
	public ListColumn createListColumn() {
		return new ListColumn();
	}

	/**
	 * Create an instance of {@link InputUI }
	 * 
	 * @return new InputUI
	 */
	public InputUI createInputUI() {
		return new InputUI();
	}

	/**
	 * Create an instance of {@link ListUI }
	 * 
	 * @return new ListUI
	 */
	public ListUI createListUI() {
		return new ListUI();
	}

	/**
	 * Create an instance of {@link Format }
	 * 
	 * @return new Format
	 */
	public Format createFormat() {
		return new Format();
	}

	/**
	 * Create an instance of {@link Validator }
	 * 
	 * @return new Validator
	 */
	public Validator createValidator() {
		return new Validator();
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}{@link Module }{@code >}
	 * 
	 * @param value value
	 * @return new JAXBElement
	 */
	@XmlElementDecl(namespace = "panda.tool.codegen", name = "module")
	public JAXBElement<Module> createModule(Module value) {
		return new JAXBElement<Module>(_Module_QNAME, Module.class, null, value);
	}

}
