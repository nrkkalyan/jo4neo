package thewebsemantic;

import java.util.Collection;

public class IndexQuery<T> {

	PersistenceManager pm;
	Class<T> c;
	FieldContext f;
	
	public IndexQuery(FieldContext f, PersistenceManager pm, Class<T> c) {
		this.pm = pm;
		this.c = c;
		this.f = f;
	}
	
	public Collection<T> is(Object o) {
		return pm.get(c,f.getIndexName(),o);
	}
	

}
