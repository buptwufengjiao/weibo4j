package com.jungstudy;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.Paint;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Arc2D;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import org.apache.commons.collections15.Transformer;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.AnnotationConfiguration;
import org.hibernate.cfg.Configuration;

import weibo4j.Relation;
import weibo4j.Weibo;
import weibo4j.WeiboException;

import com.jungstudy.RelationLink.RelationLinkFactory;

import edu.uci.ics.jung.algorithms.layout.CircleLayout;
import edu.uci.ics.jung.algorithms.layout.FRLayout;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.SparseMultigraph;
import edu.uci.ics.jung.graph.util.Context;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.DefaultModalGraphMouse;
import edu.uci.ics.jung.visualization.control.EditingModalGraphMouse;
import edu.uci.ics.jung.visualization.control.ModalGraphMouse;
import edu.uci.ics.jung.visualization.decorators.ToStringLabeller;
import edu.uci.ics.jung.visualization.renderers.Renderer.VertexLabel.Position;


public class StudyCaseFromDB extends Frame  {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
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
	static int pa[][]=new int[500][500];//链路矩阵  【前】到达，【后】依次经过
	static int groupCenters[]=new int[100];//社团中心节点下标数组，初始化为-1
	static int gC=-1;
	static int addCount=0;
	private static final int ADDCOUNT=3;//手工输入增加节点的次数
	private static final int LINEFIND=10;//找介数最大的边的条数
	private static final int MAX=100;//定义的无穷大数，权值为此表示不连通

