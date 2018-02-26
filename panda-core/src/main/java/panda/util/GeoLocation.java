package panda.util;

public class GeoLocation {
	public static final int EARTH_RADIUS = 6371000; // meters
	public static final int METERS_PER_NAUTICAL_MILE = 1852;

	private String name;
	private double latitude;
	private double longitude;

	public GeoLocation(double latitude, double longitude) {
		this(null, latitude, longitude);
	}

	public GeoLocation(String name, double latitude, double longitude) {
		super();
		this.name = name;
		this.latitude = latitude;
		this.longitude = longitude;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the latitude
	 */
	public double getLatitude() {
		return latitude;
	}

	/**
	 * @param latitude the latitude to set
	 */
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	/**
	 * @return the longitude
	 */
	public double getLongitude() {
		return longitude;
	}

	/**
	 * @param longitude the longitude to set
	 */
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	/**
	 * calculate the distance between this location and that location
	 * @param that that location
	 * @return distance between this location and that location
	 */
	public double distanceTo(GeoLocation that) {
		return calcDistance(this.latitude, this.longitude, that.latitude, that.longitude);
	}

	/**
	 * http://digdeeply.org/archives/06152067.html
	 * @param dis distance (meter)
	 * @return square point GeoLocation array (left-top, right-top, left-bottom, right-bottom)
	 */
	public GeoLocation[] squarePoints(double dis) {
		return calcSquarePoints(this.latitude, this.longitude, dis);
	}
	
	/**
	 * @return string representation of this point
	 */
	public String toString() {
		return name + " (" + latitude + ", " + longitude + ")";
	}

	/**
	 * calculate the distance between two geo-location
	 * @param lat1 latitude 1
	 * @param lng1 longitude 1
	 * @param lat2 latitude 2
	 * @param lng2 longitude 2
	 * @return distance
	 */
	public static double calcDistance(double lat1, double lng1, double lat2, double lng2) {
		double rlat1 = Math.toRadians(lat1);
		double rlng1 = Math.toRadians(lng1);
		double rlat2 = Math.toRadians(lat2);
		double rlng2 = Math.toRadians(lng2);

		// great circle distance in radians, using law of cosines formula
		double angle = Math.acos(Math.sin(rlat1) * Math.sin(rlat2) 
			+ Math.cos(rlat1) * Math.cos(rlat2) * Math.cos(rlng1 - rlng2));

		// each degree on a great circle of Earth is 60 nautical miles
		double nauticalMiles = 60 * Math.toDegrees(angle);
		double meters = METERS_PER_NAUTICAL_MILE * nauticalMiles;
		return meters;
	}

	/**
	 * http://digdeeply.org/archives/06152067.html
	 * @param lat latitude
	 * @param lng longitude
	 * @param dis distance
	 * @return square point GeoLocation array (left-top, right-top, left-bottom, right-bottom)
	 */
	public static GeoLocation[] calcSquarePoints(double lat, double lng, double dis) {
		double dlng = Math.toDegrees(2 * Math.asin(Math.sin(dis / (2 * EARTH_RADIUS)) / Math.cos(Math.toRadians(lat))));

		double dlat = Math.toDegrees(dis / EARTH_RADIUS);

		GeoLocation[] gls = new GeoLocation[4];
		gls[0] = new GeoLocation(lat + dlat, lng - dlng);
		gls[1] = new GeoLocation(lat + dlat, lng + dlng);
		gls[2] = new GeoLocation(lat - dlat, lng - dlng);
		gls[3] = new GeoLocation(lat - dlat, lng + dlng);
		return gls;
	}
}
