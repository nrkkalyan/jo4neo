package example;

import java.util.Collection;

import jo4neo.neo;

public class Month extends Base {
	@neo(inverse="parent") public Collection<Day> days;
	@neo public Year parent;
	@neo public int value;
	
	public Month() {}
	
	public Month(int month, int days) {
		value = month;
	}



}
