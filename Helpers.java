import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import java.util.Vector;

//import Document;



/**
 * Provides static methods used by various modules.
 * 
 * @author Eric Hildebrand
 * 
 *         Part of the Information Retrieval 2012 Basic IR models project.
 */
public class Helpers {
    

    /**
     * Detects if the term is a stop word or not
     * 
     * @param term
     *            A word
     * @return True if it is indeed a stop word
     *         False if it's not
     */
    public static Boolean stopWordDetect(String term) {
        StopWord stopword = new StopWord();
        return stopword.detect(term.toLowerCase());
    }

    /**
     * Stems a given word.
     * 
     * @param term
     *            A word.
     * @return The stemmed form of the word.
     */
    public static String stem( String term ) {
        Stemmer stemmer = new Stemmer( );
        char[] letters = term.toLowerCase( ).toCharArray( );
        for ( char letter : letters ) {
            stemmer.add( letter );
        }
        stemmer.stem( );
        return stemmer.toString( );
    }
    
    /**
     * Reads and returns the text contained in a given file.
     * 
     * @param path
     *            The path to a text file.
     * @return The text contained in the file.
     */
    public static String readFile( String path ) {
        StringBuilder res = new StringBuilder( );
        try {
            BufferedReader in = new BufferedReader( new FileReader( path ) );
            String line = null;
            while ( ( line = in.readLine( ) ) != null ) {
                res.append( new String( line.getBytes( ), "UTF-8" ) + "\n" );
            }
            in.close( );
        } catch ( IOException e ) {
            e.printStackTrace( );
        }
        return res.toString( );
    }
    
    /**
     * Parses the text from a Cystic Fibrosis data file and creates documents
     * 
     * @param path
     * The path of the data file that needs to be parsed
     * 
     * @return documents
     * The list of documents created from the data file
     */
    public static Document[] parse(String path) {
      Vector<Document> documents = new Vector<Document>();
      Document[] documents_array = new Document[1];
      Document document = new Document(-1);
      int count = 0;
      int committed = 0;
      boolean recording = false;
      boolean topic_recording = false;
      try {
        BufferedReader in = new BufferedReader( new FileReader(path) );
        String line = null;
        while ( (line = in.readLine() ) != null) {
          String[] parts = line.split(" ");
          if(parts[0].equals("PN")) {
            if(committed < count) {
                documents.add(document);
                committed++;
            }

            recording = false;
            topic_recording = false;
            continue;
        }
          else if(parts[0].equals("RN")) {
            document = new Document(Integer.parseInt(parts[1]));
            count++;
            recording = false;
            topic_recording = false;
          }
          else if(parts[0].equals("AN") || parts[0].equals("AU") || parts[0].equals("SO") || parts[0].equals("RF") || parts[0].equals("CT")) {
            recording = false;
            topic_recording = false;
              continue;
          }
          else if(parts[0].equals("TI")) {
              //Title
              recording = true;
              topic_recording = false;
              int c = 0;
              for (String term : parts) {
                  if (c==0) {
                    c++;
                    continue;
                  }
                  else {
                    document.add(term);
                  }
              }
          }
          else if(parts[0].equals("AB")) {
            recording = true;
            topic_recording = false;
            int c = 0;
            for (String term : parts) {
                  if (c==0) {
                    c++;
                    continue;
                  }
                  else {
                    document.add(term);
                  }
            }
          }
          else if(parts[0].equals("MJ") || parts[0].equals("MN")) {
            recording = false;
            topic_recording = true;
            String[] parts_t = line.split("[ \\.,:]");
            int c = 0;
            for (String term : parts_t) {
                if (c==0) {
                    c++;
                    continue;
                }
                else {
                    if(term.length()>2) {
                        document.addTopic(term);
                    }
                }
            }
          }
          else {
            if(recording) {
                document.add(line, true);
            }
            else if(topic_recording) {
                String[] parts_t = line.split("[ \\.,:]");
                for (String term : parts_t) {
                    if (term.length()>2) {
                        document.addTopic(term);
                    }
                }
            }
          }
        }

        if(committed < count) {
            documents.add(document);
            committed++;
        }

        in.close();
      } catch(IOException e) {
        e.printStackTrace();
      }
      return documents.toArray(documents_array);
    }
    
