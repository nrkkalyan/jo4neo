package jo4neo;

import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.ServiceLoader;

import jo4neo.spi.ObjectGraphProvider;

import org.neo4j.graphdb.GraphDatabaseService;

/**
 * Creates or retrieves exisiting {@link ObjectGraph} instances associated 
 * with a particular NeoService.
 * 
 * @author Taylor Cowan
 *
 */
public class ObjectGraphFactory {

	private static ObjectGraphFactory myself = new ObjectGraphFactory();
	private Map<GraphDatabaseService, ObjectGraph> cache;

	private ObjectGraphFactory() {
		cache = Collections.synchronizedMap(new IdentityHashMap<GraphDatabaseService, ObjectGraph>());
	}

	public static ObjectGraphFactory instance() {
		return myself;
	}

	public ObjectGraph get(GraphDatabaseService neo) {
		ObjectGraph graph;
		if (!cache.containsKey(neo)) {
			ObjectGraphProvider provider = getProvider();
			graph = provider.create(neo);
			cache.put(neo, graph);
			registerShutdownHook(neo);
		} else {
			graph = cache.get(neo);
		}
		return graph;
	}

	ObjectGraphProvider getProvider() {
		ServiceLoader<ObjectGraphProvider> service = ServiceLoader
				.load(ObjectGraphProvider.class);
		for (ObjectGraphProvider objectGraphProvider : service)
			return objectGraphProvider;
		return null;
	}

	private void registerShutdownHook(final GraphDatabaseService neo) {
		// Registers a shutdown hook for the Neo4j instance so that it
		// shuts down nicely when the VM exits (even if you "Ctrl-C" the
		// running example before it's completed)
		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				neo.shutdown();
			}
		});
	}

}
