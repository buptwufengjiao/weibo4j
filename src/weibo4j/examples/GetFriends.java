package weibo4j.examples;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import weibo4j.Friend;
import weibo4j.User;
import weibo4j.Weibo;

public class GetFriends {

	/**
	 * 获得关注用户的信息 Usage: java -DWeibo4j.oauth.consumerKey=[consumer key]
	 * -DWeibo4j.oauth.consumerSecret=[consumer secret]
	 * Weibo4j.examples.GetFriends [accessToken] [accessSecret]
	 * 
	 * @param args
	 *            message
	 */
	public static void main(String[] args) {
		
		
		//String id="1866179020";
		//String user1Name="黄秋生";
		//String id="1704116960";
		//String user1Name="小s";
		//String id="1746274673";
		//String user1Name="李娜";
		//String id="1266321801";
		//String user1Name="姚晨";
		//String id="1195242865";
		//String user1Name="杨幂";
		//String id="1609740364";
		//String user1Name="清风之逸";
		String user1Id="1609740364";
		String user1Name="清风之逸";
		
			System.setProperty("weibo4j.oauth.consumerKey", Weibo.CONSUMER_KEY);
			System.setProperty("weibo4j.oauth.consumerSecret",
					Weibo.CONSUMER_SECRET);
			
			try {
	    		Configuration cfg = new Configuration();
	    	    SessionFactory sf = cfg.configure().buildSessionFactory();
	    	    Session session =sf.openSession();
	    	    Transaction tx=session.beginTransaction();

			try {
				Weibo weibo = getWeibo(true, args);
				
				List<User> list = weibo.getFriendsStatuses(user1Id);
				for (User user : list) {
					System.out.println(user.getId() + ":" + user.getName());
					//近期联系人不包括关注人
					
					Friend friend = new Friend();
					friend.setUser1Id(user1Id);
					friend.setUser1Name(user1Name);
					friend.setUser2Id(user.getId());
					friend.setUser2Name(user.getName());

					System.out.println(friend.getUser1Id());
					System.out.println(friend.getUser2Id());
					
		    	    session.saveOrUpdate(friend);

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
			System.out.println("friend保存完成!");
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
