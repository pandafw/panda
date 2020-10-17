package panda.mvc.view.ftl;

import freemarker.template.ObjectWrapper;
import freemarker.template.SimpleHash;
import freemarker.template.TemplateModel;
import freemarker.template.TemplateModelException;

import panda.bean.BeanHandler;
import panda.lang.Classes;
import panda.mvc.ActionContext;
import panda.mvc.Mvcs;

/**
 * Simple Hash model that also searches other scopes.
 * <p/>
 * If the key doesn't exist in this hash, this template model tries to resolve the key within the
 * attributes of the following scopes, in the order stated: Request, Session, Servlet Context
 * Updated to subclass AllHttpScopesHashModel.java to incorporate invisible scopes and compatibility
 * with freemarker.
 */
public class ActionHash extends SimpleHash {

	public static class TopModel {
		private Object top;
		
		public TopModel(Object top) {
			this.top = top;
		}

		/**
		 * @return the top
		 */
		public Object getTop() {
			return top;
		}

		/**
		 * @param top the top to set
		 */
		public void setTop(Object top) {
			this.top = top;
		}
	}
	private static final long serialVersionUID = 1L;

	private Object model;
	private BeanHandler mbh;
	
	private ActionContext ac;
	private BeanHandler acb;

	public ActionHash(ObjectWrapper wrapper, ActionContext context) {
		super(wrapper);
		ac = context;
		acb = Mvcs.getBeans().getBeanHandler(ac.getClass());
	}

	public void setModel(Object model) {
		this.model = model;
		if (model == null) {
			mbh = null;
		}
		else {
			if (Classes.isImmutable(model.getClass())) {
				this.model = new TopModel(model);
			}
			mbh = Mvcs.getBeans().getBeanHandler(this.model.getClass());
		}
	}
	
	@SuppressWarnings("unchecked")
	public TemplateModel get(String key) throws TemplateModelException {
		// Lookup in default scope
		TemplateModel tm = super.get(key);
		if (tm != null) {
			return tm;
		}

		// Lookup in the specified model
		if (mbh != null) {
			Object obj = mbh.getBeanValue(model, key);
			if (obj != null) {
				return wrap(obj);
			}
		}

		// Lookup in the action context
		if (acb != null) {
			Object obj = acb.getBeanValue(ac, key);
			if (obj != null) {
				return wrap(obj);
			}
		}

		return null;
	}
}
