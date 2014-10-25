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

    /*
    Pass the documents collected to vector space model
     */

    // VectorModel model = new VectorModel(documents, true);
    // BooleanModel model = new BooleanModel(documents);
    ProbabilisticModel model = new ProbabilisticModel(documents);

    /*
    Create a query string and pass it to the vector model;
     */
    Double avg_precision = 0.0;
    for (Query query : queries) {
    	String queryString = query.getQueryString();
    	Integer numRelDocs = query.getNumRelDocs();

    	SortedMap<Document, Double> results = model.getDocuments(queryString, true);

    	Vector<Integer> rnums = new Vector<Integer>();
    	Integer[] rnums_array = new Integer[1];
    	int count = 0;
    	for (Document document : results.keySet()) {
    		if (count>=numRelDocs)
    			break;
    		rnums.add(document.getRNum());
    		count++;
    	}

    	avg_precision += query.getPrecision(rnums.toArray(rnums_array));
    }
    avg_precision /= 100;

    System.out.println(avg_precision);
}
}