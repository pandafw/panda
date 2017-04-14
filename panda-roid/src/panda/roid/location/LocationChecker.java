package panda.roid.location;

import android.location.Location;

import panda.lang.Strings;

public class LocationChecker {
	private static final int TWO_MINUTES = 1000 * 60 * 2;
	
	private Location current;

	public boolean isFineLocation(Location location) {
		if (isBetterLocation(current, location)) {
			current = location;
			return true;
		}
		return false;
	}
	
	/**
	 * Determines whether one Location reading is better than the current Location fix
	 * 
	 * @param current The current Location fix, to which you want to compare the new one
	 * @param location The new Location that you want to evaluate
	 */
	public static boolean isBetterLocation(Location current, Location location) {
		if (current == null) {
			// A new location is always better than no location
			return true;
		}

		// Check whether the new location fix is newer or older
		long timeDelta = location.getTime() - current.getTime();
		boolean isSignificantlyNewer = timeDelta > TWO_MINUTES;
		boolean isSignificantlyOlder = timeDelta < -TWO_MINUTES;
		boolean isNewer = timeDelta > 0;

		// If it's been more than two minutes since the current location, use the new location
		// because the user has likely moved
		if (isSignificantlyNewer) {
			return true;
		}

		// If the new location is more than two minutes older, it must be worse
		if (isSignificantlyOlder) {
			return false;
		}

		// Check whether the new location fix is more or less accurate
		int accuracyDelta = (int)(location.getAccuracy() - current.getAccuracy());
		boolean isLessAccurate = accuracyDelta > 0;
		boolean isMoreAccurate = accuracyDelta < 0;
		boolean isSignificantlyLessAccurate = accuracyDelta > 200;

		// Check if the old and new location are from the same provider
		boolean isFromSameProvider = Strings.equals(location.getProvider(), current.getProvider());

		// Determine location quality using a combination of timeliness and accuracy
		if (isMoreAccurate) {
			return true;
		}
		
		if (isNewer && !isLessAccurate) {
			return true;
		}
		
		if (isNewer && !isSignificantlyLessAccurate && isFromSameProvider) {
			return true;
		}
		return false;
	}
}
