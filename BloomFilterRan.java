import java.util.Random;

public class BloomFilterRan extends BloomFilterDet {
	
	private int MAXPRIME;
	
	private int HASHA;
	
	private int HASHB;

	public BloomFilterRan(int setSize, int bitsPerElement) {
		super(setSize, bitsPerElement);
		this.hash();
	}
	
	public void add(String s) {
		s.toLowerCase();
		long hash = this.HASHB;
		for(int i = 0; i < this.numHashes; i++) {
			for(int j = 0; j < s.length(); j++) {
				long check = (hash * this.HASHA) + s.charAt(j);
				hash = (check % this.MAXPRIME) % this.filterSize;
			}
			this.bitArray.set((int) hash);
		}
		this.dataSize++;
	}
	
	public boolean appears(String s) {
		s.toLowerCase();
		long hash = this.HASHB;
		for(int i = 0; i < this.numHashes; i++) {
			for(int j = 0; j < s.length(); j++) {
				long check = (hash * this.HASHA) + s.charAt(j);
				hash = (check % this.MAXPRIME) % this.filterSize;
			}
			if(!this.bitArray.get((int) hash)) return false;
		}
		return true;
	}
	
	public void hash() {
		this.MAXPRIME = prime(this.filterSize);
		Random rand = new Random();
		this.HASHA = rand.nextInt(this.MAXPRIME - 1) + 1;
		this.HASHB = rand.nextInt(this.MAXPRIME);
	}
	
	public int prime(int max) {
		Random rand = new Random();
		boolean run = true;
		long number = 0;
		while(run) {
			if(max == 1) return 2;
			number = rand.nextInt(max) + max + 1;
			int divide = 3;
	        while((divide <= Math.sqrt(number)) && (number % divide != 0) && (number % 2 != 0)) {
	        	divide += 2;
	        }
	       if((number % divide != 0) && (number % 2 != 0)) run = false;
		}
		return (int) number;
    }

}
