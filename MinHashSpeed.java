import java.io.IOException;

public class MinHashSpeed {
	
	@SuppressWarnings("unused")
	public static void main(String[] args) throws IOException {
		if(args.length == 2) {
			String name = args[0];
			int permutations = Integer.parseInt(args[1]);
			MinHash min = new MinHash(name, permutations);
			long start = 0; long end = 0;
			float exact = 0; float estimate = 0;
			start = System.currentTimeMillis();
			String s1 = ""; String s2 = "";
			String[] documents = min.allDocs();
			for(int i = 0; i < documents.length; i++) {
				s1 = documents[i];
				for(int j = i + 1; j < documents.length; j++) {
					s2 = documents[j];
					min.exactJaccard(s1, s2);
				}
			}
			end = System.currentTimeMillis();
			exact = (float) (end - start) / 1000;
			start = System.currentTimeMillis();
			int[][] matrix = min.minHashMatrix();
			for(int i = 0; i < matrix.length; i++) {
				for(int j = i + 1; j < matrix.length; j++) {
					int count = 0; double result = 0;
					for(int k = 0; k < min.numPermutations(); k++) {
						if(matrix[i][k] == matrix[j][k]) count++;
					}
					result = (count / (double) min.numPermutations());
				}
			}
			end = System.currentTimeMillis();
			estimate = (float) (end - start) / 1000;
			System.out.println("Time Exact: " + exact + " seconds\nTime Estimate: " + estimate + " seconds");
		} else {
			System.out.println("INVALID ARGUMENTS");
		}
	}

}
