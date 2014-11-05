package panda.mvc.view.tag;

import java.io.Writer;

import panda.ioc.annotation.IocBean;
import panda.mvc.MvcException;

@IocBean(singleton=false)
public class Param extends Component {
	protected String name;
	protected Object value;
	protected boolean suppressEmptyParameters;

	public boolean end(Writer writer, String body) {
		Component component = findAncestor(Component.class);
		if (value != null) {
			if (component instanceof UnnamedParametric) {
				((UnnamedParametric)component).addParameter(value);
			}
			else {
				if (name == null) {
					throw new MvcException("No name found for following expression: " + this.name);
				}

				if (suppressEmptyParameters) {
					if (value != null) {
						component.setParameter(name, value);
					}
				}
				else {
					component.setParameter(name, value);
				}
			}
		}
		else {
			if (component instanceof UnnamedParametric) {
				((UnnamedParametric)component).addParameter(body);
			}
			else {
				component.setParameter(name, body);
			}
		}

		return super.end(writer, "");
	}

	public boolean usesBody() {
		return true;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public void setSuppressEmptyParameters(boolean suppressEmptyParameters) {
		this.suppressEmptyParameters = suppressEmptyParameters;
	}

	/**
	 * Tags can implement this to support nested param tags without the <tt>name</tt> attribute.
	 * <p/>
	 * The {@link Text TextTag} uses this approach. For unnamed parameters an example is given in
	 * the class javadoc for {@link Param ParamTag}.
	 */
	public interface UnnamedParametric {

		/**
		 * Adds the given value as a parameter to the outer tag.
		 * 
		 * @param value the value
		 */
		public void addParameter(Object value);
	}

}