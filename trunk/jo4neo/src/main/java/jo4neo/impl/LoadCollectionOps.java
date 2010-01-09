package jo4neo.impl;

import java.util.Collection;

import org.neo4j.api.core.Direction;




interface LoadCollectionOps {

	Collection<Object> load(FieldContext field);
	void removeRelationship(FieldContext field, Object o);
	long count(FieldContext field, Direction d);
	boolean isClosed();
}
