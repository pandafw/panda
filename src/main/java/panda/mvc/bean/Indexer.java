package panda.mvc.bean;

public class Indexer {
	protected Pager pager = new Pager();
	protected Sorter sorter = new Sorter();
	protected String key;
	protected String cat;
	protected String tag;
	
	/**
	 * @return the p
	 */
	public Pager getPager() {
		return pager;
	}

	/**
	 * @param p the p to set
	 */
	public void setPager(Pager p) {
		this.pager = p;
	}

	/**
	 * @return the sorter
	 */
	public Sorter getSorter() {
		return sorter;
	}

	/**
	 * @param s the s to set
	 */
	public void setSorter(Sorter s) {
		this.sorter = s;
	}

	/**
	 * @return the key
	 */
	public String getKey() {
		return key;
	}

	/**
	 * @param key the key to set
	 */
	public void setKey(String key) {
		this.key = key;
	}

	/**
	 * @return the cat
	 */
	public String getCat() {
		return cat;
	}

	/**
	 * @param cat the cat to set
	 */
	public void setCat(String cat) {
		this.cat = cat;
	}

	/**
	 * @return the tag
	 */
	public String getTag() {
		return tag;
	}

	/**
	 * @param tag the tag to set
	 */
	public void setTag(String tag) {
		this.tag = tag;
	}
	
	//--------------------------------------------
	/**
	 * @return the p
	 */
	public Pager getP() {
		return pager;
	}

	/**
	 * @param p the p to set
	 */
	public void setP(Pager p) {
		this.pager = p;
	}

	/**
	 * @return the s
	 */
	public Sorter getS() {
		return sorter;
	}

	/**
	 * @param s the s to set
	 */
	public void setS(Sorter s) {
		this.sorter = s;
	}

	/**
	 * @return the k
	 */
	public String getK() {
		return key;
	}
	/**
	 * @param k the k to set
	 */
	public void setK(String k) {
		this.key = k;
	}
	/**
	 * @return the c
	 */
	public String getC() {
		return cat;
	}
	/**
	 * @param c the c to set
	 */
	public void setC(String c) {
		this.cat = c;
	}
	/**
	 * @return the t
	 */
	public String getT() {
		return tag;
	}
	/**
	 * @param t the t to set
	 */
	public void setT(String t) {
		this.tag = t;
	}
}
