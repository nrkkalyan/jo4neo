package thewebsemantic;


class IndexQuery<T> implements Is<T> {

	PersistenceManager pm;
	Class<T> c;
	FieldContext f;
	
	public IndexQuery(FieldContext f, PersistenceManager pm, Class<T> c) {
		this.pm = pm;
		this.c = c;
		this.f = f;
	}
	
	public Result<T> is(Object o) {
		return new ResultImpl<T>(pm,c,f.getIndexName(), o);
	}

}
