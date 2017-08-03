package panda.idx.gae;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;

import com.google.appengine.api.search.Document;
import com.google.appengine.api.search.Index;
import com.google.appengine.api.search.PutException;
import com.google.appengine.api.search.Query;
import com.google.appengine.api.search.Results;
import com.google.appengine.api.search.ScoredDocument;
import com.google.appengine.api.search.SearchException;

import panda.idx.IDocument;
import panda.idx.IQuery;
import panda.idx.IResult;
import panda.idx.IndexException;
import panda.idx.Indexer;

public class GaeIndexer implements Indexer {
	protected String name;
	protected Index index;
	protected Locale locale;

	/**
	 * @param name name
	 * @param index gae index
	 */
	public GaeIndexer(String name, Index index, Locale locale) {
		this.name = name;
		this.index = index;
		this.locale = locale;
	}

	/**
	 * @return the locale
	 */
	public Locale getLocale() {
		return locale;
	}

	/**
	 * @param locale the locale to set
	 */
	public void setLocale(Locale locale) {
		this.locale = locale;
	}

	/**
	 * @return the index
	 */
	public Index getIndex() {
		return index;
	}

	@Override
	public String name() {
		return name;
	}

	@Override
	public IDocument newDocument() {
		return new GaeDocument(locale);
	}

	@Override
	public void insert(IDocument doc) {
		put(doc);
	}

	@Override
	public void inserts(Collection<IDocument> docs) {
		puts(docs);
	}

	@Override
	public void update(IDocument doc) {
		put(doc);
	}

	@Override
	public void updates(Collection<IDocument> docs) {
		puts(docs);
	}

	protected void put(IDocument doc) {
		try {
			GaeDocument gd = (GaeDocument)doc;
			index.put(gd.getBuilder());
		}
		catch (PutException e) {
			throw new IndexException("Failed to put document - " + doc, e);
		}
	}

	protected void puts(Collection<IDocument> docs) {
		try {
			int i = 0;

			Document.Builder[] bs = new Document.Builder[docs.size()];
			for (IDocument doc : docs) {
				GaeDocument gd = (GaeDocument)doc;
				bs[i++] = gd.getBuilder();
			}
			index.put(bs);
		}
		catch (PutException e) {
			throw new IndexException("Failed to put documents - " + docs, e);
		}
	}

	@Override
	public void remove(String id) {
		try {
			index.delete(id);
		}
		catch (PutException e) {
			throw new IndexException("Failed to delete document - " + id, e);
		}
	}

	@Override
	public void removes(Collection<String> ids) {
		try {
			index.delete(ids);
		}
		catch (PutException e) {
			throw new IndexException("Failed to remove documents - " + ids, e);
		}
	}

	@Override
	@SuppressWarnings("deprecation")
	public void drop() {
		try {
			index.deleteSchema();
		}
		catch (Exception e) {
			throw new IndexException("Failed to drop index " + name, e);
		}
	}
	
	@Override
	public IQuery newQuery() {
		return new GaeQuery();
	}

	@Override
	public IResult search(IQuery query) {
		try {
			Query q = ((GaeQuery)query).buildQuery();
			
			Results<ScoredDocument> rs = getIndex().search(q);
			
			IResult ir = new IResult();
			
			ir.setTotalHits(rs.getNumberFound());
			List<IDocument> docs = new ArrayList<IDocument>(rs.getNumberReturned());
			for (ScoredDocument d : rs) {
				docs.add(new GaeDocument(d));
			}
			ir.setDocuments(docs);
			
			return ir;
		}
		catch (SearchException e) {
			throw new IndexException("Failed to search " + query);
		}
	}

}
