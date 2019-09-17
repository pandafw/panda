package ${package}.action.entity.pet;

import java.util.ArrayList;
import java.util.List;
import panda.app.action.crud.GenericListAction;
import panda.dao.query.DataQuery;
import panda.mvc.annotation.At;
import panda.mvc.annotation.To;
import panda.mvc.annotation.param.Param;
import panda.mvc.annotation.validate.VisitValidate;
import panda.mvc.bean.QueryerEx;
import panda.mvc.view.Views;
import panda.mvc.view.util.ListColumn;

import ${package}.entity.Pet;
import ${package}.entity.query.PetQuery;

@At("/pet")
public class PetExportAction extends GenericListAction<Pet> {

	/**
	 * Constructor
	 */
	public PetExportAction() {
		setType(Pet.class);
		setDisplayFields(Pet.NAME, Pet.GENDER, Pet.BIRTHDAY, Pet.AMOUNT, Pet.PRICE, Pet.SHOP_NAME, Pet.STATUS, Pet.UPDATED_AT, Pet.UPDATED_BY);
	}


	/*----------------------------------------------------------------------*
	 * Joins
	 *----------------------------------------------------------------------*/
	/**
	 * add query joins
	 * @param dq data query
	 */
	@Override
	protected void addQueryJoins(DataQuery<Pet> dq) {
		super.addQueryJoins(dq);

		PetQuery eq = new PetQuery(dq);
		eq.autoLeftJoinUU();
	}

	/*----------------------------------------------------------------------*
	 * Actions
	 *----------------------------------------------------------------------*/
	/**
	 * expo_csv
	 * @param qr queryer
	 * @return result or view
	 */
	@At
	@To(value=Views.SFTL, error=Views.SFTL)
	public Object expo_csv(@Param @VisitValidate QueryerEx qr) {
		List<ListColumn> columns = new ArrayList<ListColumn>();
		if (displayField(Pet.NAME)) {
			ListColumn lc = new ListColumn();
			lc.name = Pet.NAME;
			lc.header = getFieldLabel(Pet.NAME);
			lc.hidden = false;
			columns.add(lc);
		}
		if (displayField(Pet.GENDER)) {
			ListColumn lc = new ListColumn();
			lc.name = Pet.GENDER;
			lc.header = getFieldLabel(Pet.GENDER);
			lc.hidden = false;
			ListColumn.Format lcf = new ListColumn.Format();
			lcf.type = "code";
			lcf.codemap = consts().get("petGenderMap");
			lc.format = lcf;
			columns.add(lc);
		}
		if (displayField(Pet.BIRTHDAY)) {
			ListColumn lc = new ListColumn();
			lc.name = Pet.BIRTHDAY;
			lc.header = getFieldLabel(Pet.BIRTHDAY);
			lc.hidden = false;
			ListColumn.Format lcf = new ListColumn.Format();
			lcf.type = "date";
			lc.format = lcf;
			columns.add(lc);
		}
		if (displayField(Pet.AMOUNT)) {
			ListColumn lc = new ListColumn();
			lc.name = Pet.AMOUNT;
			lc.header = getFieldLabel(Pet.AMOUNT);
			lc.hidden = false;
			columns.add(lc);
		}
		if (displayField(Pet.PRICE)) {
			ListColumn lc = new ListColumn();
			lc.name = Pet.PRICE;
			lc.header = getFieldLabel(Pet.PRICE);
			lc.hidden = false;
			columns.add(lc);
		}
		if (displayField(Pet.SHOP_NAME)) {
			ListColumn lc = new ListColumn();
			lc.name = Pet.SHOP_NAME;
			lc.header = getFieldLabel(Pet.SHOP_NAME);
			lc.hidden = false;
			columns.add(lc);
		}
		if (displayField(Pet.STATUS)) {
			ListColumn lc = new ListColumn();
			lc.name = Pet.STATUS;
			lc.header = getFieldLabel(Pet.STATUS);
			lc.hidden = false;
			ListColumn.Format lcf = new ListColumn.Format();
			lcf.type = "code";
			lcf.codemap = consts().get("dataStatusMap");
			lc.format = lcf;
			columns.add(lc);
		}
		if (displayField(Pet.UPDATED_AT)) {
			ListColumn lc = new ListColumn();
			lc.name = Pet.UPDATED_AT;
			lc.header = getFieldLabel(Pet.UPDATED_AT);
			lc.hidden = false;
			ListColumn.Format lcf = new ListColumn.Format();
			lcf.type = "datetime";
			lc.format = lcf;
			columns.add(lc);
		}
		if (displayField(Pet.UPDATED_BY)) {
			ListColumn lc = new ListColumn();
			lc.name = Pet.UPDATED_BY;
			lc.header = getFieldLabel(Pet.UPDATED_BY);
			lc.hidden = false;
			ListColumn.Format lcf = new ListColumn.Format();
			lcf.type = "expr";
			lcf.expr = "top.updatedByUser";
			lc.format = lcf;
			columns.add(lc);
		}
		return super.expo_csv(qr, columns);
	}
	
