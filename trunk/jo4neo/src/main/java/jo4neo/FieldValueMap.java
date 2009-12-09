package jo4neo;

import java.util.HashMap;
import java.util.Map;

import jo4neo.fluent.Is;
import jo4neo.fluent.Where;


public class FieldValueMap<A> implements Where<A> {
	
	Class<A> c;
	Map<Object, FieldContext> map;
	PersistenceManager pm;
	
	public FieldValueMap(A a, PersistenceManager pm) {
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