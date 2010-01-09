package jo4neo;

import java.util.Collection;

import org.neo4j.api.core.Direction;

import jo4neo.util.FieldContext;



interface LoadCollectionOps {

	Collection<Object> load(FieldContext field);
	void removeRelationship(FieldContext field, Object o);
	long count(FieldContext field, Direction d);
	boolean isClosed();
}
