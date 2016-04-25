package panda.net.util;

import java.io.Serializable;
import java.util.EventListener;
import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 */

public class ListenerList implements Serializable, Iterable<EventListener> {
	private static final long serialVersionUID = -1934227607974228213L;

	private final CopyOnWriteArrayList<EventListener> __listeners;

	public ListenerList() {
		__listeners = new CopyOnWriteArrayList<EventListener>();
	}

	public void addListener(EventListener listener) {
		__listeners.add(listener);
	}

	public void removeListener(EventListener listener) {
		__listeners.remove(listener);
	}

	public int getListenerCount() {
		return __listeners.size();
	}

	/**
	 * Return an {@link Iterator} for the {@link EventListener} instances.
	 * 
	 * @return an {@link Iterator} for the {@link EventListener} instances TODO Check that this is a
	 *         good defensive strategy
	 */
	@Override
	public Iterator<EventListener> iterator() {
		return __listeners.iterator();
	}

}
