package jo4neo.spi;

import org.neo4j.api.core.NeoService;

import jo4neo.ObjectGraph;

public interface ObjectGraphProvider  {	
	public ObjectGraph create(NeoService neo);
}
