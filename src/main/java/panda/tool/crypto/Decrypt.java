package panda.tool.crypto;

import panda.lang.Encrypts;


/**
 */
public class Decrypt {
	/**
	 * @param args arguments
	 */
	public static void main(String[] args) {
		if (args.length < 1) {
			System.out.println("Decrypt <string>");
		}
		
		Decrypt c = new Decrypt();
		for (String a : args) {
			c.setText(a);
			try {
				c.execute();
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Constructor
	 */
	public Decrypt() {
	}

	//---------------------------------------------------------------------------------------
	// properties
	//---------------------------------------------------------------------------------------
	protected String text;

	/**
	 * @return the text
	 */
	public String getText() {
		return text;
	}

	/**
	 * @param text the text to set
	 */
	public void setText(String text) {
		this.text = text;
	}

	/**
	 * execute
	 * @throws Exception if an error occurs
	 */
	public void execute() throws Exception {
		System.out.println(Encrypts.decrypt(text));
	}
}
