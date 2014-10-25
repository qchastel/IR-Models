import java.io.*;
import java.util.*;
import java.lang.*;
import java.util.SortedMap;

public class IRTask {

  public static void main(String[] args) {
  	/*
  	Parsing multiple data files and creating document objects
  	 */
  	
    Document[] documents_1 = Helpers.parse("data/cfc/cf74");
    Document[] documents_2 = Helpers.parse("data/cfc/cf75");
    Document[] documents_3 = Helpers.parse("data/cfc/cf76");
    Document[] documents_4 = Helpers.parse("data/cfc/cf77");
    Document[] documents_5 = Helpers.parse("data/cfc/cf78");
    Document[] documents_6 = Helpers.parse("data/cfc/cf79");


    /*
    Parsing the query file and creating query objects
     */
    
    Query[] queries = Helpers.parseQueryFile("data/cfc/cfquery");

    /*
    Concatenating all the above arrays into one
     */
    
    int size = 0;
    size += documents_1.length;
    size += documents_2.length;
    size += documents_3.length;
    size += documents_4.length;
    size += documents_5.length;
    size += documents_6.length;

    Document[] documents = new Document[size];
    System.arraycopy(documents_1, 0, documents, 0, documents_1.length);
    int curr_size = documents_1.length;
    System.arraycopy(documents_2, 0, documents, curr_size, documents_2.length);
    curr_size += documents_2.length;
    System.arraycopy(documents_3, 0, documents, curr_size, documents_3.length);
    curr_size += documents_3.length;
    System.arraycopy(documents_4, 0, documents, curr_size, documents_4.length);
    curr_size += documents_4.length;
    System.arraycopy(documents_5, 0, documents, curr_size, documents_5.length);
    curr_size += documents_5.length;
    System.arraycopy(documents_6, 0, documents, curr_size, documents_6.length);


    // for (Document document : documents) {
    // 	String[] topics = document.getTopics();
    // 	for (String topic : topics) {
    // 		System.out.println(topic);
    // 	}
    // 	System.out.println("*******************************************");
    // }

    /*
    Pass the documents collected to vector space model
     */

    VectorModel model = new VectorModel(documents, true);
    OntologyModel ont_model = new OntologyModel(documents);
    // BooleanModel model = new BooleanModel(documents);
    // ProbabilisticModel model = new ProbabilisticModel(documents);

    /*
    Performance of the Vector space model approach
     */
    
    /*
	TODO For now I am taking the top *k* of the retrieved docs, where *k* is the number of relevant docs of the query given in groundtruth (Cheating?)
		Need to update this to a more meaningful way: (1) Emprically set *k* (2) Choose those docs whose normalized score is above a threshold (normalized over all scores)
 	*/
 //    for (int i=1; i<=10; i++) {
	//     Double avg_precision = 0.0;
	//     Double avg_recall = 0.0;
	//     for (Query query : queries) {
	//     	String queryString = query.getQueryString();
	//     	Integer numRelDocs = query.getNumRelDocs();

	//     	SortedMap<Document, Double> results = model.getDocuments(queryString, true);

	//     	Vector<Integer> rnums = new Vector<Integer>();
	//     	Integer[] rnums_array = new Integer[1];
	//     	int count = 0;
	//     	Integer numRetDocs = i;
	//     	for (Document document : results.keySet()) {
	//     		if (count>=numRetDocs)
	//     			break;
	//     		rnums.add(document.getRNum());
	//     		count++;
	//     	}

	//     	avg_precision += query.getPrecision(rnums.toArray(rnums_array));
	//     	avg_recall += query.getRecall(rnums.toArray(rnums_array));
	//     }
	//     avg_precision /= 100;
	//     avg_recall /= 100;

	//     System.out.println("#"+i+" Precision: "+avg_precision);
	//     System.out.println("#"+i+" Recall: "+avg_recall);
	// }


    /*
    Trying the PRF + Ontology approach
     */
    for (int i=1; i<=10; i++) {
	    Double avg_precision = 0.0;
	    Double avg_recall = 0.0;
	    for (Query query : queries) {
	    	String queryString = query.getQueryString();
	    	Integer numRelDocs = query.getNumRelDocs();

	    	SortedMap<Document, Double> results = model.getDocuments(queryString, true);
	    	Map<String, Integer> topicFreq = new HashMap<String, Integer>();

	    	Integer numRetDocs = 5;
	    	int count = 0;
	    	for (Document document : results.keySet()) {
	    		if (count >= numRetDocs) {
	    			break;
	    		}
	    		else {
	    			String[] topics = document.getTopics();
	    			for (String topic : topics) {
	    				if (topicFreq.containsKey(topic)) {
	    					int val = topicFreq.get(topic);
	    					topicFreq.put(topic, val+1);
	    				}
	    				else {
	    					topicFreq.put(topic, 1);
	    				}
	    			}
	    		}
	    		count++;
	    	}

	    	// Take the top 10 most frequently occuring topics
	    	Integer numTopics = 5;
	    	String[] retTopics = new String[numTopics];
	    	SortedMap<String, Integer> topTopics = new TreeMap<String,Integer>( new ValueComparatorTopic( topicFreq ) );
	    	topTopics.putAll(topicFreq);
	    	count = 0;
	    	for (String topic : topTopics.keySet()) {
	    		if (count>=numTopics) {
	    			break;
	    		}
	    		else {
	    			retTopics[count] = topic;
	    		}
	    		count++;
	    	}

	    	ont_model.addQueryTopics(retTopics);
	    	results = ont_model.getDocuments(queryString, true);

	    	Vector<Integer> rnums = new Vector<Integer>();
	    	Integer[] rnums_array = new Integer[1];
	    	count = 0;
	    	numRetDocs = i;
	    	for (Document document : results.keySet()) {
	    		if (count>=numRetDocs) {
	    			break;
	    		}
	    		count++;
	    		rnums.add(document.getRNum());
	    	}
	    	avg_precision += query.getPrecision(rnums.toArray(rnums_array));
	    	avg_recall += query.getRecall(rnums.toArray(rnums_array));
	    }
	    avg_precision /= 100;
	    avg_recall /= 100;

	    System.out.println("#"+i+" Precision: "+ avg_precision);
	    System.out.println("#"+i+" Recall: "+ avg_recall);
	}

}
}