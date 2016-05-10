import java.util.Comparator;

public class Rank {
	
	public double rank;
	
	public String url;

	public Rank(double rank, String url) {
		this.rank = rank;
		this.url = url;
	}
	
	public void setRank(double rank) {
		this.rank = rank;
	}
	
	static Comparator<Rank> rankComparator() {
        return new Comparator<Rank>() {
			@Override
			public int compare(Rank r1, Rank r2) {
				if(r1.rank > r2.rank) {
					return -1;
				} else if(r1.rank < r2.rank) {
					return 1;
				} else {
					return 0;
				}
			}
        };
    }
	
}
