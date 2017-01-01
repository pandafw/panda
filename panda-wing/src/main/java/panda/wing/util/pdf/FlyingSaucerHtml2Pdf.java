package panda.wing.util.pdf;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

import javax.servlet.ServletContext;

import org.w3c.dom.Document;
import org.w3c.tidy.Tidy;
import org.xhtmlrenderer.extend.UserAgentCallback;
import org.xhtmlrenderer.layout.SharedContext;
import org.xhtmlrenderer.pdf.ITextFSImage;
import org.xhtmlrenderer.pdf.ITextOutputDevice;
import org.xhtmlrenderer.pdf.ITextRenderer;
import org.xhtmlrenderer.pdf.PDFAsImage;
import org.xhtmlrenderer.resource.CSSResource;
import org.xhtmlrenderer.resource.ImageResource;
import org.xhtmlrenderer.resource.XMLResource;
import org.xhtmlrenderer.util.XRLog;

import com.itextpdf.text.Image;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfReader;

import panda.ioc.annotation.IocInject;
import panda.lang.Arrays;
import panda.lang.Strings;
import panda.lang.codec.binary.Base64;
import panda.log.Log;
import panda.log.Logs;
import panda.net.http.HttpClient;
import panda.net.http.HttpHeader;

/**
 * deprecated
 * use WkHtml2Pdf
 */
public abstract class FlyingSaucerHtml2Pdf extends Html2Pdf {
	private static final Log log = Logs.getLog(FlyingSaucerHtml2Pdf.class);
	
	protected HttpClient agent;
	protected ITextRenderer renderer;
	
	@IocInject(value="panda.fonts.path", required=false)
	protected String fonts = "web://WEB-INF/fonts";

	@IocInject(required=false)
	protected ServletContext servlet;

	private class ResourceLoaderUserAgent implements UserAgentCallback {
		private SharedContext _sharedContext;
		private ITextOutputDevice _outputDevice;
		private String _baseURL;

		public ResourceLoaderUserAgent(ITextOutputDevice outputDevice,
				SharedContext sharedContext) {
			super();
			_outputDevice = outputDevice;
			_sharedContext = sharedContext;
		}

		protected InputStream resolveStream(String uri) {
			return new ByteArrayInputStream(resolveData(uri));
		}

		protected byte[] resolveData(String uri) {
			if (uri.startsWith("data:")) {
				int comma = uri.indexOf(',');
				if (comma > 0) {
					return Base64.decodeBase64(uri.substring(comma + 1));
				}
				else {
					log.warn("INVLAID URL: " + uri);
					return Arrays.EMPTY_BYTE_ARRAY;
				}
			}
			
			try {
				agent.getRequest().setUrl(uri);
				agent.doGet();
				return agent.getResponse().getContent();
			}
			catch (IOException e) {
				log.warn("GET " + uri, e);
				return Arrays.EMPTY_BYTE_ARRAY;
			}
		}

		/**
		 * Retrieves the CSS at the given URI. This is a synchronous call.
		 * 
		 * @param uri Location of the CSS
		 * @return A CSSResource for the content at the URI.
		 */
		public CSSResource getCSSResource(String uri) {
			return new CSSResource(resolveStream(uri));
		}

		/**
		 * Retrieves the Image at the given URI. This is a synchronous call.
		 * 
		 * @param uri Location of the image
		 * @return An ImageResource for the content at the URI.
		 */
		public ImageResource getImageResource(String uri) {
			URL url = null;
			byte[] data = null;
			
			if (uri.startsWith("data:")) {
				data = resolveData(uri);
				int sep = uri.indexOf(';');
				if (sep > 0) {
					uri = Strings.replaceChars(uri.substring(0, sep), ":/\\", '.');
				}
				else {
					uri = "data.png";
				}
			}
			else {
				url = resolveURL(uri);
				if (url == null) {
					return new ImageResource(uri, null);
				}

				uri = url.toString();
			}

			ImageResource resource = null;
			try {
				if (url != null && url.getPath() != null && url.getPath().toLowerCase().endsWith(".pdf")) {
					PdfReader reader = _outputDevice.getReader(url.toURI());
					PDFAsImage image = new PDFAsImage(url.toURI());
					Rectangle rect = reader.getPageSizeWithRotation(1);
					image.setInitialWidth(rect.getWidth() * _outputDevice.getDotsPerPoint());
					image.setInitialHeight(rect.getHeight() * _outputDevice.getDotsPerPoint());
					resource = new ImageResource(uri, image);
				}
				else {
					if (data == null) {
						data = resolveData(uri);
					}
					Image image = Image.getInstance(data);
					float factor = _sharedContext.getDotsPerPixel();
					image.scaleAbsolute(image.getPlainWidth() * factor, image.getPlainHeight() * factor);
					resource = new ImageResource(uri, new ITextFSImage(image));
				}
			}
			catch (Exception e) {
				log.error("Can't read image file; unexpected problem for URI '" + uri + "'", e);
			}

			if (resource == null) {
				resource = new ImageResource(uri, null);
			}

			return resource;
		}

