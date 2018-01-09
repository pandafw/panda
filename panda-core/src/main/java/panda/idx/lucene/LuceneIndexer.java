package panda.idx.lucene;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.TopDocsCollector;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

import panda.idx.IDocument;
import panda.idx.IQuery;
import panda.idx.IResult;
import panda.idx.IndexException;
import panda.idx.Indexer;
import panda.io.Files;
import panda.io.Streams;
import panda.lang.Exceptions;

public class LuceneIndexer implements Indexer {
	protected String name;
	protected Version version;
	protected Directory directory;
	protected Analyzer analyzer;
	protected IndexReader indexReader;
	protected IndexWriter indexWriter;

	/**
	 * @param name name
	 * @param directory lucene directory
	 */
	public LuceneIndexer(String name, Directory directory) {
		this(name, directory, Version.LATEST);
	}
	
	/**
	 * @param name name
	 * @param directory lucene directory
	 * @param version lucene version
	 */
	public LuceneIndexer(String name, Directory directory, Version version) {
		this(name, directory, new StandardAnalyzer(), version);
	}

	/**
	 * @param name name
	 * @param directory lucene directory
	 * @param analyzer lucene analyzer
	 */
	public LuceneIndexer(String name, Directory directory, Analyzer analyzer) {
		this(name, directory, analyzer, Version.LATEST);
	}

	/**
	 * @param name name
	 * @param directory lucene directory
	 * @param version lucene version
	 * @param analyzer lucene analyzer
	 */
	public LuceneIndexer(String name, Directory directory, Analyzer analyzer, Version version) {
		this.name = name;
		this.directory = directory;
		this.analyzer = analyzer;
		this.version = version;
	}

	/**
	 * @return the directory
	 */
	public Directory getDirectory() {
		return directory;
	}

	/**
	 * @return the version
	 */
	public Version getVersion() {
		return version;
	}

	/**
	 * @return the analyzer
	 */
	public Analyzer getAnalyzer() {
		return analyzer;
	}

	/**
	 * close
	 * @throws IOException if an IO error occurred
	 */
	protected void close() throws IOException {
		if (indexReader != null) {
			Streams.safeClose(indexReader);
			indexReader = null;
		}
		if (indexWriter != null) {
			Streams.safeClose(indexWriter);
			indexWriter = null;
		}
		if (directory != null) {
			directory.close();
			directory = null;
		}
	}

	@Override
	public void drop() {
		try {
			// close
			close();
			
			// clean
			if (directory instanceof FSDirectory) {
				Path p = ((FSDirectory)directory).getDirectory();
				File file = p.toFile();
				if (file.exists()) {
					Files.forceDelete(file);
				}
			}
		}
		catch (IOException e) {
			throw new IndexException("Failed to drop index: " + name, e);
		}
	}

	protected IndexSearcher getIndexSearcher() {
		return new IndexSearcher(getIndexReader());
	}
	
	/**
	 * @return the lucene IndexReader
	 */
	protected synchronized IndexReader getIndexReader() {
		if (indexReader == null) {
			try {
				indexReader = DirectoryReader.open(directory);
			}
			catch (IOException e) {
				throw Exceptions.wrapThrow(e);
			}
		}
		return indexReader;
	}
	
	/**
	 * @return the lucene IndexWriter
	 */
	protected synchronized IndexWriter getIndexWriter() {
		if (indexWriter == null) {
			indexWriter = openIndexWriter(directory);
		}
		return indexWriter;
	}
	
	protected IndexWriter openIndexWriter(Directory directory) {
		Analyzer analyzer = getAnalyzer();
		
		IndexWriterConfig iwc = new IndexWriterConfig(analyzer);
		iwc.setOpenMode(OpenMode.CREATE_OR_APPEND);

		// Optional: for better indexing performance, if you
		// are indexing many documents, increase the RAM
		// buffer. But if you do this, increase the max heap
		// size to the JVM (eg add -Xmx512m or -Xmx1g):
		//
		// iwc.setRAMBufferSizeMB(256.0);

		try {
			return new IndexWriter(directory, iwc);
		}
		catch (IOException e) {
			throw Exceptions.wrapThrow(e);
		}
	}

	@Override
	public String name() {
		return name;
	}

	@Override
	public IDocument newDocument() {
		return new LuceneDocument();
	}

	@Override
	public void insert(IDocument doc) {
		IndexWriter iw = getIndexWriter();
		try {
			LuceneDocument ld = (LuceneDocument)doc;
			iw.addDocument(ld.getDocument());
			iw.commit();
		}
		catch (IOException e) {
			throw new IndexException("Failed to insert document - " + doc, e);
		}
	}

	@Override
	public void inserts(Collection<IDocument> docs) {
		IndexWriter iw = getIndexWriter();
		try {
			for (IDocument doc : docs) {
				LuceneDocument ld = (LuceneDocument)doc;
				iw.addDocument(ld.getDocument());
			}
			iw.commit();
		}
		catch (IOException e) {
			throw new IndexException("Failed to inserts documents - " + docs, e);
		}
	}

	@Override
	public void update(IDocument doc) {
		IndexWriter iw = getIndexWriter();
		try {
			LuceneDocument ld = (LuceneDocument)doc;
			iw.updateDocument(ld.getIdTerm(), ld.getDocument());
			iw.commit();
		}
		catch (IOException e) {
			throw new IndexException("Failed to update document - " + doc, e);
		}
	}

	@Override
	public void updates(Collection<IDocument> docs) {
		IndexWriter iw = getIndexWriter();
		try {
			for (IDocument doc : docs) {
				LuceneDocument ld = (LuceneDocument)doc;
				iw.updateDocument(ld.getIdTerm(), ld.getDocument());
			}
			iw.commit();
		}
		catch (IOException e) {
			throw new IndexException("Failed to update documents - " + docs, e);
		}
	}

	@Override
	public void remove(String id) {
		try {
			IndexWriter iw = getIndexWriter();
			Term term = LuceneDocument.getIdTerm(id);
			iw.deleteDocuments(term);
			iw.commit();
		}
		catch (IOException e) {
			throw new IndexException("Failed to remove document - " + id, e);
		}
	}

	@Override
	public void removes(Collection<String> ids) {
		try {
			IndexWriter iw = getIndexWriter();
			for (String id : ids) {
				Term term = LuceneDocument.getIdTerm(id);
				iw.deleteDocuments(term);
			}
			iw.commit();
		}
		catch (IOException e) {
			throw new IndexException("Failed to remove documents - " + ids, e);
		}
	}

	@Override
	public IQuery newQuery() {
		return new LuceneQuery(getAnalyzer());
	}

	@Override
	public IResult search(IQuery query) {
		try {
			Query q = ((LuceneQuery)query).buildQuery();
			TopDocsCollector c = ((LuceneQuery)query).buildCollector();
			
			IndexSearcher searcher = getIndexSearcher(); 
			searcher.search(q, c);
	
			IResult r = new IResult();
			r.setTotalHits(c.getTotalHits());
			if (r.getTotalHits() > 0) {
				TopDocs hits = c.topDocs((int)query.getStart(), (int)query.getLimit());
				List<IDocument> docs = new ArrayList<IDocument>(hits.scoreDocs.length);
				for (int i = 0; i < hits.scoreDocs.length; i++) {
					Document doc = searcher.doc(hits.scoreDocs[i].doc);
					docs.add(new LuceneDocument(doc));
				}
				r.setDocuments(docs);
			}
			return r;
		}
		catch (IOException e) {
			throw new IndexException("Failed to search " + query);
		}
	}

	@Override
	public String toString() {
		return version + ":" + directory;
	}
}
