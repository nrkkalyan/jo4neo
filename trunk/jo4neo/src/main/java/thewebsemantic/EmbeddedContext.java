package thewebsemantic;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Field;

import util.Utils;

class EmbeddedContext extends FieldContext {

	public EmbeddedContext(Object o, Field field) {
		super(o, field);
	}
	
	public Object value() {
		Object result = null;
		try {
			field.setAccessible(true);
			result = field.get(subject);
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			ObjectOutputStream os = new ObjectOutputStream(bos);
			os.writeObject(result);
			result = bos.toByteArray();			
		} catch (Exception e) {
			Utils.runtime(e);
		}
		return result;
	}

	public void setProperty(Object v) {
		try {
			ByteArrayInputStream bis = new ByteArrayInputStream((byte[])v);
			ObjectInputStream os = new ObjectInputStream(bis);
			Object o = os.readObject();
			field.setAccessible(true);
			field.set(subject, o);
		} catch (Exception e) {
			Utils.runtime(e);
		}
	}
	

}
