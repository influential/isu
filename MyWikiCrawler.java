import java.io.IOException;

public class MyWikiCrawler {

	public static void main(String[] args) throws IOException {
		String[] topics = {"swimming", "greece"};
		WikiCrawler w = new WikiCrawler("/wiki/Olympic_Games", topics, 1000, "MyWikiGraph.txt");
		w.crawl();
	}

}
