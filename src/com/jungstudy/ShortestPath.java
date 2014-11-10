package com.jungstudy;


import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Paint;
import java.awt.Stroke;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;

import org.apache.commons.collections15.Factory;
import org.apache.commons.collections15.Transformer;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.AnnotationConfiguration;
import org.hibernate.cfg.Configuration;

import weibo4j.Relation;
import weibo4j.Weibo;
import weibo4j.WeiboException;
import edu.uci.ics.jung.algorithms.layout.CircleLayout;
import edu.uci.ics.jung.algorithms.layout.FRLayout;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.algorithms.shortestpath.DijkstraShortestPath;
import edu.uci.ics.jung.graph.SparseMultigraph;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.DefaultModalGraphMouse;
import edu.uci.ics.jung.visualization.decorators.ToStringLabeller;
import edu.uci.ics.jung.visualization.renderers.Renderer.VertexLabel.Position;


public class ShortestPath extends Frame  {
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
    static int graph1[][]=new int[500][500];//图矩阵
    static int times[][]=new int[500][500];//统计矩阵
	static int b1,b2;//最大次数的边的下标	
	static int weight;//边的权重
	
	
	// declare the graph with specified type of vertex & edge
	Map<RelationLink, Number> mWeightKey;// use edge type to parameter the weight key
	// declare the factories with the above specified types
	Factory<SparseMultigraph<UserVertex, UserVertex>> graphFactory;
	Factory<UserVertex> vertexFactory;
	Factory<RelationLink> edgeFactory;
	// /////////
	Hashtable<RelationLink, Number> eWeights = new Hashtable<RelationLink, Number>();

	static int vi = 0, ei = 0;// counter for creating vertics & edges
	
