package thewebsemantic;

import java.lang.reflect.Field;


class FieldContextFactory {

	public static FieldContext get(Field f, Object o) {
		Class<?> c = f.getType();
		return (PrimitiveWrapper.isPrimitive(c)) ? new FieldContext(o, f):null;
	}
}
