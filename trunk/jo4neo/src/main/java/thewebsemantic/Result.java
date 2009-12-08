package thewebsemantic;

import java.util.Collection;

public interface Result<T> {

	Collection<T> results();
	T result();
}
