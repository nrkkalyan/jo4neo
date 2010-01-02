package jo4neo.util;

import org.neo4j.api.core.Node;
import org.neo4j.api.core.Traverser;

public interface TraverserProvider {
	public Traverser get(Node n);
}
