package jo4neo;

import jo4neo.util.Lazy;

public class ListFactory {
	public static Lazy get(FieldContext field, LoadOperation load) {
		if (field.isInverse())
			return new InverseList(field, load);
		else
			return new LazyList(field, load);
	}
}
