import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;

public class BloomRunner {

	public static void main(String[] args) {
		
		ArrayList<String> list = new ArrayList<String>();
		
		try {
			BufferedReader br = new BufferedReader(new FileReader(args[0]));
			String line = br.readLine();
			while(line != null && line.length() >= 14) {
				list.add(line.split("\\s+")[0]);
				line = br.readLine();
			}
			br.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		BloomJoin join = new BloomJoin(args[0], args[1]);
		join.server1();
		join.server2();
		join.join();
	
	}

}
