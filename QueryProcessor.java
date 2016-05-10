import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Scanner;

public class QueryProcessor {

	public static void main(String[] args) throws IOException {
		QueryProcessor q = new QueryProcessor("sample");
	}
	
	public WordIndex singleWord;
	
	public BiWordIndex biWord;
	
	public QueryProcessor(String folder) throws IOException {
		this.singleWord = new WordIndex(folder);
		this.biWord = new BiWordIndex(folder);
		scan();
	}
	
	public void scan() {
		Scanner scan = new Scanner(System.in);
		System.out.println("Enter Query");
		while(scan.hasNext()) {
		    String query = scan.nextLine();
		    System.out.println("Enter Number of Results to Print");
		    String number = scan.nextLine();
		    retrieve(query, Integer.parseInt(number));
		    System.out.println("\nEnter Query");
		}
		scan.close();
	}
	
	public ArrayList<Double> queryVector(String query) {
		ArrayList<Double> vector = new ArrayList<Double>();
		HashMap<String, Integer> terms = new HashMap<String, Integer>();
		for(String word : this.singleWord.parseText(query)) {
			if(terms.get(word) != null) {
				terms.put(word, (terms.get(word) + 1));
			} else {
				terms.put(word, 1);
			}
		}
		for(String key : this.singleWord.index.keySet()) {
			if(terms.containsKey(key)) {
				vector.add(Math.log(1.0 + (double) terms.get(key)) / Math.log(2));
			} else {
				vector.add(0.0);
			}
		}
		return vector;
	}
	
	public ArrayList<Double> documentVector(String query, String document) {
		ArrayList<Double> vector = new ArrayList<Double>();
		for(String key : this.singleWord.index.keySet()) {
			vector.add(this.singleWord.weight(key, document));
		}
		return vector;
	}
	
	public double cosineSimilarity(ArrayList<Double> v1, ArrayList<Double> v2) {
		double dot = 0; double s1 = 0; double s2 = 0;
		for (int i = 0; i < v1.size(); i++) {
	        dot += v1.get(i) * v2.get(i);
	        s1 += Math.pow(v1.get(i), 2);
	        s2 += Math.pow(v2.get(i), 2);
	    }   
		return dot / (Math.sqrt(s1) * Math.sqrt(s2));
	}
	
	public void retrieve(String query, int number) {
		String[] queryBiTerms = this.biWord.parseText(query);
		ArrayList<Document> documents = new ArrayList<Document>();
		ArrayList<Double> v1 = queryVector(query);
		for(File file : this.singleWord.files) {
			Document d = new Document(file.getName());
			d.similarity = cosineSimilarity(v1, documentVector(query, file.getName()));
			documents.add(d);
		}
		if(number > documents.size()) {
			number = documents.size() / 2;
		}
		Collections.sort(documents, Document.documentComparator());
		int trim = documents.size();
		for(int i = trim - 1; i > (2 * number) - 1; i--) {
			documents.remove(i);
		}
		for(Document d : documents) {
			for(String biTerm : queryBiTerms) {
				if(this.biWord.index.get(biTerm) != null) {
					if(this.biWord.index.get(biTerm).postings.containsKey(d.file)) {
						d.biWords++;
					}
				}
			}
		}
		Collections.sort(documents, Document.documentComparator());
		for(int i = 0; i < number; i++) {
			System.out.println((i + 1) + ". " + documents.get(i).file + " BiWord Matches: " + documents.get(i).biWords + " Similarity: " + documents.get(i).similarity);
		}
	}

}
