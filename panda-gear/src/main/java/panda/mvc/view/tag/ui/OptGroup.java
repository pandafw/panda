package panda.mvc.view.tag.ui;

import java.io.Writer;

import panda.log.Log;
import panda.log.Logs;

/**
 * <!-- START SNIPPET: javadoc --> Create a optgroup component which needs to resides within a
 * select tag. <!-- END SNIPPET: javadoc -->
 * <p/>
 * <!-- START SNIPPET: notice --> This component is to be used within a Select component. <!-- END
 * SNIPPET: notice -->
 * <p/>
 * 
 * <pre>
 * <!-- START SNIPPET: example -->
 * 
 * &lt;s:select label="My Selection"
 *            name="mySelection"
 *            value="%{'POPEYE'}"
 *            list="%{#{'SUPERMAN':'Superman', 'SPIDERMAN':'spiderman'}}"&gt;
 *    &lt;s:optgroup label="Adult"
 *                 list="%{#{'SOUTH_PARK':'South Park'}}" /&gt;
 *    &lt;s:optgroup label="Japanese"
 *                 list="%{#{'POKEMON':'pokemon','DIGIMON':'digimon','SAILORMOON':'Sailormoon'}}" /&gt;
 * &lt;/s:select&gt;
 * 
 * <!-- END SNIPPET: example -->
 * </pre>
 */
public class OptGroup extends ListUIBean {
	private static Log LOG = Logs.getLog(OptGroup.class);

	public boolean end(Writer writer, String body) {
		Select select = (Select)findAncestor(Select.class);
		if (select == null) {
			LOG.error("incorrect use of OptGroup component, this component must be used within a Select component",
				new IllegalStateException(
					"incorrect use of OptGroup component, this component must be used within a Select component"));
			return false;
		}

		select.addOptGroup(this);
		return false;
	}
}
