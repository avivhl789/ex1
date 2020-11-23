package ex1.tests;

import org.junit.jupiter.api.Test;

import ex1.src.WGraph_Algo;
import ex1.src.node_info;
import ex1.src.weighted_graph;
import ex1.src.weighted_graph_algorithms;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class WGraph_AlgoTest {

    @Test
    void isConnected() {
        weighted_graph g0 = WGraph_DSTest.graph_creator(0,0,1);
        weighted_graph_algorithms ag0 = new WGraph_Algo();
        ag0.init(g0);
        assertTrue(ag0.isConnected());
        g0 = WGraph_DSTest.graph_creator(500,0,1);
        ag0.init(g0);
        assertFalse( ag0.isConnected());  
        g0 = WGraph_DSTest.graph_creator(5,10,1);
        ag0.init(g0);
        assertTrue( ag0.isConnected()); 
        g0 = WGraph_DSTest.graph_creator(5,3,1);
        ag0.init(g0);
        assertFalse( ag0.isConnected());  
    }
    @Test
    void Connected() {
        weighted_graph g0 = WGraph_DSTest.graph_creator(1000,0,1);
        for (int i = 0; i < 1000; i++) {
        	for (int j = 0; j <1000; j++) {
				g0.connect(i, j, 0.0);
			}	
		}
        assertEquals(1000*999/2, g0.edgeSize());
    }
    @Test
    void edgeSize() {
        weighted_graph g0 = WGraph_DSTest.graph_creator(2,0,1);
        assertEquals(0, g0.edgeSize());
        g0.connect(0,1,0.0);
        assertEquals(1, g0.edgeSize());

    }
    @Test
    void mc() {
        weighted_graph g0 = WGraph_DSTest.graph_creator(1000,0,1);
        assertEquals(1000, g0.getMC());
        g0.addNode(1001);
        assertEquals(1001, g0.getMC());
        g0.connect(0, 1001,0.0);
        assertEquals(1002, g0.getMC());
        g0.removeEdge(0, 1001);
        assertEquals(1003, g0.getMC());
        g0.removeNode(0);
        assertEquals(1004, g0.getMC());
        g0.connect(1, 1001,0.0);
        g0.removeNode(1);
        assertEquals(1007, g0.getMC());   
    }
    @Test
    void info() {
        weighted_graph g0 = WGraph_DSTest.graph_creator(1,0,1);
         g0.getNode(0).setInfo("test");
         assertEquals( g0.getNode(0).getInfo(),"test"); 
    }
    @Test
    void tag() {
        weighted_graph g0 = WGraph_DSTest.graph_creator(1,0,1);
         g0.getNode(0).setTag(3.6);
         assertEquals( g0.getNode(0).getTag(),3.6); 
    }
    @Test
    void shortestPathDist() {
        weighted_graph g0 = small_graph();
        weighted_graph_algorithms ag0 = new WGraph_Algo();
        ag0.init(g0);
        assertTrue(ag0.isConnected());
        double d = ag0.shortestPathDist(0,10);
        assertEquals(d, 5.1);
    }

    @Test
    void shortestPath() {
        weighted_graph g0 = small_graph();
        weighted_graph_algorithms ag0 = new WGraph_Algo();
        ag0.init(g0);
        List<node_info> sp = ag0.shortestPath(0,10);
        //double[] checkTag = {0.0, 1.0, 2.0, 3.1, 5.1};
        int[] checkKey = {0, 1, 5, 7, 10};
        int i = 0;
        for(node_info n: sp) {
        	//assertEquals(n.getTag(), checkTag[i]);
        	assertEquals(n.getKey(), checkKey[i]);
        	i++;
        }
    }
    
    @Test
    void save_load() {
    	String str="";
        weighted_graph g0 = WGraph_DSTest.graph_creator(10,30,1);
        weighted_graph_algorithms ag0 = new WGraph_Algo();
        assertFalse( ag0.save(str)); 
        ag0.init(g0);
        assertFalse( ag0.save(str)); 
        ag0.init(g0);
       str = "g0.obj";
        ag0.save(str);   
        weighted_graph g1 = WGraph_DSTest.graph_creator(10,30,1);
        ag0.load(str);
        assertEquals(g0,g1);
        g0.removeNode(0);
        assertNotEquals(g0,g1);
    }
    @Test
    void copy() {
        weighted_graph g0 = WGraph_DSTest.graph_creator(10,30,1);
        weighted_graph_algorithms ag0 = new WGraph_Algo();
        ag0.init(g0);
        g0=ag0.copy();
        weighted_graph g1 = WGraph_DSTest.graph_creator(10,30,1);
        assertEquals(g0,g1);
        g0.removeNode(0);
        assertNotEquals(g0,g1);
    }
    

    private weighted_graph small_graph() {
        weighted_graph g0 = WGraph_DSTest.graph_creator(11,0,1);
        g0.connect(0,1,1);
        g0.connect(0,2,2);
        g0.connect(0,3,3);

        g0.connect(1,4,17);
        g0.connect(1,5,1);
        g0.connect(2,4,1);
        g0.connect(3, 5,10);
        g0.connect(3,6,100);
        g0.connect(5,7,1.1);
        g0.connect(6,7,10);
        g0.connect(7,10,2);
        g0.connect(6,8,30);
        g0.connect(8,10,10);
        g0.connect(4,10,30);
        g0.connect(3,9,10);
        g0.connect(8,10,10);

        return g0;
    }
}
