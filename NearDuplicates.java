import java.io.IOException;
import java.util.ArrayList;

public class NearDuplicates {
	
	public static void main(String[] args) throws IOException {
		if(args.length == 5) {
			String folder = args[0];
			int permutations = Integer.parseInt(args[1]);
			int bands= Integer.parseInt(args[2]);
			double delta = Double.parseDouble(args[3]);
			String docName = args[4];
			MinHash min = new MinHash(folder, permutations);
			int[][] matrix = min.minHashMatrix();
			String[] names = min.allDocs();
			LSH lsh = new LSH(matrix, names, bands);
			ArrayList<String> result = lsh.nearDuplicatesOf(docName);
			result = lsh.falsePositives(result, docName, delta);
			for(String s : result) {
				System.out.println(s);
			}
		} else {
			System.out.println("INVALID ARGUMENTS - Arguments Order: Folder, Permutations, Bands, Threshold, Document");
		}
	}
	
}
