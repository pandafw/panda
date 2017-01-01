package panda.tool.esort;

import java.io.IOException;
import java.util.Comparator;
import java.util.TreeSet;

/**
 * K-Merge
 * @param <T> object type
 */
public abstract class KMerge<T> {

	protected class Segment {
		int segmentNo;
		T record;
	};
	
	protected class Compare implements Comparator<Segment> {
		protected Comparator<? super T> comparator;
		
		/**
		 * @return comparator
		 */
		public Comparator<? super T> getComparator() {
			return comparator;
		}

		/**
		 * @param comparator set comparator
		 */
		public void setComparator(Comparator<? super T> comparator) {
			this.comparator = comparator;
		}

		/**
		 * @param s1 segment 1
		 * @param s2 segment 2
		 * @return compare result
		 * @see java.util.Comparator#compare
		 */
		@SuppressWarnings("unchecked")
		public int compare(Segment s1, Segment s2) {
			if (comparator != null) {
				return comparator.compare(s1.record, s2.record);
			}
			
			if (s1.record instanceof Comparable) {
				Comparable c = (Comparable)(s1.record);
				return c.compareTo(s2.record);
			}
			return 0;
		}
	};
	
	private int size;

	private TreeSet<Segment> loserTree;
	
	private Compare compare;
	
	private KMergeIO mergeIO;
	
	/**
	 * constructor
	 * @param k size of k
	 */
	public KMerge(int k) {
		size = k;
		loserTree = new TreeSet<Segment>();
		compare = new Compare();
	}

	/**
	 * @param comparator set comparator
	 */
	public void setComparator(Comparator<? super T> comparator) {
		compare.setComparator(comparator);
	}
	
	/**
	 * @return mergeIO
	 */
	public KMergeIO getMergeIO() {
		return mergeIO;
	}

	/**
	 * @param mergeIO set mergeIO
	 */
	public void setMergeIO(KMergeIO mergeIO) {
		this.mergeIO = mergeIO;
	}

	/**
	 * construct tree
	 * @throws IOException
	 */
	protected void constructTree() throws IOException {
		for (int i = 0; i < size; i++) {
			Segment s = new Segment();
			s.segmentNo = i;
			s.record = read(i);
			if (s.record != null) {
				loserTree.add(s);
			}
		}
	}
	
	/**
	 * merge
	 * @throws IOException if an I/O error occurs
	 */
	public void merge() throws IOException {
		constructTree();

/*		while (loserTree.size() > 0) {
			Segment s = loserTree.pollFirst();
			write(s.record);
			
			s.record = read(s.segmentNo);
			if (s.record != null) {
				loserTree.add(s);
			}
		}*/
	}

	protected abstract T read(int segmentNo) throws IOException;
	
	protected abstract void write(T obj) throws IOException;
}
