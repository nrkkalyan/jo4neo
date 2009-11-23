package thewebsemantic;

import static thewebsemantic.PrimitiveWrapper.isPrimitive;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;

import org.neo4j.api.core.Direction;
import org.neo4j.api.core.Node;
import org.neo4j.api.core.Relationship;
import org.neo4j.api.core.RelationshipType;

public class FieldContext {

	Field field;
	Object subject;

	public FieldContext(Object o, Field field) {
		this.field = field;
		subject = o;
	}

	public boolean isSimpleType() {
		return PrimitiveWrapper.isPrimitive(field.getType())
				|| field.isAnnotationPresent(Embed.class)
				|| arrayPrimitive();
	}

	public boolean isIndexed() {
		return (field.isAnnotationPresent(Graph.class) && 
				!field.getAnnotation(Graph.class).index().equals(
						Graph.DEFAULT));
	}

	private boolean arrayPrimitive() {
		return field.getType().isArray()
				&& PrimitiveWrapper.isPrimitive(field.getType()
						.getComponentType());
	}

	public Iterable<Relationship> relationships(Node n, RelationFactory f) {
		return n.getRelationships(toRelationship(f), Direction.OUTGOING);
	}

	public Object value() {
		Object result = null;
		try {
			field.setAccessible(true);
			result = field.get(subject);
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (result == null)
			return result;
		if (result instanceof Date)
			return ((Date) result).getTime();
		else if (isPlural())
			return ((Collection) result).toArray();
		else
			return result;
	}

	public void setProperty(Object v) {
		try {
			field.setAccessible(true);
			if (field.getType() == Date.class)
				v = new Date((Long) v);
			else if (isPluralPrimitive())
				v = Arrays.asList((Object[]) v);
			field.set(subject, v);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void applyTo(Node n) {
		if (value() != null)
			n.setProperty(name(), value());

	}

	public Node targetNode(IndexedNeo neo) {
		Object o;
		try {
			field.setAccessible(true);
			o = field.get(subject);
			TypeWrapper t = TypeWrapperFactory.wrap(o);
			Neo id = t.id(o);
			Node node = id.mirror(neo);
			t.setId(o, id);
			return node;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public FieldContext[] getTargetFields() {
		TypeWrapper t = TypeWrapperFactory.wrap(field);
		return t.getValueContexts(value());
	}

	public Collection<Object> values() {
		try {
			field.setAccessible(true);
			return (Collection) field.get(subject);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public String name() {
		return field.getName();
	}

	public RelationshipType toRelationship(RelationFactory f) {
		if (field.isAnnotationPresent(Graph.class)) {
			Graph p = field.getAnnotation(Graph.class);
			String name = p.value();
			if (!Graph.DEFAULT.equals(name))
				return f.relationshipType(name);

		}
		String n = field.getName();
		return f.relationshipType(n);
	}

	public Iterable<Relationship> getRelationships(Node n, RelationFactory f) {
		return n.getRelationships(toRelationship(f), Direction.OUTGOING);
	}

	public boolean isSingular() {
		return !field.getType().isAssignableFrom(Collection.class);
	}

	public boolean isEmbedded() {
		return field.isAnnotationPresent(Embed.class);
	}

	public Class<?> type() {
		return field.getType();
	}

	public boolean isPlural() {
		return field.getType().isAssignableFrom(Collection.class);
	}

	public boolean isPluralPrimitive() {
		return isPlural() && isPrimitive(type2());
	}

	public Class<?> type2() {
		return getGenericType();
	}

	public Class<?> getGenericType() {
		return getGenericType((ParameterizedType) field.getGenericType());
	}

	public Class<?> getGenericType(ParameterizedType type) {
		return (type == null) ? NullType.class : (Class<?>) type
				.getActualTypeArguments()[0];
	}

	public void applyFrom(Node n) {
		if (n.hasProperty(name()))
			setProperty(n.getProperty(name()));
	}

	public String getIndexName() {
		return field.getAnnotation(Graph.class).index();		
	}

}
