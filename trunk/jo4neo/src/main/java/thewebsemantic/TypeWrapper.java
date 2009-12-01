package thewebsemantic;

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
	public abstract FieldContext[] getValueContexts(Object o);
	public abstract Neo id(Object o);

	public abstract void setId(Object bean, Neo n);
	public abstract Class<?> getWrappedType();
	public abstract String name();
}
