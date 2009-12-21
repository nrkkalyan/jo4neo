package action;

import java.util.Collection;
import java.util.LinkedList;

import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.UrlBinding;
import example.model.HubView;
import example.model.Post;
import example.model.Tag;

@UrlBinding("/blog/home")
public class HubAction extends BaseAction implements HubView {
	
	private Collection<Post> posts;
	private boolean singlePost = false;
	
	@DefaultHandler
	public Resolution show() {
		String pathInfo = context.getRequest().getPathInfo();
		if ( pathInfo.length() > 6) {
			selected = Long.valueOf(pathInfo.substring(6));		
			posts = new LinkedList<Post>();
			posts.add(load(Post.class, selected));
			singlePost = true;
		} else
			posts = pm().getMostRecent(Post.class, 5);
		return new ForwardResolution("/hub.jsp");
	}
	public Collection<Post> getPosts() {return posts;}
	public Collection<Tag> getTags() {return pm().get(Tag.class);}

	public boolean isSinglePost() {
		return singlePost;
	}
	
}
