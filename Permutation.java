public class Permutation {
	
	public int a;
	
	public int b;
	
	public int prime;
	
	public Permutation(int prime, int a, int b) {
		this.prime = prime;
		this.a = a;
		this.b = b;
	}
	
	public int hash(String s) {
		int hash = 0;
		for(int i = 0; i < s.length(); i++) {
			hash = ((hash * this.a) + this.b) % this.prime;
			hash ^= s.charAt(i); 
		}
		return hash;
	}
	
	public boolean equals(Object o) {
		if(o == null || !(o instanceof Permutation)) return false;
		Permutation p = (Permutation) o;
		return(this.a == p.a && this.b == p.b);
	}
	
}