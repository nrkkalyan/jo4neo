package thewebsemantic;

import java.util.Collection;



class ResultImpl<T> implements Result<T> {

	Class<T> c;
	String indexName;
	Object o;
	PersistenceManager pm;
	
	public ResultImpl(PersistenceManager pm, Class<T> c, String indexName, Object o) {
		this.o = o;
		this.indexName = indexName;
		this.c = c;
		this.pm = pm;
	}

	public T result() {
		return pm.getSingle(c,indexName,o);
	}

	public Collection<T> results() {
		return pm.get(c,indexName,o);
	}
}
