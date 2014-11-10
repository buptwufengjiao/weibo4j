/**
 * 
 */
package weibo4j.examples.timeline;

import java.util.Iterator;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import weibo4j.Comment;
import weibo4j.Relation;
import weibo4j.Repost;
import weibo4j.Status;
import weibo4j.Tag;
import weibo4j.User;
import weibo4j.Weibo;


/**
 * @author sina
 *
 */
public class GetRepostRelation {
	/**
	 * 获取用户发布的微博信息列表 
	 * @param args
	 */
	public static void main(String[] args) {
		System.setProperty("weibo4j.oauth.consumerKey", Weibo.CONSUMER_KEY);
    	System.setProperty("weibo4j.oauth.consumerSecret", Weibo.CONSUMER_SECRET);
    	
		try {
    		Configuration cfg = new Configuration();
    	    SessionFactory sf = cfg.configure().buildSessionFactory();
    	    Session session =sf.openSession();
    	    Transaction tx=session.beginTransaction();
		try {
			//获取24小时内前20条用户的微博信息;args[2]:用户ID
			Weibo weibo = getWeibo(true,args);
			List<Status> statuses = weibo.getUserTimeline(args[2]);
			User user1 = new User();//发微博人id,被转发人
			for (Status status : statuses) {
				
				System.out.println(status.getText());
				
				status.setUserId(args[2]);//微博原创者的用户id
				status.setUserName(status.getUser().getName());//微博原创者的用户姓名
				
        		StringBuilder ids = new StringBuilder();
        		ids.append(status.getId()).append(',');   
            	
				//User user1 = new User();//发微博人id,被转发人
				user1.setId(args[2]);
				user1.setName(status.getUser().getName());
				user1.setLocation(status.getUser().getLocation());
				user1.setFollowersCount(status.getUser().getFollowersCount());
				user1.setFriendsCount(status.getUser().getFriendsCount());
				user1.setStatusesCount(status.getUser().getStatusesCount());
				System.out.println("原创人发的微博数："+status.getUser().getStatusesCount());

            	
				
				
	        	List <Status> Repost =  weibo.getreposttimeline(String.valueOf(status.getId()));
	        	
	        	for(Status repost:Repost){
	        	//System.out.println(status.toString());
	        	System.out.println("转发人id,name："+repost.getUser().getId()+","+repost.getUser().getName());
	        	System.out.println("被转发人(原创人)id,name："+args[2]+","+user1.getName());
	        	System.out.println("被转发微博id："+status.getId());
	        	System.out.println("被转发微博内容："+status.getText());
	        	//System.out.println("评论内容+"+repost.getText());
	        	//System.out.println("转发时间+"+repost.getCreatedAt());
				
	            List<Tag> gettags=getWeibo(true,args).gettags(repost.getUser().getId());
	            String tags ="";
	        	for(Tag tag : gettags) {
	        		 tags=tags+tag.toString();
	        		System.out.println(tag);
	        	}
	        	System.out.println("转发人User2's tags:"+tags);
	        	
				User user2 = new User();//转发人id
				user2.setId(repost.getUser().getId());
				user2.setName(repost.getUser().getName());
				user2.setLocation(repost.getUser().getLocation());
				user2.setTags(tags);
				user2.setFollowersCount(repost.getUser().getFollowersCount());
				user2.setFriendsCount(repost.getUser().getFriendsCount());
				user2.setStatusesCount(repost.getUser().getStatusesCount());
				
				if(user2.getId()==user1.getId())
				{
					//user2.setStatusesCount(status.getUser().getStatusesCount());
					System.out.println("是同一个人"+","+"发的微博数："+status.getUser().getStatusesCount());
				}else
				{
					user2.setStatusesCount(repost.getUser().getStatusesCount());
					System.out.println("不是同一个人"+","+"发的微博数："+user2.getStatusesCount());
				}
				
				//新建转发类对象
				Repost rep=new Repost();
				rep.setUser1Id(user1.getId());
				rep.setUser1Name(user1.getName());
				rep.setUser2Id(user2.getId());
				rep.setUser2Name(user2.getName());
				rep.setRepost_time(repost.getCreatedAt());
				rep.setStatusId(status.getId());
				rep.setStatusText(status.getText());
				//rep.setCommentsText(repost.getText());
				
				boolean isCommented=false;
        		List<Comment> comments = weibo.getComments(String.valueOf(status.getId()));        		
        		for(Comment comment : comments) {

				if(repost.getCreatedAt().equals(comment.getCreatedAt()))
				{
					rep.setCommented(true);
				rep.setCommentsText(repost.getText());
				System.out.println("转发时间："+repost.getCreatedAt());
				System.out.println("评论时间："+comment.getCreatedAt());
				System.out.println("评论人"+comment.getUser().getId());
				System.out.println("转发人"+rep.getUser2Id());
				System.out.println("评论内容："+rep.getCommentsText());
        		
				System.out.println("转发的详细内容为:"+rep.toString());
				
				}else
				{
					//rep.setCommentsText(null);
					/*
					System.out.println("转发时间："+repost.getCreatedAt());
					System.out.println("评论时间："+comment.getCreatedAt());
					System.out.println("评论人"+comment.getUser().getId());
					System.out.println("转发人"+rep.getUser2Id());
					System.out.println("评论内容："+rep.getCommentsText());
					
					System.out.println("转发的详细内容为:"+rep.toString());
					*/
				}
        		}
				
				Relation relation = new Relation();
				relation.setUser1Id(user1.getId());
				relation.setUser1Name(user1.getName());
				relation.setUser2Id(user2.getId());
				relation.setUser2Name(user2.getName());
				//re.setCommentsNum(0);
				System.out.println("初始rtNum: " + relation.getRtNum());
    			
				String user1Id=String.valueOf(relation.getUser1Id());
				String user2Id=String.valueOf(relation.getUser2Id());
				
				System.out.println("user1Id: " + user1Id);
				System.out.println("user2Id: " + user2Id);

				
				//数据库的save操作
				//session.saveOrUpdate(user1);
        	    if(user1.getId()==user2.getId())
        	    {}
        	    else
        	    {
        	    	System.out.println("save评论人");
        	    session.merge(user2);
        	    }
        	    System.out.println("save转发表");
        	    session.saveOrUpdate(rep);
        	    
        	    String hql="from weibo4j.Relation relation where (relation.user1Id=:user1Id and relation.user2Id=:user2Id) or (relation.user1Id=:user2Id and relation.user2Id=:user1Id)";
        	    Query query = session.createQuery(hql);
           	    query.setString("user1Id",user1Id);
           	    query.setString("user2Id",user2Id);

           	    List<Relation> list1 = query.list(); 
           	    Iterator itr = list1.iterator();
           	    int rtNum=0;
           	    int rtAndCommentNum=0;

           	if(itr.hasNext()){
           		if(rep.isCommented())
           		{
           			
         	       System.out.println("relation表不为空，在getComment类中已保存过，不再保存");
         	       /*
       		       relation=(Relation)itr.next();
       		       rtAndCommentNum=relation.getRtAndCommentNum();
       		       rtAndCommentNum++;
    	    	   relation.setRtAndCommentNum(rtAndCommentNum);
        	       session.merge(relation);
        	       System.out.println("存之后的rtAndCommentNum1: " + relation.getRtAndCommentNum());
        	       */
        	       
        	     } else
            		{
           	        System.out.println("relation表不为空，只存转发数");
          		    relation=(Relation)itr.next();
          		    rtNum=relation.getRtNum();
          		    rtNum++;
       	    	    relation.setRtNum(rtNum);
           	        session.saveOrUpdate(relation);
           	        System.out.println("存之后的rtNum1: " + relation.getRtNum());
      	     }
           	}
           	else {
           		if(rep.isCommented())
           		{
           			
         	        System.out.println("relation表为空，在getComment类中已保存过，不再保存");
         	        /*
       	            relation.setRtAndCommentNum(1);
                	session.merge(relation);
                	System.out.println("存之后的rtAndCommentNum0: " + relation.getRtAndCommentNum()); 
                	 */
                	
                 }else
                 {
             	    System.out.println("relation表为空，只存转发数");
           	        relation.setRtNum(1);
                    session.saveOrUpdate(relation);
                    System.out.println("存之后的rtNum0: " + relation.getRtNum());
                 }
           		
           	}
	        }//for(Status repost:Repost)
				System.out.println("*********************************************************************");
	        	
			}//for (Status status : statuses) 
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		tx.commit();
		session.close();
		//sf.close();
	} catch (HibernateException e) {
		e.printStackTrace();
	}
		System.out.println("repost保存完成!");
	}	
	
	private static Weibo getWeibo(boolean isOauth,String[] args) {
		Weibo weibo = new Weibo();
		if(isOauth) {//oauth验证方式 args[0]:访问的token；args[1]:访问的密匙
			weibo.setToken(args[0], args[1]);
		}else {//用户登录方式
    		weibo.setUserId(args[0]);//用户名/ID
    		weibo.setPassword(args[1]);//密码
		}
		return weibo;
	}

}
