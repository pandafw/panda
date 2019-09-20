package panda.gems.pages.action;

import java.util.List;

import panda.app.action.BaseAction;
import panda.app.bean.IndexArg;
import panda.dao.Dao;
import panda.gems.pages.V;
import panda.gems.pages.entity.Page;
import panda.gems.pages.entity.query.PageQuery;
import panda.gems.tager.entity.Tag;
import panda.gems.tager.entity.query.TagQuery;
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

public abstract class PageBrowseAction extends BaseAction {
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

	@At
	@To(Views.REDIRECT)
	public String tag(@Param("tag") String tag) throws Exception {
		String url = Strings.removeEnd(getContext().getPath(), "tag") + "t/";
		if (Strings.isNotEmpty(tag)) {
			url += URLHelper.encodeURL(tag);
		}
		return url;
	}

	@At("t/(.*)$")
	@To(Views.SFTL)
	public Object tags(@PathArg String tag, @Param IndexArg arg) {
		if (Strings.isNotEmpty(tag)) {
			arg.setTag(tag);
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
		addFilters(pq, arg);

		List<Page> pages = null;
		Dao dao = getDaoClient().getDao();
		if (Systems.IS_OS_APPENGINE) {
			arg.getPager().normalize();
			addSorters(pq);
			pages = dao.select(pq);
			arg.getPager().setCount(pages.size());
		}
		else {
			arg.getPager().setTotal(dao.count(pq));
			arg.getPager().normalize();
			if (arg.getPager().getTotal() > 0) {
				addSorters(pq);
				pages = dao.select(pq);
			}
		}
		assist().saveLimitParams(arg.getPager());

		return pages;
	}

	protected void addFilters(PageQuery pq, IndexArg arg) {
		pq.publishDate().le(DateTimes.getDate());

		if (!Systems.IS_OS_APPENGINE) {
			// GQL does not support like
			if (Strings.isNotEmpty(arg.getKey())) {
				pq.title().contains(arg.getKey());
			}
		}
		if (Strings.isNotEmpty(arg.getTag())) {
			TagQuery tq = new TagQuery();
			tq.kind().eq(V.PAGE_TAG_KIND);
			tq.name().eq(arg.getTag());
			pq.innerJoin(tq, "tg", Page.ID + '=' + Tag.CODE);
		}
	}

	protected void addSorters(PageQuery pq) {
		pq.publishDate().desc().id().desc();
	}
}

