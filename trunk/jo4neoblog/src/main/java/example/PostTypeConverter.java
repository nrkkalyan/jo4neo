package example;

import java.util.Collection;
import java.util.Locale;

import org.neo4j.api.core.NotFoundException;

import net.sourceforge.stripes.validation.TypeConverter;
import net.sourceforge.stripes.validation.ValidationError;

import example.model.Post;

public class PostTypeConverter implements TypeConverter<Post>
	{


		public Post convert(String id, Class<? extends Post> arg1,
				Collection<ValidationError> arg2) {
			System.out.println("my type converter works!");
			long nodeid = Long.parseLong(id);
			try {
				return ContextListener.pm.get(Post.class, nodeid);
			} catch (NotFoundException e) {
				return new Post();
			}
		}

		@Override
		public void setLocale(Locale arg0) {
			// TODO Auto-generated method stub
			
		}
	}
