package panda.taskqueue;

import java.util.HashMap;
import java.util.Map;

/**
 * @author yf.frank.wang@gmail.com
 */
public class TaskQueueFactory {
	private static TaskQueueFactory instance = new TaskQueueFactory();
	
	private Map<String, TaskQueue> queues;

	/**
	 * @return the instance
	 */
	public static TaskQueueFactory getInstance() {
		return instance;
	}

	/**
	 * @param instance the instance to set
	 */
	public static void setInstance(TaskQueueFactory instance) {
		TaskQueueFactory.instance = instance;
	}

	public TaskQueueFactory() {
		queues = new HashMap<String, TaskQueue>();
		queues.put(TaskQueue.DEFAULT_QUEUE, new TaskQueue(TaskQueue.DEFAULT_QUEUE));
	}
	
	public TaskQueue getDefaultQueue() {
		return getQueue(TaskQueue.DEFAULT_QUEUE);
	}
	
	public TaskQueue getQueue(String name) {
		return queues.get(name);
	}
}
