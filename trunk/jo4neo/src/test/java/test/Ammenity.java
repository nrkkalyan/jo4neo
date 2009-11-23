package test;

import thewebsemantic.Neo;
import thewebsemantic.Graph;

public class Ammenity {
	transient Neo neo;
	
	@Graph String name;
	@Graph String code;
	
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
}
