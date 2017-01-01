package panda.mvc.view.tag;

import panda.ioc.annotation.IocBean;

/**
 * Render TSV.
 * 
 */
@IocBean(singleton=false)
public class Tsv extends Csv {

	public Tsv() {
		separator = '\t';
	}
}
