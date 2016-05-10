import java.io.IOException;

public class WikiTennisRanker {

	public static void main(String[] args) throws IOException {
		String[] topics = {"tennis", "grand slam"};
		WikiCrawler w = new WikiCrawler("/wiki/Tennis", topics, 10, "WikiTennisGraph.txt");
		w.crawl();
		PageRank rank1 = new PageRank("WikiTennisGraph.txt", .01);
		rank1.print(15);
		PageRank rank2 = new PageRank("WikiTennisGraph.txt", .005);
		rank2.print(15);
	}

}
