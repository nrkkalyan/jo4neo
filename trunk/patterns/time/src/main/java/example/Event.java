package example;

import java.util.Date;

import jo4neo.neo;

public class Event extends Base {
	@neo public Date time;
	@neo public String title;
	@neo public Hour hour;
}
