package test;

import java.util.Collection;

import thewebsemantic.Nodeid;
import thewebsemantic.neo;

public class Hotel {

	transient Nodeid neo;
	
	@neo Collection<Ammenity> ammenities;
	@neo(index="hotelname") String name;
	
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
