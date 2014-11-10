package weibo4j.examples;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import weibo4j.Comment;
import weibo4j.Follower;
import weibo4j.Friend;
import weibo4j.User;
import weibo4j.Weibo;
import weibo4j.WeiboException;

public class GetFollowers {

	/**
	 * 返回用户粉丝列表，并返回最新微博文章。
	 * @param args
	 */
	public static void main(String[] args) {
		String user1Id="1257196584";
		String user1Name="娇娇_Candice";
		
			System.setProperty("weibo4j.oauth.consumerKey", Weibo.CONSUMER_KEY);
			System.setProperty("weibo4j.oauth.consumerSecret",
					Weibo.CONSUMER_SECRET);
			
			try {
	    		Configuration cfg = new Configuration();
	    	    SessionFactory sf = cfg.configure().buildSessionFactory();
	    	    Session session =sf.openSession();
	    	    Transaction tx=session.beginTransaction();
	 	try {
			Weibo weibo = getWeibo(true,args);
			List<User> list = weibo.getFollowersStatuses(user1Id);
			
			for(User user : list) {
				Follower follower = new Follower();
				follower.setUser1Id(user1Id);
				follower.setUser1Name(user1Name);
				follower.setUser2Id(user.getId());
				follower.setUser2Name(user.getName());

				System.out.println(follower.getUser1Id());
				System.out.println(follower.getUser2Id());
				
	    	    session.saveOrUpdate(follower);

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
		System.out.println("Follower保存完成!");
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