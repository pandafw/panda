package panda.tube.gcloud.vision.images;

public enum LandmarkType {
	UNKNOWN_LANDMARK,		// Unknown face landmark detected. Should not be filled.
	LEFT_EYE,				// Left eye.
	RIGHT_EYE,				// Right eye.
	LEFT_OF_LEFT_EYEBROW,	// Left of left eyebrow.
	RIGHT_OF_LEFT_EYEBROW,	// Right of left eyebrow.
	LEFT_OF_RIGHT_EYEBROW,	// Left of right eyebrow.
	RIGHT_OF_RIGHT_EYEBROW,	// Right of right eyebrow.
	MIDPOINT_BETWEEN_EYES,	// Midpoint between eyes.
	NOSE_TIP,				// Nose tip.
	UPPER_LIP,				// Upper lip.
	LOWER_LIP,				// Lower lip.
	MOUTH_LEFT,				// Mouth left.
	MOUTH_RIGHT,			// Mouth right.
	MOUTH_CENTER,				// Mouth center.
	NOSE_BOTTOM_RIGHT,			// Nose, bottom right.
	NOSE_BOTTOM_LEFT,			// Nose, bottom left.
	NOSE_BOTTOM_CENTER,			// Nose, bottom center.
	LEFT_EYE_TOP_BOUNDARY,		// Left eye, top boundary.
	LEFT_EYE_RIGHT_CORNER,		// Left eye, right corner.
	LEFT_EYE_BOTTOM_BOUNDARY,	// Left eye, bottom boundary.
	LEFT_EYE_LEFT_CORNER,		// Left eye, left corner.
	RIGHT_EYE_TOP_BOUNDARY,		// Right eye, top boundary.
	RIGHT_EYE_RIGHT_CORNER,		// Right eye, right corner.
	RIGHT_EYE_BOTTOM_BOUNDARY,	// Right eye, bottom boundary.
	RIGHT_EYE_LEFT_CORNER,		// Right eye, left corner.
	LEFT_EYEBROW_UPPER_MIDPOINT,// Left eyebrow, upper midpoint.
	RIGHT_EYEBROW_UPPER_MIDPOINT,	// Right eyebrow, upper midpoint.
	LEFT_EAR_TRAGION,				// Left ear tragion.
	RIGHT_EAR_TRAGION,				// Right ear tragion.
	LEFT_EYE_PUPIL,					// Left eye pupil.
	RIGHT_EYE_PUPIL,				// Right eye pupil.
	FOREHEAD_GLABELLA,				// Forehead glabella.
	CHIN_GNATHION,					// Chin gnathion.
	CHIN_LEFT_GONION,	// Chin left gonion.
	CHIN_RIGHT_GONION	// Chin right gonion.
}
