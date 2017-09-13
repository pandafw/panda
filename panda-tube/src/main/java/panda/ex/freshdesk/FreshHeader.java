package panda.ex.freshdesk;

public class FreshHeader {
	/** FreshDesk api version */
	public static final String X_FRESHDESK_API_VERSION = "X-Freshdesk-Api-Version";

	/** Total number of API calls allowed per hour.*/
	public static final String X_RATELIMIT_TOTAL = "X-RateLimit-Total";
	
	/** The number of requests remaining in the current rate limit window.*/
	public static final String X_RATELIMIT_REMAING = "X-RateLimit-Remaining";

	/** The number of API calls consumed by the current request. Most API requests consume one call, however, including additional information in the response will consume more calls.*/
	public static final String X_RATELIMIE_USED_CURRENTREQUEST = "X-RateLimit-Used-CurrentRequest";
	
	/** The number in seconds that you will have to wait to fire your next API request. This header will be returned only when the rate limit has been reached.*/
	public static final String RETRY_AFTER = "Retry-After";
}
