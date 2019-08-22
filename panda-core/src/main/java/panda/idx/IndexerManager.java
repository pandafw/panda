package panda.idx;

public interface IndexerManager {
	Indexer getIndexer();

	Indexer getIndexer(String name);
	
	void dropIndexer();
	
	void dropIndexer(String name);
}
