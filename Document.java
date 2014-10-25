import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

//import Helpers;



/**
 * A representation for a document. 
 * 
 * Term "vectors" are realized with mappings from terms to frequencies or weights,
 * respectively. Missing terms are considered to have frequency and weight 0.
 * Frequencies are abolute, weights could be relative term frequencies, 
 * tf/idf or any other weighting functions.
 * 
 * @author Eric Hildebrand
 * 
 *         Part of the Information Retrieval 2012 Basic IR models project.
 */
public class Document {
    
    private int rnum = 0;
    private int fLength = 0; // Number of terms
    private int fMaxFreq = 0;
    private int fTopics = 0;
    private String fName = null;
    private Map<String,Integer> fFreqs = null; // The term frequency "vector"
    private Map<String,Double> fWeights = null; // The term weight "vector"
    private Vector<String> topics = null;
    
    protected Document( ) {
        throw new UnsupportedOperationException( );
    }
    
    /**
     * Creates a new document.
     * 
     * @param name
     *            An identifier for the document.
     * @param text
     *            The text included in the document.
     * @param stem
     *            Optionally stems terms.
     */
    public Document( String name, String text, boolean stem ) {
        fName = name;
        String[] terms = text.replaceAll( "[^\\w\\d\\s]+", " " ).trim( ).toLowerCase( ).split( "\\s+" ); // Preprocessing
        if ( stem ) {
            for ( int i = 0; i < terms.length; i++ ) {
                terms[i] = Helpers.stem( terms[i] );
            }
        }
        fFreqs = new HashMap<String,Integer>( );
        for ( String term : terms ) {
            fLength++;
            if ( !fFreqs.containsKey( term ) ) {
                fFreqs.put( term, 0 ); // Init term frequency
            }
            int freq = fFreqs.get( term ) + 1;
            fFreqs.put( term, freq ); // Update term frequency
            if ( freq > fMaxFreq ) {
                fMaxFreq = freq; // Update maximum frequency
            }
        }
    }
    
    public Document( int num ) {
        rnum = num;
        fName = Integer.toString(rnum);
        fFreqs = new HashMap<String,Integer>( );
        topics = new Vector<String>();
    }
    
    public void add (String text, boolean stem) {
        String[] terms = text.replaceAll( "[^\\w\\d\\s]+", " " ).trim( ).toLowerCase( ).split( "\\s+" ); // Preprocessing
        if ( stem ) {
            for ( int i = 0; i < terms.length; i++ ) {
                terms[i] = Helpers.stem( terms[i] );
            }
        }
        for ( String term : terms ) {
            fLength++;
            if ( !fFreqs.containsKey( term ) ) {
                fFreqs.put( term, 0 ); // Init term frequency
            }
            int freq = fFreqs.get( term ) + 1;
            fFreqs.put( term, freq ); // Update term frequency
            if ( freq > fMaxFreq ) {
                fMaxFreq = freq; // Update maximum frequency
            }
        }
    }
    
    public void add (String term) {
        String term_modified = term.replaceAll( "[^\\w\\d\\s]+", " " ).trim( ).toLowerCase( );
        if (term_modified.equals("") || term_modified.equals(" ")) {
            return;
        }
        
        String term_stemmed = Helpers.stem(term_modified);
        fLength++;
        if ( !fFreqs.containsKey( term_stemmed ) ) {
            fFreqs.put( term_stemmed, 0 ); // Init term frequency
        }
        int freq = fFreqs.get( term_stemmed ) + 1;
        fFreqs.put( term_stemmed, freq ); // Update term frequency
        if ( freq > fMaxFreq ) {
            fMaxFreq = freq; // Update maximum frequency
        }
    }

    public void addTopic (String term) {
        String term_modified = term.replaceAll( "[^\\w\\d\\s]+", " " ).trim( ).toLowerCase( );
        if (term_modified.equals("") || term_modified.equals(" ")) {
            return;
        }

        fTopics++;
        topics.add(term_modified);
    }
    
    public int getLength( ) {
        return fLength;
    }
    
    public String getName( ) {
        return fName;
    }
    
    public String[] getTerms( ) {
        return fFreqs.keySet( ).toArray( new String[fFreqs.keySet( ).size( )] );
    }

    public String[] getTopics( ) {
        String[] topics_array = new String[1];
        return topics.toArray(topics_array);
    }

    public int getRNum( ) {
        return rnum;
    }
    
    public int getMaxFreq( ) {
        return fMaxFreq;
    }
    
    /**
     * Returns the (absolute) frequency for a given term in the document.
     * If the term is not present in the frequency mapping, 0 is returned.
     * 
     * @param term
     *            A term
     * @return The frequency of the term in the document of 0, if not present.
     */
    public int getFrequency( String term ) {
        if ( term != null && fFreqs != null && fFreqs.containsKey( term ) ) {
            return fFreqs.get( term );
        }
        return 0;
    }
    
    /**
     * Updates the weight for a given term
     * 
     * @param term
     *            A term.
     * @param weight
     *            The new value for the term weight.
     */
    public void setWeight( String term, double weight ) {
        if ( fWeights == null ) {
            fWeights = new HashMap<String,Double>( );
        }
        fWeights.put( term, weight );
    }
    
    /**
     * Returns the weight for a given term in the document. If the term is
     * not present in the weight mapping, 0 is returned.
     * 
     * @param term
     *            A term
     * @return The weight of the term in the document of 0, if not present.
     */
    public Double getWeight( String term ) {
        if ( term != null && fWeights != null && fWeights.containsKey( term ) ) {
            return fWeights.get( term );
        }
        return 0.0;
    }
    
    @Override
    public boolean equals( Object o ) {
        if ( o instanceof Document ) {
            Document document = ( Document ) o;
            return fName.equals( document );
        }
        return false;
    }
    
    @Override
    public int hashCode( ) {
        return fName.hashCode( );
    }
}
