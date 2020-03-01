import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.TreeSet;

public class ModeleBooleen {
	
	public ArrayList<TreeSet<HashMap<Doc, Integer>>> and;
	public ArrayList<TreeSet<HashMap<Doc, Integer>>> or;
	public ArrayList<TreeSet<HashMap<Doc, Integer>>> not;
	public TraitementTexte tT;
	public Index i;
	public IndexInversé iI;
	
	
	public ModeleBooleen(TraitementTexte t, Index index,IndexInversé indexI) {
		tT=t;
		i=index;
		iI=indexI;
	}
	
	public ArrayList<Doc> resultat(String s1, String s2, String s3){
		and=resultatReq(tT.traiteTexte(s1));
		or=resultatReq(tT.traiteTexte(s2));
		not=resultatReq(tT.traiteTexte(s3));
		
		for (TreeSet<HashMap<Doc, Integer>> treeSet : and) {
			for (HashMap<Doc, Integer> hashMap : treeSet) {
				System.out.println(hashMap.entrySet());
					
			
			}
		}
		
		
		return null;
	}
	
	public ArrayList<TreeSet<HashMap<Doc, Integer>>> resultatReq(ArrayList<String> al){
		ArrayList<TreeSet<HashMap<Doc,Integer>>> listDoc=new ArrayList<>();
		for (String string : al) {
			listDoc.add(iI.listDoc(string));
		}
		
		return listDoc;
	}
	
	
	/*
	public String transformationEnReq(String expressionExacte,String mots,String auMoins,String pasCeMot) {
		ArrayList<String> nvExpressionExacte = new ArrayList<>();
		nvExpressionExacte = tT.traiteTexte(expressionExacte);
		if(nvExpressionExacte!=null) {
			
			return expressionExacte;
		}else {
			
			return mots+"^AND^"+auMoins+"^OR^"+auMoins+"^NOT^"+pasCeMot;
		}
	}
	*/
	/*public void requete(String arguments){
		ArrayList<String> motsTraites=tT.traiterTexte(arguments);
		ArrayList<String> docChemin = new ArrayList<>();
		
		for(String s : motsTraites) {
			docChemin.addAll(a.get(s));
		}
		
		HashMap<String,Integer> c = new HashMap<>();
		for(int i = 0; i< docChemin.size();i++) {
			c.put(docChemin.get(i), 0);
			docChemin.remove(0);
			for(int y=i;y<docChemin.size();y++) {
				if(docChemin.get(i).equals(docChemin.get(y))) {
					c.replace(docChemin.get(i), c.get(docChemin.get(i))+1);
					docChemin.remove(y);
				}
			}
		}
		
	}
	
	
	public HashSet<String> recherche(String requete){
	
		HashMap<HashMap<String,String>,Integer> incidenceTermeDocument = new HashMap<>();
		
		String[] decoupage,decoupageAND,decoupageOR,decoupageNOT;
		boolean booleenDecoupage,booleenDecoupageAND,booleenDecoupageOR,booleenDecoupageNOT;
		decoupageNOT = requete.split("^NOT^");
		if(decoupageNOT.length > 1)
			booleenDecoupageNOT=true;
		else
			booleenDecoupageNOT=false;
		decoupageOR = requete.split("^OR^");
		if(decoupageOR.length > 1)
			booleenDecoupageOR=true;
		else
			booleenDecoupageOR=false;
		decoupageAND = requete.split("^AND^");
		if(decoupageOR.length > 1)
			booleenDecoupageAND=true;
		else
			booleenDecoupageAND=false;
		
		ArrayList<String> decoupageArray = new ArrayList<>();
		
		decoupage = requete.split("^AND^","^OR^","^NOT^");
		for (int i = 0; i < decoupage.length; i++) {
			decoupageArray.add(decoupage[i]);
		}
		
		
		
	}
	*/

}
