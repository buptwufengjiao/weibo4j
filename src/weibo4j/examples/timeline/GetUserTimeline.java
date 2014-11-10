/**
 * 
 */
package weibo4j.examples.timeline;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import weibo4j.Count;
import weibo4j.Status;
import weibo4j.Tag;
import weibo4j.User;
import weibo4j.Weibo;

/**
 * @author sina
 *
 */
public class GetUserTimeline {
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
			try{
			//获取24小时内前20条用户的微博信息;args[2]:用户ID
			List<Status> statuses = getWeibo(true,args).getUserTimeline(args[2]);
			
			for (Status status : statuses) {
				
				//System.out.println(status.getUser().getLocation());
				
				status.setUserId(args[2]);//微博原创者的用户id
				status.setUserName(status.getUser().getName());//微博原创者的用户姓名
				
        		StringBuilder ids = new StringBuilder();
        		ids.append(status.getId()).append(',');   
        		List<Count> counts =getWeibo(true,args).getCounts(ids.toString());
            	System.out.println(ids.toString());
            	for (Count count:counts){
        		    //System.out.println("-"+count.getComments()+" - "+count.getRt());
        		    status.setComments(count.getComments());
        		    status.setRt(count.getRt());
        		}
            	
				if(status.getRetweetDetails()!=null){
					status.setRetweeted_status_id(status.getRetweeted_status().getId());
					status.setRetweeted_status_text(status.getRetweeted_status().getText());
					status.setRetweeted_status_userId(String.valueOf(status.getRetweeted_status().getUser().getId()));
					status.setRetweeted_status_useName(status.getRetweeted_status().getUser().getName());
					
	    			/*System.out.println("转发的微博id："+status.getRetweeted_status().getId());
	    			System.out.println("被转发微博原创者的用户姓名："+status.getRetweeted_status().getUser().getName());
	    			System.out.println("被转发微博原创者的用户id："+status.getRetweeted_status().getUser().getId());
	    			System.out.println("转发的微博内容："+status.getRetweeted_status_text());
	    			System.out.println("转发的微博全部信息："+status.toString());*/
	    		}
				
		            List<Tag> gettags=getWeibo(true,args).gettags(args[2]);
		            String tags ="";
		        	for(Tag tag : gettags) {
		        		 tags=tags+tag.toString();
		        		//System.out.println(tag);
		        	}
		        	//System.out.println("tags:"+tags);
		        	
				User user1 = new User();//发微博人id
				user1.setId(args[2]);
				user1.setName(status.getUser().getName());
				user1.setLocation(status.getUser().getLocation());
				user1.setTags(tags);
				user1.setFollowersCount(status.getUser().getFollowersCount());
				user1.setFriendsCount(status.getUser().getFriendsCount());
				user1.setStatusesCount(status.getUser().getStatusesCount());
				System.out.println("原创人发的微博数："+status.getUser().getStatusesCount());
				
	    	    session.merge(user1);
	    	    session.merge(status);

	        }//for
		} catch (Exception e) {
			e.printStackTrace();
		}
		tx.commit();
		session.close();
		//sf.close();
	} catch (HibernateException e) {
		e.printStackTrace();
	}
		System.out.println("status保存完成!");
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
