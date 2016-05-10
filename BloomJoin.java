import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class BloomJoin {
	
	private BloomFilterDet bloom;
	
	private HashMap<String, String> map1;
	
	private HashMap<String, String> map2;
	
	private HashMap<String, String> join;
	
	public BloomJoin(String file1, String file2) {
		try {
			BufferedReader br1 = new BufferedReader(new FileReader(file1));
			BufferedReader br2 = new BufferedReader(new FileReader(file2));
			this.map1 = new HashMap<String, String>();
			this.map2 = new HashMap<String, String>();
			String line1 = br1.readLine();
			String line2 = br2.readLine();
			while(line1 != null && line1.split("\\s+").length == 2) {
				this.map1.put(line1.split("\\s+")[0], line1.split("\\s+")[1]);
				line1 = br1.readLine();
			}
			while(line2 != null && line2.split("\\s+").length == 2) {
				this.map2.put(line2.split("\\s+")[0], line2.split("\\s+")[1]);
				line2 = br2.readLine();
			}
			br1.close();
			br2.close();
		} catch(Exception e) {
			e.printStackTrace();
		}
		this.join = new HashMap<String, String>();
		this.bloom = new BloomFilterDet(this.map1.size(), 8);
	}
		
	public void server1() {
		for (String key : map1.keySet()) {
		    this.bloom.add(key);
		}
	}
		
	@SuppressWarnings("rawtypes")
	public void server2() {
		Iterator it = map2.entrySet().iterator();
	    while (it.hasNext()) {
	    	Map.Entry pair = (Map.Entry) it.next();
	    	if(!bloom.appears((String) pair.getKey())) {
	    		it.remove();
			}
	    }
		
		
		for (String key : map2.keySet()) {
			if(!bloom.appears(key)) {
				this.map2.remove(key);
			}
		}
	}
	
	public void join() {
		for (String key : map2.keySet()) {
			String join1 = this.map1.get(key);
			String join2 = this.map2.get(key);
			this.join.put(join1, join2);
		}
	}
	
}
