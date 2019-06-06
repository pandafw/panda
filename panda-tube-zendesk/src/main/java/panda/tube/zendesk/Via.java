package panda.tube.zendesk;

public class Via {
	/** This tells you how the ticket or event was created. Examples: "web", "mobile", "rule", "system" */
	public String channel;

	/** For some channels a source object gives more information about how or why the ticket or event was created */
	public Object source;
}
