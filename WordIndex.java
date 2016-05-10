import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class WordIndex {
	
	public String folder;
	
	public File[] files;
	
	public HashMap<String, Tuple> index;
	
	public WordIndex(String folder) throws IOException {
		this.folder = folder;
		this.files = getFiles(new File(this.folder));
		this.index = new HashMap<String, Tuple>();
		buildIndex();
	}
	
	public void buildIndex() throws IOException {
		for(File file : this.files) {
			FileReader fr = new FileReader(file);
			BufferedReader br = new BufferedReader(fr);
			String document = "";
			String line = br.readLine();
			while(line != null) {
				document += line;
				line = br.readLine();
			}
			for(String word : parseText(document)) {
				Tuple t;
				if(this.index.containsKey(word)) {
					t = this.index.get(word);
				} else {
					t = new Tuple(word);
				}
				t.addPosting(file.getName());
				this.index.put(word, t);
			}
			if(br != null) br.close();
			if(fr != null) fr.close();
		}
	}
	
	public ArrayList<Pair> postingsList(String t) {
		ArrayList<Pair> pairs = new ArrayList<Pair>();
		for(String document : this.index.get(t).postings.keySet()) {
			pairs.add(new Pair(document, this.index.get(t).postings.get(document)));
		}
		return pairs;
	}

	public void printPostingsList(String t) {
		for(Pair p : this.postingsList(t)) {
			System.out.println("Document: " + p.document + " Frequency: " + p.frequency);
		}
	}
	
	public double weight(String t, String d) {
		int f1 = this.index.get(t).getPosting(d);
		int f2 = this.index.get(t).frequency;
		if(f1 == 0 || f2 == 0) return 0;
		return ((Math.log(1.0 + (double) this.index.get(t).getPosting(d)) / Math.log(2)) * Math.log10((double) this.files.length / (double) this.index.get(t).frequency));
	}
	
	public File[] getFiles(File folder) {
		FileFilter filter = new FileFilter() {
		    public boolean accept(File file) {
		        return file.getName().contains(".txt");
		    }
		};
		return folder.listFiles(filter);
	}
	
	public String[] parseText(String text) {
		ArrayList<String> terms = new ArrayList<String>();
		String[] words = text.replaceAll("[.,:;']", "").toLowerCase().split("\\s+");
		for(String word : words) {
			if(word.length() > 2 && !word.equals("the")) {
				terms.add(word);
			}
		}
		return terms.toArray(new String[0]);
	}
	
}
