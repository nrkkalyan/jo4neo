package action;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Collection;

import net.sourceforge.stripes.action.ActionBean;
import net.sourceforge.stripes.action.ActionBeanContext;


import example.StripesContext;
import example.model.Post;

public class BaseAction implements ActionBean {
	protected StripesContext context;
	
	
	public StripesContext getContext() {
		return context;
	}

	public void setContext(ActionBeanContext c) {
		context = (StripesContext)c;
	}

	protected String hashPassword(String s) throws NoSuchAlgorithmException {
		MessageDigest md;
		md = MessageDigest.getInstance("SHA-1");
		md.update(s.getBytes());
		return Hex.toHex(md.digest());
	}

	protected <T> T load(Class<T> class1, String postid2) {
		// TODO Auto-generated method stub
		return null;
	}
	
	protected <T> Collection<T> load(Class<T> class1) {
		// TODO Auto-generated method stub
		return null;
	}
	

}
