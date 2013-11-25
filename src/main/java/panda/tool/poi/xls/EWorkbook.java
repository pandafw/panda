package panda.tool.poi.xls;

import java.util.LinkedHashMap;
import java.util.Map;

import panda.tool.poi.ESummary;

public class EWorkbook {
	private ESummary summary;
	private Map<Integer, ESheet> sheets = new LinkedHashMap<Integer, ESheet>();

	/**
	 * @return the summary
	 */
	public ESummary getSummary() {
		return summary;
	}

	/**
	 * @param summary the summary to set
	 */
	public void setSummary(ESummary summary) {
		this.summary = summary;
	}

	/**
	 * @return the sheets
	 */
	public Map<Integer, ESheet> getSheets() {
		return sheets;
	}

	/**
	 * @param sheets the sheets to set
	 */
	public void setSheets(Map<Integer, ESheet> sheets) {
		this.sheets = sheets;
	}
}

