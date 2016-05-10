import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class PageRank {
	
	public String file;
	
	public double epsilon;
	
	public double beta;
	
	public int vertexCount;
	
	public int edgeCount;
	
	public HashMap<String, Vertex> vertices;
	
	public HashMap<String, Rank> rank1;
	
	public HashMap<String, Rank> rank2;
	
	public int test = 0;

	public PageRank(String file, double epsilon) {
		this.file = file;
		this.epsilon = epsilon;
		this.beta = .85;
		this.vertices = new HashMap<String, Vertex>();
		this.rank1 = new HashMap<String, Rank>();
		this.rank2 = new HashMap<String, Rank>();
		build();
		rank();
	}
	
	public void build() {
		try {
			BufferedReader br = new BufferedReader(new FileReader(this.file));
			String line = br.readLine();
			this.vertexCount = Integer.parseInt(line);
			line = br.readLine();
			while(line != null) {
				this.edgeCount++;
				String url1 = line.split("\\s+")[0];
				String url2 = line.split("\\s+")[1];
				Vertex v1; Vertex v2;
				if(vertices.containsKey(url1)) {
					v1 = vertices.get(url1);
				} else {
					v1 = new Vertex(url1);
				}
				if(vertices.containsKey(url2)) {
					v2 = vertices.get(url2);
				} else {
					v2 = new Vertex(url2);
				}
				v1.addEdge(v2);
				v2.addParent(v1);
				this.vertices.put(url1, v1);
				this.vertices.put(url2, v2);
				line = br.readLine();
			}
			br.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void rank() {
		for(String url : this.vertices.keySet()) {
			Rank r1 = new Rank(1.0 / this.vertices.size(), url);
			Rank r2 = new Rank((1.0 - this.beta) / this.vertices.size(), url);
			this.rank1.put(url, r1);
			this.rank2.put(url, r2);
		}
		while(true) {
			for(String url : this.vertices.keySet()) {
				Vertex v = this.vertices.get(url);
				Rank r = this.rank1.get(v.url);
				if(v.children.size() > 0) {
					for(Vertex vv : v.children) {
						Rank rr = this.rank2.get(vv.url);
						double d1 = (double) v.children.size();
						double d2 = r.rank / d1;
						double d3 = this.beta * d2;
						double d4 = rr.rank + d3;
						rr.setRank(d4);
						rank2.put(vv.url, rr);
					}
				} else {
					for(String urlc : this.vertices.keySet()) {
						Rank rr = this.rank2.get(urlc);
						double d1 = (double) this.vertices.size();
						double d2 = r.rank / d1;
						double d3 = this.beta * d2;
						double d4 = rr.rank + d3;
						rr.setRank(d4);
						rank2.put(urlc, rr);
					}
				}
			}
			if(normal()) {
				break;
			}
			this.rank1 = new HashMap<String, Rank>();
			for(String url : this.vertices.keySet()) {
				this.rank1.put(url, new Rank(this.rank2.get(url).rank, url));
				this.rank2.put(url, new Rank((1.0 - this.beta) / this.vertices.size(), url));
			}
		}
	}
	
	public boolean normal() {
		double sum = 0;
		for(String url : this.vertices.keySet()) {
			sum += Math.abs(this.rank2.get(url).rank - this.rank1.get(url).rank);
		}
		if(sum <= this.epsilon) {
			return true;
		} else {
			return false;
		}
	}
	
	public double pageRankOf(String url) {
		return this.rank2.get(url).rank;
	}
	
	public int outDegreeOf(String url) {
		return this.vertices.get(url).children.size();
	}
	
	public int inDegreeOf(String url) {
		return this.vertices.get(url).parents.size();
	}
	
	public int numEdges() {
		return this.edgeCount;
	}
	
	public String[] topKPageRank(int max) {
		String[] top = new String[max];
		ArrayList<Rank> list = new ArrayList<Rank>(this.rank2.values());
		Collections.sort(list, Rank.rankComparator());
		if(max > list.size()) max = list.size();
		for(int i = 0; i < max; i++) {
			top[i] = list.get(i).url;
			System.out.println(list.get(i).rank);
		}
		return top;
	}
	
	public String[] topKInDegree(int max) {
		List<Vertex> list = new ArrayList<Vertex>(this.vertices.values());
		Collections.sort(list, Vertex.inDegreeComparator());
		if(max > list.size()) max = list.size();
		String[] results = new String[max];
		for(int i = 0; i < max; i++) {
			results[i] = list.get(i).url;
		}
		return results;
	}
	
	public String[] topKOutDegree(int max) {
		List<Vertex> list = new ArrayList<Vertex>(this.vertices.values());
		Collections.sort(list, Vertex.outDegreeComparator());
		if(max > list.size()) max = list.size();
		String[] results = new String[max];
		for(int i = 0; i < max; i++) {
			results[i] = list.get(i).url;
		}
		return results;
	}
	
	public double similarity(String[] top1, String[] top2) {
		Set<String> set1 = new HashSet<String>(Arrays.asList(top1));
		Set<String> set2 = new HashSet<String>(Arrays.asList(top2));
		double s1 = set1.size();
		double s2 = set2.size();
		set1.retainAll(set2);
		double and = set1.size();
		return (and / (s1 + s2 - and));
	}
	
	public void print(int top) {
		int counter = 1;
		System.out.println("\n------------------- Epsilon: " + this.epsilon + "-------------------");
		System.out.println("\nHighest Page Rank\n");
		String[] topPageRank = topKPageRank(top);
		for(String s : topPageRank) {
			System.out.println(counter +": " + s);
			counter++;
		}
		counter = 1;
		System.out.println("\nHighest In Degree\n");
		String[] topInDegree = topKInDegree(top);
		for(String s : topInDegree) {
			System.out.println(counter +": " + s);
			counter++;
		}
		counter = 1;
		System.out.println("\nHighest Out Degree\n");
		String[] topOutDegree = topKOutDegree(top);
		for(String s : topOutDegree) {
			System.out.println(counter +": " + s);
			counter++;
		}
		System.out.println("\nSimilarity of Page Rank to In Degree\n");
		System.out.println(similarity(topPageRank, topInDegree));
		System.out.println("\nSimilarity of Page Rank to Out Degree\n");
		System.out.println(similarity(topPageRank, topOutDegree));
		System.out.println("\nSimilarity of In Degree to Out Degree\n");
		System.out.println(similarity(topInDegree, topOutDegree));
	}
	
}
