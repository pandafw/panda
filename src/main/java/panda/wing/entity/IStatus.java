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
	 * is this data valid
	 * @return true if this data is valid
	 */
	public boolean isValid();

	/**
	 * is this data invalid
	 * @return true if this data is invalid
	 */
	public boolean isInvalid();
}

