package test;

import java.util.Collection;

import thewebsemantic.Neo;
import thewebsemantic.Graph;

public class Hotel {

	transient Neo neo;
	
	@Graph Collection<Ammenity> ammenities;
	@Graph(index="hotelname") String name;
	
	public Collection<Ammenity> getAmmenities() {
		return ammenities;
	}
	public void setAmmenities(Collection<Ammenity> ammenities) {
		this.ammenities = ammenities;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	
}
