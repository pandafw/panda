package panda.idx;

public interface Indexes {
	Indexer getIndexer();

	Indexer getIndexer(String name);
	
	void dropIndexer();
	
	void dropIndexer(String name);
}
