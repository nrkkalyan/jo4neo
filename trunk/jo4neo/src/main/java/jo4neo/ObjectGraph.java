package jo4neo;

import java.net.URI;
import java.util.Collection;
import java.util.Date;

import jo4neo.fluent.Where;

import org.neo4j.api.core.Node;
import org.neo4j.api.core.Transaction;

/**
 * Interface used to interact with neo4j in an object oriented manner.
 * 
 * An <code>ObjectGraph</code> instance is associated with a particular
 * <code>NeoService</code> instance.  It is thread safe and therefore you can
 * only have one ObjectGraph instance per NeoService.
 * 
 * @author Taylor Cowan
 *
 */
/**
 * @author tcowan
 *
 */
public interface ObjectGraph {

	
	/**
	 * As in neo4j, starts a new transaction and associates it with the current thread.
	 * @return a transaction from NeoService.
	 */
	public abstract Transaction beginTx();

	/**
	 * Mirror a java object within the neo4j graph.  Only fields annotated with {@code}neo
	 * will be considered.
	 * @param o
	 */
	public abstract <A> void persist(A... o);

	/**
	 * removes all data representing an object from the graph.
	 * 
	 * @param o an object retrieved from this {@link ObjectGraph}
	 */
	public abstract void delete(Object... o);
	
	
	/**
	 * Looks up a neo4j graph node using it's java object
	 * mirror. 
	 * @param o an object retrieved from this {@link ObjectGraph}
	 * @return neo4j node represented by o
	 */
	public abstract Node get(Object o);
	
	/**
	 * Looks up all instances of {@code}type in the graph.
	 * 
	 * @param type a type previously stored in the graph
	 * @return a Collection of {@code}type instances.
	 */
	public abstract <T> Collection<T> get(Class<T> type);

	/**
	 * Type safe lookup of object given it's neo4j nodeid.
	 * Your domain classes may use {@link Nodeid#id()} to discover
	 * their neo4j nodeid.   
	 *  
	 * @param t
	 * @param key neo4j node id.
	 * @return
	 */
	public abstract <T> T get(Class<T> t, long key);

	
	/**
	 * 
	 * @param node
	 * @return
	 */
	public abstract Object get(Node node);
	
	/**
	 * 
	 * @param uri
	 * @return
	 */
	public abstract Node get(URI uri);


	/**
	 * 
	 * @param type
	 * @param nodes
	 * @return
	 */
	public abstract <T> Collection<T> get(Class<T> type, Iterable<Node> nodes);

	/**
	 * 
	 * @param uri
	 * @return
	 */
	public abstract void close();

	/**
	 * @param <A>
	 * @param a
	 * @return
	 */
	public abstract <A> Where<A> find(A a);

	/**
	 * @param values
	 * @return
	 */
	public abstract long count(Collection<? extends Object> values);

	public abstract <T> Collection<T> getAddedSince(Class<T> t, Date d);

	public abstract <T> Collection<T> getAddedBetween(Class<T> t, Date from,
			Date to);

	public abstract <T> Collection<T> getMostRecent(Class<T> t, int max);
	
	public <T> T getSingle(Class<T> t, String indexname, Object value);
	
	public <T> Collection<T> get(Class<T> t, String indexname, Object value);

}