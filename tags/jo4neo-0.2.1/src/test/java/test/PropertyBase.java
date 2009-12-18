package test;

import org.neo4j.api.core.RelationshipType;

public class PropertyBase implements RelationshipType {

	public String name() {
		return "base";
	}

}
