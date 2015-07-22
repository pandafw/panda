package panda.mvc.view.tag.io.ftl;

import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import freemarker.ext.beans.BeansWrapper;
import freemarker.template.TemplateModel;
import freemarker.template.TemplateModelException;
import freemarker.template.TemplateTransformModel;

import panda.log.Log;
import panda.log.Logs;
import panda.mvc.ActionContext;
import panda.mvc.view.tag.Component;

public class TagModel implements TemplateTransformModel {
	private static final Log LOG = Logs.getLog(TagModel.class);

	private Class<? extends Component> tagCls;
	protected ActionContext context;

	public TagModel(ActionContext ac, Class<? extends Component> tagCls) {
		this.context = ac;
		this.tagCls = tagCls;
	}

	public Writer getWriter(Writer writer, Map params) throws TemplateModelException, IOException {
		Component bean = getBean();

		Map<String, Object> ups = unwrapParameters(params);
		bean.copyParams(ups);
		return new CallbackWriter(bean, writer);
	}

	protected Component getBean() {
		return context.getIoc().get(tagCls);
	}

	protected Map<String, Object> unwrapParameters(Map params) {
		Map<String, Object> map = new HashMap<String, Object>(params.size());
		BeansWrapper objectWrapper = BeansWrapper.getDefaultInstance();
		for (Iterator iterator = params.entrySet().iterator(); iterator.hasNext();) {
			Map.Entry entry = (Map.Entry)iterator.next();

			Object value = entry.getValue();

			if (value != null) {
				// the value should ALWAYS be a decendant of TemplateModel
				if (value instanceof TemplateModel) {
					try {
						map.put(entry.getKey().toString(), objectWrapper.unwrap((TemplateModel)value));
					}
					catch (TemplateModelException e) {
						if (LOG.isErrorEnabled()) {
							LOG.errorf("failed to unwrap [%s] it will be ignored", value.toString(), e);
						}
					}
				}
				// if it doesn't, we'll do it the old way by just returning the toString()
				// representation
				else {
					map.put(entry.getKey().toString(), value.toString());
				}
			}
		}
		return map;
	}
}


