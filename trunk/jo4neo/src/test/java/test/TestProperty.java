package test;

import org.junit.Test;
import org.neo4j.api.core.DynamicRelationshipType;
import org.neo4j.api.core.EmbeddedNeo;
import org.neo4j.api.core.NeoService;
import org.neo4j.api.core.Node;
import org.neo4j.api.core.RelationshipType;
import org.neo4j.api.core.Transaction;

public class TestProperty {
	
	@Test
	public void basic() {
		NeoService neo = new EmbeddedNeo("neo_store");
		Transaction t = neo.beginTx();
		Node a = neo.createNode();
		Node b = neo.createNode();
		
		a.createRelationshipTo(b, new PropertyBase());
		
		b.createRelationshipTo(a, DynamicRelationshipType.withName("base"));
		
		for (RelationshipType type : neo.getRelationshipTypes()) {
			System.out.println(type.name());
		}
		t.finish();
	}

}
