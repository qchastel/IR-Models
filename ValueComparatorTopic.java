import java.util.Comparator;
import java.util.Map;



/**
 * From http://stackoverflow.com/questions/109383/how-to-sort-a-mapkey-value-on-the-values-in-java
 * 
 * Part of the Information Retrieval 2012 Basic IR models project.
 */
class ValueComparatorTopic implements Comparator<String> {
    
    Map<String,Integer> fBase;
    
    public ValueComparatorTopic( Map<String,Integer> base ) {
        fBase = base;
    }
    
    // Note: this comparator imposes orderings that are inconsistent with equals.    
    public int compare( String a, String b ) {
        if ( fBase.get( a ) >= fBase.get( b ) ) {
            return -1;
        } else {
            return 1;
        } // returning 0 would merge keys
    }
}