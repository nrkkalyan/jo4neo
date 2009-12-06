package thewebsemantic;

import java.util.Comparator;

public  class NeoComparator implements Comparator<Object> {

	public int compare(Object o1, Object o2) {
		TypeWrapper t1 = TypeWrapperFactory.$(o1);
		TypeWrapper t2 = TypeWrapperFactory.$(o2);		
		return compare(t1.id(o1),  t2.id(o2));
	}
	
	private int compare(Nodeid n1, Nodeid n2) {
		return (n1.id() == n2.id()) ?  0:1;
	}

}
