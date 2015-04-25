package panda.net.p2p;

import panda.lang.Objects;
import panda.net.http.URLHelper;

public class Magnet {
	/**
	 * dn (Display Name) – Filename
	 */
	private String dn;
	
	/**
	 * xl (eXact Length) – Size in bytes
	 */
	private String xl;
	
	/**
	 * xt (eXact Topic) – URN containing file hash
	 */
	private String xt;
	
	/**
	 * as (Acceptable Source) – Web link to the file online
	 */
	private String as;

	/**
	 * xs (eXact Source) – P2P link.
	 */
	private String xs;
	
	/**
	 * kt (Keyword Topic) – Key words for search
	 */
	private String kt;
	
	/**
	 * mt (Manifest Topic) – link to the metafile that contains a list of magneto (MAGMA – MAGnet MAnifest)
	 */
	private String mt;
	
	/**
	 * tr (address TRacker) – Tracker URL for BitTorrent downloads
	 */
	private String tr;

	/**
	 * @return the dn
	 */
	public String getDn() {
		return dn;
	}

	/**
	 * @param dn the dn to set
	 */
	public void setDn(String dn) {
		this.dn = dn;
	}

	/**
	 * @return the xl
	 */
	public String getXl() {
		return xl;
	}

	/**
	 * @param xl the xl to set
	 */
	public void setXl(String xl) {
		this.xl = xl;
	}

	/**
	 * @return the xt
	 */
	public String getXt() {
		return xt;
	}

	/**
	 * @param xt the xt to set
	 */
	public void setXt(String xt) {
		this.xt = xt;
	}

	/**
	 * @return the as
	 */
	public String getAs() {
		return as;
	}

	/**
	 * @param as the as to set
	 */
	public void setAs(String as) {
		this.as = as;
	}

	/**
	 * @return the xs
	 */
	public String getXs() {
		return xs;
	}

	/**
	 * @param xs the xs to set
	 */
	public void setXs(String xs) {
		this.xs = xs;
	}

	/**
	 * @return the kt
	 */
	public String getKt() {
		return kt;
	}

	/**
	 * @param kt the kt to set
	 */
	public void setKt(String kt) {
		this.kt = kt;
	}

	/**
	 * @return the mt
	 */
	public String getMt() {
		return mt;
	}

	/**
	 * @param mt the mt to set
	 */
	public void setMt(String mt) {
		this.mt = mt;
	}

	/**
	 * @return the tr
	 */
	public String getTr() {
		return tr;
	}

	/**
	 * @param tr the tr to set
	 */
	public void setTr(String tr) {
		this.tr = tr;
	}

	public void setBtih(String btih) {
		this.setXt("urn:btih:" + btih);
	}
	
	public String toURL() {
		return URLHelper.buildURL("magnet:", this);
	}
	
	public String toString() {
		return Objects.toStringBuilder()
				.append("dn", dn)
				.append("xl", xl)
				.append("xt", xt)
				.append("as", as)
				.append("xs", xs)
				.append("kt", kt)
				.append("mt", mt)
				.append("tr", tr)
				.toString();
	}
}
