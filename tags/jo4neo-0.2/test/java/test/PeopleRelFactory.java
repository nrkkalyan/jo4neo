package test;

import jo4neo.RelationFactory;

import org.neo4j.api.core.RelationshipType;


public class PeopleRelFactory implements RelationFactory {

	public RelationshipType relationshipType(String s) {
		return PeopleRelationships.valueOf(s);
	}

}
