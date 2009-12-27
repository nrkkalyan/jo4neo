package example;

import java.util.Collection;

import jo4neo.neo;

public class Day extends Base {
	
	@neo(inverse="parent") public Collection<Hour> hours;
	@neo public Month parent;
	@neo public int value;

}
