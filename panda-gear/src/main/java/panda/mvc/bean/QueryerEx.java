package panda.mvc.bean;

/**
 * disable p.s/p.l for input parameter
 * used by export methods
 */
public class QueryerEx extends Queryer {

	private static final long serialVersionUID = 1L;

	private static final Pager DUMMY = new Pager();
	
	@Override
	public Pager getP() {
		return DUMMY;
	}

	@Override
	public void setP(Pager p) {
	}

	// short name for Csv/Json/Xml pager parameter
	public Pager getE() {
		return getPager();
	}

	public void setE(Pager p) {
		setPager(p);
	}
}