	public static void main(String[] args) throws WeiboException{

		   for(int k1=0;k1<=499;k1++){//初始化图的矩阵和最短路径经过每条边次数的统计
   			for(int k2=0;k2<=499;k2++){
   				
   				graph1[k1][k2]=MAX;
   				}
   			}
		   for(int n=0;n<groupCenters.length;n++)groupCenters[n]=-1;//社团中心节点下标数组
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
           	    String hql="from weibo4j.Relation relation where relation.user1Id=:user1Id or relation.user2Id=:user1Id";
           	    Query query = session.createQuery(hql);
           	    query.setString("user1Id",user1Id);

           	    List<Relation> list = query.list(); 
           	    Iterator itr = list.iterator();
           	     while(itr.hasNext()){
           	    	Relation relation = (Relation)itr.next();
           	    	
           	    	//System.out.println("Relation-User2Name: " + relation.getUser2Name());
           	    	System.out.println("Edge-weight: " + relation.getCommentsNum()+relation.getRtNum()+relation.getRtAndCommentNum());
           	     }

           	    //session.getTransaction().commit();
           	    //session.close();
           	    //sf.close();
           	    //数据库操作结束
           	    
				p++;
				userVertexArray[p]=new UserVertex(user1Id,name);
				
				graph.addVertex(userVertexArray[p]);//自己
				
				
				for (Relation u : list) {
					
					//System.out.println("朋友ID"+String.valueOf(u.getUser2Id()));
					if(u.getUser2Id().equals(u.getUser1Id())){
						System.out.println("同一个人只加边"); 
						q++;
						weight=u.getCommentsNum()+u.getRtNum()+u.getRtAndCommentNum();
						relationLinkArray[q]=new RelationLink(null, user1Id+"*"+user1Id, weight);//放进数组	
						graph.addEdge(relationLinkArray[q], userVertexArray[0], userVertexArray[0]);//加边}
					}
					//System.out.println(u.getId() + ":" + u.getName());
					
					
					else
					{
						System.out.println("不是同一个人，加点加边"); 
					//System.out.println(user1Id);
					//System.out.println(String.valueOf(u.getUser2Id()));
					if(u.getUser1Id().equals(user1Id)){//user1是中心
					
					p++;		
					userVertexArray[p]=new UserVertex(String.valueOf(u.getUser2Id()),u.getUser2Name());//放进数组	
					graph.addVertex(userVertexArray[p]);
					q++;
					weight=u.getCommentsNum()+u.getRtNum()+u.getRtAndCommentNum();
					relationLinkArray[q]=new RelationLink(null, user1Id+"*"+String.valueOf(u.getUser2Id()), weight);//放进数组	
					graph.addEdge(relationLinkArray[q], userVertexArray[0], userVertexArray[p]);//加边
					graph1[0][p]=weight;graph1[p][0]=weight;
					}
					else{
						p++;		
						userVertexArray[p]=new UserVertex(String.valueOf(u.getUser1Id()),u.getUser1Name());//放进数组	
						graph.addVertex(userVertexArray[p]);
						q++;
						weight=u.getCommentsNum()+u.getRtNum()+u.getRtAndCommentNum();
						relationLinkArray[q]=new RelationLink(null, user1Id+"*"+String.valueOf(u.getUser1Id()), weight);//放进数组	
						graph.addEdge(relationLinkArray[q], userVertexArray[0], userVertexArray[p]);//加边
						graph1[0][p]=weight;graph1[p][0]=weight;
					}
					}	
				}//for
				
				print(graph1,p);
				
				System.out.println("The graph  = " + graph.toString());
				//默认显示两层
				int count=p;
			    for(int i=1;i<=count;i++){callfunc(userVertexArray[i].userId);}
			    
			    session.getTransaction().commit();
           	    session.close();
           	    sf.close();
           	    //数据库操作结束
				
					Layout<UserVertex, RelationLink> layout = new FRLayout<UserVertex, RelationLink>(graph);
					final VisualizationViewer<UserVertex, RelationLink> vv = new VisualizationViewer<UserVertex, RelationLink>( layout);
					vv.setPreferredSize(new Dimension(1200,1000));
					vv.getRenderContext().setVertexLabelTransformer(new ToStringLabeller());//显示结点内容
					vv.getRenderContext().setEdgeLabelTransformer(new ToStringLabeller());//显示边的内容
				
					/*
					
					//自定义结点形状
					// 1. transformer from vertex to its size (width)
					Transformer<UserVertex, Integer> vst = new Transformer<UserVertex, Integer>() {
						public Integer transform(UserVertex i) {
							int len = i.toString().length();
							if (len < 3)
								len = 3;
							return new Integer(len * 8 + 16);
						}
					};
					// 2. transformer from vertex to its shape's "aspect ratio"
					Transformer<UserVertex, Float> vart = new Transformer<UserVertex, Float>() {
						public Float transform(UserVertex i) {
							int len = i.toString().length();
							if (len < 3)
								len = 3;
							return new Float(2.0 / len);
						}
					};
					//3. create the shape factory
					final VertexShapeFactory<UserVertex> vsf = new VertexShapeFactory<UserVertex>(vst, vart);
					// 4. EASY way to have a "vertex shape transformer"
					Transformer<UserVertex, Shape> vstr = new Transformer<UserVertex, Shape>() {
						public Shape transform(UserVertex i) {
							//return vsf.getRegularStar(i, 5);
							return vsf.getRegularPolygon(i,5);
							
						}
					};
					//5. put the shape transformer to render context, done!
					vv.getRenderContext().setVertexShapeTransformer(vstr);
					*/
					
					/*
					// //自定义结点形状(Manually create a custom vertex shape)
					Transformer<UserVertex, Shape> vstr = new Transformer<UserVertex, Shape>() {
						public Shape transform(UserVertex i) {
							int len = i.toString().length();
							if (len < 4)
								len = 4;
							Arc2D.Double r = new Arc2D.Double();
							r.setArc(-len * 5, -len * 3, len * 10, len * 6, 60, 240, Arc2D.PIE);
							//RoundRectangle2D.Double r = new RoundRectangle2D.Double(-len * 5, -10, len * 10, 20 ,10 ,10);
							return r;
						}
					};
					vv.getRenderContext().setVertexShapeTransformer(vstr);
					*/
					
					/*
					//为结点自定义图标
					final ImageIcon ii=new ImageIcon("shy.jpg");
					Transformer <UserVertex, Icon> vit=new Transformer<UserVertex,Icon>(){
						public Icon transform(UserVertex arg0) {
							return ii;
						}
					};
					vv.getRenderContext().setVertexIconTransformer(vit);
					vv.getRenderer().getVertexLabelRenderer().setPosition(Position.E);
					*/
                     
					//改变边的粗细  Set up stroke Transformers for the edges
					final Stroke edgeStroke = new BasicStroke(1);
					final Stroke shortestStroke = new BasicStroke(4);// thick edge line!
					Transformer<RelationLink, Stroke> edgeStrokeTransformer = new Transformer<RelationLink, Stroke>() {
						public Stroke transform(RelationLink s) {
							/*for (int i = 0; i < relationLinkArray.length; i++) {
								if (relationLinkArray[i].equals(s))
									return shortestStroke;
							}*/
							return edgeStroke;
						}
					};
					vv.getRenderContext().setEdgeStrokeTransformer(edgeStrokeTransformer);
					
			
					// Create a graph mouse and add it to the visualization component
					//DefaultModalGraphMouse<Object, Object> gm = new DefaultModalGraphMouse<Object, Object>();
					
					// mouse interactive
					EditingModalGraphMouse<UserVertex, RelationLink> gm = new EditingModalGraphMouse<UserVertex, RelationLink>(
							vv.getRenderContext(), UserVertexFactory.getInstance(), RelationLinkFactory
									.getInstance());
					//gm.setMode(ModalGraphMouse.Mode.TRANSFORMING);
					
					
					
					PopupVertexEdgeMenuMousePlugin<UserVertex, RelationLink> myPlugin = new PopupVertexEdgeMenuMousePlugin<UserVertex, RelationLink>();
					JPopupMenu edgeMenu = new JPopupMenu("Vertex Menu");
					JPopupMenu vertexMenu = new JPopupMenu("Edge Menu");
					edgeMenu.add(new JMenuItem("edge!"));
					//edgeMenu.add(new JMenuItem("edge's weight:"+relationLinkArray[0].getWeight()));
					vertexMenu.add(new JMenuItem("vertex!"));
					myPlugin.setEdgePopup(edgeMenu);
					myPlugin.setVertexPopup(vertexMenu);
					
					gm.remove(gm.getPopupEditingPlugin());
					// Add our new plugin to the mouse
					gm.add(myPlugin);
					
					vv.setGraphMouse(gm);
					gm.setMode(ModalGraphMouse.Mode.PICKING);
					
					 frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
					 JMenuBar menuBar = new JMenuBar();
					 JMenu modeMenu = gm.getModeMenu(); // Obtain mode menu from the mouse
					 modeMenu.setText("Mouse Mode");
					 modeMenu.setIcon(null); // I'm using this in a main menu
					 modeMenu.setPreferredSize(new Dimension(80,20)); // Change the size
					 menuBar.add(modeMenu);
					 frame.setJMenuBar(menuBar);
					 
							
					 
					 button1.setBackground(Color.green);
					 button1.setSize(10, 10);
				        // 用先前创建的Frame来显示这个按钮
					 frame.getContentPane().add(button1);
				     frame.getContentPane().setLayout(new FlowLayout());
				     
				     button1.addActionListener(new ActionListener(){

	                       public void actionPerformed(ActionEvent e){

	                       	try {
	                       		System.out.println("当前的addcount是："+addCount);
	                       		if(addCount==ADDCOUNT){
	                       		
	                       		 final int red1[]=findGroup();
	                       			
                    				
                    			//对找到的阶数最大的边涂色（红色）
                    				vv.getRenderContext().setEdgeFillPaintTransformer(
                    						new Transformer<RelationLink, Paint>() {
                    							public Paint transform(RelationLink p) {
                    								for(int nn=0;nn<red1.length;nn++)
                    								if (p.equals(relationLinkArray[red1[nn]]))
                    									return Color.RED;
                    								    //return Color.GREEN;
                    								    return null;
                    								
                    							}
                    						});
                    				button1.setEnabled(false); 
                    				
	                       		}
	                       		else{	                       			
	                       			System.out.println("当前人数为:"+(p+1)+"    当前边数为:"+(q+1));
	                       			if((p+1)>400){//如果人数太多，就执行寻找社团算法
	                       				 final int red1[]=findGroup();
	                       				findGroup();
	                       			//对找到的阶数最大的边涂色（红色）
	                       				vv.getRenderContext().setEdgeFillPaintTransformer(
	                       						new Transformer<RelationLink, Paint>() {
	                       							public Paint transform(RelationLink p) {
	                       								for(int nn=0;nn<red1.length;nn++)
	                       								if (p.equals(relationLinkArray[red1[nn]]))
	                       									return Color.RED;
	                       								    //return Color.GREEN;
	                       								      return null;
	                       								
	                       							}
	                       						});
	                       				button1.setEnabled(false); 
	                       			}
	                       			else{
	                       			System.out.println("*******************请输入用户id****************************:");
	                       			String id=null;
	                       			Scanner keyboard = new Scanner(System.in); 
	                       			id=keyboard.next();
									callfunc(id);
									addCount++;
									}
	                       		}
	                       		
	        					vv.getRenderContext().setVertexFillPaintTransformer(
	        							new Transformer<UserVertex, Paint>() {
	        								public Paint transform(UserVertex q) {
	        									
	        									
	        									if (isinGc(q.getId())){//q是中心节点
	        										if(q.getId()==0)
	        											return Color.PINK;
	        										else if(q.getId()==1)
	        											return Color.GREEN;
	        										else if(q.getId()==2)
	        											return Color.BLUE;
	        										else if(q.getId()==3)
	        											return Color.YELLOW;
	        										else if(q.getId()==4)
	        											return Color.GRAY;
	        										else if(q.getId()==5)
	        											return Color.MAGENTA;
	        										else if(q.getId()==6)
	        											return Color.LIGHT_GRAY;
	        										else if(q.getId()==7)
	        											return Color.CYAN;
	        										else if(q.getId()==8)
	        											return Color.DARK_GRAY;
	        										else if(q.getId()==9)
	        											return Color.ORANGE;
	        										if(q.getId()==10)
	        											return Color.PINK;
	        										else if(q.getId()==11)
	        											return Color.GREEN;
	        										else if(q.getId()==12)
	        											return Color.BLUE;
	        										else if(q.getId()==13)
	        											return Color.YELLOW;
	        										else if(q.getId()==14)
	        											return Color.GRAY;
	        										else if(q.getId()==15)
	        											return Color.MAGENTA;
	        										else if(q.getId()==16)
	        											return Color.LIGHT_GRAY;
	        										else if(q.getId()==17)
	        											return Color.CYAN;
	        										else if(q.getId()==18)
	        											return Color.DARK_GRAY;
	        										else if(q.getId()==19)
	        											return Color.ORANGE;
	        										
	        									}
	        									else
	        										for(int j=0;j<=p;j++){
	        											if(j!=q.getId()&&graph1[q.getId()][j]<100){
	        												if(j==0)
	        													return Color.PINK;
	        												else if(j==1)
	        													return Color.GREEN;
	        												else if(j==2)
	        													return Color.BLUE;
	        												else if(j==3)
	        													return Color.YELLOW;
	        												else if(j==4)
	        													return Color.GRAY;
	        												else if(j==5)
	        													return Color.MAGENTA;
	        												else if(j==6)
	        													return Color.LIGHT_GRAY;
	        												else if(j==7)
	        													return Color.CYAN;
	        												else if(j==8)
	        													return Color.DARK_GRAY;
	        												else if(j==9)
	        													return Color.ORANGE;
	         												if(j==10)
	        													return Color.PINK;
	        												else if(j==11)
	        													return Color.GREEN;
	        												else if(j==12)
	        													return Color.BLUE;
	        												else if(j==13)
	        													return Color.YELLOW;
	        												else if(j==14)
	        													return Color.GRAY;
	        												else if(j==15)
	        													return Color.MAGENTA;
	        												else if(j==16)
	        													return Color.LIGHT_GRAY;
	        												else if(j==17)
	        													return Color.CYAN;
	        												else if(j==18)
	        													return Color.DARK_GRAY;
	        												else if(j==19)
	        													return Color.ORANGE;
	        											}
	        										}//for j
	        									return Color.BLACK;
	        										
	        										
	        								}
	        							});
								}//try
	                       	catch (WeiboException e1) {
									
									e1.printStackTrace();
								}
	                       }//actionPerformed
	               });
				     
					 frame.getContentPane().add(vv);
					 frame.pack();
					 frame.setVisible(true);
					
	}
	
