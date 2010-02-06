package test.specific;

import java.util.Collection;

import org.junit.Test;
import org.neo4j.graphdb.Transaction;

import test.BaseTest;

public class TestFullText extends BaseTest {

	@Test
	public void basic() {
		Transaction t = neo.beginTx();
		try {
			Article a = new Article();
			a.author = "Johnson";
			a.content = "this is a story bout a man name Jed, a poor mountaineer nearly kept his family fed";
			//a.tags = new String[] {"a", "b", "c", "tag"};
			graph.persist(a);
			
			t.success();
		} finally {
			t.finish();
		}
		
	
		
	
		Article a = new Article();
		Collection<Article> articles = graph.find(a).where(a.content).is("+his").results();
		for (Article article : articles) {
			System.out.println(article.author);
		}
		
		articles = graph.getFullText(Article.class, "test.specific.Article.content_INDEX", "nearly Jed");
		for (Article article : articles) {
			System.out.println(article.author);
		}
		
	}

}
