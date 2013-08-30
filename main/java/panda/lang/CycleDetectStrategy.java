package panda.lang;

/**
 * 
 * @author yf.frank.wang@gmail.com
 *
 */
public interface CycleDetectStrategy {
	public static final int CYCLE_DETECT_NOPROP = 1;

	public static final int CYCLE_DETECT_LENIENT = 2;

	public static final int CYCLE_DETECT_STRICT = 3;

}
