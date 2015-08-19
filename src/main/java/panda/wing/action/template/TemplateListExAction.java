package panda.wing.action.template;

import panda.dao.query.GenericQuery;
import panda.mvc.annotation.At;
import panda.mvc.bean.Queryer;
import panda.wing.entity.Template;
import panda.wing.entity.query.TemplateQuery;

@At("${settings['admin.context']}/template")
public class TemplateListExAction extends TemplateListAction {
	/**
	 * Constructor
	 */
	public TemplateListExAction() {
	}

	@Override
	protected void addQueryFilter(final GenericQuery<Template> gq, Queryer qr) {
		super.addQueryFilter(gq, qr);
		TemplateQuery tq = new TemplateQuery(gq);
		tq.source().exclude();
	}
}
