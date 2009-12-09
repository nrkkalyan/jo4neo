package jo4neo;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.util.logging.Logger;


public abstract class TypeWrapper {
	public static Logger logger = Logger.getLogger("com.thewebsemantic");
	
	protected static BeanInfo beanInfo(Class<?> c) {
		try {
			return Introspector.getBeanInfo(c);
		} catch (IntrospectionException e1) {
			e1.printStackTrace();
		}
		return null;
	}
	
	public abstract Object newInstance();	
	public abstract FieldContext[] getFields(Object o);
	public abstract Nodeid id(Object o);

	public abstract void setId(Object bean, Nodeid n);
	public abstract Class<?> getWrappedType();
	public abstract String name();
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