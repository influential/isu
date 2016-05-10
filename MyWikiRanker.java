import java.io.IOException;

public class MyWikiRanker {

	public static void main(String[] args) throws IOException {
		String[] topics = {"swim", "greece", "games"};
		WikiCrawler w = new WikiCrawler("/wiki/Olympic_Games", topics, 1000, "MyWikiGraph.txt");
		w.crawl();
		PageRank rank1 = new PageRank("MyWikiGraph.txt", .01);
		rank1.print(15);
		PageRank rank2 = new PageRank("MyWikiGraph.txt", .005);
		rank2.print(15);
	}

}
