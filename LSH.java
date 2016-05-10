import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;

public class LSH {

	private int[][] matrix;
	
	private int bands;
	
	private String[] documents;
	
	private ArrayList<HashMap<Integer, HashSet<String>>> table;
	
	private int prime;
	
	private int a;
	
	private int b;
	
	public LSH(int[][] minHashMatrix, String[] docNames, int bands) {
		this.matrix = minHashMatrix;
		this.documents = docNames;
		this.bands = bands;
		generateHashFunction(this.matrix.length);
		this.table = new ArrayList<HashMap<Integer, HashSet<String>>>();
		for(int i = 0; i < bands; i++) {
			this.table.add(new HashMap<Integer, HashSet<String>>());
		}
		int groups = this.matrix[0].length / bands;
		int band = 0; int hash = 0;
		for(int i = 0; i < this.documents.length; i++) {
			for(int j = 0; j < this.matrix[0].length; j++) {
				band = j / groups;
				hash = ((this.a * matrix[i][j] + this.b) + hash) % this.prime;
				if(j > 0) {
					if((j + 1) % groups == 0 && band != (bands - 1) || (j == this.matrix[0].length - 1)) {
						if(band == bands) band -= 1;
						HashSet<String> list = this.table.get(band).get(hash);
						if(list == null) {
							list = new HashSet<String>();
							list.add(this.documents[i]);
						} else {
							list.add(this.documents[i]);
						}
						this.table.get(band).put(hash, list);
						hash = 0; band = 0;
					}
				}
			}

		}
	}
	
	public ArrayList<String> nearDuplicatesOf(String docName) {
		HashSet<String> similar = new HashSet<String>();
		ArrayList<String> names = new ArrayList<String>();
		int index = 0;
		for(int i = 0; i < this.documents.length; i++) {
			if(this.documents[i].equals(docName)) {
				index = i;
				break;
			}
		}
		int band = 0; int hash = 0;
		int groups = this.matrix[0].length / this.bands;
		for(int i = 0; i < this.matrix[0].length; i++) {
			band = i / groups;
			hash = ((this.a * matrix[index][i] + this.b) + hash) % this.prime;
			if(i > 0) {
				if((i + 1) % groups == 0 && band != (bands - 1) || (i == this.matrix[0].length - 1)) {
					band = i / groups;
					if(band == bands) band -= 1;
					HashSet<String> list = this.table.get(band).get(hash);
					if(list != null) {
						similar.addAll(list);
					}
					hash = 0; band = 0;
				}
			}
		}
		similar.remove(docName);
		names.addAll(similar);
		return names;
	}
	
	public ArrayList<String> falsePositives(ArrayList<String> names, String docName, double delta) {
		ArrayList<String> list = new ArrayList<String>();
		int index = 0;
		for(int j = 0; j < this.documents.length; j++) {
			if(this.documents[j].equals(docName)) {
				index = j;
				break;
			}
		}
		int[] s1 = this.matrix[index];
		index = 0;
		for(int i = 0; i < names.size(); i++) {
			for(int j = 0; j < this.documents.length; j++) {
				if(this.documents[j].equals(names.get(i))) {
					index = j;
					break;
				}
			}
			int[] s2 = this.matrix[index];
			double count = 0;
			for(int k = 0; k < this.matrix[0].length; k++) {
				if(s1[k] == s2[k]) count++;
			}
			if((count / (double) this.matrix[0].length) > delta) {
				list.add(names.get(i));
			}
		}
		System.out.println("False Positives: " + (names.size() - list.size()));
		return list;
	}
	
	public void generateHashFunction(int max) {
		this.prime = prime(max);
		Random rand = new Random();
		this.a = rand.nextInt(this.prime - 2) + 1;
		this.b = rand.nextInt(this.prime - 2) + 1;
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
	
}