	//callfunc函数用于从第一层关系网扩展
	
    @SuppressWarnings("unchecked")
	public static void callfunc(final String user1Id) throws WeiboException{
			
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
   	    	//Object[] o = (Object[]) itr.next();
   	    	//User user = (User)o[0];
   	    	//Relation relation = (Relation) o[1];
   	    	Relation relation = (Relation)itr.next();
   	    	//System.out.println("Relation-User2Name: " + relation.getUser2Name());
   	    	System.out.println("Edge-weight: " + relation.getCommentsNum()+relation.getRtNum()+relation.getRtAndCommentNum());
   	     }

   	    //session.getTransaction().commit();
   	    //session.close();
   	    //sf.close();
   	    
   	    
			
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
                		if(u.getUser1Id().equals(user1Id)){
                			re.setUser1Id(user1Id);
                			re.setUser2Id(u.getUser2Id());
                			re.setUser1Name(u.getUser1Name());
                			re.setUser2Name(u.getUser2Name());}
                		else{
                			re.setUser1Id(u.getUser2Id());
                			re.setUser2Id(user1Id);
                			re.setUser1Name(u.getUser2Name());
                			re.setUser2Name(u.getUser1Name());
                		}
				
                		//遍历节点数字，看是否有必要加节点
              		//System.out.println("判断是否有必要加点");
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
                				//System.out.println("判断是否有必要加边");
                				
