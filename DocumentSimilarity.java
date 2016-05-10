import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashMap;

public class DocumentSimilarity {
	
	public static final int SHINGLE_SIZE = 8;
	
	public static void main(String[] args) {
		String d1 = documentToString("shakespeare1.txt");
		String d2 = documentToString("shakespeare2.txt");
		long start = 0;
		long end = 0;
		double result = 0;
		System.out.println("----- Brute Force Test -----");
		start = System.currentTimeMillis();
		result = bruteForceSimilarity(d1, d2);
		end = System.currentTimeMillis();
		System.out.println("Document Similarity: " + result);
		System.out.println("Execution Time = " + (end - start) + "ms");
		System.out.println("----- Hash Based Test -----");
		start = System.currentTimeMillis();
		result = hashBasedSimilarity(d1, d2);
		end = System.currentTimeMillis();
		System.out.println("Document Similarity: " + result);
		System.out.println("Execution Time = " + (end - start) + "ms");
		System.out.println("----- Karp-Rabin Test -----");
		start = System.currentTimeMillis();
		result = krSimilarity(d1, d2);
		end = System.currentTimeMillis();
		System.out.println("Document Similarity: " + result);
		System.out.println("Execution Time = " + (end - start) + "ms");
	}
	
	public static String documentToString(String fileName) {
		String document = "";
		try {
			BufferedReader br = new BufferedReader(new FileReader(fileName));
			String line = br.readLine();
			while(line != null) {
				document += line;
				line = br.readLine();
			}
			br.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return document.replaceAll("[:;,.\\s]", "").toLowerCase();
	}
	
	public static int hashValue(String s) {
		long hash = s.charAt(s.length() - 1);
		for(int i = 1; i < s.length(); i++) {
			long add = (s.charAt(s.length() - i - 1) * ((long) Math.pow(31, i)));
			hash += add;
		}
		hash = hash % Integer.MAX_VALUE;
		return (int) hash;
	}
	
	public static int rollOverHash(int hashVal, char firstChar, char nextChar) {
		long last = (long) firstChar * (long) Math.pow(31, SHINGLE_SIZE - 1);
		long hash = hashVal - last;
		hash = (hash * 31) + nextChar;
		return (int) (hash % Integer.MAX_VALUE);
	}
	
	public static double bruteForceSimilarity(String s1, String s2) {
		int counter = 0;
		for(int i = 0; i < s2.length() - SHINGLE_SIZE + 1; i++) {
			for(int j = 0; j < s1.length() - SHINGLE_SIZE + 1; j++) {
				if(s1.substring(j, j + SHINGLE_SIZE).equals(s2.substring(i, i + SHINGLE_SIZE))) {
					counter++;
					break;
				}
			}
		}
		return (double) counter / (s1.length() + s2.length() - (SHINGLE_SIZE * 2));
	}
	
	public static double hashBasedSimilarity(String s1, String s2) {
		int counter = 0;
		for(int i = 0; i < s2.length() - SHINGLE_SIZE + 1; i++) {
			for(int j = 0; j < s1.length() - SHINGLE_SIZE + 1; j++) {
				if(s2.substring(i, i + SHINGLE_SIZE).hashCode() == s1.substring(j, j + SHINGLE_SIZE).hashCode()) {
					counter++;
					break;
				}
			}
		}
		return (double) counter / (s1.length() + s2.length() - (SHINGLE_SIZE * 2));
	}
	
	public static double krSimilarity(String s1, String s2) {
		int counter = 0;
		long hash1 = hashValue(s1.substring(0, SHINGLE_SIZE));
		long hash2 = hashValue(s2.substring(0, SHINGLE_SIZE));
		
		HashMap<Long, Long> map = new HashMap<Long, Long>();
		map.put(hash1, hash1);
		for(int i = 1; i < s1.length() - SHINGLE_SIZE + 1; i++) {
			hash1 = rollOverHash((int) hash1, s1.charAt(i - 1), s1.charAt(i + SHINGLE_SIZE - 1));
			map.put(hash1, hash1);
		}
		for(int i = 1; i < s2.length() - SHINGLE_SIZE + 1; i++) {
			if(map.get(hash2) != null) counter++;
			hash2 = rollOverHash((int) hash2, s2.charAt(i - 1), s2.charAt(i + SHINGLE_SIZE - 1));
		}
		
		return (double) counter / (s1.length() + s2.length() - (SHINGLE_SIZE * 2));
	}

}
