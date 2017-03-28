package panda.lang.time;

/**
 * <p>
 * <code>StopWatch</code> provides a convenient API for timings.
 * </p>
 * 
 * <p>
 * To start the watch, call {@link #start()}. At this point you can:
 * </p>
 * <ul>
 * <li>{@link #split()} the watch to get the time whilst the watch continues in the background. {@link #unsplit()} will
 * remove the effect of the split. At this point, these three options are available again.</li>
 * <li>{@link #suspend()} the watch to pause it. {@link #resume()} allows the watch to continue. Any time between the
 * suspend and resume will not be counted in the total. At this point, these three options are available again.</li>
 * <li>{@link #stop()} the watch to complete the timing session.</li>
 * </ul>
 * 
 * <p>
 * It is intended that the output methods {@link #toString()} and {@link #getTime()} should only be called after stop,
 * split or suspend, however a suitable result will be returned at other points.
 * </p>
 * 
 * <p>
 * NOTE: The methods protect against inappropriate calls. Thus you cannot now call stop before start,
 * resume before suspend or unsplit before split.
 * </p>
 * 
 * <p>
 * 1. split(), suspend(), or stop() cannot be invoked twice<br />
 * 2. unsplit() may only be called if the watch has been split()<br />
 * 3. resume() may only be called if the watch has been suspend()<br />
 * 4. start() cannot be called twice without calling reset()
 * </p>
 * 
 * <p>This class is not thread-safe</p>
 * 
 */
public class StopWatch {
	/**
	 * Enumeration type which indicates the status of stopwatch.
	 */
	private enum State {
		UNSTARTED {
			@Override
			boolean isStarted() {
				return false;
			}

			@Override
			boolean isStopped() {
				return true;
			}

			@Override
			boolean isSuspended() {
				return false;
			}
		},
		RUNNING {
			@Override
			boolean isStarted() {
				return true;
			}

			@Override
			boolean isStopped() {
				return false;
			}

			@Override
			boolean isSuspended() {
				return false;
			}
		},
		STOPPED {
			@Override
			boolean isStarted() {
				return false;
			}

			@Override
			boolean isStopped() {
				return true;
			}

			@Override
			boolean isSuspended() {
				return false;
			}
		},
		SUSPENDED {
			@Override
			boolean isStarted() {
				return true;
			}

			@Override
			boolean isStopped() {
				return false;
			}

			@Override
			boolean isSuspended() {
				return true;
			}
		};

		/**
		 * <p>
		 * The method is used to find out if the StopWatch is started. A suspended StopWatch is also
		 * started watch.
		 * </p>
		 * 
		 * @return boolean If the StopWatch is started.
		 */
		abstract boolean isStarted();

		/**
		 * <p>
		 * This method is used to find out whether the StopWatch is stopped. The stopwatch which's
		 * not yet started and explicitly stopped stopwatch is considered as stopped.
		 * </p>
		 * 
		 * @return boolean If the StopWatch is stopped.
		 */
		abstract boolean isStopped();

		/**
		 * <p>
		 * This method is used to find out whether the StopWatch is suspended.
		 * </p>
		 * 
		 * @return boolean If the StopWatch is suspended.
		 */
		abstract boolean isSuspended();
	}

	/**
	 * Enumeration type which indicates the split status of stopwatch.
	 */
	private enum SplitState {
		SPLIT, UNSPLIT
	}

	/**
	 * The current running state of the StopWatch.
	 */
	private State runningState = State.UNSTARTED;

	/**
	 * Whether the stopwatch has a split time recorded.
	 */
	private SplitState splitState = SplitState.UNSPLIT;

	/**
	 * The start time.
	 */
	private long startTime;

	/**
	 * The start time in Millis - nanoTime is only for elapsed time so we need to also store the
	 * currentTimeMillis to maintain the old getStartTime API.
	 */
	private long startTimeMillis;

	/**
	 * The stop time.
	 */
	private long stopTime;

	/**
	 * The stop time in Milis.
	 */
	private long stopTimeMillis;

	/**
	 * Constructor.
	 */
	public StopWatch() {
		this(true);
	}

	/**
	 * Constructor.
	 * @param start start the stopwatch
	 */
	public StopWatch(boolean start) {
		if (start) {
			start();
		}
	}

	/**
	 * <p>
	 * Start the stopwatch.
	 * </p>
	 * <p>
	 * This method starts a new timing session, clearing any previous values.
	 * </p>
	 * 
	 * @return this
	 * @throws IllegalStateException if the StopWatch is already running.
	 */
	public StopWatch start() {
		if (this.runningState == State.STOPPED) {
			throw new IllegalStateException("Stopwatch must be reset before being restarted. ");
		}
		if (this.runningState != State.UNSTARTED) {
			throw new IllegalStateException("Stopwatch already started. ");
		}
		this.startTime = System.nanoTime();
		this.startTimeMillis = System.currentTimeMillis();
		this.runningState = State.RUNNING;
		return this;
	}

