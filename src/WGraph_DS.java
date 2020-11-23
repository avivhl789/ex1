package ex1.src;


import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

public class WGraph_DS implements weighted_graph{

	private HashMap<Integer, node_info> gr = new HashMap<Integer,node_info>();
	private int numofedge;
	private int mc;
	public void addNode(nodeinfo n) {
		if(gr.containsKey(n.getKey()))return;
		mc++;
		gr.put(n.getKey(),n);
	}
	@Override
	public boolean equals(Object o) {
		if ( o.getClass() != this.getClass())  
			return false;       
		if(this.nodeSize()!=((WGraph_DS) o).nodeSize())
			return false;
		if(this.numofedge!=((WGraph_DS) o).numofedge)
			return false;
		if(this.mc!=((WGraph_DS) o).mc)
			return false;
		if (  !new ArrayList<>(((WGraph_DS) o).gr.values()).equals(new ArrayList<>(this.gr.values()))) { 
			return  false;         
		}	
		return true;	
	}
	@Override
	public node_info getNode(int key) {
		return gr.get(key);
	}

	@Override
	public boolean hasEdge(int node1, int node2) {
		if(gr.get(node1)==null||gr.get(node2)==null)
			return false;
		if(gr.containsKey(node1)&&gr.containsKey(node2))
			if(((nodeinfo) gr.get(node1)).hasNi(node2)&&((nodeinfo) gr.get(node2)).hasNi(node1))
				return true;
		return false;
	}

	@Override
	public double getEdge(int node1, int node2) {
		if (hasEdge(node1, node2)) {
			return ((nodeinfo) gr.get(node1)).getw(node2);
		}
		return -1;
	}

	@Override
	public void addNode(int key) {
		if(gr.containsKey(key))return;
		mc++;
		gr.put(key,new nodeinfo(key));
	}

	@Override
	public void connect(int node1, int node2, double w) {
		if(gr.get(node1)==null||gr.get(node2)==null)return;
		if(node1==node2)return;
		if(hasEdge(node1, node2))return;
		if(gr.containsKey(node1)&&gr.containsKey(node2)) {
			mc++;numofedge++;
			((nodeinfo) gr.get(node1)).addNi(gr.get(node2),w);
			((nodeinfo) gr.get(node2)).addNi(gr.get(node1),w);
		}
	}
	@Override
	public Collection<node_info> getV() {
		return (Collection<node_info>) new HashMap<Integer, node_info>(gr).values();
	}

	@Override
	public Collection<node_info> getV(int node_id) {
		if(!gr.containsKey(node_id))
			return null;
		HashMap<Integer,Double> temp = ((nodeinfo) gr.get(node_id)).getNi();
		List<node_info> coll = new ArrayList<node_info>();
		temp.forEach((k,v) ->coll.add(gr.get(k)));
		return coll;
	}

	@Override
	public node_info removeNode(int key) {
		if(gr.get(key)!=null)
			if(gr.containsKey(key)) {
				numofedge-=((nodeinfo) gr.get(key)).getNi().size();
				mc+=((nodeinfo) gr.get(key)).getNi().size()+1;
				HashMap<Integer,Double> temp = ((nodeinfo) gr.get(key)).getNi();
				temp.forEach((k,v) ->((nodeinfo) gr.get(k)).removeNode(gr.get(key)));		
				((nodeinfo) gr.get(key)).getNi().clear();
				return gr.remove(key);
			}
		return null;	
	}

	@Override
	public void removeEdge(int node1, int node2) {

		if(gr.containsKey(node1)&&gr.containsKey(node2))
			if((hasEdge(node1,node2))&&(node1 != node2)){
				mc++;
				((nodeinfo) gr.get(node1)).removeNode(gr.get(node2));
				((nodeinfo) gr.get(node2)).removeNode(gr.get(node1));
				numofedge--;
			}	
	}

	@Override
	public int nodeSize() {
		return gr.size();
	}

	@Override
	public int edgeSize() {
		return numofedge;
	}

	@Override
	public int getMC() {
		return mc;
	}
	public static class nodeinfo implements node_info{
		private static int counter;
		private int key;
		private String Info;
		private double tag;
		private HashMap<Integer,Double> Wnei = new HashMap <Integer,Double>();

		public nodeinfo() {
			this.Info="empty";
			key=counter;
			counter++;
			tag=0.0;
		}
		public nodeinfo(String Info,double tag) {
			this.Info=Info;
			key=counter;
			counter++;
			this.tag=tag;
		}
		public nodeinfo(int key,String Info,double tag) {
			this.Info=Info;
			this.key=key;
			this.tag=tag;
		}
		public nodeinfo(int key) {
			this.Info="empty";
			this.key=key;
			this.tag=0.0;
		}

		public boolean hasNi(int key) {
			return (Wnei.containsKey(key) && key != this.getKey());
		}
		public Double getw(int key) {
			if(hasNi(key))
				return Wnei.get(key);
			return -1.0;
		}
		public void addNi(node_info t, double w) {
			if(t!=null)
				if(!Wnei.containsKey(t.getKey()))
					if(t.getKey()!=this.key)
						Wnei.put(t.getKey(), w);
		}
		public HashMap<Integer, Double> getNi() {
			return Wnei;
		}
		public void removeNode(node_info node) {
			if(Wnei.containsKey(node.getKey()))
				Wnei.remove(node.getKey());
		}
		@Override
		public boolean equals(Object o) {        
			if ( o.getClass() != this.getClass()) { 
				return false; 

			}
			if ( ((node_info) o).getInfo() != this.getInfo()) { 
				return  false;  

			}
			if ( ((node_info) o).getTag() != this.getTag()) { 
				return  false;  

			}
			if (  !new ArrayList<>(((nodeinfo) o).getNi().values()).equals(new ArrayList<>(this.getNi().values()))) { 
				return  false;         
			}	        
			return true;
		}
		@Override
		public int getKey() {
			return key;
		}

		@Override
		public String getInfo() {
			return Info;
		}

		@Override
		public void setInfo(String s) {
			Info=s;
		}

		@Override
		public double getTag() {
			return tag;
		}

		@Override
		public void setTag(double t) {
			tag=t;			
		}}

}
