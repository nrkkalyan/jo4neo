package example;

import jo4neo.neo;

public class Hour extends Base {

	@neo public int value;
	@neo("hasDay") public Day parent;
}
