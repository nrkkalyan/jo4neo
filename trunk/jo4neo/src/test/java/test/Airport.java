package test;

import java.util.Collection;

import thewebsemantic.Graph;
import thewebsemantic.Neo;

public class Airport {
	transient Neo neo;

	@Graph(index="airportcode") String code;
	@Graph String name;
	@Graph Collection<City> citiesServed;
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
	public Collection<City> getCitiesServed() {
		return citiesServed;
	}
	public void setCitiesServed(Collection<City> citiesServed) {
		this.citiesServed = citiesServed;
	}
	
	
}
