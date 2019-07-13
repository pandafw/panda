package panda.gems.pages.action;

import panda.app.action.BaseAction;
import panda.app.auth.Auth;
import panda.app.constant.AUTH;
import panda.dao.Dao;
import panda.gems.pages.S;
import panda.gems.pages.entity.Page;
import panda.gems.pages.entity.query.PageQuery;
import panda.lang.Numbers;
import panda.lang.Strings;
import panda.lang.time.DateTimes;
import panda.mvc.annotation.At;
import panda.mvc.annotation.To;
import panda.mvc.annotation.param.Param;
import panda.mvc.annotation.param.PathArg;
import panda.mvc.view.Views;
import panda.servlet.HttpServletResponser;
import panda.servlet.HttpServlets;

@At("${!!admin_path|||'/admin'}/pages")
@Auth(AUTH.ADMIN)
public class PageDetailAction extends BaseAction {
	@At({"v/(.*)$", "v"})
	@To(Views.SFTL)
	public Object show(@PathArg String slug, @Param("id") Long id) throws Exception {
		Dao dao = getDaoClient().getDao();

		PageQuery pq = new PageQuery();
		if (id != null) {
			pq.id().eq(id);
		}
		else if (Strings.isNotEmpty(slug)) {
			if (Numbers.isLong(slug)) {
				pq.id().eq(Numbers.toLong(slug));
			}
			else {
				pq.slug().eq(slug);
			}
		}
		else {
			return Views.seNotFound(context);
		}
		
		pq.publishDate().le(DateTimes.getDate());
		Page p = dao.fetch(pq);
		if (p == null) {
			return Views.seNotFound(context);
		}

		int maxage = getSettings().getPropertyAsInt(S.PAGES_CACHE_MAXAGE, 0);
		if (maxage > 0) {
			if (HttpServlets.checkAndSetNotModified(getRequest(), getResponse(), p.getUpdatedAt(), maxage)) {
				return Views.none(context);
			}
			
			HttpServletResponser hsrs = new HttpServletResponser(getRequest(), getResponse());
			hsrs.setLastModified(p.getUpdatedAt());
			hsrs.setMaxAge(maxage);
			hsrs.writeHeader();
		}
		
		return p;
	}

}

