package thewebsemantic;

import java.lang.reflect.Field;

import org.neo4j.api.core.DynamicRelationshipType;
import org.neo4j.api.core.RelationshipType;

public class RelationFactoryImpl implements RelationFactory {

	public RelationshipType relationshipType(Field f) {		
		if ( f.isAnnotationPresent(Graph.class)) {
			Graph p = f.getAnnotation(Graph.class);
			String name = p.value();
			if (!Graph.DEFAULT.equals(name))
				return relationshipType(name);
			
		}
		String n = f.getName();
		return relationshipType(n);
			
	}

	public RelationshipType relationshipType(String name) {
		return DynamicRelationshipType.withName(name);
	}
}
