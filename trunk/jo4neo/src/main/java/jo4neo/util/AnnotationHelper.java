package jo4neo.util;

import java.lang.reflect.Field;

import jo4neo.TraverserProvider;

import org.neo4j.api.core.RelationshipType;


public interface AnnotationHelper {
	public TraverserProvider getTraverserProvider(Field field);
	boolean isInverse(Field field);
	boolean isIndexed(Field field);
	boolean isTraverser(Field field);
	boolean isEmbedded(Field field);
	RelationshipType toRelationship(RelationFactory f, Field field);
	FieldContext[] getFields(Field[] fields, Object o);
}
