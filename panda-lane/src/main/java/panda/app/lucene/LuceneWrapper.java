package panda.app.lucene;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

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

import panda.io.Files;
import panda.io.Streams;
import panda.lang.Classes;
import panda.lang.Exceptions;
import panda.lang.Strings;

/**
 */
public class LuceneWrapper implements Closeable {
	protected Version version;
	protected Directory directory;
	protected Class<? extends Analyzer> analyzer;
	protected IndexReader indexReader;
	protected IndexWriter indexWriter;

	protected LuceneWrapper() {
		this(null, Version.LATEST);
	}

	/**
	 * @param directory lucene directory
	 */
	public LuceneWrapper(Directory directory) {
		this(directory, Version.LATEST);
	}
	
	/**
	 * @param directory lucene directory
	 * @param version lucene version
	 */
	public LuceneWrapper(Directory directory, Version version) {
		this(directory, StandardAnalyzer.class, version);
	}

	/**
	 * @param directory lucene directory
	 * @param analyzer analyzer class
	 */
	public LuceneWrapper(Directory directory, Class<? extends Analyzer> analyzer) {
		this(directory, analyzer, Version.LATEST);
	}

	/**
	 * @param directory lucene directory
	 * @param version lucene version
	 * @param analyzer analyzer class
	 */
	public LuceneWrapper(Directory directory, Class<? extends Analyzer> analyzer, Version version) {
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

	@Override
	public void close() throws IOException {
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

	public void clean() throws IOException {
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

	public Analyzer getAnalyzer() {
		return Classes.born(analyzer);
	}
	
	public IndexSearcher getIndexSearcher() {
		return new IndexSearcher(getIndexReader());
	}
	
	public QueryParser getQueryParser(String field) {
		return new QueryParser(field, getAnalyzer());
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
	public synchronized IndexWriter getIndexWriter() {
		if (indexWriter == null) {
			indexWriter = openIndexWriter(directory);
		}
		return indexWriter;
	}
	
	private IndexWriter openIndexWriter(Directory directory) {
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

	/**
	 * @return query
	 */
	public Query parseQuery(String field, String text) {
		text = Strings.strip(text);
		if (Strings.isEmpty(text)) {
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
		return version + ":" + directory;
	}
}
