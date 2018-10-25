package panda.tool.crypto;

import java.security.Key;

import panda.args.Argument;
import panda.args.Option;
import panda.tool.AbstractCommandTool;
import panda.util.crypto.Algorithms;
import panda.util.crypto.Cryptor;
import panda.util.crypto.Keys;


/**
 */
public class Encrypt extends AbstractCommandTool {
	/**
	 * main
	 * @param args arugments
	 */
	public static void main(String args[]) {
		new Encrypt().execute(args);
	}

	//---------------------------------------------------------------------------------------
	// properties
	//---------------------------------------------------------------------------------------
	private String algorithm = Algorithms.AES;
	private String key = "== Panda Java ==";
	private boolean decrypt = false;
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
	@Option(opt='a', option="algorithm", arg="ALGO", usage="Encrypt algorithm (default is AES)")
	public void setAlgorithm(String algorithm) {
		this.algorithm = algorithm;
	}

	/**
	 * @param decrypt the decrypt to set
	 */
	@Option(opt='d', option="dec", usage="Decrypt")
	public void setDecrypt(boolean decrypt) {
		this.decrypt = decrypt;
	}

	/**
	 * @param args the args to set
	 */
	@Argument(name="string", required=true, usage="The string to encrypt or decrypt")
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
			if (decrypt) {
				System.out.println(s + " -> " + c.decrypt(s));
			}
			else {
				System.out.println(s + " -> " + c.encrypt(s));
			}
		}
	}
}
