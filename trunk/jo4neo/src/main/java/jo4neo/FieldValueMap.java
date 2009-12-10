package jo4neo;

import java.util.HashMap;
import java.util.Map;

import jo4neo.fluent.Is;
import jo4neo.fluent.Where;


public class FieldValueMap<A> implements Where<A> {
	
	Class<A> c;
	Map<Object, FieldContext> map;
	ObjectGraph pm;
	
	public FieldValueMap(A a, ObjectGraph pm) {
		c = (Class<A>) a.getClass();
		this.pm = pm;
		map = new HashMap<Object, FieldContext>();
		TypeWrapper type = TypeWrapperFactory.$(a);	
		for (FieldContext f : type.getFields(a)) {
			Object val = f.initWithNewObject();
			map.put(val, f);
		} 
	}

	public FieldContext getField(Object value) {
		return map.get(value);
	}
	
	public Is<A> where(Object o) {
		return new IndexQuery<A>(map.get(o), pm, c);
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