	/**
	 * <p>
	 * Stop the stopwatch.
	 * </p>
	 * <p>
	 * This method ends a new timing session, allowing the time to be retrieved.
	 * </p>
	 * 
	 * @return this
	 * @throws IllegalStateException if the StopWatch is not running.
	 */
	public StopWatch stop() {
		if (this.runningState != State.RUNNING && this.runningState != State.SUSPENDED) {
			throw new IllegalStateException("Stopwatch is not running. ");
		}
		if (this.runningState == State.RUNNING) {
			this.stopTime = System.nanoTime();
			this.stopTimeMillis = System.currentTimeMillis();
		}
		this.runningState = State.STOPPED;
		return this;
	}

	/**
	 * <p>
	 * Resets the stopwatch. Stops it if need be.
	 * </p>
	 * <p>
	 * This method clears the internal values to allow the object to be reused.
	 * </p>
	 * @return this
	 */
	public StopWatch reset() {
		this.runningState = State.UNSTARTED;
		this.splitState = SplitState.UNSPLIT;
		return this;
	}

	/**
	 * <p>
	 * Restarts the stopwatch.
	 * </p>
	 * @return this
	 */
	public StopWatch restart() {
		reset();
		start();
		return this;
	}

	/**
	 * <p>
	 * Split the time.
	 * </p>
	 * <p>
	 * This method sets the stop time of the watch to allow a time to be extracted. The start time
	 * is unaffected, enabling {@link #unsplit()} to continue the timing from the original start
	 * point.
	 * </p>
	 * 
	 * @return this
	 * @throws IllegalStateException if the StopWatch is not running.
	 */
	public StopWatch split() {
		if (this.runningState != State.RUNNING) {
			throw new IllegalStateException("Stopwatch is not running. ");
		}
		this.stopTime = System.nanoTime();
		this.stopTimeMillis = System.currentTimeMillis();
		this.splitState = SplitState.SPLIT;
		return this;
	}

	/**
	 * <p>
	 * Remove a split.
	 * </p>
	 * <p>
	 * This method clears the stop time. The start time is unaffected, enabling timing from the
	 * original start point to continue.
	 * </p>
	 * 
	 * @return this
	 * @throws IllegalStateException if the StopWatch has not been split.
	 */
	public StopWatch unsplit() {
		if (this.splitState != SplitState.SPLIT) {
			throw new IllegalStateException("Stopwatch has not been split. ");
		}
		this.splitState = SplitState.UNSPLIT;
		return this;
	}

	/**
	 * <p>
	 * Suspend the stopwatch for later resumption.
	 * </p>
	 * <p>
	 * This method suspends the watch until it is resumed. The watch will not include time between
	 * the suspend and resume calls in the total time.
	 * </p>
	 * 
	 * @return this
	 * @throws IllegalStateException if the StopWatch is not currently running.
	 */
	public StopWatch suspend() {
		if (this.runningState != State.RUNNING) {
			throw new IllegalStateException("Stopwatch must be running to suspend. ");
		}
		this.stopTime = System.nanoTime();
		this.stopTimeMillis = System.currentTimeMillis();
		this.runningState = State.SUSPENDED;
		return this;
	}

	/**
	 * <p>
	 * Resume the stopwatch after a suspend.
	 * </p>
	 * <p>
	 * This method resumes the watch after it was suspended. The watch will not include time between
	 * the suspend and resume calls in the total time.
	 * </p>
	 * 
	 * @return this
	 * @throws IllegalStateException if the StopWatch has not been suspended.
	 */
	public StopWatch resume() {
		if (this.runningState != State.SUSPENDED) {
			throw new IllegalStateException("Stopwatch must be suspended to resume. ");
		}
		this.startTime += System.nanoTime() - this.stopTime;
		this.startTimeMillis += System.currentTimeMillis() - this.stopTimeMillis;
		this.runningState = State.RUNNING;
		return this;
	}

	/**
	 * <p>
	 * Get the time on the stopwatch.
	 * </p>
	 * <p>
	 * This is either the time between the start and the moment this method is called, or the amount
	 * of time between start and stop.
	 * </p>
	 * 
	 * @return the time in milliseconds
	 */
	public long getTime() {
		if (this.runningState == State.STOPPED || this.runningState == State.SUSPENDED) {
			return this.stopTimeMillis - this.startTimeMillis;
		}
		else if (this.runningState == State.UNSTARTED) {
			return 0;
		}
		else if (this.runningState == State.RUNNING) {
			return System.currentTimeMillis() - this.startTimeMillis;
		}
		throw new RuntimeException("Illegal running state has occured. ");
	}

