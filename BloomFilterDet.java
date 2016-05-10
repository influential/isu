import java.util.BitSet;

public class BloomFilterDet {
	
	private final int PRIME32 = 16777619;
	
	private final int HASH32 = 12345678;
	
	protected BitSet bitArray;
	
	protected int filterSize;
	
	protected int numHashes;
	
	protected int dataSize;
	
	public BloomFilterDet(int setSize, int bitsPerElement) {
		this.filterSize = setSize * bitsPerElement;
		this.bitArray = new BitSet(this.filterSize);
		this.numHashes = (int) Math.round(Math.log(2) * bitsPerElement);
	}
	
	public void add(String s) {
		s.toLowerCase();
		long hash = this.HASH32;
		for(int i = 0; i < this.numHashes; i++) {
			for(int j = 0; j < s.length(); j++) {
	        	hash = (hash * this.PRIME32) % this.filterSize;
	            hash ^= s.charAt(j); 
	        }
			this.bitArray.set((int) hash);
		}
		this.dataSize++;
	}
	
	public boolean appears(String s) {
		s.toLowerCase();
		long hash = this.HASH32;
		for(int i = 0; i < this.numHashes; i++) {
			for(int j = 0; j < s.length(); j++) {
	        	hash = (hash * this.PRIME32) % this.filterSize;
	            hash ^= s.charAt(j);   
	        }
			if(!this.bitArray.get((int) hash)) return false;
		}
		return true;
	}
	
	public int filterSize() {
		return this.filterSize;
	}
	
	public int numHashes() {
		return this.numHashes;
	}
	
	public int dataSize() {
		return this.dataSize;
	}
	
}