    /**
     * Parses the query file in the Cystic Fibrosis collection and creates query objects
     * 
     * @param path
     *        Path of the query file
     * 
     * @return queries
     *         Array of query objects
     */

    public static Query[] parseQueryFile(String path) {
        Vector<Query> queries = new Vector<Query>();
        Query[] queries_array = new Query[1];
        Query query = new Query(-1);
        boolean recording = false;
        boolean doc_recording = false;
        int count = 0;
        int committed = 0;
        int nRelDocs = 0;

        try {
            BufferedReader in = new BufferedReader( new FileReader(path) );
            String line = null;
            String text = "";
            while ( (line = in.readLine() ) != null) {
                String[] parts = line.split(" ");
                if(parts.length==0)
                    continue;
                if(parts[0].equals("QN")) {

                    if (committed < count) {
                        queries.add(query);
                        committed++;
                    }

                    query = new Query(Integer.parseInt(parts[1]));
                    count++;
                    nRelDocs = 0;
                    recording = false;
                    doc_recording = false;
                }
                else if(parts[0].equals("QU")) {
                    recording = true;
                    doc_recording = false;
                    int c = 0;
                    for (String term : parts) {
                        if(c==0) {
                            c++;
                            continue;
                        }
                        text += " "+term;
                    }
                }
                else if(parts[0].equals("NR")) {
                    query.addText(text);
                    text = "";
                    nRelDocs = Integer.parseInt(parts[1]);
                    recording = false;
                    doc_recording = false;
                }
                else if(parts[0].equals("RD")) {
                    recording = false;
                    doc_recording = true;
                    // Add relevant docs with scores
                    int c=0;
                    int rnum = -1;
                    int score = -1;
                    for (String term : parts) {
                        if (c==0) {
                            c++;
                            continue;
                        }
                        if(c%2==1) {
                            if(term.equals("")) {
                                continue;
                            }
                            c++; 
                            rnum = Integer.parseInt(term);
                        }
                        else {
                            if(term.equals("")) {
                                continue;
                            }
                            c++;
                            int i1 = Character.getNumericValue(term.charAt(0));
                            int i2 = Character.getNumericValue(term.charAt(1));
                            int i3 = Character.getNumericValue(term.charAt(2));
                            int i4 = Character.getNumericValue(term.charAt(3));
                            score = i1 + i2 + i3 + i4;
                            query.addRelDoc(rnum, score);
                        }
                    }
                }
                else {
                    if(recording) {
                        for (String term : parts) {
                            if(term.equals(""))
                                continue;
                            text += " "+term;
                        }
                    }
                    else if(doc_recording){
                        // Add relevant docs with scores
                        int c = 0;
                        int rnum = -1;
                        int score = -1;
                        for (String term : parts) {
                            if(term.equals("")) {
                                continue;
                            }
                            
                            if (c%2==0) {
                                rnum = Integer.parseInt(term);
                            }
                            else {
                                int i1 = Character.getNumericValue(term.charAt(0));
                                int i2 = Character.getNumericValue(term.charAt(1));
                                int i3 = Character.getNumericValue(term.charAt(2));
                                int i4 = Character.getNumericValue(term.charAt(3));
                                score = i1 + i2 + i3 + i4;
                                query.addRelDoc(rnum, score);
                            }
                            c++;
                        }
                    }
                }

                if (committed < count) {
                    queries.add(query);
                    committed++;
                }
            }
        } catch(IOException e) {
            e.printStackTrace();
        }
        return queries.toArray(queries_array);
    }

    /**
     * Updates the weight of a given document for a given term.
     * 
     * @param document
     *            The document whose weight should be updated.
     * @param term
     *            The term whose weight should be updated.
     * @param documents
     *            All documents relevant for updating the term weight.
     */
    public static void setTfIdfWeight( Document document, String term, Document[] documents ) {
        double tf = document.getMaxFreq( ) == 0 ? 0.0 : ( double ) document.getFrequency( term ) / document.getMaxFreq( );
        if ( Double.isNaN( tf ) ) {
            tf = 0.0;
        }
        int n = 0;
        for ( Document doc : documents ) {
            if ( doc.getFrequency( term ) > 0 ) {
                n++;
            }
        }
        double idf = Math.log( ( double ) documents.length / n ) / Math.log( 2 );
        if ( Double.isNaN( idf ) ) {
            idf = 0.0;
        }
        document.setWeight( term, tf * idf );
    }
    
}
