package panda.lang.chardet ;

public abstract class nsVerifier {

	static final byte eStart = (byte)0;
	static final byte eError = (byte)1;
	static final byte eItsMe = (byte)2;
	static final int eidxSft4bits = 3;
	static final int eSftMsk4bits = 7;
	static final int eBitSft4bits = 2;
	static final int eUnitMsk4bits = 0x0000000F;

	nsVerifier() {
	}

	public abstract String charset();

	public abstract int stFactor();

	public abstract int[] cclass();

	public abstract int[] states();

	public abstract boolean isUCS2();

	public static byte getNextState(nsVerifier v, int b, byte s) {
         return (byte) ( 0xFF & 
	     (((v.states()[((
		   (s*v.stFactor()+(((v.cclass()[((b&0xFF)>>eidxSft4bits)]) 
		   >> ((b & eSftMsk4bits) << eBitSft4bits)) 
		   & eUnitMsk4bits ))&0xFF)
		>> eidxSft4bits) ]) >> (((
		   (s*v.stFactor()+(((v.cclass()[((b&0xFF)>>eidxSft4bits)]) 
		   >> ((b & eSftMsk4bits) << eBitSft4bits)) 
		   & eUnitMsk4bits ))&0xFF) 
		& eSftMsk4bits) << eBitSft4bits)) & eUnitMsk4bits )
	 ) ;

     }


}
