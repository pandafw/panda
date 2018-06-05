package panda.tool.crypto;

import java.security.Key;

import panda.args.Argument;
import panda.args.Option;
import panda.tool.AbstractCommandTool;
import panda.util.crypto.Ciphers;
import panda.util.crypto.Cryptor;
import panda.util.crypto.Keys;


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
	private String algorithm = Ciphers.AES;
	private String key = "== Panda Java ==";
	private String[] args;
	
	/**
	 * @param key the key to set
	 */
	@Option(opt='k', option="key", arg="KEY", usage="Encrypt key")
	public void setKey(String key) {
		this.key = key;
	}

	/**
	 * @param algorithm the algorithm to set
	 */
	@Option(opt='t', option="algorithm", arg="ALGO", usage="Encrypt algorithm (default is AES)")
	public void setAlgorithm(String algorithm) {
		this.algorithm = algorithm;
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
		Key key = Keys.secretKeySpec(this.key, algorithm);
		Cryptor c = new Cryptor(algorithm, key, key);
		for (String s : args) {
			System.out.println(s + " -> " + c.decrypt(s));
		}
	}
}
