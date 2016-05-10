import java.util.HashMap;

public class Tuple {
	
	public int frequency;
	
	public HashMap<String, Integer> postings;
	
	public Tuple(String term) {
		this.frequency = 0;
		this.postings = new HashMap<String, Integer>();
	}
	
	public void addPosting(String document) {
		if(this.postings.containsKey(document)) {
			this.postings.put(document, (this.postings.get(document) + 1));
		} else {
			this.postings.put(document, 1);
			this.frequency++;
		}
	}
	
	public int getPosting(String document) {
		if(this.postings.get(document) != null) {
			return this.postings.get(document);
		} else {
			return 0;
		}
	}
	
}
