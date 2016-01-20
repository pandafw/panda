package panda.net.http;

import java.util.HashMap;
import java.util.Map;

/**
 * @author yf.frank.wang@gmail.com
 */
public class HttpStatus {
	/*
	 * Server status codes; see RFC 2068.
	 */
	/**
	 * Status code (100) indicating the client can continue.
	 */
	public static final int SC_CONTINUE = 100;

	/**
	 * Status code (101) indicating the server is switching protocols according to Upgrade header.
	 */
	public static final int SC_SWITCHING_PROTOCOLS = 101;

	/**
	 * Status code (200) indicating the request succeeded normally.
	 */
	public static final int SC_OK = 200;

	/**
	 * Status code (201) indicating the request succeeded and created a new resource on the server.
	 */
	public static final int SC_CREATED = 201;

	/**
	 * Status code (202) indicating that a request was accepted for processing, but was not
	 * completed.
	 */
	public static final int SC_ACCEPTED = 202;

	/**
	 * Status code (203) indicating that the meta information presented by the client did not
	 * originate from the server.
	 */
	public static final int SC_NON_AUTHORITATIVE_INFORMATION = 203;

	/**
	 * Status code (204) indicating that the request succeeded but that there was no new information
	 * to return.
	 */
	public static final int SC_NO_CONTENT = 204;

	/**
	 * Status code (205) indicating that the agent <em>SHOULD</em> reset the document view which
	 * caused the request to be sent.
	 */
	public static final int SC_RESET_CONTENT = 205;

	/**
	 * Status code (206) indicating that the server has fulfilled the partial GET request for the
	 * resource.
	 */
	public static final int SC_PARTIAL_CONTENT = 206;

	/**
	 * Status code (300) indicating that the requested resource corresponds to any one of a set of
	 * representations, each with its own specific location.
	 */
	public static final int SC_MULTIPLE_CHOICES = 300;

	/**
	 * Status code (301) indicating that the resource has permanently moved to a new location, and
	 * that future references should use a new URI with their requests.
	 */
	public static final int SC_MOVED_PERMANENTLY = 301;

	/**
	 * Status code (302) indicating that the resource has temporarily moved to another location, but
	 * that future references should still use the original URI to access the resource. This
	 * definition is being retained for backwards compatibility. SC_FOUND is now the preferred
	 * definition.
	 */
	public static final int SC_MOVED_TEMPORARILY = 302;

	/**
	 * Status code (302) indicating that the resource reside temporarily under a different URI.
	 * Since the redirection might be altered on occasion, the client should continue to use the
	 * Request-URI for future requests.(HTTP/1.1) To represent the status code (302), it is
	 * recommended to use this variable.
	 */
	public static final int SC_FOUND = 302;

	/**
	 * Status code (303) indicating that the response to the request can be found under a different
	 * URI.
	 */
	public static final int SC_SEE_OTHER = 303;

	/**
	 * Status code (304) indicating that a conditional GET operation found that the resource was
	 * available and not modified.
	 */
	public static final int SC_NOT_MODIFIED = 304;

	/**
	 * Status code (305) indicating that the requested resource <em>MUST</em> be accessed through
	 * the proxy given by the <code><em>Location</em></code> field.
	 */
	public static final int SC_USE_PROXY = 305;

	/**
	 * Status code (307) indicating that the requested resource resides temporarily under a
	 * different URI. The temporary URI <em>SHOULD</em> be given by the
	 * <code><em>Location</em></code> field in the response.
	 */
	public static final int SC_TEMPORARY_REDIRECT = 307;

	/**
	 * Status code (400) indicating the request sent by the client was syntactically incorrect.
	 */
	public static final int SC_BAD_REQUEST = 400;

	/**
	 * Status code (401) indicating that the request requires HTTP authentication.
	 */
	public static final int SC_UNAUTHORIZED = 401;

	/**
	 * Status code (402) reserved for future use.
	 */
	public static final int SC_PAYMENT_REQUIRED = 402;

	/**
	 * Status code (403) indicating the server understood the request but refused to fulfill it.
	 */
	public static final int SC_FORBIDDEN = 403;

	/**
	 * Status code (404) indicating that the requested resource is not available.
	 */
	public static final int SC_NOT_FOUND = 404;

	/**
	 * Status code (405) indicating that the method specified in the
	 * <code><em>Request-Line</em></code> is not allowed for the resource identified by the
	 * <code><em>Request-URI</em></code>.
	 */
	public static final int SC_METHOD_NOT_ALLOWED = 405;

	/**
	 * Status code (406) indicating that the resource identified by the request is only capable of
	 * generating response entities which have content characteristics not acceptable according to
	 * the accept headers sent in the request.
	 */
	public static final int SC_NOT_ACCEPTABLE = 406;

	/**
	 * Status code (407) indicating that the client <em>MUST</em> first authenticate itself with the
	 * proxy.
	 */
	public static final int SC_PROXY_AUTHENTICATION_REQUIRED = 407;

	/**
	 * Status code (408) indicating that the client did not produce a request within the time that
	 * the server was prepared to wait.
	 */
	public static final int SC_REQUEST_TIMEOUT = 408;

