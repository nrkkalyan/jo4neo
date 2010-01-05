package jo4neo;

import java.util.Collection;

import jo4neo.util.FieldContext;



interface LoadCollectionOps {

	Collection<Object> load(FieldContext field);
	void removeRelationship(FieldContext field, Object o);
	long count(FieldContext field);
	boolean isClosed();
}
