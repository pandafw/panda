package panda.lang;

import java.util.Comparator;

import panda.lang.comparator.ComparableComparator;
import panda.lang.comparator.DictionaryComparator;
import panda.lang.comparator.IgnoreCaseStringComparator;
import panda.lang.comparator.NumberComparator;
import panda.lang.comparator.NumericStringComparator;
import panda.lang.comparator.PropertyComparator;
import panda.lang.comparator.ReverseComparator;
import panda.lang.comparator.StringComparator;
import panda.lang.comparator.StringLengthComparator;

public class Comparators {
	public static <T> Comparator<T> reverse(Comparator<T> comparator) {
		return new ReverseComparator<T>(comparator);
	}
	
	public static <T> Comparator<T> property(Class<T> type, String prop) {
		return new PropertyComparator<T>(type, prop);
	}
	
	public static ComparableComparator comparable() {
		return ComparableComparator.i();
	}
	
	public static DictionaryComparator dictionary() {
		return DictionaryComparator.i();
	}
	
	public static IgnoreCaseStringComparator ignorecase() {
		return IgnoreCaseStringComparator.i();
	}

	public static StringComparator string() {
		return StringComparator.i();
	}

	public static StringLengthComparator stringLength() {
		return StringLengthComparator.i();
	}

	public static NumericStringComparator numericString() {
		return NumericStringComparator.i();
	}

	public static NumberComparator number() {
		return NumberComparator.i();
	}

}
