package test;

import thewebsemantic.Graph;
import thewebsemantic.Neo;

public class City {
	transient Neo neo;
	@Graph String name;
	@Graph State state;
	@Graph double lat;
	@Graph double lon;
}
