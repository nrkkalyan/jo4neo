package thewebsemantic;

import java.lang.reflect.Field;
import java.util.ArrayList;

public class Util {

	public static Field[] getDeclaredFields(Class c) {
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