	/**
	 * <p>
	 * Get the time on the stopwatch in nanoseconds.
	 * </p>
	 * <p>
	 * This is either the time between the start and the moment this method is called, or the amount
	 * of time between start and stop.
	 * </p>
	 * 
	 * @return the time in nanoseconds
	 */
	public long getNanoTime() {
		if (this.runningState == State.STOPPED || this.runningState == State.SUSPENDED) {
			return this.stopTime - this.startTime;
		}
		else if (this.runningState == State.UNSTARTED) {
			return 0;
		}
		else if (this.runningState == State.RUNNING) {
			return System.nanoTime() - this.startTime;
		}
		throw new RuntimeException("Illegal running state has occured. ");
	}

	/**
	 * <p>
	 * Get the split time on the stopwatch.
	 * </p>
	 * <p>
	 * This is the time between start and latest split.
	 * </p>
	 * 
	 * @return the split time in milliseconds
	 * @throws IllegalStateException if the StopWatch has not yet been split.
	 */
	public long getSplitTime() {
		if (this.splitState != SplitState.SPLIT) {
			throw new IllegalStateException("Stopwatch must be split to get the split time. ");
		}
		return this.stopTimeMillis - this.startTimeMillis;
	}

	/**
	 * <p>
	 * Get the split time on the stopwatch in nanoseconds.
	 * </p>
	 * <p>
	 * This is the time between start and latest split.
	 * </p>
	 * 
	 * @return the split time in nanoseconds
	 * @throws IllegalStateException if the StopWatch has not yet been split.
	 */
	public long getSplitNanoTime() {
		if (this.splitState != SplitState.SPLIT) {
			throw new IllegalStateException("Stopwatch must be split to get the split time. ");
		}
		return this.stopTime - this.startTime;
	}

	/**
	 * Returns the time this stopwatch was started.
	 * 
	 * @return the time this stopwatch was started
	 * @throws IllegalStateException if this StopWatch has not been started
	 */
	public long getStartTime() {
		if (this.runningState == State.UNSTARTED) {
			throw new IllegalStateException("Stopwatch has not been started");
		}
		return this.startTimeMillis;
	}

	/**
	 * Returns the time this stopwatch was started.
	 * 
	 * @return the time this stopwatch was started
	 * @throws IllegalStateException if this StopWatch has not been started
	 */
	public long getStartNanoTime() {
		if (this.runningState == State.UNSTARTED) {
			throw new IllegalStateException("Stopwatch has not been started");
		}
		return this.startTime;
	}

	/**
	 * Returns the time this stopwatch was stopped or suspended.
	 * 
	 * @return the time this stopwatch was stopped or suspended
	 * @throws IllegalStateException if this StopWatch has not been stopped or suspended
	 */
	public long getStopTime() {
		if (this.runningState != State.STOPPED && this.runningState != State.SUSPENDED) {
			throw new IllegalStateException("Stopwatch must be stopped or suspended to get the stop time. ");
		}

		return this.stopTimeMillis;
	}

	/**
	 * Returns the time this stopwatch was stopped or suspended.
	 * 
	 * @return the time this stopwatch was stopped or suspended
	 * @throws IllegalStateException if this StopWatch has not been stopped or suspended
	 */
	public long getStopNanoTime() {
		if (this.runningState != State.STOPPED || this.runningState != State.SUSPENDED) {
			throw new IllegalStateException("Stopwatch must be stopped or suspended to get the stop time. ");
		}

		return this.stopTime;
	}

	/**
	 * <p>
	 * Gets a summary of the time that the stopwatch recorded as a string.
	 * </p>
	 * 
	 * @return the time as a String
	 */
	@Override
	public String toString() {
		return TimeSpan.toDisplayString(getTime());
	}

	/**
	 * <p>
	 * Gets a summary of the split time that the stopwatch recorded as a string.
	 * </p>
	 * 
	 * @return the split time as a String
	 */
	public String toSplitString() {
		return TimeSpan.toDisplayString(getSplitTime());
	}

	/**
	 * <p>
	 * The method is used to find out if the StopWatch is started. A suspended StopWatch is also
	 * started watch.
	 * </p>
	 * 
	 * @return boolean If the StopWatch is started.
	 */
	public boolean isStarted() {
		return runningState.isStarted();
	}

	/**
	 * <p>
	 * This method is used to find out whether the StopWatch is suspended.
	 * </p>
	 * 
	 * @return boolean If the StopWatch is suspended.
	 */
	public boolean isSuspended() {
		return runningState.isSuspended();
	}

	/**
	 * <p>
	 * This method is used to find out whether the StopWatch is stopped. The stopwatch which's not
	 * yet started and explicitly stopped stopwatch is considered as stopped.
	 * </p>
	 * 
	 * @return boolean If the StopWatch is stopped.
	 */
	public boolean isStopped() {
		return runningState.isStopped();
	}

	public static StopWatch run(Runnable r) {
		StopWatch sw = new StopWatch();
		r.run();
		sw.stop();
		return sw;
	}
}
