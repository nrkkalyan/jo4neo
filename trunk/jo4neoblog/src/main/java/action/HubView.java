package action;

import java.util.Collection;

import example.model.Post;
import example.model.Tag;

public interface HubView {
	Collection<Post> getPosts();
	Collection<Tag> getTags();
	boolean isSinglePost();
}
