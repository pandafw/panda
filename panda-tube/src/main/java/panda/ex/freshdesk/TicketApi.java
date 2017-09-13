package panda.ex.freshdesk;

import java.io.IOException;

import panda.bind.json.Jsons;
import panda.io.MimeTypes;
import panda.net.http.HttpClient;
import panda.net.http.HttpResponse;
import panda.net.http.HttpStatus;

public class TicketApi {
	private Authentication authentication;

	public static final String ENDPOINT = "/api/v2/tickets";
	
	public TicketApi() {
	}

	/**
	 * @param authentication
	 */
	public TicketApi(Authentication authentication) {
		this.authentication = authentication;
	}

	/**
	 * @return the authentication
	 */
	public Authentication getAuthentication() {
		return authentication;
	}

	/**
	 * @param authentication the authentication to set
	 */
	public void setAuthentication(Authentication authentication) {
		this.authentication = authentication;
	}
	
	public Ticket createTicket(Ticket ticket) throws FreshException {
		String b = Jsons.toJson(ticket);
		
		HttpClient hc = new HttpClient();
		
		authentication.authenticateRequest(hc, ENDPOINT);
		hc.getRequest().setContentType(MimeTypes.APP_JSON);
		hc.getRequest().setBody(b);

		try {
			HttpResponse hr = hc.doPost();
			if (hr.getStatusCode() == HttpStatus.SC_CREATED) {
				Ticket t = Jsons.fromJson(hr.getContentText(), Ticket.class);
				return t;
			}
			
			if (MimeTypes.APP_JSON.equalsIgnoreCase(hr.getContentType())) {
				ErrorResult er = Jsons.fromJson(hr.getContentText(), ErrorResult.class);
				throw new FreshException("Failed to create ticket: " + er.getDescription(), er);
			}
			
			throw new FreshException("Failed to create ticket: " + hr.getContentText());
		}
		catch (FreshException e) {
			throw e;
		}
		catch (IOException e) {
			throw new FreshException("Failed to create ticket: " + e.getMessage(), e);
		}
	}
}
