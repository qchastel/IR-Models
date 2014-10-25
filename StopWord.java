import java.io.*;
import java.util.*;


public class StopWord {
	private static final String DEFAULT_STOPWORDS = 
    "a about add ago after all also an and another any are as at be " +
    "because been before being between big both but by came can come " +
    "could did do does due each else end far few for from get got had " +
    "has have he her here him himself his how if in into is it its " +
    "just let lie like low make many me might more most much must " +
    "my never no nor not now of off old on only or other our out over " +
    "per pre put re said same see she should since so some still such " +
    "take than that the their them then there these they this those " +
    "through to too under up use very via want was way we well were " +
    "what when where which while who will with would yes yet you your";

    public StopWord() {
    }

    public Boolean detect(String term) {
    	String[] stopwords = DEFAULT_STOPWORDS.split(" ");
    	for (String word : stopwords) {
    		if (term.equals(word)) {
    			return true;
    		}
    	}
    	return false;
    }

}