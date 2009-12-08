package thewebsemantic;

import java.lang.reflect.Field;
import java.util.HashMap;

import util.Utils;

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
