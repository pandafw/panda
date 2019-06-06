package panda.tube.gcloud.vision;

import java.io.IOException;
import java.util.List;

import panda.bind.json.JsonException;
import panda.bind.json.Jsons;
import panda.io.MimeTypes;
import panda.lang.Collections;
import panda.lang.Strings;
import panda.net.URLHelper;
import panda.net.http.HttpClient;
import panda.net.http.HttpRequest;
import panda.net.http.HttpResponse;
import panda.tube.gcloud.vision.images.AnnotateApiRequest;
import panda.tube.gcloud.vision.images.AnnotateApiResponse;
import panda.tube.gcloud.vision.images.AnnotateImageRequest;
import panda.tube.gcloud.vision.images.AnnotateImageResponse;
import panda.tube.gcloud.vision.images.EntityAnnotation;
import panda.tube.gcloud.vision.images.Feature;
import panda.tube.gcloud.vision.images.FeatureType;
import panda.tube.gcloud.vision.images.Image;
import panda.tube.gcloud.vision.images.ImageSource;

public class ImagesAnnotate {
	public static final String ENDPOINT = "https://vision.googleapis.com/v1/images:annotate";

	private String apikey;

	public ImagesAnnotate() {
	}

	public ImagesAnnotate(String apikey) {
		this.apikey = apikey;
	}

	public String getApikey() {
		return apikey;
	}

	public void setApikey(String apikey) {
		this.apikey = apikey;
	}

	protected String getApiEndPoint() {
		return ENDPOINT + "?key=" + URLHelper.encodeURL(apikey);
	}
	
	public AnnotateApiResponse call(AnnotateApiRequest aar) throws IOException {
		if (Strings.isEmpty(apikey)) {
			throw new IllegalArgumentException("Empty Google Cloud API KEY");
		}

		String b = Jsons.toJson(aar);

		HttpClient hc = new HttpClient();
		HttpRequest hr = hc.getRequest();

		hr.setUrl(getApiEndPoint());
		hr.setUserAgent(getClass().getName());
		hr.setContentType(MimeTypes.APP_JSON);
		hr.setBody(b);

		HttpResponse hrs = hc.doPost();
		if (!hrs.isOK()) {
			throw new IOException(hrs.getStatusLine());
		}

		String s = hrs.getContentText();
		try {
			AnnotateApiResponse aae = Jsons.fromJson(s, AnnotateApiResponse.class);
			return aae;
		}
		catch (JsonException e) {
			throw new IOException("Failed to parse response", e);
		}
	}

	public EntityAnnotation detectLabel(byte[] image) throws IOException {
		List<EntityAnnotation> eas = detectLabels(image, 1);
		return Collections.isEmpty(eas) ? null : eas.get(0);
	}

	public EntityAnnotation detectLabel(String imageUri) throws IOException {
		List<EntityAnnotation> eas = detectLabels(imageUri, 1);
		return Collections.isEmpty(eas) ? null : eas.get(0);
	}

	public List<EntityAnnotation> detectLabels(byte[] image) throws IOException {
		return detectLabels(image, 0);
	}

	public List<EntityAnnotation> detectLabels(String imageUri) throws IOException {
		return detectLabels(imageUri, 0);
	}
	
	public List<EntityAnnotation> detectLabels(byte[] image, int maxResults) throws IOException {
		return detectLabels(Image.fromContent(image), maxResults);
	}
	
	public List<EntityAnnotation> detectLabels(String imageUrl, int maxResults) throws IOException {
		return detectLabels(new Image(ImageSource.fromUri(imageUrl)), maxResults);
	}
	
	public List<EntityAnnotation> detectLabels(Image image, int maxResults) throws IOException {
		AnnotateApiRequest aar = new AnnotateApiRequest();
		AnnotateImageRequest air = new AnnotateImageRequest();

		Feature f = new Feature(FeatureType.LABEL_DETECTION);
		if (maxResults > 0) {
			f.setMaxResults(maxResults);
		}
		air.addFeature(f);
		air.setImage(image);
		aar.addRequest(air);
		
		AnnotateApiResponse aae = call(aar);
		List<AnnotateImageResponse> airs = aae.getResponses();
		if (Collections.isEmpty(airs)) {
			return null;
		}
		AnnotateImageResponse ir = airs.get(0);
		List<EntityAnnotation> eas = ir.getLabelAnnotations();
		return eas;
	}
}
