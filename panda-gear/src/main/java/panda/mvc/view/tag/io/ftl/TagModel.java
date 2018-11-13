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
import panda.mvc.view.tag.TagBean;

public class TagModel implements TemplateTransformModel {
	private static final Log log = Logs.getLog(TagModel.class);

	private Class<? extends TagBean> tagCls;
	protected ActionContext context;

	public TagModel(ActionContext ac, Class<? extends TagBean> tagCls) {
		this.context = ac;
		this.tagCls = tagCls;
	}

	public Writer getWriter(Writer writer, Map params) throws TemplateModelException, IOException {
		TagBean bean = getBean();

		Map<String, Object> ups = unwrapParameters(params);
		bean.setParameters(ups);
		return new CallbackWriter(bean, writer);
	}

	protected TagBean getBean() {
		return context.getIoc().get(tagCls);
	}

	@SuppressWarnings("deprecation")
	protected Map<String, Object> unwrapParameters(Map params) {
		Map<String, Object> map = new HashMap<String, Object>(params.size());
		BeansWrapper objectWrapper = BeansWrapper.getDefaultInstance();
		for (Iterator iterator = params.entrySet().iterator(); iterator.hasNext();) {
			Map.Entry entry = (Map.Entry)iterator.next();

			Object value = entry.getValue();

			if (value != null) {
				// the value should ALWAYS be a descendant of TemplateModel
				if (value instanceof TemplateModel) {
					try {
						map.put(entry.getKey().toString(), objectWrapper.unwrap((TemplateModel)value));
					}
					catch (TemplateModelException e) {
						if (log.isErrorEnabled()) {
							log.errorf("failed to unwrap [%s] it will be ignored", value.toString(), e);
						}
					}
				}
				else {
					// if it doesn't, we'll do it the old way by just returning the toString()
					// representation
					map.put(entry.getKey().toString(), value.toString());
				}
			}
		}
		return map;
	}
}


