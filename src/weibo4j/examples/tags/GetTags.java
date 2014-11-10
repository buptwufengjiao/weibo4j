package weibo4j.examples.tags;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import weibo4j.Tag;
import weibo4j.Weibo;
import weibo4j.org.json.JSONObject;

public class GetTags{

	/**
	 * 获取tags；
	 * @param args
	 */
	public static void main(String[] args) {
		System.setProperty("weibo4j.oauth.consumerKey", Weibo.CONSUMER_KEY);
    	System.setProperty("weibo4j.oauth.consumerSecret", Weibo.CONSUMER_SECRET);
        try {
        	Weibo weibo = getWeibo(true,args);
            List<Tag> gettags=weibo.gettags(args[2]);
            String tag ="";
        	for(Tag status : gettags) {
        		 tag=tag+status.toString();
        		System.out.println(tag);
        		
        		//status.setId(String.valueOf(args[2]));
        		/*
        		Configuration cfg = new Configuration();
	    	    SessionFactory sf = cfg.configure().buildSessionFactory();
	    	    Session session =sf.openSession();

	    	    session.beginTransaction();
	    	    session.save(status);
	    	   // session.delete(customer);
	    	    session.getTransaction().commit();

	    	    session.close();
	    	    sf.close();
	    	    */
        	}
        	System.out.println("tags:"+tag);
        	
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static Weibo getWeibo(boolean isOauth,String ... args) {
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