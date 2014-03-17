package panda.task;


import java.util.Date;

/**
 * Context object encapsulating last execution times and last completion time
 * of a given task.
 *
 */
public class TriggerContext {

	private volatile Date lastScheduledExecutionTime;

	private volatile Date lastActualExecutionTime;

	private volatile Date lastCompletionTime;


	/**
	 * Create a SimpleTriggerContext with all time values set to {@code null}.
	 */
	 public TriggerContext() {
	}

	/**
	 * Create a SimpleTriggerContext with the given time values.
	 * @param lastScheduledExecutionTime last <i>scheduled</i> execution time
	 * @param lastActualExecutionTime last <i>actual</i> execution time
	 * @param lastCompletionTime last completion time
	 */
	public TriggerContext(Date lastScheduledExecutionTime, Date lastActualExecutionTime, Date lastCompletionTime) {
		this.lastScheduledExecutionTime = lastScheduledExecutionTime;
		this.lastActualExecutionTime = lastActualExecutionTime;
		this.lastCompletionTime = lastCompletionTime;
	}


	/**
	 * Update this holder's state with the latest time values.
 	 * @param lastScheduledExecutionTime last <i>scheduled</i> execution time
	 * @param lastActualExecutionTime last <i>actual</i> execution time
	 * @param lastCompletionTime last completion time
	 */
	public void update(Date lastScheduledExecutionTime, Date lastActualExecutionTime, Date lastCompletionTime) {
		this.lastScheduledExecutionTime = lastScheduledExecutionTime;
		this.lastActualExecutionTime = lastActualExecutionTime;
		this.lastCompletionTime = lastCompletionTime;
	}


	public Date lastScheduledExecutionTime() {
		return this.lastScheduledExecutionTime;
	}

	public Date lastActualExecutionTime() {
		return this.lastActualExecutionTime;
	}

	public Date lastCompletionTime() {
		return this.lastCompletionTime;
	}

}