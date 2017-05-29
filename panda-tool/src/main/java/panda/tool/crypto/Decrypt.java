package panda.tool.crypto;

import panda.args.Argument;
import panda.args.Option;
import panda.lang.crypto.Encrypts;
import panda.tool.AbstractCommandTool;


/**
 */
public class Decrypt extends AbstractCommandTool {
	/**
	 * main
	 * @param args arugments
	 */
	public static void main(String args[]) {
		new Decrypt().execute(args);
	}

	//---------------------------------------------------------------------------------------
	// properties
	//---------------------------------------------------------------------------------------
	private String key = Encrypts.DEFAULT_KEY;
	private String transform = Encrypts.DEFAULT_CIPHER;
	private String[] args;
	
	/**
	 * @param key the key to set
	 */
	@Option(opt='k', option="key", arg="KEY", usage="Encrypt key")
	public void setKey(String key) {
		this.key = key;
	}

	/**
	 * @param transform the transform to set
	 */
	@Option(opt='t', option="transform", arg="ALGO", usage="Encrypt transform (default is AES)")
	public void setTransform(String transform) {
		this.transform = transform;
	}

	/**
	 * @param args the args to set
	 */
	@Argument(name="string", required=true, usage="The string to decrypt")
	public void setArgs(String[] args) {
		this.args = args;
	}

	/**
	 * execute
	 */
	public void execute() {
		for (String s : args) {
			System.out.println(s + " -> " + Encrypts.decrypt(s, key, transform));
		}
	}
}
