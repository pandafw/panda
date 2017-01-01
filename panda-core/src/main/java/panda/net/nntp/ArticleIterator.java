package panda.net.nntp;

import java.util.Iterator;

/**
 * Class which wraps an {@code Iterable<String>} of raw article information to generate an
 * {@code Iterable<Article>} of the parsed information.
 */
class ArticleIterator implements Iterator<Article>, Iterable<Article> {

	private final Iterator<String> stringIterator;

	public ArticleIterator(Iterable<String> iterableString) {
		stringIterator = iterableString.iterator();
	}

	@Override
	public boolean hasNext() {
		return stringIterator.hasNext();
	}

	/**
	 * Get the next Article
	 * 
	 * @return the next {@link Article}, never {@code null}, if unparseable then isDummy() will be
	 *         true, and the subject will contain the raw info.
	 */
	// @Override
	public Article next() {
		String line = stringIterator.next();
		return NNTPClient.__parseArticleEntry(line);
	}

	// @Override
	public void remove() {
		stringIterator.remove();
	}

	// @Override
	public Iterator<Article> iterator() {
		return this;
	}
}