		/**
		 * Retrieves the XML at the given URI. This is a synchronous call.
		 * 
		 * @param uri Location of the XML
		 * @return A XMLResource for the content at the URI.
		 */
		public XMLResource getXMLResource(String uri) {
			return XMLResource.load(resolveStream(uri));
		}

		/**
		 * Retrieves a binary resource located at a given URI and returns its
		 * contents as a byte array or <code>null</code> if the resource could
		 * not be loaded.
		 * 
		 * @param uri uri 
		 * @return binary resource
		 */
		public byte[] getBinaryResource(String uri) {
			return resolveData(uri);
		}

		/**
		 * Normally, returns true if the user agent has visited this URI.
		 * UserAgent should consider if it should answer truthfully or not for
		 * privacy reasons.
		 * 
		 * @param uri A URI which may have been visited by this user agent.
		 * @return The visited value
		 */
		public boolean isVisited(String uri) {
			return false;
		}

		/**
		 * Does not need to be a correct URL, only an identifier that the
		 * implementation can resolve.
		 * 
		 * @param url A URL against which relative references can be resolved.
		 */
		public void setBaseURL(String url) {
			_baseURL = url;
		}

		/**
		 * @return the base uri, possibly in the implementations private
		 *         uri-space
		 */
		public String getBaseURL() {
			return _baseURL;
		}

		/**
		 * Used to find a uri that may be relative to the BaseURL. The returned
		 * value will always only be used via methods in the same implementation
		 * of this interface, therefore may be a private uri-space.
		 * 
		 * @param uri an absolute or relative (to baseURL) uri to be resolved.
		 * @return the full uri in uri-spaces known to the current
		 *         implementation.
		 */
		public String resolveURI(String uri) {
			URL url = resolveURL(uri);
			return url == null ? null : url.toString();
		}
		
		public URL resolveURL(String uri) {
			if (uri == null) {
				return null;
			}
			
			if (_baseURL == null) {// first try to set a base URL
				try {
					URL result = new URL(uri);
					setBaseURL(result.toExternalForm());
				}
				catch (MalformedURLException e) {
					try {
						setBaseURL(new File(".").toURI().toURL().toExternalForm());
					}
					catch (Exception e1) {
						XRLog.exception("The default NaiveUserAgent doesn't know how to resolve the base URL for "
								+ uri);
						return null;
					}
				}
			}
			// test if the URI is valid; if not, try to assign the base url as
			// its parent
			try {
				return new URL(uri);
			}
			catch (MalformedURLException e) {
				XRLog.load("Could not read "
						+ uri
						+ " as a URL; may be relative. Testing using parent URL "
						+ _baseURL);
				try {
					return new URL(new URL(_baseURL), uri);
				}
				catch (MalformedURLException e1) {
					XRLog.exception("The default NaiveUserAgent cannot resolve the URL "
							+ uri + " with base URL " + _baseURL);
					return null;
				}
			}
		}
	}
	
	protected void createHttpClientAgent(String url, Map<String, Object> headers) {
		agent = new HttpClient();

		HttpHeader hh = new HttpHeader();
		if (headers == null) {
			hh.setDefaultAgentPC();
		}
		else {
			hh.putAll(headers);
		}

		agent.getRequest().setHeader(hh);
	}
	
	protected void createITextRenderer() throws Exception {
		renderer = new ITextRenderer();

		if (Strings.isNotEmpty(fonts)) {
			File fontdir;
			if (fonts.startsWith("web://")) {
				fontdir = new File(servlet.getRealPath(fonts.substring(5)));
			}
			else {
				fontdir = new File(fonts);
			}

			if (fontdir.isDirectory()) {
				File[] files = fontdir.listFiles(new FilenameFilter() {
					public boolean accept(File dir, String name) {
						String lower = name.toLowerCase();
						return lower.endsWith(".otf") || lower.endsWith(".ttf") || lower.endsWith(".ttc");
					}
				});

				if (Arrays.isNotEmpty(files)) {
					for (File file : files) {
						renderer.getFontResolver().addFont(
								file.getAbsolutePath(), BaseFont.IDENTITY_H,
								BaseFont.EMBEDDED);
					}
				}
			}
		}


		ResourceLoaderUserAgent callback = new ResourceLoaderUserAgent(
			renderer.getOutputDevice(), renderer.getSharedContext());
		renderer.getSharedContext().setUserAgentCallback(callback);
	}
	
	protected Document getResponseDocument(String charset) throws Exception {
		String html = agent.getResponse().getContentText(charset);

		Tidy tidy = new Tidy();
		tidy.setShowErrors(0);
		tidy.setShowWarnings(false);
		tidy.setQuiet(true);
		tidy.setXHTML(true);

		Document doc = tidy.parseDOM(new StringReader(html), null);
		
		return doc;
	}

	@Override
	public void process(OutputStream os) throws Exception {
		createHttpClientAgent(url, headers);
		createITextRenderer();
		
		agent.getRequest().setUrl(url);
		agent.doGet();

		Document doc = getResponseDocument(charset);

		renderer.setDocument(doc, url);
		renderer.layout();
		renderer.createPDF(os);
		renderer.finishPDF();
	}
}
