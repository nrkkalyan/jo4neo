package thewebsemantic;

import org.neo4j.api.core.RelationshipType;

public interface RelationFactory {
	public RelationshipType relationshipType(String f);
}
