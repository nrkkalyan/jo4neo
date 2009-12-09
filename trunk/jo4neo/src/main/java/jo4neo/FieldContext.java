package jo4neo;

import static jo4neo.PrimitiveWrapper.isPrimitive;
import static jo4neo.TypeWrapperFactory.*;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;

import javax.lang.model.type.PrimitiveType;

import jo4neo.util.Utils;

import org.neo4j.api.core.Direction;
import org.neo4j.api.core.NeoService;
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
		return isPrimitive(field.getType())
				|| isEmbedded()
				|| arrayPrimitive();
	}

	public boolean isIndexed() {
		return (field.isAnnotationPresent(neo.class) && 
				field.getAnnotation(neo.class).index());
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
			String name = field.getAnnotation(neo.class).value();
			if (!neo.DEFAULT.equals(name))
				n = name;
		}
		return f.relationshipType(n);
	}

	public Iterable<Relationship> getRelationships(Node n, RelationFactory f) {
		return n.getRelationships(toRelationship(f), Direction.OUTGOING);
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
		return subject.getClass().getName() + '.' +  field.getName() + "_INDEX";
	}
	
	public String getFieldname() {
		return field.getName();
	}

}

/**
 * Copyright (C) 2009
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */