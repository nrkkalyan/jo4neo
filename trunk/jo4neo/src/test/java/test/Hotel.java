package test;

import java.util.Collection;

import thewebsemantic.Nodeid;
import thewebsemantic.neo;

public class Hotel {

	public static final String HOTEL_NAME_IDX = "hotelname";
	transient Nodeid neo;
	
	@neo Collection<Ammenity> ammenities;
	@neo(index=HOTEL_NAME_IDX) String name;
	
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
