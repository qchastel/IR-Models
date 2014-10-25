import java.io.*;
import java.util.*;


class Query {
	private int qnum = 0;
	private String queryString = null;
	private int nTopics = 0;
	private Vector<String> topics = null;
	private int nRelDocs = 0;
	private Map<Integer, Integer> relDocs  = null;

	protected Query() {
		throw new UnsupportedOperationException( );
	}

	public Query(int queryNum) {
		qnum = queryNum;
		relDocs = new HashMap<Integer, Integer>();
	}

	public void addText(String text) {
		queryString = text;
	}

	public void addTopic(String topic) {
		String term_modified = topic.replaceAll( "[^\\w\\d\\s]+", " " ).trim( ).toLowerCase( );
		if (term_modified.equals("") || term_modified.equals(" "))
			return;

		nTopics++;
		topics.add(term_modified);
	}

	public void addRelDoc(int rnum, int score) {
		relDocs.put(rnum, score);
		nRelDocs++;
	}

	public String getQueryString() {
		return queryString;
	}

	public int getQueryNum() {
		return qnum;
	}

	public String[] getTopics() {
		String[] topics_array = new String[1];
		return topics.toArray(topics_array);
	}

	public Integer getNumRelDocs() {
		return nRelDocs;
	}

	public Map<Integer, Integer> getRelDocs() {
		return relDocs;
	}

	public Double getPrecision(Integer[] rnums) {
		Double correct = 0.0;
		for (Integer rnum : rnums) {
			if (relDocs.containsKey(rnum))
				correct++;
		}
		return correct/rnums.length;
	}
}