package jo4neo;

import org.neo4j.api.core.Node;
import org.neo4j.api.core.Traverser;

public class DefaultTraverserProvider implements TraverserProvider {
	public Traverser get(Node n) {
		return null;
	}
}
