package panda.util.crypto;

/**
 * https://docs.oracle.com/javase/7/docs/technotes/guides/security/StandardNames.html#Cipher
 */
public class Ciphers {
	//------------------------------------------------------------------------------
	public static final String AES = "AES";
	public static final String Blowfish = "Blowfish";
	public static final String DES = "DES";
	public static final String DESede = "DESede";
	public static final String RC2 = "RC2";
	public static final String RC4 = "RC4";
	
	//------------------------------------------------------------------------------
	/** AES/CBC/NoPadding (128) */
	public static final String AES_CBC_NOPADDING                      =  "AES/CBC/NoPadding";
	/** AES/CBC/PKCS5Padding (128) */
	public static final String AES_CBC_PKCS5PADDING                   = "AES/CBC/PKCS5Padding";
	/** AES/ECB/NoPadding (128) */
	public static final String AES_ECB_NOPADDING                      = "AES/ECB/NoPadding";
	/** AES/ECB/PKCS5Padding (128) */
	public static final String AES_ECB_PKCS5PADDING                   = "AES/ECB/PKCS5Padding";
	/** DES/CBC/NoPadding (56) */
	public static final String DES_CBC_NOPADDING                      = "DES/CBC/NoPadding";
	/** DES/CBC/PKCS5Padding (56) */
	public static final String DES_CBC_PKCS5PADDING                   = "DES/CBC/PKCS5Padding";
	/** DES/ECB/NoPadding (56) */
	public static final String DES_ECB_NOPADDING                      = "DES/ECB/NoPadding";
	/** DES/ECB/PKCS5Padding (56) */
	public static final String DES_ECB_PKCS5PADDING                   = "DES/ECB/PKCS5Padding";
	/** DESede/CBC/NoPadding (168) */
	public static final String DESEDE_CBC_NOPADDING                   = "DESede/CBC/NoPadding";
	/** DESede/CBC/PKCS5Padding (168) */
	public static final String DESEDE_CBC_PKCS5PADDING                = "DESede/CBC/PKCS5Padding";
	/** DESede/ECB/NoPadding (168) */
	public static final String DESEDE_ECB_NOPADDING                   = "DESede/ECB/NoPadding";
	/** DESede/ECB/PKCS5Padding (168) */
	public static final String DESEDE_ECB_PKCS5PADDING                = "DESede/ECB/PKCS5Padding";
	/** RSA/ECB/PKCS1Padding (1024, 2048) */
	public static final String RSA_ECB_PKCS1PADDING                   = "RSA/ECB/PKCS1Padding";
	/** RSA/ECB/OAEPWithSHA-1AndMGF1Padding (1024, 2048) */
	public static final String RSA_ECB_OAEPWITHSHA1_AND_MGF1PADDING    = "RSA/ECB/OAEPWithSHA-1AndMGF1Padding";
	/** RSA/ECB/OAEPWithSHA-256AndMGF1Padding (1024, 2048) */
	public static final String RSA_ECB_OAEPWITHSHA256_AND_MGF1PADDING  = "RSA/ECB/OAEPWithSHA-256AndMGF1Padding";
}
