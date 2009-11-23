package test;

import java.util.Collection;
import java.util.Date;

import thewebsemantic.Neo;
import thewebsemantic.Graph;

public class TypeBean {
	
	Neo neo;
	
	@Graph Date date;
	@Graph int intItem;
	@Graph long longItem;
	@Graph int[] ages;
	@Graph String[] names;
	@Graph Collection<String> tags;
	@Graph Collection<Double> values;

	public Collection<Double> getValues() {
		return values;
	}
	public void setValues(Collection<Double> values) {
		this.values = values;
	}
	public Collection<String> getTags() {
		return tags;
	}
	public void setTags(Collection<String> tags) {
		this.tags = tags;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public int getIntItem() {
		return intItem;
	}
	public void setIntItem(int intItem) {
		this.intItem = intItem;
	}
	public long getLongItem() {
		return longItem;
	}
	public void setLongItem(long longItem) {
		this.longItem = longItem;
	}
	public int[] getAges() {
		return ages;
	}
	public void setAges(int[] ages) {
		this.ages = ages;
	}
	public String[] getNames() {
		return names;
	}
	public void setNames(String[] names) {
		this.names = names;
	}
	
	

}
