package thewebsemantic;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.logging.Level;

public class DefaultTypeWrapper extends TypeWrapper {
	Field[] fields;
	Field idfield;
	Class<?> me;

	public DefaultTypeWrapper(Class<?> c) {
		me = c;
		fields = Util.getDeclaredFields(c);
		for (Field f : fields)
			if (f.getType().equals(Nodeid.class))
				idfield = f;
		if (idfield==null && !c.isAssignableFrom(Collection.class))
			throw new PersistenceException(Resources.msg("MISSING_ID", c.getName()));
	}

	public Nodeid id(Object o) {
		try {
			if (!idfield.isAccessible())
				idfield.setAccessible(true);
			Nodeid n = (Nodeid)idfield.get(o);
			if (n!=null)
				return n;
		} catch (Exception e) {
			logger.log(Level.WARNING, "Error retrieving id field value.", e);
		}
		return new Nodeid(o.getClass());
	}

	public FieldContext[] getValueContexts(Object o) {
		ArrayList<FieldContext> values = new ArrayList<FieldContext>();
		for (Field field : fields) {
			if (field.isAnnotationPresent(neo.class))
				values.add(new FieldContext(o, field));
			else if ( field.isAnnotationPresent(Embed.class))
				values.add(new EmbeddedContext(o,field));
		}
		return values.toArray(new FieldContext[0]);
	}
	
	

	@Override
	public Object newInstance() {
		try {
			return me.newInstance();
		} catch (Exception e) {
			e.printStackTrace();
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
			e.printStackTrace();
		}
	}

	@Override
	public Class<?> getWrappedType() {
		return this.me;
	}
	
	public String name() {
		return this.me.getName();
	}

}