	/**
	 * Status code (409) indicating that the request could not be completed due to a conflict with
	 * the current state of the resource.
	 */
	public static final int SC_CONFLICT = 409;

	/**
	 * Status code (410) indicating that the resource is no longer available at the server and no
	 * forwarding address is known. This condition <em>SHOULD</em> be considered permanent.
	 */
	public static final int SC_GONE = 410;

	/**
	 * Status code (411) indicating that the request cannot be handled without a defined
	 * <code><em>Content-Length</em></code>.
	 */
	public static final int SC_LENGTH_REQUIRED = 411;

	/**
	 * Status code (412) indicating that the precondition given in one or more of the request-header
	 * fields evaluated to false when it was tested on the server.
	 */
	public static final int SC_PRECONDITION_FAILED = 412;

	/**
	 * Status code (413) indicating that the server is refusing to process the request because the
	 * request entity is larger than the server is willing or able to process.
	 */
	public static final int SC_REQUEST_ENTITY_TOO_LARGE = 413;

	/**
	 * Status code (414) indicating that the server is refusing to service the request because the
	 * <code><em>Request-URI</em></code> is longer than the server is willing to interpret.
	 */
	public static final int SC_REQUEST_URI_TOO_LONG = 414;

	/**
	 * Status code (415) indicating that the server is refusing to service the request because the
	 * entity of the request is in a format not supported by the requested resource for the
	 * requested method.
	 */
	public static final int SC_UNSUPPORTED_MEDIA_TYPE = 415;

	/**
	 * Status code (416) indicating that the server cannot serve the requested byte range.
	 */
	public static final int SC_REQUESTED_RANGE_NOT_SATISFIABLE = 416;

	/**
	 * Status code (417) indicating that the server could not meet the expectation given in the
	 * Expect request header.
	 */
	public static final int SC_EXPECTATION_FAILED = 417;

	/**
	 * Status code (500) indicating an error inside the HTTP server which prevented it from
	 * fulfilling the request.
	 */
	public static final int SC_INTERNAL_SERVER_ERROR = 500;

	/**
	 * Status code (501) indicating the HTTP server does not support the functionality needed to
	 * fulfill the request.
	 */
	public static final int SC_NOT_IMPLEMENTED = 501;

	/**
	 * Status code (502) indicating that the HTTP server received an invalid response from a server
	 * it consulted when acting as a proxy or gateway.
	 */
	public static final int SC_BAD_GATEWAY = 502;

	/**
	 * Status code (503) indicating that the HTTP server is temporarily overloaded, and unable to
	 * handle the request.
	 */
	public static final int SC_SERVICE_UNAVAILABLE = 503;

	/**
	 * Status code (504) indicating that the server did not receive a timely response from the
	 * upstream server while acting as a gateway or proxy.
	 */
	public static final int SC_GATEWAY_TIMEOUT = 504;

	/**
	 * Status code (505) indicating that the server does not support or refuses to support the HTTP
	 * protocol version that was used in the request message.
	 */
	public static final int SC_HTTP_VERSION_NOT_SUPPORTED = 505;

	// WebDAV Server-specific status codes
	/**
	 * Status code (424) incidating that the method could not be performed on the resource, because the requested action depended on another action which failed.
	 */
	public static final int SC_FAILED_DEPENDENCY = 424;

	/**
	 * Status code (507) indicating that the resource does not have sufficient space to record the state of the resource after the execution of this method.
	 */
	public static final int SC_INSUFFICIENT_SPACE_ON_RESOURCE = 507;

	/**
	 * Status code (423) indicating the destination resource of a method is locked, and either the request did not contain a valid Lock-Info header, or the Lock-Info header identifies a lock held by another principal.
	 */
	public static final int SC_LOCKED = 423;
	
	/**
	 * Status code (207) indicating that the response requires providing status for multiple independent operations.
	 */
	public static final int SC_MULTI_STATUS = 207;

	/**
	 * The 102 (Processing) status code is an interim response used to inform the client that the server has accepted the complete request, but has not yet completed it.
	 */
	public static final int SC_PROCESSING = 102;

	/**
	 * The 422 (Unprocessable Entity) status code means the server understands the content type of the request entity (hence a 415(Unsupported Media Type) status code is inappropriate), and the syntax of the request entity is correct (thus a 400 (Bad Request) status code is inappropriate) but was unable to process the contained instructions.
	 */
	public static final int SC_UNPROCESSABLE_ENTITY = 422;

	/**
	 * Status code (420) indicating the method was not executed on a particular resource within its scope because some part of the method's execution failed causing the entire method to be aborted.
	 */
	public static final int SC_METHOD_FAILURE = 420;
	
	public static final int SC_INSUFFICIENT_STORAGE = 507;

	/** Set up status code to "reason phrase" map. */
	private static Map<Integer, String> statusReasons = new HashMap<Integer, String>();

