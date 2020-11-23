package ex1.src;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Scanner;

import ex1.src.WGraph_DS.nodeinfo;

import java.io.*;


public class WGraph_Algo implements weighted_graph_algorithms {

	private weighted_graph gr;
	@Override
	public void init(weighted_graph g) {
		gr=g;		
	}
	@Override
	public boolean equals(Object o) {
		if ( o.getClass() != this.getClass())  
			return false;
		return ((WGraph_DS)gr).equals(o);
	}
	@Override
	public weighted_graph getGraph() {
		return gr;
	}

	@Override
	public weighted_graph copy() {
		weighted_graph temp=new WGraph_DS();
		HashMap<Integer,Integer> keymaching = new HashMap <Integer,Integer>();
		for (node_info node :this.gr.getV()) {
			nodeinfo tempnode=new nodeinfo(node.getInfo(),0.0);
			temp.addNode(tempnode.getKey());
			keymaching.put(node.getKey(), tempnode.getKey());
		}
		for (node_info node :this.gr.getV()) {
			for (node_info ni  : this.gr.getV(node.getKey())) {
				temp.connect(keymaching.get(node.getKey()), keymaching.get(ni.getKey()),gr.getEdge(node.getKey(),ni.getKey()));
			}	
		}
		return temp;
	}

	@Override
	public boolean isConnected() {
		if(gr.nodeSize()<2)
			return true;
		reset_tag();
		Queue<node_info> queue = new ArrayDeque<>();		
		node_info N =this.gr.getV().iterator().next(), ni;
		int count=0;
		queue.add(N);
		while(!queue.isEmpty()) {
			N = queue.remove();
			HashMap<Integer,Double> temp = ((nodeinfo) gr.getNode(N.getKey())).getNi();
			for (Integer keyofnei : temp.keySet()) {
				ni=gr.getNode(keyofnei);
				if(ni.getTag()==-1) {
					queue.add(ni);
					ni.setTag(1);
					count++;
				}
			}
		}
		if(count==gr.nodeSize())
			return true;
		return false;
	}

	@Override
	public double shortestPathDist(int src, int dest) {
		if(gr.nodeSize()<2)
			return -1;
		if(gr.nodeSize()==2) {
			if(gr.hasEdge(src, dest))
				return gr.getEdge(src,dest);
		}
		reset_tag();
		PriorityQueue<node_info> queue = new PriorityQueue<node_info>((Comparator <node_info>) new Comparator<node_info>() {
			@Override
			public int compare(node_info node1, node_info node2) {
				if (node1.getTag() < node2.getTag()) 
					return -1; 
				else if (node1.getTag() > node2.getTag()) 
					return 1; 
				return 0; 
			}
		}
				);		
		node_info N = this.gr.getNode(src);
		node_info ni;
		N.setTag(0);
		queue.add(N);
		double cost=Double.MAX_VALUE;
		while(!queue.isEmpty()) {
			N = queue.remove();
			HashMap<Integer,Double> temp = ((nodeinfo) gr.getNode(N.getKey())).getNi();
			for (Integer keyofnei : temp.keySet()) {
				ni=gr.getNode(keyofnei);
				if(ni.getTag()>N.getTag()+temp.get(keyofnei)) {
					ni.setTag(N.getTag()+temp.get(keyofnei));
					queue.add( ni);

				}
				else if(ni.getTag()==-1) {
					ni.setTag(N.getTag()+temp.get(keyofnei));
					queue.add( ni);
				}
				if(ni.getKey()==dest)
					cost=Math.min(cost, ni.getTag());
			}
		}
		if(cost!=Double.MAX_VALUE)
			return  cost;
		return -1;
	}

	@Override
	public List<node_info> shortestPath(int src, int dest) {
		if(gr.nodeSize()<2)
			return null;
		if(gr.nodeSize()==2) {
			if(gr.hasEdge(src, dest))
				return null;
		}
		PriorityQueue<node_info> queue = new PriorityQueue<node_info>((Comparator <node_info>) new Comparator<node_info>() {
			@Override
			public int compare(node_info node1, node_info node2) {
				if (node1.getTag() < node2.getTag()) 
					return -1; 
				else if (node1.getTag() > node2.getTag()) 
					return 1; 
				return 0; 
			}
		}
				);	
		node_info N = gr.getNode(src), ni;
		reset_tag();
		N.setTag(0);
		queue.add(N);
		boolean flag=false;
		HashMap<Integer, Integer> mappath = new HashMap<Integer, Integer>();
		while(!queue.isEmpty()) { 
			N = queue.remove();
			HashMap<Integer,Double> temp = ((nodeinfo) gr.getNode(N.getKey())).getNi();
			for (Integer keyofnei : temp.keySet()) {
				ni=gr.getNode(keyofnei);
				if(ni.getTag()>N.getTag()+temp.get(keyofnei)) {
					ni.setTag(N.getTag()+temp.get(keyofnei));
					mappath.put(ni.getKey(), N.getKey());			    	
					queue.add( ni);
				}
				else if(ni.getTag()==-1) {
					ni.setTag(N.getTag()+temp.get(keyofnei));
					mappath.put(ni.getKey(), N.getKey());
					queue.add( ni);
				}
				if(ni.getKey()==dest)
					flag=true;
			}
		}
		if(flag)
			return shortestPath(src,gr.getNode(dest),mappath);
		return null;
	}
	private List<node_info> shortestPath(int src, node_info nei,HashMap<Integer, Integer> mappath) {
		List<node_info> path2rev = new ArrayList<node_info>();
		node_info N = nei;
		while(N != gr.getNode(src))
		{
			path2rev.add(N);
			N = gr.getNode(mappath.get(N.getKey()));
		}
		path2rev.add(gr.getNode(src));
		// reverse order to top to bottom:
			List<node_info> path2send = new ArrayList<node_info>(); 
			for (int i = path2rev.size()-1; i >= 0; i--) 
				path2send.add(path2rev.get(i));
			return path2send; 
	}


