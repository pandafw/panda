package panda.util.chardet ;

public abstract class nsEUCStatistics {

     public abstract float[] mFirstByteFreq() ;
     public abstract float   mFirstByteStdDev();
     public abstract float   mFirstByteMean();
     public abstract float   mFirstByteWeight();
     public abstract float[] mSecondByteFreq();
     public abstract float   mSecondByteStdDev();
     public abstract float   mSecondByteMean();
     public abstract float   mSecondByteWeight();

     public nsEUCStatistics() {
     }

}
