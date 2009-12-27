package example;

import java.util.Calendar;

import org.neo4j.api.core.Transaction;

import jo4neo.ObjectGraph;

public class Util {

	public static Day findDay(ObjectGraph graph, Calendar cal) {
		int yearNum = cal.get(Calendar.YEAR);
		int monthNum = cal.get(Calendar.MONTH);
		int dayNum = cal.get(Calendar.DAY_OF_MONTH);
		Day day = null;
				
		// find year
		Year y = new Year();
		y = graph.find(y).where(y.value).is(yearNum).result();
		if (y == null) {
			newYear(graph, cal);
			y = new Year();
			y = graph.find(y).where(y.value).is(yearNum).result();
		}
		
	
		Month month = y.getMonth(monthNum);
				
				
		for (Day d : month.days)
			if (d.value == dayNum)
				day = d;
		
		if (day == null) {
			day = new Day();
			day.value = dayNum;
			day.parent = month;
			graph.persist(day);
		}

		return day;
	}

	private static Year newYear(ObjectGraph graph, Calendar cal) {
		Year y;
		y = new Year(cal);
		Calendar tmp = (Calendar) cal.clone();

		Transaction t = graph.beginTx();
		try {
			for (int i = 0; i < 12; i++) {
				tmp.set(i, Calendar.JANUARY);
				Month month = new Month(i, tmp.getLeastMaximum(Calendar.MONTH));
				month.parent = y;
				graph.persist(month);
			}
			t.success();
		} finally {
			t.finish();
		}
		return y;
	}
}
