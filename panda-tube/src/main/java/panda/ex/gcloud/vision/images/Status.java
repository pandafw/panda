package panda.ex.gcloud.vision.images;

import java.util.List;
import java.util.Map;

import panda.ex.gcloud.Code;

public class Status {
	private Code code;
	private String message;
	private List<Map<String, Object>> details;

	public Code getCode() {
		return code;
	}
	public void setCode(Code code) {
		this.code = code;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public List<Map<String, Object>> getDetails() {
		return details;
	}
	public void setDetails(List<Map<String, Object>> details) {
		this.details = details;
	}
}
