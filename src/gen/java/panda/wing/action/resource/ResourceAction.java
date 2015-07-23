package panda.wing.action.resource;

import java.util.List;
import panda.mvc.View;
import panda.mvc.annotation.At;
import panda.mvc.annotation.param.Param;
import panda.mvc.annotation.view.Err;
import panda.mvc.annotation.view.Ok;
import panda.mvc.validation.annotation.Validate;
import panda.wing.action.BaseEntityDaoAction;
import panda.wing.entity.Resource;

public class ResourceAction extends BaseEntityDaoAction<Resource> {

	/**
	 * Constructor
	 */
	public ResourceAction() {
		setType(Resource.class);
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
		return super.list();
	}
	
	/**
	 * list_csv
	 */
	@At
	@Ok(View.FREEMARKER)
	@Err(View.FREEMARKER)
	public Object list_csv(@Param @Validate Queryer qr) {
		return super.list_csv();
	}
	
	/**
	 * list_print
	 */
	@At
	@Ok(View.FREEMARKER)
	@Err(View.FREEMARKER)
	public Object list_print(@Param @Validate Queryer qr) {
		return super.list_print();
	}
	
}

