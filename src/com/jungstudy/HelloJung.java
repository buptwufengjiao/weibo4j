package com.jungstudy;

import java.awt.Paint;

import javassist.CannotCompileException;
import javassist.CtClass;
import javassist.bytecode.BadBytecode;
import javassist.bytecode.CodeIterator;
import javassist.bytecode.ConstPool;

import javax.swing.JFrame;

import org.eclipse.swt.graphics.Color;

import edu.uci.ics.jung.algorithms.layout.CircleLayout;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.graph.SparseMultigraph;
import edu.uci.ics.jung.graph.util.EdgeType;
import edu.uci.ics.jung.visualization.BasicVisualizationServer;
import edu.uci.ics.jung.visualization.decorators.ToStringLabeller;

public class HelloJung {
	public static void main(String[] args){
		//1. 初始化图g
	SparseMultigraph<String, String> g = new SparseMultigraph<String, String>();
	g.addVertex("A");
	g.addVertex("B");
	g.addVertex("C"); 
	g.addEdge("Edge-1", "A", "B");
	g.addEdge("Edge-2", "B", "C", EdgeType.DIRECTED); 
	//2. 使用该图创建布局对象
	Layout<String, String> layout = new CircleLayout(g);
	//3. 使用布局对象创建BasicVisualizationServer对象
	BasicVisualizationServer<String, String> vv = new BasicVisualizationServer<String, String>(	layout);
	// Show vertex and edge labels
	vv.getRenderContext().setVertexLabelTransformer(new ToStringLabeller());//显示结点内容
	vv.getRenderContext().setEdgeLabelTransformer(new ToStringLabeller());//显示边的内容

	//4. 将上述对象放置在一个Swing容器中并显示之
	JFrame frame = new JFrame("Interactive Graph View 1");
	frame.getContentPane().add(vv);
	frame.pack();
	frame.setVisible(true);

	System.out.println("The graph g = " + g.toString());
	
	}

}