                				for(int n2=0;n2<=e2&&!isFound2;n2++){
                					RelationLink s2 = relationLinkArray[n2];
        	                			if((s.getUserId()+"*"+user1Id).equals(s2.getId1id2())||(user1Id+"*"+s.getUserId()).equals(s2.getId1id2()))//边已经存在
        	                			{
        	                				isFound2=true;
        	                			//	System.out.println("边已经存在");
        	                				}//if
                				}//while
        	                			if(!isFound2)//边不存在
        	                			{
        	                				//System.out.println("边不存在");
        	                				q++;
        	                				weight=u.getCommentsNum()+u.getRtNum()+u.getRtAndCommentNum();
        	                				relationLinkArray[q]=new RelationLink(s, user1Id+"*"+s.getUserId(), weight);
        	                				graph.addEdge(relationLinkArray[q], userVertexCenter, s);//加边    
        	                				graph1[n3][n1]=weight;graph1[n1][n3]=weight;
        	                				//System.out.println("加边");
        	                			} //if		       				 
                			}//if
                		}//while
                	 
                	    		if (!isFound1)//点不存在
                	    		{
              	    			   // System.out.println("加边 加点"); 
              	    			    p++;
              	    			    userVertexArray[p]=new UserVertex(re.getUser2Id(),re.getUser2Name());
              	    			    graph.addVertex(userVertexArray[p]);
               					    q++;
               					    weight=u.getCommentsNum()+u.getRtNum()+u.getRtAndCommentNum();
               					    relationLinkArray[q]=new RelationLink(userVertexCenter, user1Id+"*"+String.valueOf(re.getUser2Id()), weight);
                					graph.addEdge(relationLinkArray[q], userVertexCenter, userVertexArray[p]);//加边
                					graph1[n3][p]=weight;graph1[p][n3]=weight;	
                	    			
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
		
		
		
		public static int[] findGroup(){
			
			final	int red[]=new int[LINEFIND];//存储找到的每条边的下标
			for(int n=0;n<LINEFIND&&n<=q;n++){//循环找到介数最大的n条边
				  for(int k1=0;k1<=499;k1++){//初始化图的矩阵和最短路径经过每条边次数的统计
		    			for(int k2=0;k2<=499;k2++){
		    				times[k1][k2]=0;
		    				
		    				}
		    			}
				
			//*******************************************************************************************88				
           // print(graph1,p);//输出当前得到的图的矩阵
			for(int n1=0;n1<=p;n1++)//循环找从每个节点到其他节点的最短路径
            {shortPath(n1);}
			
			//System.out.println("打印统计结果");
			//print(times,p);//打印统计结果			
			//********************************************************************************************88			
			//找到那条边 放进red数组
			final int fn=findMax(times,p);
			if(fn>=0){
				red[n]=fn;
				System.out.println("次数最多的边是"+relationLinkArray[fn].toString());
			
			graph1[b1][b2]=MAX;graph1[b2][b1]=MAX;//修改graph，两个节点状态为不连通（删除边）
			
			}
			else{
				System.out.println("再也找不到介数最大的边啦！请下次重新定义“LINEFIND”的大小！");
			}
				
			}
			System.out.println("当前找到的介数最大的边下标数组：");
			for(int m=0;m<red.length;m++)System.out.print(red[m]+", ");System.out.println();
			return red;
		}//findGroup
		
		
		
		
		
		static boolean finals[]=new boolean[500];
		static int min;
		static int d[]=new int[500];
					
		public  static void shortPath(int v0){//求不同结点之间的最短路径
			

	           
	            for(int k1=0;k1<=499;k1++){
					for(int k2=0;k2<=499;k2++){
						pa[k1][k2]=-2;
						}
					}
			
						int q[]= new int[500];//链路下标数组
						for(int n=0;n<q.length;n++)q[n]=0;//下标初始化 从0开始
						
						
														
						//for(int n=0;n<d.length;n++)d[n]=500;
						for(int n=0;n<finals.length;n++)finals[n]=false;
						
						for(int v=0;v<=p;v++){
							
							finals[v]=false; d[v]=graph1[v0][v];
							//for(int w=0;w<=p;w++)pa[v][w]=0;
							if(d[v]<MAX){pa[v][q[v]]=v0;q[v]++;pa[v][q[v]]=v;q[v]++;}	//把v0放第一个，自己放第二个	 每次放完记得下标+1
						}//for  v
						//System.out.println("节点4的初始路径："+pa[4][0]+pa[4][1]+pa[4][2]);
						d[v0]=0;finals[v0]=true;//把自己加进去
						//for(int n=0;n<=p;n++)System.out.println(d[n]+" "); System.out.println();
		 
						int v=-1;
						int temp=-1;
						for(int i=1;i<=p;i++){//主循环，每次求得v0到某个v定点的最短路径，并加到finalS集合
							min=MAX;//当前所知，距离v0最近的距离
							
							for(int w=0;w<=p;w++){//找出当前不在finals中的，可(一步)到达v0的最近的定点v，并放进finals
								if(!finals[w])
									if(d[w]<min){ v=w;  min=d[w];}}
							
							if(v>=0){
								finals[v]=true;
							
							//System.out.println(v);//打印出放进去的节点
							
							for(int w=0;w<=p;w++)//
								if(!finals[w]&&(min+graph1[v][w])<d[w]){
									//System.out.println(w+"复制"+v);
									d[w]=min+graph1[v][w];//*******（v--->W）*****
									//System.out.println(w+"的长度改为"+d[w]);
									
									for(int n=0;n<=p;n++){temp=pa[v][n];pa[w][n]=temp;}
									temp=q[v];q[w]=temp; pa[w][q[w]]=w;q[w]++; //pa[w]=p[v]+p[w]
									
									//System.out.print(w+"的路径修改为");
									//for(int n=0;n<pa[w].length;n++)System.out.print(pa[w][n]);System.out.println();								
								}//if
						}//for i
						}
						//print(pa,p);//打印链路矩阵
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
			int max=a[0][0];
			for(int n1=0;n1<=l;n1++){
				for(int n2=0;n2<=l;n2++){
					if(a[n1][n2]>max){
						max=a[n1][n2];
						b1=n1;b2=n2;
					}//if 替换
				}//for n2			
			}//for n1
			addGv(b1);
			addGv(b2);

			String id1=userVertexArray[b1].userId;
			String id2=userVertexArray[b2].userId;
			for(int n=0;n<relationLinkArray.length;n++){
				if(relationLinkArray[n].getId1id2().equals(id1+"*"+id2)||relationLinkArray[n].getId1id2().equals(id2+"*"+id1)){
					return n;
				}
		}
		return -1;
		
		}
		
		public static int addGv(int e){//把可能的中心节点加到中心数组里面
			for(int n=0;n<=gC;n++){
				if(groupCenters[n]==e)return -1;
			}
			gC++;
			groupCenters[gC]=e;
			return 1;
		}

		public static boolean isinGc(int e){
			for(int n=0;n<=gC;n++)
				if(groupCenters[n]==e)return true;
			return false;
		}
}
