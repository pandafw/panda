package panda.tool.poi.ppt;

import java.util.LinkedHashMap;
import java.util.Map;

import panda.tool.poi.ESummary;

public class ESlideShow {
	private ESummary summary;
	private EHeaderFooter headers;
	private Map<String, ESlide> masters = new LinkedHashMap<String, ESlide>();
	private Map<String, ESlide> slides = new LinkedHashMap<String, ESlide>();

	/**
	 * @return the summary
	 */
	public ESummary getSummary() {
		return summary;
	}

	/**
	 * @param summary the summary to set
	 */
	public void setSummary(ESummary summary) {
		this.summary = summary;
	}


	/**
	 * @return the headers
	 */
	public EHeaderFooter getHeaders() {
		return headers;
	}

	/**
	 * @param headers the headers to set
	 */
	public void setHeaders(EHeaderFooter headers) {
		this.headers = headers;
	}

	/**
	 * @return the masters
	 */
	public Map<String, ESlide> getMasters() {
		return masters;
	}

	/**
	 * @param masters the masters to set
	 */
	public void setMasters(Map<String, ESlide> masters) {
		this.masters = masters;
	}

	/**
	 * @return the slides
	 */
	public Map<String, ESlide> getSlides() {
		return slides;
	}

	/**
	 * @param slides the slides to set
	 */
	public void setSlides(Map<String, ESlide> slides) {
		this.slides = slides;
	}

}

