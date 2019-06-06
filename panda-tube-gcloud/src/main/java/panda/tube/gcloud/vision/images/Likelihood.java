package panda.tube.gcloud.vision.images;

public enum Likelihood {
	UNKNOWN,		// Unknown likelihood.
	VERY_UNLIKELY,	// It is very unlikely that the image belongs to the specified vertical.
	UNLIKELY,		// It is unlikely that the image belongs to the specified vertical.
	POSSIBLE,		// It is possible that the image belongs to the specified vertical.
	LIKELY,			//It is likely that the image belongs to the specified vertical.
	VERY_LIKELY
}
