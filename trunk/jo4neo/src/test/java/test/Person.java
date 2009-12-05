package test;

import thewebsemantic.Embed;
import thewebsemantic.Nodeid;
import thewebsemantic.neo;

public class Person {
	
	transient Nodeid neo;
	
	@neo int age;
	@neo String firstName;
	@neo String lastName;
	@Embed Address address;
	@neo("FRIEND") 
	Person friend;
	
	
	public Person getFriend() {
		return friend;
	}

	public void setFriend(Person friend) {
		this.friend = friend;
	}

	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

}
