package thewebsemantic;

import java.text.MessageFormat;
import java.util.ResourceBundle;

public class Resources {
	
	ResourceBundle bundle;
	private static Resources myself = new Resources();
	
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
