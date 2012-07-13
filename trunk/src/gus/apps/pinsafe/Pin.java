package gus.apps.pinsafe;

public class Pin {

	public int id = 0;
	public int value = 0;
	public String label = "";
	
	public Pin(String label, int pin){
		this.value = pin;
		this.label = label;
	}
	
	public Pin(){
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return label.toString() + " " + String.format("%04d", value);
	}
}
