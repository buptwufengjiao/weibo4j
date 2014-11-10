package com.jungstudy;


import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.Paint;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Scanner;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;

import org.apache.commons.collections15.Transformer;

import weibo4j.Relation;
import weibo4j.User;
import weibo4j.Weibo;
import weibo4j.WeiboException;
import edu.uci.ics.jung.algorithms.layout.CircleLayout;
import edu.uci.ics.jung.algorithms.layout.FRLayout;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.graph.SparseMultigraph;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.DefaultModalGraphMouse;
import edu.uci.ics.jung.visualization.decorators.ToStringLabeller;


public class StudyCaseFromNet extends Frame  {
    static boolean isFound1=false;
    static boolean isFound2=false;   
    static boolean isFound3=false; 
	static UserVertex  userVertexArray[]=new UserVertex[500];
	static int p=-1;
	static RelationLink relationLinkArray[]=new RelationLink[1000];
	static int q=-1;
	static SparseMultigraph<UserVertex, RelationLink> graph = new SparseMultigraph<UserVertex, RelationLink>();
	static JFrame frame = new JFrame("Interactive Graph View 1");
    static JButton button1 = new JButton("添加结点");
    static int weight;//边的权重

	public static void main(final String[] args) throws WeiboException{

		//try {
			System.setProperty("weibo4j.oauth.consumerKey", Weibo.CONSUMER_KEY);
			System.setProperty("weibo4j.oauth.consumerSecret",
					Weibo.CONSUMER_SECRET);

			//try {
				Weibo weibo = getWeibo(true, args);
				System.out.println("请输入第一个中心人物的id:");
           		String id=null;
           		Scanner keyboard = new Scanner(System.in); 
           		id=String.valueOf(keyboard.nextInt());
           		
				System.out.println("请输入第一个中心人物的name:");
           		String name=null;
           	    keyboard = new Scanner(System.in); 
           		name=keyboard.next();
				//String id="1609740364";
           		//String name="清风之逸";
				List<User> list = weibo.getFriendsStatuses(id);
				p++;
				userVertexArray[p]=new UserVertex(id,name);
				
				graph.addVertex(userVertexArray[p]);//自己
				
				
				for (User u : list) {
					System.out.println(u.getId() + ":" + u.getName());
					Relation re = new Relation();
					re.setUser1Id(id);
					re.setUser2Id(u.getId());
					
					System.out.println(re.getUser1Id());
					System.out.println(re.getUser2Id());
					p++;		
					userVertexArray[p]=new UserVertex(String.valueOf(re.getUser2Id()),u.getName());//放进数组	
					graph.addVertex(userVertexArray[p]);
					q++;
					weight=re.getCommentsNum()+re.getRtNum()+re.getRtAndCommentNum();
					relationLinkArray[q]=new RelationLink(null, id+"*"+String.valueOf(re.getUser2Id()),weight);//放进数组	
					graph.addEdge(relationLinkArray[q], userVertexArray[0], userVertexArray[p]);//加边
							
				}//for
				
				System.out.println("The graph  = " + graph.toString());
				//默认显示两层
				int count=p;
				for(int i=1;i<=count;i++){callfunc(args,userVertexArray[i].userId);}
				
					Layout<UserVertex, RelationLink> layout = new FRLayout<UserVertex, RelationLink>(graph);
					VisualizationViewer<UserVertex, RelationLink> vv = new VisualizationViewer<UserVertex, RelationLink>( layout);
					vv.setPreferredSize(new Dimension(1200,1000));
					vv.getRenderContext().setVertexLabelTransformer(new ToStringLabeller());//显示结点内容
					vv.getRenderContext().setEdgeLabelTransformer(new ToStringLabeller());//显示边的内容
					vv.getRenderContext().setVertexFillPaintTransformer(
							new Transformer<UserVertex, Paint>() {
								public Paint transform(UserVertex p) {
									return Color.GREEN;
								}
							});
					
					// Create a graph mouse and add it to the visualization component
					DefaultModalGraphMouse<Object, Object> gm = new DefaultModalGraphMouse<Object, Object>();
					//gm.setMode(ModalGraphMouse.Mode.TRANSFORMING);
					vv.setGraphMouse(gm);
					frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
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
					 
					 button1.setBackground(Color.green);
					 button1.setSize(10, 10);
				        // 用先前创建的Frame来显示这个按钮
					 frame.getContentPane().add(button1);
				     frame.getContentPane().setLayout(new FlowLayout());
				     button1.addActionListener(new ActionListener(){

	                       public void actionPerformed(ActionEvent e){

	                               //System.out.println("you click button1!");
	                       	try {
	                       		System.out.println("*******************请输入用户id****************************:");
	                       		String id=null;
	                       		Scanner keyboard = new Scanner(System.in); 
	                       		id=keyboard.next();
									callfunc(args,id);
								} catch (WeiboException e1) {
									// TODO Auto-generated catch block
									e1.printStackTrace();
								}
	                       }
	               });
				
	}
	
