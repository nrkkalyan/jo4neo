package jo4neo;

import java.lang.reflect.Field;
import java.util.HashMap;

import jo4neo.util.Utils;


public class TypeWrapperFactory {
	
	public static HashMap<Class, TypeWrapper> cache = new HashMap<Class, TypeWrapper>();

	public static TypeWrapper newwrapper(Class<?> c) {
		return new DefaultTypeWrapper(c);
	}
	
	public static synchronized TypeWrapper wrap(Class<?> c) {
		return (cache.containsKey(c)) ? cache.get(c) : TypeWrapperFactory.newwrapper(c);
	}
	
	public static synchronized TypeWrapper $(Object o) {
		return wrap(o.getClass());
	}
	
	public static synchronized TypeWrapper wrap(Field o) {
		return wrap(o.getType());
	}
	
	public static synchronized TypeWrapper wrap(String s) {
		try {
			return wrap(Class.forName(s));
		} catch (ClassNotFoundException e) {
			Utils.runtime(e);
		}
		return null;
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