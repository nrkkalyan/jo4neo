package test;

import java.util.ArrayList;
import java.util.Collection;

import thewebsemantic.Timeline;
import thewebsemantic.neo;
import thewebsemantic.Nodeid;

@Timeline
public class State {
	transient Nodeid neo;
	public static final String STATE_CODE_IDX = "statecode";
	
	@neo(index=STATE_CODE_IDX) String code;
	@neo String name;	
	@neo Collection<City> cities = new ArrayList<City>();
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public Collection<City> getCities() {
		return cities;
	}
	public void setCities(Collection<City> cities) {
		this.cities = cities;
	}
}
