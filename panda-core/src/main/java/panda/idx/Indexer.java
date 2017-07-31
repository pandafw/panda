package panda.idx;

import java.util.Collection;

public interface Indexer {
	/**
	 * @return the index name
	 */
	String name();

	/**
	 * Create a document 
	 * @return a document
	 */
	IDocument newDocument();
	
	/**
	 * insert a document to index
	 * @param doc document
	 */
	void insert(IDocument doc);
	
	/**
	 * insert documents to index
	 * @param docs documents
	 */
	void inserts(Collection<IDocument> docs);

	/**
	 * update a document to index
	 * @param doc document
	 */
	void update(IDocument doc);
	
	/**
	 * update documents to index
	 * @param docs documents
	 */
	void updates(Collection<IDocument> docs);

	/**
	 * remove a indexed document
	 * @param id document id
	 */
	void remove(String id);

	/**
	 * remove indexed documents
	 * @param ids document id collection
	 */
	void removes(Collection<String> ids);

	/**
	 * drop index
	 */
	void drop();
	
	/**
	 * create a query
	 * @return a Query
	 */
	IQuery newQuery();
	
	/**
	 * search object by query
	 * @param query index query
	 * @return search result
	 */
	IResult search(IQuery query);
}
