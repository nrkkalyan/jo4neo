package test;

import org.neo4j.api.core.RelationshipType;

public enum PeopleRelationships implements RelationshipType {
	FRIEND,
	ENEMY,
	BROTHER,
	SISTER,
	FATHER, 
	MOTHER
}
