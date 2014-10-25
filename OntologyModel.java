import java.util.*;
import java.io.*;


public class OntologyModel extends AbstractModel {

	private double k = 0.1;
	private String[] queryTopics;

	protected OntologyModel() {
		throw new UnsupportedOperationException( );
	}

	public OntologyModel(Document[] documents) {
		fDocuments = documents;

		for (Document document : fDocuments) {
			for (String term : document.getTerms()) {
				Helpers.setTfIdfWeight(document, term, documents);
			}
		}
	}

	private double getCosineSimilarity(Document document, Document query) {
		Set<String> terms = new HashSet<String>();
		for (String term : document.getTerms()) {
			terms.add(term);
		}
		for (String term : query.getTerms()) {
			terms.add(term);
		}

		double numerator = 0.0;
		double denominator1 = 0.0;
		double denominator2 = 0.0;

		for (String term : terms) {
			numerator += document.getWeight(term) * query.getWeight(term);
			denominator1 += document.getWeight(term) * document.getWeight(term);
			denominator2 += query.getWeight(term) * query.getWeight(term);
		}

		if(denominator2==0 || denominator1==0)
			return 0;

		return numerator/(Math.sqrt(denominator1) * (Math.sqrt(denominator2)));
	}

	private double getTopicSimilarity(Document document, String queryString) {
		String[] documentTopics = document.getTopics();

		if (documentTopics.length==0)
			return k;	

		int intersect = 0;
		for (String queryTopic : queryTopics) {
			for (String documentTopic : documentTopics) {
				// System.out.println("query " + queryTopic);
				// System.out.println("doc "+documentTopic);
				if (documentTopic==null || queryTopic==null)
					continue;
				String qTopic = queryTopic.trim().toLowerCase();
				String dTopic = documentTopic.trim().toLowerCase();
				if(qTopic.equals(dTopic))
					intersect++;
			}
		}

		if (intersect!=0)
			return intersect;
		else
			return k;
	}

	public void addQueryTopics(String[] qTopics) {
		queryTopics = qTopics;
	}

	@Override
	public SortedMap<Document,Double> getDocuments( String queryString, boolean stem ) {
        
        Document query = new Document( "query", queryString, stem );
        Document[] documents = new Document[fDocuments.length + 1];
        System.arraycopy( fDocuments, 0, documents, 0, fDocuments.length );
        documents[documents.length - 1] = query;
        for ( String term : query.getTerms( ) ) {
            Helpers.setTfIdfWeight( query, term, documents );
        }
        Map<Document,Double> map = new HashMap<Document,Double>( );
        SortedMap<Document,Double> result = new TreeMap<Document,Double>( new ValueComparator( map ) );
        
        for ( Document document : fDocuments ) {
            map.put( document, getCosineSimilarity( document, query )*getTopicSimilarity(document, queryString));
        }
        result.putAll( map );
        return result;
    }

}