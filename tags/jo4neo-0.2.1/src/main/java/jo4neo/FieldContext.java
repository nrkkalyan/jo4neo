package jo4neo;

import static jo4neo.PrimitiveWrapper.isPrimitive;
import static jo4neo.TypeWrapperFactory.*;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;


import jo4neo.util.Utils;

import org.neo4j.api.core.Direction;
import org.neo4j.api.core.NeoService;
import org.neo4j.api.core.Node;
import org.neo4j.api.core.Relationship;
import org.neo4j.api.core.RelationshipType;

public class FieldContext {

	private static final String INDEX = "_INDEX";
	Field field;
	Object subject;

	public FieldContext(Object o, Field field) {
		this.field = field;
		subject = o;
	}

	public boolean isInverse() {
		if (field.isAnnotationPresent(neo.class)) {
			neo n = field.getAnnotation(neo.class);
			return !n.inverse().equals(neo.DEFAULT);
		}
		return false;		
	}

	public boolean isSimpleType() {
		return isPrimitive(field.getType()) || isEmbedded() || arrayPrimitive();
	}

	public boolean isIndexed() {
		return (field.isAnnotationPresent(neo.class) && field.getAnnotation(
				neo.class).index());
	}

	private boolean arrayPrimitive() {
		return field.getType().isArray()
				&& PrimitiveWrapper.isPrimitive(field.getType()
						.getComponentType());
	}

	public Iterable<Relationship> relationships(Node n, RelationFactory f) {
		return n.getRelationships(toRelationship(f), Direction.OUTGOING);
	}
	
	public Iterable<Relationship> inverseRelationships(Node n, RelationFactory f) {
		return n.getRelationships(toRelationship(f), Direction.INCOMING);
	}

	public Object value() {
		Object result = null;
		try {
			field.setAccessible(true);
			result = field.get(subject);
		} catch (Exception e) {
			Utils.runtime(e);
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

	Object rawValue() {
		try {
			field.setAccessible(true);
			return field.get(subject);
		} catch (Exception e) {
			Utils.runtime(e);
		}
		return null;
	}

	Object initWithNewObject() {
		try {
			field.setAccessible(true);
			Object o = Utils.newObject(field.getType());
			field.set(subject, o);
			return o;
		} catch (Exception e) {
			Utils.runtime(e);
		}
		return null;
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
			Utils.runtime(e);
		}
	}

	public void applyTo(Node n) {
		if (value() != null)
			n.setProperty(name(), value());
	}

	public Node subjectNode(NeoService neo) {
		return neo.getNodeById($(subject).id(subject).id());
	}

	public Collection<Object> values() {
		try {
			field.setAccessible(true);
			return (Collection) field.get(subject);
		} catch (Exception e) {
			Utils.runtime(e);
		}
		return null;
	}

	public String name() {
		return field.getName();
	}

	public RelationshipType toRelationship(RelationFactory f) {
		String n = field.getName();
		if (field.isAnnotationPresent(neo.class)) {
			neo annot = field.getAnnotation(neo.class);
			if (!neo.DEFAULT.equals(annot.value()))
				n = annot.value();
			else if (!neo.DEFAULT.equals(annot.inverse()))
				n = annot.inverse();
		}
		return f.relationshipType(n);
	}

	public boolean isSingular() {
		return !field.getType().isAssignableFrom(Collection.class);
	}

	public boolean isEmbedded() {
		return field.isAnnotationPresent(embed.class);
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
		return getGenericType((ParameterizedType) field.getGenericType());
	}

	private Class<?> getGenericType(ParameterizedType type) {
		return (type == null) ? NullType.class : (Class<?>) type
				.getActualTypeArguments()[0];
	}

	public void applyFrom(Node n) {
		if (n.hasProperty(name()))
			setProperty(n.getProperty(name()));
	}

	public String getIndexName() {
		return subject.getClass().getName() + '.' + field.getName() + INDEX;
	}

	public String getFieldname() {
		return field.getName();
	}
	
	public TraverserProvider getTraverserProvider() {
		Class<? extends TraverserProvider> c = field.getAnnotation(neo.class).traverser();
		try {
			return c.newInstance();
		} catch (Exception e) {
			throw new RuntimeException("Type lacks default constructor:" +  c.getName(), e);
		}
	}

	public boolean isTraverser() {
		if (field.isAnnotationPresent(neo.class)) {
			neo n = field.getAnnotation(neo.class);
			return !n.traverser().equals(DefaultTraverserProvider.class);
		}
		return false;		
	}

}

/**
 * jo4neo is a java object binding library for neo4j
 * Copyright (C) 2009  Taylor Cowan
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */