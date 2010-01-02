package example;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URI;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;

import jo4neo.ObjectGraph;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.neo4j.api.core.EmbeddedNeo;
import org.neo4j.api.core.NeoService;

public class Main {
	
	//"Mon, 28 Dec 2009 18:35:25 +0000"	
	public static DateFormat df = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss Z");
	
	public static void main(String[] args) throws MalformedURLException, IOException, ParseException {
		
		URI apicall = URI.create("http://search.twitter.com/search.json?q=neo4j&rpp=100");
		
		InputStreamReader reader = new InputStreamReader( apicall.toURL().openStream());
		BufferedReader br = new BufferedReader(reader);
		
		StringBuilder buffer = new StringBuilder();
		String line;
		while((line=br.readLine()) != null)
			buffer.append(line);

		JSONObject json =  JSONObject.fromObject(buffer.toString());
		JSONArray results =  json.getJSONArray("results");

		Collection<Tweet> tweets = new ArrayList<Tweet>();
		for (int i=0; i<results.size(); i++) {	
			JSONObject result =  (JSONObject)results.get(i);			
			Date d = df.parse(result.get("created_at").toString());
			String content = result.get("text").toString();
			String from = result.get("from_user").toString();
			Tweet tweet = new Tweet();
			tweet.content = content;
			tweet.time = d;
			tweet.from = URI.create("http://twitter.com/" + from);
			tweets.add(tweet);
		}
		
		
		NeoService neo = new EmbeddedNeo("neo_store");
		ObjectGraph graph = new ObjectGraph(neo);
		Util util = new Util(graph);
		Calendar cal = Calendar.getInstance();
		try {
			for (Tweet tweet : tweets) {
				cal.setTime(tweet.time);
				int hour = cal.get(Calendar.HOUR_OF_DAY);
				Day day = util.findDay(cal);
				for (Hour h : day.hours) {
					if (h.value == hour)
						tweet.hour = h;
				}
				graph.persist(tweet);
			}
			
		} finally {
			graph.close();
			neo.shutdown();
		}
	}
}
