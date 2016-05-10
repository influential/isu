import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.Set;

public class Vertex {
		
		public String url;
		
		public Set<Vertex> children;
		
		public Set<Vertex> parents;
		
		public Vertex(String url) {
			this.url = url;
			this.children = new LinkedHashSet<Vertex>();
			this.parents = new LinkedHashSet<Vertex>();
		}
		
		public void addEdge(Vertex v) {
			this.children.add(v);
		}
		
		public void addParent(Vertex v) {
			this.parents.add(v);
		}
		
		@Override
		public boolean equals(Object o) {
		    return o != null && o instanceof Vertex && this.url.equals(((Vertex) o).url);
		}
		
		@Override
		public int hashCode() {
		    StringBuffer buffer = new StringBuffer();
		    buffer.append(this.url);
		    return buffer.toString().hashCode();
		}
		
		static Comparator<Vertex> outDegreeComparator() {
	        return new Comparator<Vertex>() {
				@Override
				public int compare(Vertex v1, Vertex v2) {
					int s1 = v1.children.size();
					int s2 = v2.children.size();
					if(s1 > s2) {
						return -1;
					} else if(s1 < s2) {
						return 1;
					} else {
						return 0;
					}
				}
	        };
	    }
		
		static Comparator<Vertex> inDegreeComparator() {
	        return new Comparator<Vertex>() {
				@Override
				public int compare(Vertex v1, Vertex v2) {
					int s1 = v1.parents.size();
					int s2 = v2.parents.size();
					if(s1 > s2) {
						return -1;
					} else if(s1 < s2) {
						return 1;
					} else {
						return 0;
					}
				}
	        };
	    }
		
	}