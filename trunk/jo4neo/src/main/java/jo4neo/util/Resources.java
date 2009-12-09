package jo4neo.util;

import java.text.MessageFormat;
import java.util.ResourceBundle;

public class Resources {
	
	ResourceBundle bundle;
	private static Resources myself = new Resources();
	public static String MISSING_ID = "MISSING_ID";
	public static String MISSING_TIMELINE_ANNOTATION = "MISSING_TIMELINE_ANNOTATION";
	
	private Resources() {
		bundle = ResourceBundle.getBundle("thewebsemantic.message");
	}
	
	public String message(String key) {
		return bundle.getString(key);
	}
	
	public static Resources getInstance() {
		return myself;
	}
	
	public static String msg(String key) {
		return myself.message(key);
	}
	
	public static String msg(String key, Object o) {
		return MessageFormat.format(msg(key), o);
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