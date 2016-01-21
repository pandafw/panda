package panda.wing.entity;


public interface IStatus {
	public static final String STATUS = "status";
	
	/**
	 * @return the status
	 */
	public Character getStatus();

	/**
	 * @param status the status to set
	 */
	public void setStatus(Character status);

	//----------------------------------------------------------------------
	/**
	 * is this data active
	 * @return true if this data is active
	 */
	public boolean isActive();

	/**
	 * is this data disabled
	 * @return true if this data is disabled
	 */
	public boolean isDisabled();

	/**
	 * is this data recycled
	 * @return true if this data is recycled
	 */
	public boolean isRecycled();
}

