package panda.wing.lucene;

import panda.io.Files;
import panda.lang.Classes;
import panda.lang.Exceptions;
import panda.lang.Strings;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

/**
 */
public class LuceneWrapper implements Closeable {
	protected String path;
	protected Version version;
	protected Class<? extends Analyzer> analyzer;
	protected Directory directory;
	protected IndexReader indexReader;
	protected IndexWriter indexWriter;

	protected LuceneWrapper() {
		this(null, Version.LUCENE_46);
	}

	protected LuceneWrapper(LuceneWrapper lw) {
		copy(lw);
	}

	/**
	 * @param path lucene directory
	 */
	public LuceneWrapper(String path) {
		this(path, Version.LUCENE_46);
	}
	
	/**
	 * @param path lucene directory
	 * @param version lucene version
	 */
	public LuceneWrapper(String path, Version version) {
		this(path, StandardAnalyzer.class, version);
	}

	/**
	 * @param path lucene directory
	 * @param analyzer analyzer class
	 */
	public LuceneWrapper(String path, Class<? extends Analyzer> analyzer) {
		this(path, analyzer, Version.LUCENE_46);
	}

	/**
	 * @param path lucene directory
	 * @param version lucene version
	 * @param analyzer analyzer class
	 */
	public LuceneWrapper(String path, Class<? extends Analyzer> analyzer, Version version) {
		super();
		this.path = path;
		this.analyzer = analyzer;
		this.version = version;
	}

	/**
	 * @return the path
	 */
	public String getPath() {
		return path;
	}

	/**
	 * @return the version
	 */
	public Version getVersion() {
		return version;
	}

	public void init() throws IOException {
		this.directory = FSDirectory.open(new File(path));
	}
	
	@Override
	public void close() throws IOException {
		if (directory != null) {
			directory.close();
			directory = null;
		}
	}
	
	protected void copy(LuceneWrapper lw) {
		this.path = lw.path;
		this.version = lw.version;
		this.analyzer = lw.analyzer;
		this.directory = lw.directory;
		this.indexReader = lw.indexReader;
		this.indexWriter = lw.indexWriter;
	}

	public void clean() throws IOException {
		close();
		
		File file = new File(path);
		if (file.exists()) {
			Files.forceDelete(file);
		}
	}
	
	public Analyzer getAnalyzer() {
		return Classes.born(analyzer, version, version.getClass());
	}
	
	public IndexSearcher getIndexSearcher() {
		return new IndexSearcher(getIndexReader());
	}
	
	public QueryParser getQueryParser(String field) {
		return new QueryParser(version, field, getAnalyzer());
	}
	
	/**
	 * @return the lucene IndexReader
	 */
	public synchronized IndexReader getIndexReader() {
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
	public IndexWriter getIndexWriter() {
		if (indexWriter == null) {
			indexWriter = openIndexWriter(directory);
		}
		return indexWriter;
	}
	
	private IndexWriter openIndexWriter(Directory directory) {
		Analyzer analyzer = getAnalyzer();
		IndexWriterConfig iwc = new IndexWriterConfig(version, analyzer);
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

	/**
	 * @return query
	 */
	public Query parseSimpleQuery(String field, String text) {
		text = Strings.replaceChars(text, "+-&|!(){}[]^\"~*?:\\/", ' ');
		if (Strings.isBlank(text)) {
			return null;
		}
		
		QueryParser qp = getQueryParser(field);
		try {
			return qp.parse(text);
		}
		catch (ParseException e) {
			throw new RuntimeException(e);
		}
	}
	
	@Override
	public String toString() {
		return version + ":" + path;
	}
}
