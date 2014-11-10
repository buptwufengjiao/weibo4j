
package weibo4j.examples.timeline;

import java.util.Iterator;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import com.sun.xml.internal.ws.util.StringUtils;

import weibo4j.Comment;
import weibo4j.Relation;
import weibo4j.Status;
import weibo4j.Tag;
import weibo4j.User;
import weibo4j.Weibo;

/**
 * @author sina
 *
 */
public class GetComments {

	/**
	 * 返回指定微博的最新n条评论 
	 * @param args
	 */
	@SuppressWarnings("unchecked")
	public static void main(String[] args) {
		System.setProperty("weibo4j.oauth.consumerKey", Weibo.CONSUMER_KEY);
    	System.setProperty("weibo4j.oauth.consumerSecret", Weibo.CONSUMER_SECRET);
		try {
    		Configuration cfg = new Configuration();
    	    SessionFactory sf = cfg.configure().buildSessionFactory();
    	    Session session =sf.openSession();
    	    Transaction tx=session.beginTransaction();
        try {
        	Weibo weibo = getWeibo(true,args);
        	//args[2]:用户id
        	List<Status> list = weibo.getUserTimeline(args[2]);
        	for(int i=0;i<list.size();i++){                  	
        		//第i条微博信息id
        		String sid = list.get(i).getId()+"";   
        		/*
        		System.out.println(list.get(i).getId());
        		System.out.println(list.get(i).getUser().getName());
        		System.out.println(list.get(i).getUser().getLocation());
        		System.out.println(list.get(i).getUser().getFollowersCount());
        		System.out.println(list.get(i).getUser().getFriendsCount());
        		System.out.println(list.get(i).getUser().getStatusesCount());
        		*/
        		
        		List<Comment> comments = weibo.getComments(sid);        		
        		for(Comment comment : comments) {
        			System.out.println(""+sid);
        			System.out.println(comment.getText()+":"+comment.getCreatedAt()+":"+comment.getSource()+":"+comment.getUser().getName());
        			//System.out.println("评论转发信息："+comment.getRetweetDetails());
        			
        			comment.setUser1Id(list.get(i).getUser().getId());//被评论人id
        			comment.setUser1Name(list.get(i).getUser().getName());
        			comment.setUser2Id(comment.getUser().getId());//评论人id
        			comment.setUser2Name(comment.getUser().getName());
        			comment.setStatusId(sid);
        			comment.setStatusText(list.get(i).getText());
        			
        			boolean isReposted=false;
    	        	List <Status> Repost =  weibo.getreposttimeline(String.valueOf(list.get(i).getId()));
    	        	for(Status repost:Repost){
    	        		if(repost.getCreatedAt().equals(comment.getCreatedAt())){
        	        		comment.setReposted(true);
        	        		System.out.println("转发时间："+repost.getCreatedAt());
        	        		System.out.println("评论时间："+comment.getCreatedAt());
    	    				System.out.println("是否被同时转发？"+comment.isReposted());
    	    				System.out.println("评论并转发！");
    	        		}else{
    	    				//comment.setReposted(false);
        	        		System.out.println("转发时间："+repost.getCreatedAt());
        	        		System.out.println("评论时间："+comment.getCreatedAt());
    	    				System.out.println("是否被同时转发？"+comment.isReposted());
    	        			System.out.println("只评论！");
    	        		}
    	        	}
        			
					User user1 = new User();//被评论人id
					user1.setId(comment.getUser1Id());
					user1.setName(comment.getUser1Name());
					user1.setLocation(list.get(i).getUser().getLocation());
					user1.setFollowersCount(list.get(i).getUser().getFollowersCount());
					user1.setFriendsCount(list.get(i).getUser().getFriendsCount());
					user1.setStatusesCount(list.get(i).getUser().getStatusesCount());
					
		            List<Tag> gettags=getWeibo(true,args).gettags(comment.getUser2Id());
		            String tags ="";
		        	for(Tag tag : gettags) {
		        		 tags=tags+tag.toString();
		        		System.out.println(tag);
		        	}
		        	System.out.println("转发人User2's tags:"+tags);
		        	
					User user2 = new User();//评论人id
					user2.setId(comment.getUser2Id());
					user2.setName(comment.getUser2Name());
					user2.setLocation(comment.getUser().getLocation());
					user2.setTags(tags);
					user2.setFollowersCount(comment.getUser().getFollowersCount());
					user2.setFriendsCount(comment.getUser().getFriendsCount());
					user2.setStatusesCount(comment.getUser().getStatusesCount());
					
					Relation relation = new Relation();
					relation.setUser1Id(args[2]);
					relation.setUser1Name(list.get(i).getUser().getName());
					relation.setUser2Id(comment.getUser2Id());
					relation.setUser2Name(comment.getUser2Name());
					//re.setCommentsNum(0);
					System.out.println("初始commentsNum: " + relation.getCommentsNum());
        			
					String user1Id=String.valueOf(relation.getUser1Id());
					String user2Id=String.valueOf(relation.getUser2Id());
					
					System.out.println("user1Id: " + user1Id);
					System.out.println("user2Id: " + user2Id);
					
					//数据库的save操作
            	   // session.merge(user1);
            	    if(user1.getId()==user2.getId())
            	    {}
            	    else
            	    {
            	    session.merge(user2);
            	    }
            	    session.merge(comment);
            	    
            	    
            	    String hql="from weibo4j.Relation relation where (relation.user1Id=:user1Id and relation.user2Id=:user2Id) or (relation.user1Id=:user2Id and relation.user2Id=:user1Id)";
            	    Query query = session.createQuery(hql);
               	    query.setString("user1Id",user1Id);
               	    query.setString("user2Id",user2Id);
            	    
               	    List<Relation> list1 = query.list(); 
               	    Iterator itr = list1.iterator();
               	    int commentsNum=0;
               	    int rtAndCommentNum=0;

               	if(itr.hasNext()){
               		if(comment.isReposted())
               		{
            		       relation=(Relation)itr.next();
            		       rtAndCommentNum=relation.getRtAndCommentNum();
            		       rtAndCommentNum++;
            		       System.out.println("存之hou的rtAndCommentNum: ");
            	    	   relation.setRtAndCommentNum(rtAndCommentNum);
                	       session.saveOrUpdate(relation);
                	       //session.clear();
                	       System.out.println("存之后的rtAndCommentNum1: " + relation.getRtAndCommentNum());
               		}else{
               		       relation=(Relation)itr.next();
               		commentsNum=relation.getCommentsNum();
               		commentsNum++;
            	    	   relation.setCommentsNum(commentsNum);
                	       session.saveOrUpdate(relation);
                	       //session.clear();
                	       System.out.println("存之后的commentsNum1: " + relation.getCommentsNum());
            	    	 }
               	}
               	else{
               		System.out.println(comment.isReposted());
               		if(comment.isReposted())
               		{
               			
    	    	        relation.setRtAndCommentNum(1);
                     	session.saveOrUpdate(relation);
                     	System.out.println("存之后的RtAndCommentNum0: " + relation.getRtAndCommentNum());
               		}else
               		{
	    	        relation.setCommentsNum(1);
                 	session.saveOrUpdate(relation);
                 	System.out.println("存之后的commentsNum0: " + relation.getCommentsNum());
                   	}
               	}
        		    }//for comment
   	   
        		}//for int
        	
        }//try
        catch (Exception e) {
			e.printStackTrace();
		}
		tx.commit();
		session.close();
		sf.close();
		} catch (HibernateException e) {
			e.printStackTrace();
		}
			System.out.println("comment保存完成!");
		}
	

	private static Weibo getWeibo(boolean isOauth,String ... args) {
		Weibo weibo = new Weibo();
		if(isOauth) {//oauth验证方式 args[0]:访问的token；args[1]:访问的密匙
			//weibo.setToken(args[0], args[1]);
			weibo.setToken(args[0],args[1]);
		}else {//用户登录方式
    		weibo.setUserId(args[0]);//用户名/ID
    		weibo.setPassword(args[1]);//密码
		}
		return weibo;
	}

}
