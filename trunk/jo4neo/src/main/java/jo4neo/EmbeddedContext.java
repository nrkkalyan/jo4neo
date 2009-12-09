package jo4neo;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Field;

import jo4neo.util.Utils;


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