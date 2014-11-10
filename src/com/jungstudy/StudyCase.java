package com.jungstudy;


import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.Paint;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.util.Iterator;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;

import org.apache.commons.collections15.Transformer;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.AnnotationConfiguration;
import org.hibernate.cfg.Configuration;

import weibo4j.Relation;
import weibo4j.User;
import weibo4j.Weibo;
import weibo4j.WeiboException;
import edu.uci.ics.jung.algorithms.layout.CircleLayout;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.graph.SparseMultigraph;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.CrossoverScalingControl;
import edu.uci.ics.jung.visualization.control.DefaultModalGraphMouse;
import edu.uci.ics.jung.visualization.control.PluggableGraphMouse;
import edu.uci.ics.jung.visualization.control.ScalingGraphMousePlugin;
import edu.uci.ics.jung.visualization.control.TranslatingGraphMousePlugin;
import edu.uci.ics.jung.visualization.decorators.ToStringLabeller;

public class StudyCase extends Frame  {

	
	static SparseMultigraph<String, String> graph = new SparseMultigraph<String, String>();
	
	public static void main(final String[] args) throws WeiboException{
		
		//try {
			System.setProperty("weibo4j.oauth.consumerKey", Weibo.CONSUMER_KEY);
			System.setProperty("weibo4j.oauth.consumerSecret",
					Weibo.CONSUMER_SECRET);

			//try {
				Weibo weibo = getWeibo(true, args);
				String id="1257196584";
				Configuration cfg = new AnnotationConfiguration();
			    SessionFactory sf = cfg.configure().buildSessionFactory();
			    Session session =sf.openSession();
			    session.beginTransaction();
			    Query q = session.createQuery("from weibo4j.User h");
			    List l = q.list(); 
			    Iterator itr = l.iterator();
			     while(itr.hasNext()){
			    	 User user = (User)itr.next();
			    	 System.out.println(user.getId()+","+user.getName());
			     }
			     session.getTransaction().commit();

			     session.close();
			     sf.close();
			    
				List<User> list = weibo.getFriendsStatuses(id);
				
				for (User u : list) {
					System.out.println(u.getId() + ":" + u.getName());
					Relation re = new Relation();
					re.setUser1Id(id);
					re.setUser2Id(u.getId());
					
					System.out.println(re.getUser1Id());
					System.out.println(re.getUser2Id());
					
					graph.addVertex(String.valueOf(re.getUser1Id()));
					graph.addVertex(u.getName());
					
					
					graph.addEdge("Edge-"+u.getName(), String.valueOf(re.getUser1Id()), u.getName());
				}
					
					System.out.println("The graph  = " + graph.toString());
					
					
					Layout<String, String> layout = new CircleLayout<String, String>(graph);
					//layout.setSize(new Dimension(600,600));
					VisualizationViewer<String, String> vv = new VisualizationViewer<String, String>( layout);
					vv.setPreferredSize(new Dimension(700,700));
					//Layout<String, String> layout = new StaticLayout(graph);
					//VisualizationViewer<String,String> vv =
					//new VisualizationViewer<String,String>(layout);
					
					// Show vertex and edge labels
					vv.getRenderContext().setVertexLabelTransformer(new ToStringLabeller());//显示结点内容
					vv.getRenderContext().setEdgeLabelTransformer(new ToStringLabeller());//显示边的内容
					//vv.getRenderer().getVertexLabelRenderer().setPosition(Position.CNTR);
					
					
					vv.getRenderContext().setVertexFillPaintTransformer(
							new Transformer<String, Paint>() {
								public Paint transform(String p) {
									return Color.YELLOW;
								}
							});
					
					
					// Create a graph mouse and add it to the visualization component
					DefaultModalGraphMouse<Object, Object> gm = new DefaultModalGraphMouse<Object, Object>();
					//gm.setMode(ModalGraphMouse.Mode.TRANSFORMING);
					vv.setGraphMouse(gm);
					//vv.addKeyListener(gm.getModeKeyListener());
					
					JFrame frame = new JFrame("Interactive Graph View 1");
					frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
					
					// Let's add a menu for changing mouse modes
					 JMenuBar menuBar = new JMenuBar();
					 JMenu modeMenu = gm.getModeMenu(); // Obtain mode menu from the mouse
					 modeMenu.setText("Mouse Mode");
					 modeMenu.setIcon(null); // I'm using this in a main menu
					 modeMenu.setPreferredSize(new Dimension(80,20)); // Change the size
					 menuBar.add(modeMenu);
					 frame.setJMenuBar(menuBar);
				
					 frame.getContentPane().add(vv);
					 frame.pack();
					 frame.setVisible(true);						
					 
					
					 JButton button1 = new JButton("http://www.cn-java.com");
					 button1.setBackground(Color.green);
					 button1.setSize(10, 10);
				        // 创建一个Fram来显示这个按钮

				       // JFrame frame = new JFrame();
					 frame.getContentPane().add(button1);
				     frame.getContentPane().setLayout(new FlowLayout());

				        //frame.setSize(10,20);

				        //frame.setVisible(true);
					//测试单击事件

				        button1.addActionListener(new ActionListener(){

				                        public void actionPerformed(ActionEvent e){

				                                //System.out.println("you click button1!");
				                        	try {
												callfunc(args,"1609740364");
											} catch (WeiboException e1) {
												// TODO Auto-generated catch block
												e1.printStackTrace();
											}

				                        }

				                });


					 

					
					//new StudyCase().show(); 
					
					
	}
	public static void callfunc(String[] args,String id) throws WeiboException{
		System.setProperty("weibo4j.oauth.consumerKey", Weibo.CONSUMER_KEY);
		System.setProperty("weibo4j.oauth.consumerSecret",
				Weibo.CONSUMER_SECRET);

		//try {
			Weibo weibo = getWeibo(true, args);
			//String id="1195242865";
			List<User> list = weibo.getFriendsStatuses(id);
			
			for (User u : list) {
				//System.out.println(u.getId() + ":" + u.getName());
				Relation re = new Relation();
				re.setUser1Id(id);
				re.setUser2Id(u.getId());
				
				
				graph.addVertex(String.valueOf(re.getUser1Id()));
				graph.addVertex(u.getName());
				
				
				graph.addEdge("Edge-"+u.getName(), String.valueOf(re.getUser1Id()), u.getName());
			}
				
				System.out.println("The graph  = " + graph.toString());
				
				
				Layout<String, String> layout = new CircleLayout<String, String>(graph);
				layout.setSize(new Dimension(600,600));
				VisualizationViewer<String, String> vv = new VisualizationViewer<String, String>( layout);
				vv.setPreferredSize(new Dimension(700,700));
				//Layout<String, String> layout = new StaticLayout(graph);
				//VisualizationViewer<String,String> vv =
				//new VisualizationViewer<String,String>(layout);
				
				// Show vertex and edge labels
				vv.getRenderContext().setVertexLabelTransformer(new ToStringLabeller());//显示结点内容
				vv.getRenderContext().setEdgeLabelTransformer(new ToStringLabeller());//显示边的内容
				//vv.getRenderer().getVertexLabelRenderer().setPosition(Position.CNTR);
				
				// Create a graph mouse and add it to the visualization component
				DefaultModalGraphMouse<Object, Object> gm = new DefaultModalGraphMouse<Object, Object>();
				//gm.setMode(ModalGraphMouse.Mode.TRANSFORMING);
				vv.setGraphMouse(gm);
				//vv.addKeyListener(gm.getModeKeyListener());
				
				JFrame frame = new JFrame("Interactive Graph View 1");
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				
				// Let's add a menu for changing mouse modes
				 JMenuBar menuBar = new JMenuBar();
				 JMenu modeMenu = gm.getModeMenu(); // Obtain mode menu from the mouse
				 modeMenu.setText("Mouse Mode");
				 modeMenu.setIcon(null); // I'm using this in a main menu
				 modeMenu.setPreferredSize(new Dimension(80,20)); // Change the size
				 menuBar.add(modeMenu);
				 frame.setJMenuBar(menuBar);
				 
				 frame.getContentPane().add(vv);
				 frame.pack();
				 frame.setVisible(true);
	}
	
	
	private static Weibo getWeibo(boolean isOauth, String... args) {
		Weibo weibo = new Weibo();
		if (isOauth) {// oauth验证方式 args[0]:访问的token；args[1]:访问的密匙
			// weibo.setToken(args[0], args[1]);
			weibo.setToken(args[0], args[1]);
		} else {// 用户登录方式
			weibo.setUserId(args[0]);// 用户名/ID
			weibo.setPassword(args[1]);// 密码
		}
		return weibo;
	}

}
