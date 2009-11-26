package test;

import java.util.ArrayList;
import java.util.Collection;

import thewebsemantic.Graph;
import thewebsemantic.Neo;

public class State {
	transient Neo neo;
	
	@Graph(index="statecode") String code;
	@Graph String name;	
	@Graph Collection<City> cities = new ArrayList<City>();
	
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
