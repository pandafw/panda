package panda.wing.action.template;

import panda.mvc.View;
import panda.mvc.annotation.At;
import panda.mvc.annotation.param.Param;
import panda.mvc.annotation.view.Err;
import panda.mvc.annotation.view.Ok;
import panda.mvc.bean.Queryer;
import panda.mvc.validation.annotation.Validate;
import panda.wing.action.GenericListAction;
import panda.wing.entity.Template;

public abstract class TemplateListAction extends GenericListAction<Template> {

	/**
	 * Constructor
	 */
	public TemplateListAction() {
		setType(Template.class);
	}


	/*----------------------------------------------------------------------*
	 * Actions
	 *----------------------------------------------------------------------*/
	/**
	 * list
	 */
	@At
	@Ok(View.FREEMARKER)
	@Err(View.FREEMARKER)
	public Object list(@Param @Validate Queryer qr) {
		return super.list(qr);
	}
	
	/**
	 * list_csv
	 */
	@At
	@Ok(View.FREEMARKER)
	@Err(View.FREEMARKER)
	public Object list_csv(@Param @Validate Queryer qr) {
		return super.list_csv(qr);
	}
	
	/**
	 * list_print
	 */
	@At
	@Ok(View.FREEMARKER)
	@Err(View.FREEMARKER)
	public Object list_print(@Param @Validate Queryer qr) {
		return super.list_print(qr);
	}
	
}

