package test;

import org.neo4j.api.core.RelationshipType;

import thewebsemantic.RelationFactory;

public class PeopleRelFactory implements RelationFactory {

	public RelationshipType relationshipType(String s) {
		return PeopleRelationships.valueOf(s);
	}

}