		public static void callfunc(final String[] args,String id) throws WeiboException{
		System.setProperty("weibo4j.oauth.consumerKey", Weibo.CONSUMER_KEY);
		System.setProperty("weibo4j.oauth.consumerSecret",
				Weibo.CONSUMER_SECRET);

		//try {
			Weibo weibo = getWeibo(true, args);
			//String id="1195242865";
			
			List<User> list = weibo.getFriendsStatuses(id);
			int e1=p;
			int e2=q;
            isFound3=false;
            for(int n3=0;n3<=e1&&!isFound3;n3++){ //遍历之前的数组，查找中心节点
            	UserVertex  userVertexCenter= userVertexArray[n3];
                if(userVertexCenter.getUserId().equals(id))//找到button节点啦！！！function开始"
                {
                	System.out.println("找到button节点啦！！！function开始"+id);isFound3=true;
                
            
                	for (User u : list) {
                		isFound1=false;
                		Relation re = new Relation();
                		System.out.println(u.getId() + ":" + u.getName());			
                		re.setUser1Id(id);
                		re.setUser2Id(u.getId());
				
                		//遍历节点数字，看是否有必要加节点
              		System.out.println("判断是否有必要加点");
              		for(int n1=0;n1<=e1&&!isFound1;n1++){ 
                			UserVertex s = userVertexArray[n1];
                			//判断是否有必要加点
                			if(String.valueOf(re.getUser2Id()).equals(s.getUserId()))//点已经存在
                			{ 
                				isFound1=true;
                				System.out.println("找到啦！");
                				System.out.println(re.getUser1Id());
                				System.out.println(re.getUser2Id());
        				
                				//判断是否有必要加边
                				isFound2=false;
                				System.out.println("判断是否有必要加边");
                				
                				for(int n2=0;n2<=e2&&!isFound2;n2++){
                					RelationLink s2 = relationLinkArray[n2];
        	                			if((s.getUserId()+"*"+id).equals(s2.getId1id2())||(id+"*"+s.getUserId()).equals(s2.getId1id2()))//边已经存在
        	                			{
        	                				isFound2=true;
        	                				System.out.println("边已经存在");
        	                				}//if
                				}//while
        	                			if(!isFound2)//边不存在
        	                			{
        	                				System.out.println("边不存在");
        	                				q++;
        	                				relationLinkArray[q]=new RelationLink(s, id+"*"+s.getUserId(), weight);
        	                				graph.addEdge(relationLinkArray[q], userVertexCenter, s);//加边       	                				
        	                				System.out.println("加边");
        	                			} //if		       				 
                			}//if
                		}//while
                	 
                	    		if (!isFound1)//点不存在
                	    		{
              	    			    System.out.println("加边 加点"); 
              	    			    p++;
              	    			    userVertexArray[p]=new UserVertex(String.valueOf(re.getUser2Id()),u.getName());
              	    			    graph.addVertex(userVertexArray[p]);
               					    q++;
               					    relationLinkArray[q]=new RelationLink(userVertexCenter, id+"*"+String.valueOf(re.getUser2Id()), weight);
                					graph.addEdge(relationLinkArray[q], userVertexCenter, userVertexArray[p]);//加边
                						
                	    			
                	    		}//if(!isFound1)	
                	    		
                	}//for (User u : list)
                	
                }//找到button节点啦！！！function开始"

           }//找button   while(iter3.hasNext()&&!isFound3)
			
            if(!isFound3){
            	System.out.println("在现有图中查无此人！！！");
            }
			
			
				
				System.out.println("The graph  = " + graph.toString());
				Layout<UserVertex, RelationLink> layout = new CircleLayout<UserVertex, RelationLink>(graph);
				layout.setSize(new Dimension(600,600));
				VisualizationViewer<UserVertex, RelationLink> vv = new VisualizationViewer<UserVertex, RelationLink>( layout);
				vv.setPreferredSize(new Dimension(700,700));
				vv.getRenderContext().setVertexLabelTransformer(new ToStringLabeller());//显示结点内容
				vv.getRenderContext().setEdgeLabelTransformer(new ToStringLabeller());//显示边的内容
					DefaultModalGraphMouse<Object, Object> gm = new DefaultModalGraphMouse<Object, Object>();			
				vv.setGraphMouse(gm);
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
