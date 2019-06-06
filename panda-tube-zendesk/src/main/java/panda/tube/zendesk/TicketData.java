package panda.tube.zendesk;

import panda.bind.json.Jsons;

public class TicketData {
	private Ticket ticket;

	private Audit audit;
	
	/**
	 * 
	 */
	public TicketData() {
	}

	/**
	 * @param ticket
	 */
	public TicketData(Ticket ticket) {
		this.ticket = ticket;
	}

	/**
	 * @return the ticket
	 */
	public Ticket getTicket() {
		return ticket;
	}

	/**
	 * @param ticket the ticket to set
	 */
	public void setTicket(Ticket ticket) {
		this.ticket = ticket;
	}

	/**
	 * @return the audit
	 */
	public Audit getAudit() {
		return audit;
	}

	/**
	 * @param audit the audit to set
	 */
	public void setAudit(Audit audit) {
		this.audit = audit;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return Jsons.toJson(this, true);
	}
}
