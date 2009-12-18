package example;

import org.neo4j.api.core.RelationshipType;

public enum UserRoleRelationships implements RelationshipType {
	role,
	parent
}
