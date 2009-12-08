package thewebsemantic;

import java.util.HashMap;
import java.util.Map;

public class FieldValueMap {
	
	Object subject;
	Map<Object, FieldContext> map;
	
	public FieldValueMap(Object o) { 		

		map = new HashMap<Object, FieldContext>();
		TypeWrapper type = TypeWrapperFactory.$(o);	
		for (FieldContext f : type.getValueContexts(o)) {
			Object val = f.initWithNewObject();
			map.put(val, f);
		} 
	}
	
	public FieldContext getField(Object value) {
		return map.get(value);
	}

}
