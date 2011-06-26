package test;

import jo4neo.impl.FieldValueMap;

import org.junit.Test;


public class TestFieldValueMap {
	
	@Test
	public void basic() {
		
		Person p = new Person();
		p.setAge(27);
		p.setFirstName("taylor");
		p.setLastName("cowan");

		
		FieldValueMap<?> map = new FieldValueMap<Object>(p, null);
		
		System.out.println(map.getField(p.age).getFieldname());
		System.out.println(map.getField(p.address).getFieldname());
		System.out.println(map.getField(p.friend).getFieldname());
		System.out.println(map.getField(p.firstName).getFieldname());
		System.out.println(map.getField(p.lastName).getFieldname());
		
		FunkyWinkerBean f = new FunkyWinkerBean(); 
		f.setI(1);
		f.setJ(1);
		f.setX(3);
		
		map = new FieldValueMap<Object>(f, null);
		System.out.println(map.getField(f.i).getFieldname());
		System.out.println(map.getField(f.j).getFieldname());
		System.out.println(map.getField(f.x).getFieldname());
		System.out.println(map.getField(f.good).getFieldname());
		System.out.println(map.getField(f.bad).getFieldname());
		
	}
	

}
