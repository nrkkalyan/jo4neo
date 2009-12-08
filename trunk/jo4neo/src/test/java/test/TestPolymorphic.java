package test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Date;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.neo4j.api.core.EmbeddedNeo;
import org.neo4j.api.core.NeoService;

import thewebsemantic.PersistenceManager;

public class TestPolymorphic {
	static NeoService neo;

	@BeforeClass
	public static void setup() {
		neo = new EmbeddedNeo("neo_store");
	}

	@AfterClass
	public static void teardown() {
		neo.shutdown();
	}

	@Test
	public void basic() {
		PersistenceManager pm = new PersistenceManager(neo);
		BlogPost post = new BlogPost();
		post.setPublishDate(new Date());
		post.setTitle("Nosql: take that Oracle");

		BlogPost post2 = new BlogPost();
		post.setPublishDate(new Date());
		post.setTitle("TopQuadrant creates semantic spider for web");

		HotelReview review = new HotelReview();
		review
				.setReviewContent("Right across from the train station, nice breakfast.");
		review.setReviewDate(new Date());

		Tag semweb = new Tag();
		semweb.setName("semweb");
		semweb.getItems().add(post2);
		post2.getTags().add(semweb);
		semweb.getItems().add(post);
		post.getTags().add(semweb);
		semweb.getItems().add(review);

		pm.persist(semweb);

		Tag tag = pm.getSingle(Tag.class, Tag.NAME_INDEX, "semweb");

		for (Taggable thing : tag.getItems()) {
			System.out.println(thing);
			assertTrue(thing instanceof ContentItem);
		}
		assertEquals(3, tag.getItems().size());

		pm.delete(semweb);
		pm.close();

	}
}