	public void createFactories() {
		vertexFactory = new Factory<UserVertex>() {
			public UserVertex create() {
				return new UserVertex(vi++);
			}
		};
		edgeFactory = new Factory<RelationLink>() {
			public RelationLink create() {
				return new RelationLink(ei++);
			}
		};
		graphFactory = new Factory<SparseMultigraph<UserVertex, UserVertex>>() {
			public SparseMultigraph<UserVertex, UserVertex> create() {
				return new SparseMultigraph<UserVertex, UserVertex>();
			}
		};
	}
	
	
	@SuppressWarnings("unchecked")
	public static void main(String[] args) throws WeiboException{
		
		final ShortestPath myApp = new ShortestPath();
		myApp.createFactories();

		for(int k1=0;k1<=499;k1++){//初始化图的矩阵和最短路径经过每条边次数的统计
			for(int k2=0;k2<=499;k2++){
				times[k1][k2]=0;
				graph1[k1][k2]=9;
				}
			}
		//try {
			System.setProperty("weibo4j.oauth.consumerKey", Weibo.CONSUMER_KEY);
			System.setProperty("weibo4j.oauth.consumerSecret",
					Weibo.CONSUMER_SECRET);

			//try {
				//Weibo weibo = getWeibo(true, args);
				
				System.out.println("请输入第一个中心人物的id:");
           		String user1Id=null;
           		Scanner keyboard = new Scanner(System.in); 
           		user1Id=keyboard.next();
           		
				System.out.println("请输入第一个中心人物的name:");
           		String name=null;
           	    keyboard = new Scanner(System.in); 
           		name=keyboard.next();
           		
				//String id="1609740364";
           		//String name="清风之逸";
           		 
           	//从数据库中取数据
           		Configuration cfg = new AnnotationConfiguration();
           	    SessionFactory sf = cfg.configure().buildSessionFactory();
           	    Session session =sf.openSession();
           	    session.beginTransaction();
           	    String hql="from weibo4j.Relation relation where relation.user1Id=:user1Id";
           	    Query query = session.createQuery(hql);
           	    query.setString("user1Id",user1Id);

           	    List<Relation> list = query.list(); 
           	    Iterator itr = list.iterator();
           	     while(itr.hasNext()){
           	    	Relation relation = (Relation)itr.next();
           	    	System.out.println("Relation-User2Name: " + relation.getUser2Name());
           	    	System.out.println("Edge-weight: " + relation.getCommentsNum());
           	     }

           	    session.getTransaction().commit();
           	    session.close();
           	    sf.close();
           	    
				//List<User> list = weibo.getFriendsStatuses(id);
				p++;
				userVertexArray[p]=new UserVertex(user1Id,name);
				
				graph.addVertex(userVertexArray[p]);//自己
				
				
				for (Relation u : list) {
					
					System.out.println("朋友ID"+String.valueOf(u.getUser2Id()));
					if(u.getUser2Id()==u.getUser1Id()){
						System.out.println("同一个人只加边"); 
						q++;
						weight=u.getCommentsNum();
						relationLinkArray[q]=new RelationLink(null, user1Id+"*"+user1Id, weight);//放进数组	
						graph.addEdge(relationLinkArray[q], userVertexArray[0], userVertexArray[0]);//加边}
					}
					//System.out.println(u.getId() + ":" + u.getName());
					
					
					else
					{
						//Relation re = new Relation();
						//re.setUser1Id(Long.valueOf(user1Id));
						//re.setUser2Id(Long.valueOf(u.getUser2Id()));
						System.out.println("不是同一个人，加点加边"); 
					System.out.println(user1Id);
					System.out.println(String.valueOf(u.getUser2Id()));
					p++;		
					userVertexArray[p]=new UserVertex(String.valueOf(u.getUser2Id()),u.getUser2Name());//放进数组	
					graph.addVertex(userVertexArray[p]);
					q++;
					weight=u.getCommentsNum();
					relationLinkArray[q]=new RelationLink(null, user1Id+"*"+String.valueOf(u.getUser2Id()), weight);//放进数组	
					graph.addEdge(relationLinkArray[q], userVertexArray[0], userVertexArray[p]);//加边
					graph1[0][p]=1;graph1[p][0]=1;
					}	
				}//for
				
				print(graph1,p);
				
				System.out.println("The graph  = " + graph.toString());
				
				
				// converts a string (of an edge) to the edge's weight
				Transformer<RelationLink, Double> nev = new Transformer<RelationLink, Double>() {
					public Double transform(RelationLink s) {
						Number v = (Number) myApp.eWeights.get(s);
						// System.out.println(s);
						if (v != null) {
							// System.out.println(v);
							return v.doubleValue();
						} else
							return 0.0;
					}
				};
				
				//求最短路径
				DijkstraShortestPath<UserVertex, RelationLink> alg = new DijkstraShortestPath<UserVertex, RelationLink>(graph, nev);
				final List<RelationLink> l = alg.getPath(userVertexArray[1], userVertexArray[2]);
				System.out.println("The shortest unweighted path from i to j is:");
				System.out.println(l.toString());
				Number dist = alg.getDistance(userVertexArray[1], userVertexArray[2]);
				System.out.println("and the length of the path is: " + dist);
				
				//默认显示两层
				int count=p;
			    for(int i=1;i<=count;i++){callfunc(userVertexArray[i].userId);}
				
					Layout<UserVertex, RelationLink> layout = new FRLayout<UserVertex, RelationLink>(graph);
					VisualizationViewer<UserVertex, RelationLink> vv = new VisualizationViewer<UserVertex, RelationLink>(layout);
					vv.setPreferredSize(new Dimension(1200,1000));
					vv.getRenderContext().setVertexLabelTransformer(new ToStringLabeller());//显示结点内容
					vv.getRenderContext().setEdgeLabelTransformer(new ToStringLabeller());//显示边的内容
					
					
				
					vv.getRenderContext().setVertexFillPaintTransformer(
							new Transformer<UserVertex, Paint>() {
								public Paint transform(UserVertex p) {
									if (p.equals(userVertexArray[0]))
										return Color.GREEN;
									else
										return Color.YELLOW;
								}
							});
					
					
					vv.getRenderContext().setEdgeFontTransformer(new Transformer() {

						public Object transform(Object arg0) {
							return new Font("宋体", Font.ITALIC, 24);
						}

					});

					/*
					@SuppressWarnings("unused")
					Transformer<String, String> ev = new Transformer<String, String>() {
						public String transform(String s) {
							Number v = (Number) myApp.eWeights.get(s);
							if (v != null) {
								return "----" + v.intValue();
							} else
								return "";
						}

						public RelationLink transform(UserVertex arg0) {
							// TODO Auto-generated method stub
							return null;
						}
					};
					vv.getRenderContext().setEdgeLabelTransformer(ev);
					*/
					
					vv.getRenderContext().setLabelOffset(30);
					vv.getRenderer().getVertexLabelRenderer().setPosition(Position.CNTR);
					vv.setPreferredSize(new Dimension(1200, 1000));
					
					// Set up stroke Transformers for the edges
					final Stroke edgeStroke = new BasicStroke(1);
					final Stroke shortestStroke = new BasicStroke(4);// thick edge line!
					Transformer<RelationLink, Stroke> edgeStrokeTransformer = new Transformer<RelationLink, Stroke>() {
						public Stroke transform(RelationLink s) {
							//System.out.println("s是什么玩意啊？："+s);
							for (int i = 0; i < l.size(); i++) {
								if (l.get(i).equals(s))
									return shortestStroke;
							}
							return edgeStroke;
						}
					};
					vv.getRenderContext().setEdgeStrokeTransformer(edgeStrokeTransformer);
					
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
									callfunc(id);
								} catch (WeiboException e1) {
									// TODO Auto-generated catch block
									e1.printStackTrace();
								}
	                       }
	               });
				
	}
	
	//callfunc函数用于从第一层关系网扩展
	
	@SuppressWarnings("unchecked")
	public static void callfunc(String user1Id) throws WeiboException{
			
		    final ShortestPath myApp1 = new ShortestPath();
			myApp1.createFactories();
			
		System.setProperty("weibo4j.oauth.consumerKey", Weibo.CONSUMER_KEY);
		System.setProperty("weibo4j.oauth.consumerSecret",
				Weibo.CONSUMER_SECRET);

		//try {
			//Weibo weibo = getWeibo(true, args);
			//String id="1195242865";
		
		//从数据库中取数据
   		Configuration cfg = new AnnotationConfiguration();
   	    SessionFactory sf = cfg.configure().buildSessionFactory();
   	    Session session =sf.openSession();
   	    session.beginTransaction();
   	    //String hql="from User user, Relation relation where user.id = relation.user1Id";
   	    String hql="from weibo4j.Relation relation where relation.user1Id=:user1Id";
   	    Query query = session.createQuery(hql);
   	    query.setString("user1Id", user1Id);
   	    List<Relation> list = query.list(); 
   	    Iterator itr = list.iterator();
   	     while(itr.hasNext()){
   	    	Relation relation = (Relation)itr.next();
   	    	System.out.println("Relation-User2Name: " + relation.getUser2Name());
   	    	System.out.println("Edge-weight: " + relation.getCommentsNum());
   	     }
   	    session.getTransaction().commit();
   	    session.close();
   	    sf.close();
   	    
   	    
			
			//List<User> list = weibo.getFriendsStatuses(id);
			int e1=p;
			int e2=q;
            isFound3=false;
            for(int n3=0;n3<=e1&&!isFound3;n3++){ //遍历之前的数组，查找中心节点
            	UserVertex  userVertexCenter= userVertexArray[n3];
                if(userVertexCenter.getUserId().equals(user1Id))//找到button节点啦！！！function开始"
                {
                	System.out.println("找到button节点啦！！！function开始"+user1Id);isFound3=true;
                
            
                	for (Relation u : list) {
                		isFound1=false;
                		Relation re = new Relation();
                		//System.out.println(u.getId() + ":" + u.getName());			
                		re.setUser1Id(user1Id);
                		re.setUser2Id(u.getUser2Id());
				
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
        	                			if((s.getUserId()+"*"+user1Id).equals(s2.getId1id2())||(user1Id+"*"+s.getUserId()).equals(s2.getId1id2()))//边已经存在
        	                			{
        	                				isFound2=true;
        	                				System.out.println("边已经存在");
        	                				}//if
                				}//while
        	                			if(!isFound2)//边不存在
        	                			{
        	                				System.out.println("边不存在");
        	                				q++;
        	                				weight=u.getCommentsNum();
        	                				relationLinkArray[q]=new RelationLink(s, user1Id+"*"+s.getUserId(), weight);
        	                				graph.addEdge(relationLinkArray[q], userVertexCenter, s);//加边    
        	                				graph1[n3][n1]=1;graph1[n1][n3]=1;
        	                				System.out.println("加边");
        	                			} //if		       				 
                			}//if
                		}//while
                	 
                	    		if (!isFound1)//点不存在
                	    		{
              	    			    System.out.println("加边 加点"); 
              	    			    p++;
              	    			    userVertexArray[p]=new UserVertex(String.valueOf(re.getUser2Id()),u.getUser2Name());
              	    			    graph.addVertex(userVertexArray[p]);
               					    q++;
               					    weight=u.getCommentsNum();
               					    relationLinkArray[q]=new RelationLink(userVertexCenter, user1Id+"*"+String.valueOf(re.getUser2Id()), weight);
                					graph.addEdge(relationLinkArray[q], userVertexCenter, userVertexArray[p]);//加边
                					graph1[n3][p]=1;graph1[p][n3]=1;	
                	    			
                	    		}//if(!isFound1)	
                	    		
                	}//for (User u : list)
                	
                }//找到button节点啦！！！function开始"

           }//找button   while(iter3.hasNext()&&!isFound3)
			
            if(!isFound3){
            	System.out.println("在现有图中查无此人！！！");
            }
			
        	//*******************************************************************************************88
            print(graph1,p);//输出当前得到的图的矩阵
			for(int n1=0;n1<=p;n1++)//循环找从每个节点到其他节点的最短路径
            shortPath(n1);
			System.out.println("打印统计结果");
			print(times,p);//打印统计结果
			
			//********************************************************************************************88
			
				
				System.out.println("The graph  = " + graph.toString());
				
				
				Layout<UserVertex, RelationLink> layout = new CircleLayout<UserVertex, RelationLink>(graph);
				layout.setSize(new Dimension(1200,1000));
				VisualizationViewer<UserVertex, RelationLink> vv = new VisualizationViewer<UserVertex, RelationLink>(layout);
				vv.setPreferredSize(new Dimension(700,700));
				vv.getRenderContext().setVertexLabelTransformer(new ToStringLabeller());//显示结点内容
				vv.getRenderContext().setEdgeLabelTransformer(new ToStringLabeller());//显示边的内容
					DefaultModalGraphMouse<Object, Object> gm = new DefaultModalGraphMouse<Object, Object>();			
				vv.setGraphMouse(gm);
				
				vv.getRenderContext().setVertexFillPaintTransformer(
						new Transformer<UserVertex, Paint>() {
							public Paint transform(UserVertex p) {
								if (p.equals(userVertexArray[0]))
									return Color.GREEN;
								else
									return Color.YELLOW;
							}
						});
				
				
				vv.getRenderContext().setEdgeFontTransformer(new Transformer() {

					public Object transform(Object arg0) {
						return new Font("宋体", Font.ITALIC, 24);
					}

				});
				
				/*
				@SuppressWarnings("unused")
				Transformer<UserVertex, RelationLink> ev = new Transformer<UserVertex, RelationLink>() {
					public String transform(RelationLink s) {
						Number v = (Number) myApp1.eWeights.get(s);
						if (v != null) {
							return "----" + v.intValue();
						} else
							return "";
					}

					public RelationLink transform(UserVertex arg0) {
						// TODO Auto-generated method stub
						return null;
					}
				};

				vv.getRenderContext().setEdgeLabelTransformer(ev);
				*/
				
				vv.getRenderContext().setLabelOffset(30);
				vv.getRenderer().getVertexLabelRenderer().setPosition(Position.CNTR);
				vv.setPreferredSize(new Dimension(1200, 1000));
				
				// converts a string (of an edge) to the edge's weight
				Transformer<RelationLink, Double> nev1 = new Transformer<RelationLink, Double>() {
					public Double transform(RelationLink s) {
						Number v = (Number) myApp1.eWeights.get(s);
						// System.out.println(s);
						if (v != null) {
							// System.out.println(v);
							return v.doubleValue();
						} else
							return 0.0;
					}
				};
				DijkstraShortestPath<UserVertex, RelationLink> alg1 = new DijkstraShortestPath<UserVertex, RelationLink>(graph, nev1);
				final List<RelationLink> l1 = alg1.getPath(userVertexArray[1], userVertexArray[2]);
			
				// Set up stroke Transformers for the edges
				final Stroke edgeStroke = new BasicStroke(1);
				final Stroke shortestStroke = new BasicStroke(4);// thick edge line!
				Transformer<RelationLink, Stroke> edgeStrokeTransformer = new Transformer<RelationLink, Stroke>() {
					public Stroke transform(RelationLink s) {
						System.out.println("s是什么玩意啊？："+s);
						for (int i = 0; i < l1.size(); i++) {
							if (l1.get(i).equals(s))
								return shortestStroke;
						}
						return edgeStroke;
					}
				};
				vv.getRenderContext().setEdgeStrokeTransformer(edgeStrokeTransformer);
				
				
			/*
				//找到第一条 并加粗
				int fn=findMax(times,p);
				if(fn>=0){
				
				 RelationLink f=relationLinkArray[fn];//调用找到经过次数最多的边 函数
			
				
				// ***************************888Set up stroke Transformers for the edges
				final Stroke edgeStroke = new BasicStroke(1);
				final Stroke shortestStroke = new BasicStroke(4);// thick edge line!
				Transformer<RelationLink, Stroke> edgeStrokeTransformer = new Transformer<RelationLink, Stroke>() {
					public Stroke transform(RelationLink f) {
						for(int n=0;n<relationLinkArray.length;n++){
						if(relationLinkArray[n]==f)
							return shortestStroke;					
					}
						return edgeStroke;
				}
				};
				vv.getRenderContext().setEdgeStrokeTransformer(edgeStrokeTransformer);
				//********************************* End  Set up stroke Transformers for the edges
				graph1[b1][b2]=9;graph1[b2][b1]=9;  //此时将矩阵中两点状态改为不连通
				}//if fn
				
				else System.out.println("没找到最大边！");
				
				*/
				
				
	}
		
		static boolean finals[]=new boolean[500];
		static int min;
		static int d[]=new int[500];
					
		public  static void shortPath(int v0){
			
						int q[]= new int[500];//链路下标数组
						for(int n=0;n<q.length;n++)q[n]=0;//下标初始化 从0开始
						int pa[][]=new int[500][500];//链路矩阵  【前】到达，【后】依次经过
						for(int k1=0;k1<=499;k1++){
							for(int k2=0;k2<=499;k2++){
								pa[k1][k2]=-2;
								}
							}
														
						for(int n=0;n<d.length;n++)d[n]=500;
						for(int n=0;n<finals.length;n++)finals[n]=false;
						
						for(int v=0;v<=p;v++){
							finals[v]=false; d[v]=graph1[v0][v];
							//for(int w=0;w<=p;w++)pa[v][w]=0;
							if(d[v]<9){pa[v][q[v]]=v0;q[v]++;pa[v][q[v]]=v;q[v]++;}	//把v0放第一个，自己放第二个	 每次放完记得下标+1
						}//for  v
						System.out.println("节点4的初始路径："+pa[4][0]+pa[4][1]+pa[4][2]);
						d[v0]=0;finals[v0]=true;//把自己加进去
						for(int n=0;n<=p;n++)System.out.println(d[n]+" "); System.out.println();
		 
						int v=-1;
						int temp=-1;
						for(int i=1;i<=p;i++){//主循环，每次求得v0到某个v定点的最短路径，并加到finalS集合
							min=100;//当前所知，距离v0最近的距离
							
							for(int w=0;w<=p;w++){//找出当前不在finals中的，可(一步)到达v0的最近的定点v，并放进finals
								if(!finals[w])
									if(d[w]<min){ v=w;  min=d[w];}}
							
							finals[v]=true;
							System.out.println(v);//打印出放进去的节点
							
							for(int w=0;w<=p;w++)//
								if(!finals[w]&&(min+graph1[v][w])<d[w]){
									System.out.println(w+"复制"+v);
									d[w]=min+graph1[v][w];//*******（v--->W）*****
									System.out.println(w+"的长度改为"+d[w]);
									
									for(int n=0;n<=p;n++){temp=pa[v][n];pa[w][n]=temp;}
									temp=q[v];q[w]=temp; pa[w][q[w]]=w;q[w]++; //pa[w]=p[v]+p[w]
									
									//System.out.print(w+"的路径修改为");
									for(int n=0;n<pa[w].length;n++)System.out.print(pa[w][n]);System.out.println();								
								}//if
						}//for i
						print(pa,p);//打印链路矩阵
						for(int n1=0;n1<=p;n1++){//统计次数,对称
							for(int n2=0;n2<n1;n2++){
								if(pa[n1][n2+1]>=0){
								times[pa[n1][n2]][pa[n1][n2+1]]++;
								times[pa[n1][n2+1]][pa[n1][n2]]++;}
							}
						}				
					}//方法shortPath

		
			public static void print(int a[][],int l){//打印l*l的矩阵
				for(int n1=0;n1<=l;n1++){
					for(int n2=0;n2<=l;n2++)
						System.out.print(a[n1][n2]+" ");			
					System.out.println();
				}
			}			
	

		public static int findMax(int a[][],int l){
			int minn=a[0][0];
			for(int n1=0;n1<=l;n1++){
				for(int n2=0;n2<=l;n2++){
					if(a[n1][n2]>minn){
						minn=a[n1][n2];
						b1=n1;b2=n2;
					}//if 替换
				}//for n2			
			}//for n1

			String id1=userVertexArray[b1].userId;
			String id2=userVertexArray[b2].userId;
			for(int n=0;n<relationLinkArray.length;n++){
				if(relationLinkArray[n].getId1id2()==(id1+"*"+id2)||relationLinkArray[n].getId1id2()==(id2+"*"+id1)){
						return n;
					}else{
						return -2;
					}
			}
			return -1;
			
			/*for(int n=0;n<relationLinkArray.length;n++){
				if(relationLinkArray[n].getId1id2()==(id1+"*"+id2)||relationLinkArray[n].getId1id2()==(id2+"*"+id1))
					String dLink=relationLinkArray[n].getId1id2();
					return shortestStroke;
				
			}*/

		}
		
		
		
}