	@Override
	public boolean save(String file) {
		if(gr==null||gr.getV()==null||file==""||file==null)
			return false;
		ArrayList<Integer> keys = new ArrayList<Integer>();
		ArrayList<String> info = new ArrayList<String>();
		ArrayList<Double> tag = new ArrayList<Double>();
		ArrayList<StringBuilder> nei = new ArrayList<StringBuilder>();
		for (node_info n : gr.getV()) {
			HashMap<Integer,Double> temp;
			keys.add(n.getKey());
			info.add(n.getInfo());
			tag.add(n.getTag());
			if(gr.edgeSize()!=0) {
				temp =((nodeinfo) n).getNi();
				StringBuilder temps= new StringBuilder();
				temps.append("NeiOf:");
				temps.append(n.getKey());
				temps.append(",");
				for (Integer key : temp.keySet()) {
					temps.append(key);
					temps.append(",");
					temps.append(temp.get(key));
					temps.append(",");
				}
				temps.append(" NeiEnd");
				nei.add(temps);
			}
		}
		try {
			BufferedWriter fileOut = new BufferedWriter(new FileWriter(file));
			fileOut.write(gr.nodeSize()+",");
			fileOut.write(keys.toString());
			fileOut.write(info.toString());
			fileOut.write(tag.toString());
			fileOut.write(nei.toString());
			fileOut.close();
			System.out.printf("Serialized data is saved in" +file);
			return true;
		}catch(IOException i) {
			i.printStackTrace();
			return false;
		}		
	}

	@Override
	public boolean load(String file) {
		if(file==""||file==null)
			return false;
		try {
			Scanner filein =new Scanner (new FileReader(file));
			String line;
			ArrayList<Integer> keys = new ArrayList<Integer>();
			ArrayList<String> info = new ArrayList<String>();
			ArrayList<Double> tag = new ArrayList<Double>();
			weighted_graph temp=new WGraph_DS();
			if(filein.hasNextLine()) {
				line=filein.nextLine();
				int nodesize=Integer.parseInt(line.substring(0, line.indexOf(",")));
				int last=line.indexOf("[");	
				int next=line.indexOf(",");		
				while (filein.hasNextLine()) {
					for (int i = 0; i < nodesize-1; i++) {				
						keys.add(Integer.parseInt(line.substring(last+1, next)));
						last=next;
						next=line.indexOf(",",last+1);				
					}
					next=line.indexOf("]",last)-1;
					keys.add(Integer.parseInt(line.substring(last+1,next)));
					last=line.indexOf("[",last);
					next=line.indexOf(",",last);
					for (int i = 0; i < nodesize-1; i++) {				
						info.add(line.substring(last+1, next));
						last=next;
						next=line.indexOf(",",last+1);
					}
					next=line.indexOf("]",last)-1;
					info.add(line.substring(last+1, line.indexOf(",")));
					last=line.indexOf("[",last);
					next=line.indexOf(",",last);
					for (int i = 0; i < nodesize-1; i++) {				
						tag.add(Double.parseDouble(line.substring(last+1, next)));
						last=next;
						next=line.indexOf(",",last+1);
					}
					next=line.indexOf("]",last)-1;
					tag.add(Double.parseDouble(line.substring(last+1, line.indexOf(","))));
					for (int i = 0; i < nodesize; i++) {				
						nodeinfo n=new nodeinfo(keys.get(i),info.get(i),tag.get(i));
						((WGraph_DS) temp).addNode(n);
					}
					while (next!=-1) {
						last=line.indexOf("NeiOf:",last)+1;
						next=line.indexOf(",",last);
						int keytoegdge=Integer.parseInt(line.substring(last, next));
						int tempkey;
						double tempw;
						last=next+1;
						next=line.indexOf(",",last+1);
						while (next<line.indexOf("NeiEnd",last)&&next!=-1) {
							tempkey=Integer.parseInt(line.substring(last, next));
							last=next+1;
							next=line.indexOf(",",last+1);
							tempw=Double.parseDouble(line.substring(last, next));
							((nodeinfo) gr.getNode(keytoegdge)).addNi(gr.getNode(tempkey), tempw);
							last=next+1;
							next=line.indexOf(",",last+1);
						}
					}
				}
			}
			return true;	

		}
		catch (FileNotFoundException e) {
			e.printStackTrace();
			return false;	
		}

	}
	private void reset_tag() {
		Iterator<node_info> ite = this.gr.getV().iterator();
		node_info node;
		while (ite.hasNext()) {
			node = ite.next();
			node.setTag(-1);
		}
	}


}
