import java.util.Comparator;

public class Document {
	
	public String file;
	
	public int biWords;
	
	public double similarity;
	
	public Document(String file) {
		this.file = file;
		this.biWords = 0;
		this.similarity = 0;
	}
	
	static Comparator<Document> documentComparator() {
        return new Comparator<Document>() {
			@Override
			public int compare(Document d1, Document d2) {
				if(d1.biWords > d2.biWords) {
					return -1;
				} else if(d1.biWords < d2.biWords) {
					return 1;
				} else {
					if(d1.similarity > d2.similarity) {
						return -1;
					} else {
						return 1;
					}
				}
			}
        };
    }
	
}
