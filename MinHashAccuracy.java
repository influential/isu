import java.io.IOException;

public class MinHashAccuracy {
	
	public static void main(String[] args) throws IOException {
		if(args.length == 3) {
			String name = args[0];
			int permutations = Integer.parseInt(args[1]);
			double error = Double.parseDouble(args[2]);
			MinHash min = new MinHash(name, permutations);
			String[] documents = min.allDocs();
			double exact = 0; double about = 0;
			String s1 = ""; String s2 = "";
			int count = 0; int total = 0;
			int[][] matrix = min.minHashMatrix();
			int[] m1; int[] m2;
			for(int i = 0; i < documents.length; i++) {
				s1 = documents[i];
				m1 = matrix[i];
				for(int j = i + 1; j < documents.length; j++) {
					s2 = documents[j];
					m2 = matrix[j];
					exact = min.exactJaccard(s1, s2);
					about = min.approximateJaccardAcc(m1, m2);
					if(Math.abs(exact - about) > error) count++;
					total++;
				}
			}
			System.out.println("Total: " + count + "/" + total);
		} else {
			System.out.println("INVALID ARGUMENTS");
		}
	}

}
