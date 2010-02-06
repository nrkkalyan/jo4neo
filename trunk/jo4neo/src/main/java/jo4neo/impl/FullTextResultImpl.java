package jo4neo.impl;

import java.util.Collection;

import jo4neo.ObjectGraph;
import jo4neo.fluent.Result;

public class FullTextResultImpl<T> implements Result<T> {

	Class<T> c;
	String indexName;
	Object o;
	ObjectGraph pm;

	public FullTextResultImpl(ObjectGraph pm, Class<T> c, String indexName, Object o) {
		this.o = o;
		this.indexName = indexName;
		this.c = c;
		this.pm = pm;
	}

	public T result() {
		return pm.getSingle(c,indexName,o);
	}

	public Collection<T> results() {
		return pm.getFullText(c,indexName,o);
	}

}
