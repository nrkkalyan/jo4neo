package action;

import java.util.Collection;
import java.util.LinkedList;

import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.UrlBinding;
import example.model.Post;

@UrlBinding("/blog/home")
public class HubAction extends BaseAction {
	
	private Collection<Post> posts;
	private long postid = -1;
	
	@DefaultHandler
	public Resolution show() {
		String pathInfo = context.getRequest().getPathInfo();
		if ( pathInfo.length() > 6) {
			postid = Long.valueOf(pathInfo.substring(6));		
			posts = new LinkedList<Post>();
			posts.add(load(Post.class, postid));
		} else
			posts = pm().getMostRecent(Post.class, 5);
		return new ForwardResolution("/hub.jsp");
	}
	public Collection<Post> getPosts() {return posts;}
	public long getP() {return postid;}
	
}
