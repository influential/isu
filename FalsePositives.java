import java.util.ArrayList;

public class FalsePositives {
	
	private BloomFilterDet bloomDet;
	
	private BloomFilterRan bloomRan;
	
	public FalsePositives(int setSize, int bitsPerElement) {
		this.bloomDet = new BloomFilterDet(setSize, bitsPerElement);
		this.bloomRan = new BloomFilterRan(setSize, bitsPerElement); 
	}
	
	public void test(ArrayList<String> list) {
		int split = list.size() / 2;
		int falsePositivesDet = 0;
		int falsePositivesRan = 0;
		for(int i = 0; i < split; i++) {
			bloomDet.add(list.get(i));
			bloomRan.add(list.get(i));
		}
		for(int i = split; i < list.size(); i++) {
			if(bloomDet.appears(list.get(i))) falsePositivesDet++;
			if(bloomRan.appears(list.get(i))) falsePositivesRan++;
		}
		double percentDet = (double) falsePositivesDet / (double) bloomDet.filterSize() * 100;
		double percentRan = (double) falsePositivesRan / (double) bloomRan.filterSize() * 100;
		System.out.println("BloomFilterDet: " + falsePositivesDet + " False Positives");
		System.out.println("BloomFilterDet: " + percentDet + " % Probability");
		System.out.println("BloomFilterRan: " + falsePositivesRan + " False Positives");
		System.out.println("BloomFilterRan: " + percentRan + " % Probability");
	}
	
}
