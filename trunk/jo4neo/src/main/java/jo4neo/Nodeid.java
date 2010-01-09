package jo4neo;

/**
 * Used to inject neo4j context into a javabean.  Before persisting a 
 * javabean/POJO with jo4neo your bean should have a Nodeid field declared
 * as transient:
 * 
 * <code>
 * public DomainObject {
 *     //used by jo4neo
 *     private transient Nodeid id;  
 *     // indicates a field to be persisted in graph  
 *     @neo private String name;
 *     ...
 * }
 * </code>
 * This example is the minimum necessary to augment a graph with a single node
 * holding a single property.
 */
public interface Nodeid {

	public abstract long id();

	public abstract String hostingType();

	public abstract Class<?> type();

	public abstract boolean valid();

}