package jo4neo;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.logging.Level;

import jo4neo.util.Utils;

import static jo4neo.util.Resources.*;


class DefaultTypeWrapper extends TypeWrapper {
	Field[] fields;
	Field idfield;
	Class<?> me;

	public DefaultTypeWrapper(Class<?> c) {
		me = c;
		fields = getDeclaredFields(c);
		for (Field f : fields)
			if (f.getType().equals(Nodeid.class))
				idfield = f;
		if (idfield==null && !c.isAssignableFrom(Collection.class))
			throw new PersistenceException(msg(MISSING_ID, c.getName()));
	}

	public Nodeid id(Object o) {
		Nodeid n = null;
		try {
			if (!idfield.isAccessible())
				idfield.setAccessible(true);
			n = (Nodeid)idfield.get(o);
			if (n== null)
				n = new Nodeid(o.getClass());
			idfield.set(o, n);
		} catch (Exception e) {
			logger.log(Level.WARNING, "Error retrieving id field value.", e);
		}
		return n;

	}

	public FieldContext[] getFields(Object o) {
		ArrayList<FieldContext> values = new ArrayList<FieldContext>();
		for (Field field : fields) {
			if (field.isAnnotationPresent(neo.class))
				values.add(new FieldContext(o, field));
			else if ( field.isAnnotationPresent(embed.class))
				values.add(new EmbeddedContext(o,field));
		}
		return values.toArray(new FieldContext[0]);
	}
	
	

	@Override
	public Object newInstance() {
		try {
			return me.newInstance();
		} catch (Exception e) {
			Utils.runtime(e);
		}
		return null;
	}

	@Override
	public void setId(Object bean, Nodeid n) {		
		try {
			if (!idfield.isAccessible())
				idfield.setAccessible(true);			
			idfield.set(bean, n);
		} catch (Exception e) {
			Utils.runtime(e);
		}
	}

	@Override
	public Class<?> getWrappedType() {
		return this.me;
	}
	
	public String name() {
		return this.me.getName();
	}
	
	public Field[] getDeclaredFields(Class c) {
		ArrayList<Field> fields = new ArrayList<Field>();
		for (Field field : c.getDeclaredFields())
			fields.add(field);
		Class<?> cls = c;
		while (cls.getSuperclass() != Object.class && cls.getSuperclass() != null) {
			cls = cls.getSuperclass();
			for (Field field : cls.getDeclaredFields())
				fields.add(field);
		}
		return fields.toArray(new Field[0]);
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