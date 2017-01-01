package panda.net.nntp;

import java.util.Iterator;

/**
 * Class which wraps an {@code Iterable<String>} of raw newgroup information to generate an
 * {@code Iterable<NewsgroupInfo>} of the parsed information.
 */
class NewsgroupIterator implements Iterator<NewsgroupInfo>, Iterable<NewsgroupInfo> {

	private final Iterator<String> stringIterator;

	public NewsgroupIterator(Iterable<String> iterableString) {
		stringIterator = iterableString.iterator();
	}

	@Override
	public boolean hasNext() {
		return stringIterator.hasNext();
	}

	// @Override
	public NewsgroupInfo next() {
		String line = stringIterator.next();
		return NNTPClient.__parseNewsgroupListEntry(line);
	}

	// @Override
	public void remove() {
		stringIterator.remove();
	}

	// @Override
	public Iterator<NewsgroupInfo> iterator() {
		return this;
	}
}
