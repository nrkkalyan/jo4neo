package test;

import thewebsemantic.Graph;
import thewebsemantic.Neo;

public class City {
	transient Neo neo;
	@Graph(index="cityname") String name;
	@Graph State state;
	@Graph double lat;
	@Graph double lon;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public State getState() {
		return state;
	}
	public void setState(State state) {
		this.state = state;
	}
	public double getLat() {
		return lat;
	}
	public void setLat(double lat) {
		this.lat = lat;
	}
	public double getLon() {
		return lon;
	}
	public void setLon(double lon) {
		this.lon = lon;
	}
	
}
