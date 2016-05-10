import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.io.IOException;

public class WikiCrawler {
	
	public static final String BASE_URL = "https://en.wikipedia.org";

	public String fileName;

	public String seedURL;
	
	public String[] topics;

	public int max;

	public int requests;
	
	public Set<Vertex> vertices;
	
	public Set<Vertex> discovered;
	
	public LinkedList<Vertex> queue;
	
	public Set<String> restricted;

	public WikiCrawler(String seedURL, String[] topics, int max, String fileName) throws IOException {
		this.fileName = fileName;
		this.topics = topics;
		this.seedURL = seedURL;
		this.max = max;
		this.requests = 0;
		this.vertices = new LinkedHashSet<Vertex>();
		this.discovered = new HashSet<Vertex>();
		this.queue = new LinkedList<Vertex>();
		this.restricted = new HashSet<String>();
		Vertex v = new Vertex(seedURL);
		discovered.add(v);
		queue.add(v);
	}
	
	public ArrayList<String> extractLinks(String doc) {
		ArrayList<String> list = new ArrayList<String>();
		if(doc.length() > 2) {
			int f1 = doc.indexOf("<p>");
		    int f2 = doc.indexOf("<P>");
		    int first = f1;
		    if(f2 < f1 && f2 >= 0) first = f2;
		    if(first < 0) first = 0;
		    int end = doc.length() - 1;
		    if(end < 0) end = 1;
		    doc = doc.substring(first, end);
		    Set<String> links = new LinkedHashSet<String>();
			Pattern p = Pattern.compile("(?<=('|\"))/wiki/[^('|\")]+");
			Matcher m = p.matcher(doc);
			while (m.find()) {
			    String match = m.group();
			    if(!match.contains(":") && !match.contains("#") && !this.restricted.contains(match)) {
			    	links.add(match);
			    }
			}
			list.addAll(links);
		}
		return list;
	}
	
	public void crawl() throws IOException {
		fetchRobots();
		while(!this.queue.isEmpty() && this.vertices.size() < this.max) {
			Vertex v = this.queue.getFirst();
			if(hasWords(getRawPage(v.url))) {
				crawlVertex(v);
			}
			this.queue.removeFirst();
		}
		printGraph();
	}
	
	public void fetchRobots() {
		String page = "";
		try {
			URL url = new URL(BASE_URL + "/robots.txt");
		    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		    BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
		    String inputLine;
		    while ((inputLine = in.readLine()) != null) page += inputLine;
		    ArrayList<String> links = extractLinks(page);
		    this.restricted.addAll(links);
		    in.close();
		} catch(Exception e) {
			System.out.println("Robots.txt Error");
		}
	}

	public String getPage(String path) throws IOException {
		String page = "";
		try {
			URL url = new URL(BASE_URL + path);
		    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		    BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
		    String inputLine;
		    while ((inputLine = in.readLine()) != null) page += inputLine;
		    in.close();
		} catch(Exception e) {
			System.out.println("FNF");
		}
	    this.requests++;
	    return page;
	}
	
	public String getRawPage(String path) throws IOException {
		String page = "";
		try {
			URL url = new URL(BASE_URL + "/w/index.php?title=" + path.split("/")[2] + "&action=raw");
		    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		    BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
		    String inputLine;
		    while ((inputLine = in.readLine()) != null) page += inputLine;
		    in.close();
		} catch(Exception e) {
			System.out.println("FNF");
		}
	    this.requests++;
	    return page;
	}
	
	public boolean hasWords(String page) {
		page = page.toLowerCase();
		boolean contains = false;
		for(String topic : topics) {
			if(page.contains(topic.toLowerCase())) {
				contains = true;
			}
		}
		return contains;
	}
	
	public void crawlVertex(Vertex v) throws IOException {
		ArrayList<String> links = extractLinks(getPage(v.url));
		if(this.requests > 100) freeze();
		for(String link : links) {
			if(!link.equals(v.url)) {
				Vertex vv = new Vertex(link);
				v.addEdge(vv);
				if(!this.discovered.contains(vv)) {
					this.queue.addLast(vv);
					this.discovered.add(vv);
				}
			}
		}
		this.vertices.add(v);
	}
	
	public void printGraph() throws IOException {
		PrintWriter writer = new PrintWriter(this.fileName, "UTF-8");
		writer.println(this.vertices.size());
		for(Vertex v : this.vertices) {
			for(Vertex vv : v.children) {
				if(this.vertices.contains(vv)) {
					writer.println(v.url + " " + vv.url);
				}
			}
		}
		writer.close();
	}
	
	public void freeze() {
		System.out.println("PAUSING...");
		try {
		    Thread.sleep(5000);
		} catch(InterruptedException ex) {
		    Thread.currentThread().interrupt();
		}
		this.requests = 0;
	}
	
}
