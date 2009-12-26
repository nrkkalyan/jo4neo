package jo4neo;

import java.util.Collection;


interface LoadCollectionOps {

	Collection<Object> load(FieldContext field);
	void removeRelationship(FieldContext field, Object o);
	boolean isClosed();
}
