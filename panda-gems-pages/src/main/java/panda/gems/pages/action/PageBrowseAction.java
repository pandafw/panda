package panda.gems.pages.action;

import java.util.List;

import panda.app.action.BaseAction;
import panda.app.auth.Auth;
import panda.app.bean.IndexArg;
import panda.app.constant.AUTH;
import panda.dao.Dao;
import panda.gems.pages.entity.Page;
import panda.gems.pages.entity.query.PageQuery;
import panda.lang.Numbers;
import panda.lang.Strings;
import panda.lang.Systems;
import panda.lang.time.DateTimes;
import panda.mvc.annotation.At;
import panda.mvc.annotation.Redirect;
import panda.mvc.annotation.To;
import panda.mvc.annotation.param.Param;
import panda.mvc.annotation.param.PathArg;
import panda.mvc.view.Views;
import panda.net.URLHelper;

@At("${!!admin_path|||'/admin'}/pages")
@Auth(AUTH.ADMIN)
public class PageBrowseAction extends BaseAction {

	@At
	@To(Views.REDIRECT)
	public String search(@Param("key") String key) throws Exception {
		String url = Strings.removeEnd(getContext().getPath(), "search") + "s/";
		if (Strings.isNotEmpty(key)) {
			url += URLHelper.encodeURL(key);
		}
		return url;
	}

	@At("s/(.*)$")
	@To(Views.SFTL)
	public Object searchs(@PathArg String key, @Param IndexArg arg) throws Exception {
		key = Strings.stripToEmpty(key);
		if (Strings.isNotEmpty(key)) {
			arg.setKey(key);
		}
		return index(arg);
	}

	@At("p(\\d*)$")
	@To(Views.SFTL)
	public Object pager(@PathArg String sno, @Param IndexArg arg) {
		int pno = Numbers.toInt(sno, 0);
		if (pno > 0) {
			arg.getPager().setPage(pno);
		}

		return index(arg);
	}
	
	@At("")
	@To(Views.SFTL)
	@Redirect(toslash=true)
	public Object index(@Param IndexArg arg) {
		assist().loadLimitParams(arg.getPager());

		PageQuery pq = new PageQuery();
		pq.publishDate().le(DateTimes.getDate());
		pq.publishDate().desc().id().desc();

		addFilters(pq);

		List<Page> pages = null;
		Dao dao = getDaoClient().getDao();
		if (Systems.IS_OS_APPENGINE) {
			arg.getPager().normalize();
			pages = dao.select(pq);
			arg.getPager().setCount(pages.size());
		}
		else {
			arg.getPager().setTotal(dao.count(pq));
			arg.getPager().normalize();
			if (arg.getPager().getTotal() > 0) {
				pages = dao.select(pq);
			}
		}
		assist().saveLimitParams(arg.getPager());

		return pages;
	}

	// add filters for sub class
	protected void addFilters(PageQuery pq) {
	}
}

