package panda.gems.tager;

import java.util.List;

import panda.dao.Dao;
import panda.gems.tager.entity.Tag;
import panda.gems.tager.entity.query.TagQuery;
import panda.ioc.annotation.IocBean;
import panda.lang.Arrays;
import panda.lang.Strings;

@IocBean
public class Tagger {
	public void update(Dao dao, String kind, String code, String tag) {
		delete(dao, kind, code);
		String[] ts = Strings.split(tag);
		if (Arrays.isNotEmpty(ts)) {
			for (String s : ts) {
				Tag t = new Tag();
				t.setKind(kind);
				t.setCode(code);
				t.setName(s);
				dao.insert(t);
			}
		}
	}
	
	public void delete(Dao dao, String kind, String code) {
		TagQuery tq = new TagQuery();
		tq.kind().eq(kind);
		tq.code().eq(code);
		dao.deletes(tq);
	}
	
	public void deletes(Dao dao, String kind, List<String> codes) {
		TagQuery tq = new TagQuery();
		tq.kind().eq(kind);
		tq.code().in(codes);
		dao.deletes(tq);
	}
}
