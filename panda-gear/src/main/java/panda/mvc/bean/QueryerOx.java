package panda.mvc.bean;

/**
 * disable p.s/p.l for input parameter
 */
public class QueryerOx extends Queryer {

	private static final long serialVersionUID = 1L;

	private static final Pager DUMMY = new Pager();
	
	@Override
	public Pager getP() {
		return DUMMY;
	}

	@Override
	public void setP(Pager p) {
	}

	// short name for Csv/Json/Xml
	public Pager getO() {
		return getPager();
	}

	public void setO(Pager p) {
		setPager(p);
	}
}
