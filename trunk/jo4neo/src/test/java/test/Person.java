package test;

import thewebsemantic.Embed;
import thewebsemantic.Neo;
import thewebsemantic.Graph;

public class Person {
	
	transient Neo neo;
	
	@Graph int age;
	@Graph String firstName;
	@Graph String lastName;
	@Embed Address address;
	@Graph("FRIEND") 
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