	/**
	 * expo_xlsx
	 * @param qr queryer
	 * @return result or view
	 */
	@At
	@To(value=Views.SFTL, error=Views.SFTL)
	public Object expo_xlsx(@Param @VisitValidate QueryerEx qr) {
		List<ListColumn> columns = new ArrayList<ListColumn>();
		if (displayField(Pet.NAME)) {
			ListColumn lc = new ListColumn();
			lc.name = Pet.NAME;
			lc.header = getFieldLabel(Pet.NAME);
			lc.hidden = false;
			columns.add(lc);
		}
		if (displayField(Pet.GENDER)) {
			ListColumn lc = new ListColumn();
			lc.name = Pet.GENDER;
			lc.header = getFieldLabel(Pet.GENDER);
			lc.hidden = false;
			ListColumn.Format lcf = new ListColumn.Format();
			lcf.type = "code";
			lcf.codemap = consts().get("petGenderMap");
			lc.format = lcf;
			columns.add(lc);
		}
		if (displayField(Pet.BIRTHDAY)) {
			ListColumn lc = new ListColumn();
			lc.name = Pet.BIRTHDAY;
			lc.header = getFieldLabel(Pet.BIRTHDAY);
			lc.hidden = false;
			ListColumn.Format lcf = new ListColumn.Format();
			lcf.type = "date";
			lc.format = lcf;
			columns.add(lc);
		}
		if (displayField(Pet.AMOUNT)) {
			ListColumn lc = new ListColumn();
			lc.name = Pet.AMOUNT;
			lc.header = getFieldLabel(Pet.AMOUNT);
			lc.hidden = false;
			columns.add(lc);
		}
		if (displayField(Pet.PRICE)) {
			ListColumn lc = new ListColumn();
			lc.name = Pet.PRICE;
			lc.header = getFieldLabel(Pet.PRICE);
			lc.hidden = false;
			columns.add(lc);
		}
		if (displayField(Pet.SHOP_NAME)) {
			ListColumn lc = new ListColumn();
			lc.name = Pet.SHOP_NAME;
			lc.header = getFieldLabel(Pet.SHOP_NAME);
			lc.hidden = false;
			columns.add(lc);
		}
		if (displayField(Pet.STATUS)) {
			ListColumn lc = new ListColumn();
			lc.name = Pet.STATUS;
			lc.header = getFieldLabel(Pet.STATUS);
			lc.hidden = false;
			ListColumn.Format lcf = new ListColumn.Format();
			lcf.type = "code";
			lcf.codemap = consts().get("dataStatusMap");
			lc.format = lcf;
			columns.add(lc);
		}
		if (displayField(Pet.UPDATED_AT)) {
			ListColumn lc = new ListColumn();
			lc.name = Pet.UPDATED_AT;
			lc.header = getFieldLabel(Pet.UPDATED_AT);
			lc.hidden = false;
			ListColumn.Format lcf = new ListColumn.Format();
			lcf.type = "datetime";
			lc.format = lcf;
			columns.add(lc);
		}
		if (displayField(Pet.UPDATED_BY)) {
			ListColumn lc = new ListColumn();
			lc.name = Pet.UPDATED_BY;
			lc.header = getFieldLabel(Pet.UPDATED_BY);
			lc.hidden = false;
			ListColumn.Format lcf = new ListColumn.Format();
			lcf.type = "expr";
			lcf.expr = "top.updatedByUser";
			lc.format = lcf;
			columns.add(lc);
		}
		return super.expo_xlsx(qr, columns);
	}
	
}

