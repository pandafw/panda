package panda.ex.zendesk;

import java.util.List;

public class TicketListResult extends ListResult {
	private List<Ticket> tickets;

	/**
	 * @return the tickets
	 */
	public List<Ticket> getTickets() {
		return tickets;
	}

	/**
	 * @param tickets the tickets to set
	 */
	public void setTickets(List<Ticket> tickets) {
		this.tickets = tickets;
	}
	
	
}
