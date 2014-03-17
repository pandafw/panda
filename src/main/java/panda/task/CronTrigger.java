package panda.task;


import java.util.Date;
import java.util.TimeZone;

/**
 * {@link Trigger} implementation for cron expressions.
 * Wraps a {@link CronSequencer}.
 *
 */
public class CronTrigger implements Trigger {

	private final CronSequencer cron;


	/**
	 * Build a {@link CronTrigger} from the pattern provided in the default time zone.
	 * @param expression a space-separated list of time fields,
	 * following cron expression conventions
	 */
	public CronTrigger(String expression) {
		cron = new CronSequencer(expression);
	}

	/**
	 * Build a {@link CronTrigger} from the pattern provided.
	 * @param cronExpression a space-separated list of time fields,
	 * following cron expression conventions
	 * @param timeZone a time zone in which the trigger times will be generated
	 */
	public CronTrigger(String cronExpression, TimeZone timeZone) {
		cron = new CronSequencer(cronExpression, timeZone);
	}


	/**
	 * Determine the next execution time according to the given trigger context.
	 * <p>Next execution times are calculated based on the
	 * {@linkplain TriggerContext#lastCompletionTime completion time} of the
	 * previous execution; therefore, overlapping executions won't occur.
	 */
	@Override
	public Date nextExecutionTime(TriggerContext triggerContext) {
		Date date = triggerContext.lastCompletionTime();
		if (date != null) {
			Date scheduled = triggerContext.lastScheduledExecutionTime();
			if (scheduled != null && date.before(scheduled)) {
				// Previous task apparently executed too early...
				// Let's simply use the last calculated execution time then,
				// in order to prevent accidental re-fires in the same second.
				date = scheduled;
			}
		}
		else {
			date = new Date();
		}
		return cron.next(date);
	}

	public String getExpression() {
		return cron.getExpression();
	}

	@Override
	public boolean equals(Object obj) {
		return (this == obj || (obj instanceof CronTrigger &&
				cron.equals(((CronTrigger) obj).cron)));
	}

	@Override
	public int hashCode() {
		return cron.hashCode();
	}

	@Override
	public String toString() {
		return cron.toString();
	}

}
