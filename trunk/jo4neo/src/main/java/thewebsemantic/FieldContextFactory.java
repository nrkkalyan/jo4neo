package thewebsemantic;

import java.lang.reflect.Field;

public class FieldContextFactory {

	public static FieldContext get(Field f, Object o) {
		Class<?> c = f.getType();
		if ( PrimitiveWrapper.isPrimitive(c))
			return new FieldContext(o, f);
		
		return null;
	}
}
