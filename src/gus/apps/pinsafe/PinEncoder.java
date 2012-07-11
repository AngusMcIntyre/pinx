package gus.apps.pinsafe;

public class PinEncoder {
	public static int EncodePin(int masterkey, int pin){
		int tmp = pin + masterkey;
		
		if(tmp > 9999){
			return tmp - 9999;
		}
		
		return tmp;
	}
	
	public static int DecodePin(int masterkey, int pin){
		int tmp = pin - masterkey;
		
		if(tmp < 0){
			return 9999 + tmp;
		}
		
		return tmp;
	}
}
