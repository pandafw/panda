package panda.tool.poi.doc;

import java.util.LinkedHashMap;
import java.util.Map;


public class EParagraph {
	private Map<Integer, ECharRun> cruns = new LinkedHashMap<Integer, ECharRun>();

	/**
	 * @return the cruns
	 */
	public Map<Integer, ECharRun> getCruns() {
		return cruns;
	}

	/**
	 * @param cruns the cruns to set
	 */
	public void setCruns(Map<Integer, ECharRun> cruns) {
		this.cruns = cruns;
	}
}

