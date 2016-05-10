import java.io.BufferedReader;
import java.io.FileReader;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Random;

public class MinHash {
	
	private File folder;
	
	private int numPermutations;
	
	private int numTerms;
	
	private int numPrime;
	
	public HashSet<Permutation> permutations;

	public MinHash(String folder, int numPermutations) throws IOException {
		this.folder = new File(folder);
		this.numPermutations = numPermutations;
		this.numTerms = termCount(this.folder);
		this.numPrime = prime(this.numTerms);
		this.permutations = generatePermutations();
	}
	
	public String[] allDocs() {
		String[] documents = new String[getFiles(this.folder).length];
		File[] files = getFiles(this.folder);
		for(int i = 0; i < documents.length; i++) {
			documents[i] = files[i].getName();
		}
		return documents;
	}
	
	public double exactJaccard(String file1, String file2) throws IOException  {
		HashSet<String> set1 = termSet(new File(this.folder + "/" + file1));
		HashSet<String> set2 = termSet(new File(this.folder + "/" + file2));
		double s1 = set1.size();
		double s2 = set2.size();
		set1.retainAll(set2);
		double and = set1.size();
		return (and / (s1 + s2 - and));
	}
	
	public int[] minHashSig(String fileName) throws IOException {
		int[] min = new int[this.numPermutations];
		Arrays.fill(min, Integer.MAX_VALUE);
		File file = new File(this.folder + "/" + fileName);
		HashSet<String> terms = termSet(file);
		for(String term : terms) {
			int index = 0;
			for(Permutation p : this.permutations) {
				int hash = p.hash(term);
				if(hash < min[index]) min[index] = hash;
				index++;
			}
		}
		return min;
	}
	
	public double approximateJaccard(String file1, String file2) throws IOException {
		int[] s1 = minHashSig(file1);
		int[] s2 = minHashSig(file2);
		double count = 0;
		for(int i = 0; i < this.numPermutations; i++) {
			if(s1[i] == s2[i]) count++;
		}
		return (count / (double) numPermutations);
	}
	
	public double approximateJaccardAcc(int[] s1, int[] s2) throws IOException {
		double count = 0;
		for(int i = 0; i < this.numPermutations; i++) {
			if(s1[i] == s2[i]) count++;
		}
		return (count / (double) numPermutations);
	}
	
	public int[][] minHashMatrix() throws IOException {
		File[] files = getFiles(this.folder);
		int[][] matrix = new int[files.length][this.numPermutations];
		for(int i = 0; i < files.length; i++) {
			int[] hash = minHashSig(files[i].getName());
			for(int j = 0; j < this.numPermutations; j++) {
				matrix[i][j] = hash[j];
			}
		}
		return matrix;
	}
	
	public int numTerms() {
		return this.numTerms;
	}
	
	public int numPermutations() {
		return this.numPermutations;
	}
	
	public int termCount(File folder) throws IOException {
		HashSet<String> terms = new HashSet<String>();
		File[] files = getFiles(this.folder);
		for(File file : files) {
			terms.addAll(termSet(file));
		}
		return terms.size();
	}
	
	public HashSet<String> termSet(File file) throws IOException {
		HashSet<String> terms = new HashSet<String>();
		FileReader fr = new FileReader(file);
		BufferedReader br = new BufferedReader(fr);
		String document = "";
		String line = br.readLine();
		while(line != null) {
			document += line;
			line = br.readLine();
		}
		String[] words = document.replaceAll("[.,:;']", "").toLowerCase().split("\\s+");
		for(String word : words) {
			if(word.length() > 1 && !word.equals("the")) terms.add(word);
		}

		if(br != null) br.close();
		if(fr != null) fr.close();
		return terms;
	}
	
	public int prime(int terms) {
		Random rand = new Random();
		boolean run = true;
		long number = 0;
		while(run) {
			if(terms == 1) return 2;
			number = rand.nextInt(terms) + terms + 1;
			int divide = 3;
	        while((divide <= Math.sqrt(number)) && (number % divide != 0) && (number % 2 != 0)) divide += 2;
	       if((number % divide != 0) && (number % 2 != 0)) run = false;
		}
		return (int) number;
    }
	
	public HashSet<Permutation> generatePermutations() {
		HashSet<Permutation> permutations = new HashSet<Permutation>();
		Random rand = new Random();
		for(int i = 0; i < this.numPermutations; i++) {
			int a = rand.nextInt(this.numPrime - 2) + 1;
			int b = rand.nextInt(this.numPrime - 2) + 1;
			Permutation p = new Permutation(this.numPrime, a, b);
			if(!permutations.contains(p)) permutations.add(p);
			else i--;
		}
		return permutations;
	}
	
	public File[] getFiles(File folder) {
		FileFilter filter = new FileFilter() {
		    public boolean accept(File file) {
		        return file.getName().contains(".txt");
		    }
		};
		return folder.listFiles(filter);
	}
	
}
