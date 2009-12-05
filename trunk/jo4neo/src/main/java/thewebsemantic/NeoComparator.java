package thewebsemantic;

import java.util.Comparator;

public class NeoComparator implements Comparator<Object> {

	public int compare(Object o1, Object o2) {
		TypeWrapper t1 = TypeWrapperFactory.wrap(o1);
		TypeWrapper t2 = TypeWrapperFactory.wrap(o2);
		
		Nodeid n1 = t1.id(o1);
		Nodeid n2 = t2.id(o2);
		
		if (n1.id() == n2.id())
			return 0;
		else
			return 1;
		

	}

}
