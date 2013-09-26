package panda.bind;

import panda.bind.filters.FilterPropertyFilter;
import panda.bind.filters.PagerPropertyFilter;
import panda.bind.filters.QueryPropertyFilter;
import panda.bind.filters.SorterPropertyFilter;
import panda.util.Filter;
import panda.util.Pager;
import panda.util.CompositeQuery;
import panda.util.Sorter;

/**
 * 
 * @author yf.frank.wang@gmail.com
 *
 */
public abstract class Binds {
	protected static void setDefaultSerializerOptions(AbstractSerializer as) {
		as.registerPropertyFilter(Filter.class, new FilterPropertyFilter(false));
		as.registerPropertyFilter(Pager.class, new PagerPropertyFilter(false));
		as.registerPropertyFilter(CompositeQuery.class, new QueryPropertyFilter(false));
		as.registerPropertyFilter(Sorter.class, new SorterPropertyFilter(false));
	}	
}
