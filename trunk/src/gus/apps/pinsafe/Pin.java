package gus.apps.pinsafe;

public class Pin {

	public int Id = 0;
	public int Pin = 0;
	public String Label = "";
	
	public Pin(String label, int pin){
		this.Pin = pin;
		this.Label = label;
	}
	
	public Pin(){
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return Label.toString() + " " + String.format("%04d", Pin);
	}
}
