package panda.app.index;

import panda.idx.Indexer;
import panda.idx.Indexes;


public interface RevisionedIndexes extends Indexes {
	public Indexer newIndexer();
	
	public Indexer newIndexer(String name);
	
	public void setIndexer(Indexer indexer);
}
