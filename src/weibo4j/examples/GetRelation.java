package weibo4j.examples;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import weibo4j.User;
import weibo4j.Weibo;

public class GetRelation {

	    /**
	     * 获得关注用户的信息
	     * Usage: java -DWeibo4j.oauth.consumerKey=[consumer key] -DWeibo4j.oauth.consumerSecret=[consumer secret] Weibo4j.examples.GetFriends [accessToken] [accessSecret]
	     * @param args message
	     */
	    public static void main(String[] args) {
	        try {
	        	System.setProperty("weibo4j.oauth.consumerKey", Weibo.CONSUMER_KEY);
	        	System.setProperty("weibo4j.oauth.consumerSecret", Weibo.CONSUMER_SECRET);
	        	
	            Weibo weibo = new Weibo();
	            
	            weibo.setToken(args[0], args[1]);
	  
				 try {
                        int cursor;
                        cursor=0;
						List<User> list= weibo.getFriendsStatuses(cursor);
						for(User u:list){
							System.out.println(u.getId()+":"+u.getName());
							/*u.setId(u.getId());
							u.setName(u.getName());
							u.setFollowersCount(u.getFollowersCount());
							u.setFriendsCount(u.getFriendsCount());
							u.setStatusesCount(u.getStatusesCount());
							*/
							Configuration cfg = new Configuration();
				    	    SessionFactory sf = cfg.configure().buildSessionFactory();
				    	    Session session =sf.openSession();

				    	    session.beginTransaction();
				    	   session.save(u);
				    	   // session.delete(customer);
				    	    session.getTransaction().commit();

				    	    session.close();
				    	    sf.close();
						}
			
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
	            System.exit(0);
	        } catch (Exception ioe) {
	            System.out.println("Failed to read the system input.");
	            System.exit( -1);
	        }
	    }
}