	static {
		// HTTP 1.0 Server status codes -- see RFC 1945
		setStatusReason(SC_OK, "OK");
		setStatusReason(SC_CREATED, "Created");
		setStatusReason(SC_ACCEPTED, "Accepted");
		setStatusReason(SC_NO_CONTENT, "No Content");
		setStatusReason(SC_MOVED_PERMANENTLY, "Moved Permanently");
		setStatusReason(SC_MOVED_TEMPORARILY, "Moved Temporarily");
//		setStatusReason(SC_FOUND, "Found");
		setStatusReason(SC_NOT_MODIFIED, "Not Modified");
		setStatusReason(SC_BAD_REQUEST, "Bad Request");
		setStatusReason(SC_UNAUTHORIZED, "Unauthorized");
		setStatusReason(SC_FORBIDDEN, "Forbidden");
		setStatusReason(SC_NOT_FOUND, "Not Found");
		setStatusReason(SC_INTERNAL_SERVER_ERROR, "Internal Server Error");
		setStatusReason(SC_NOT_IMPLEMENTED, "Not Implemented");
		setStatusReason(SC_BAD_GATEWAY, "Bad Gateway");
		setStatusReason(SC_SERVICE_UNAVAILABLE, "Service Unavailable");

		// HTTP 1.1 Server status codes -- see RFC 2048
		setStatusReason(SC_CONTINUE, "Continue");
		setStatusReason(SC_TEMPORARY_REDIRECT, "Temporary Redirect");
		setStatusReason(SC_METHOD_NOT_ALLOWED, "Method Not Allowed");
		setStatusReason(SC_CONFLICT, "Conflict");
		setStatusReason(SC_PRECONDITION_FAILED, "Precondition Failed");
		// setStatusReason(SC_REQUEST_TOO_LONG,
		// "Request Too Long");
		setStatusReason(SC_REQUEST_URI_TOO_LONG, "Request-URI Too Long");
		setStatusReason(SC_UNSUPPORTED_MEDIA_TYPE, "Unsupported Media Type");
		setStatusReason(SC_MULTIPLE_CHOICES, "Multiple Choices");
		setStatusReason(SC_SEE_OTHER, "See Other");
		setStatusReason(SC_USE_PROXY, "Use Proxy");
		setStatusReason(SC_PAYMENT_REQUIRED, "Payment Required");
		setStatusReason(SC_NOT_ACCEPTABLE, "Not Acceptable");
		setStatusReason(SC_PROXY_AUTHENTICATION_REQUIRED, "Proxy Authentication Required");
		setStatusReason(SC_REQUEST_TIMEOUT, "Request Timeout");

		setStatusReason(SC_SWITCHING_PROTOCOLS, "Switching Protocols");
		setStatusReason(SC_NON_AUTHORITATIVE_INFORMATION, "Non Authoritative Information");
		setStatusReason(SC_RESET_CONTENT, "Reset Content");
		setStatusReason(SC_PARTIAL_CONTENT, "Partial Content");
		setStatusReason(SC_GATEWAY_TIMEOUT, "Gateway Timeout");
		setStatusReason(SC_HTTP_VERSION_NOT_SUPPORTED, "Http Version Not Supported");
		setStatusReason(SC_GONE, "Gone");
		setStatusReason(SC_LENGTH_REQUIRED, "Length Required");
		setStatusReason(SC_REQUESTED_RANGE_NOT_SATISFIABLE, "Requested Range Not Satisfiable");
		setStatusReason(SC_EXPECTATION_FAILED, "Expectation Failed");

		 // WebDAV Server-specific status codes
		setStatusReason(SC_PROCESSING, "Processing");
		setStatusReason(SC_MULTI_STATUS, "Multi-Status");
		setStatusReason(SC_UNPROCESSABLE_ENTITY, "Unprocessable Entity");
		setStatusReason(SC_INSUFFICIENT_SPACE_ON_RESOURCE, "Insufficient Space On Resource");
		setStatusReason(SC_METHOD_FAILURE, "Method Failure");
		setStatusReason(SC_LOCKED, "Locked");
		setStatusReason(SC_INSUFFICIENT_STORAGE, "Insufficient Storage");
		setStatusReason(SC_FAILED_DEPENDENCY, "Failed Dependency");
	}

	private static void setStatusReason(int status, String reason) {
		statusReasons.put(status, reason);
	}

	public static String getStatusReason(int status) {
		String reason = statusReasons.get(status);
		return reason == null ? "UNKNOWN" : reason;
	}
	
	//--------------------------------------------
	private int status;
	private String reason;
	
	public HttpStatus(int code) {
		setStatus(code);
	}

	/**
	 * @param status the status code to set
	 */
	public void setStatus(int status) {
		this.status = status;
		this.reason = getStatusReason(status);
	}

	/**
	 * @param status the status code to set
	 * @param reason the status reason to set
	 */
	public void setStatus(int status, String reason) {
		this.status = status;
		this.reason = reason;
	}

	/**
	 * @return the status code
	 */
	public int getStatus() {
		return status;
	}

	/**
	 * @return the status reason
	 */
	public String getReason() {
		return reason;
	}
	
	/**
	 * @return status string
	 */
	public String toString() {
		return status + ": " + reason;
	}
}
