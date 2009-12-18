package example;

import org.neo4j.api.core.Direction;
import org.neo4j.api.core.Node;
import org.neo4j.api.core.ReturnableEvaluator;
import org.neo4j.api.core.StopEvaluator;
import org.neo4j.api.core.Traverser;
import org.neo4j.api.core.Traverser.Order;

import jo4neo.TraverserProvider;

public class RoleMembers implements TraverserProvider {

	public Traverser get(Node n) {
		return n.traverse( 
				Order.BREADTH_FIRST, 
				StopEvaluator.END_OF_GRAPH, 
				ReturnableEvaluator.ALL_BUT_START_NODE, 
				UserRoleRelationships.role, 
				Direction.INCOMING,
				UserRoleRelationships.parent,
				Direction.INCOMING);
	}
}
