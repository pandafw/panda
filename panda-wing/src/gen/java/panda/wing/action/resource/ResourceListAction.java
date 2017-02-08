package panda.wing.action.resource;

import java.util.ArrayList;
import java.util.List;
import panda.mvc.View;
import panda.mvc.annotation.At;
import panda.mvc.annotation.To;
import panda.mvc.annotation.param.Param;
import panda.mvc.bean.Queryer;
import panda.mvc.bean.QueryerOx;
import panda.mvc.validation.annotation.Validates;
import panda.mvc.view.tag.ListColumn;
import panda.wing.action.crud.GenericListAction;
import panda.wing.auth.Auth;
import panda.wing.constant.AUTH;
import panda.wing.entity.Resource;

@At("${super_path}/resource")
@Auth(AUTH.SUPER)
public class ResourceListAction extends GenericListAction<Resource> {

	/**
	 * Constructor
	 */
	public ResourceListAction() {
		setType(Resource.class);
		addDisplayFields(Resource.ID, Resource.CLAZZ, Resource.LANGUAGE, Resource.COUNTRY, Resource.SOURCE, Resource.STATUS, Resource.UUSID, Resource.UTIME);
	}


	/*----------------------------------------------------------------------*
	 * Actions
	 *----------------------------------------------------------------------*/
	/**
	 * list
	 */
	@At
	@To(value=View.SFTL, error=View.SFTL)
	public Object list(@Param @Validates Queryer qr) {
		return super.list(qr);
	}
	
	/**
	 * list_csv
	 */
	@At
	@To(value=View.SFTL, error=View.SFTL)
	public Object list_csv(@Param @Validates QueryerOx qr) {
		List<ListColumn> columns = new ArrayList<ListColumn>();
		if (displayField("id")) {
			ListColumn lc = new ListColumn();
			lc.name = "id";
			lc.header = getFieldLabel("id");
			lc.hidden = false;
			columns.add(lc);
		}
		if (displayField("clazz")) {
			ListColumn lc = new ListColumn();
			lc.name = "clazz";
			lc.header = getFieldLabel("clazz");
			lc.hidden = false;
			columns.add(lc);
		}
		if (displayField("language")) {
			ListColumn lc = new ListColumn();
			lc.name = "language";
			lc.header = getFieldLabel("language");
			lc.hidden = false;
			ListColumn.Format lcf = new ListColumn.Format();
			lcf.type = "code";
			lcf.codemap = consts().getLocaleLanguageMap();
			lc.format = lcf;
			columns.add(lc);
		}
		if (displayField("country")) {
			ListColumn lc = new ListColumn();
			lc.name = "country";
			lc.header = getFieldLabel("country");
			lc.hidden = false;
			ListColumn.Format lcf = new ListColumn.Format();
			lcf.type = "code";
			lcf.codemap = consts().getLocaleCountryMap();
			lc.format = lcf;
			columns.add(lc);
		}
		if (displayField("source")) {
			ListColumn lc = new ListColumn();
			lc.name = "source";
			lc.header = getFieldLabel("source");
			lc.hidden = true;
			columns.add(lc);
		}
		if (displayField("status")) {
			ListColumn lc = new ListColumn();
			lc.name = "status";
			lc.header = getFieldLabel("status");
			lc.hidden = false;
			ListColumn.Format lcf = new ListColumn.Format();
			lcf.type = "code";
			lcf.codemap = consts().getDataStatusMap();
			lc.format = lcf;
			columns.add(lc);
		}
		if (displayField("uusid")) {
			ListColumn lc = new ListColumn();
			lc.name = "uusid";
			lc.header = getFieldLabel("uusid");
			lc.hidden = false;
			columns.add(lc);
		}
		if (displayField("utime")) {
			ListColumn lc = new ListColumn();
			lc.name = "utime";
			lc.header = getFieldLabel("utime");
			lc.hidden = false;
			ListColumn.Format lcf = new ListColumn.Format();
			lcf.type = "datetime";
			lc.format = lcf;
			columns.add(lc);
		}
		return super.list_csv(qr, columns);
	}
	
	/**
	 * list_print
	 */
	@At
	@To(value=View.SFTL, error=View.SFTL)
	public Object list_print(@Param @Validates Queryer qr) {
		return super.list_print(qr);
	}
	
	/**
	 * list_json
	 */
	@At
	@To(all=View.JSON)
	public Object list_json(@Param @Validates QueryerOx qr) {
		return super.list_json(qr);
	}
	
	/**
	 * list_xml
	 */
	@At
	@To(all=View.XML)
	public Object list_xml(@Param @Validates QueryerOx qr) {
		return super.list_xml(qr);
	}
	
}

