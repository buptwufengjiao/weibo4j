/**
 *
 */
package weibo4j.examples.timeline;

import java.util.Iterator;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import weibo4j.Count;
import weibo4j.Status;
import weibo4j.Weibo;

/**
 * @author sina
 *
 */
public class GetCounts {

	/**
	 * 批量统计微博的评论数，转发数
	 * @param args
	 */
	public static void main(String[] args) {
		System.setProperty("weibo4j.oauth.consumerKey", Weibo.CONSUMER_KEY);
    	System.setProperty("weibo4j.oauth.consumerSecret", Weibo.CONSUMER_SECRET);
        try {
        	Weibo weibo = getWeibo(true,args);
        	List<Status> statuses = weibo.getUserTimeline(args[2]);
        	StringBuilder ids = new StringBuilder();
        	for(Status status : statuses) {
        		
        		ids.append(status.getId()).append(',');
                //System.out.println("关注数" +status.getUser().getFriendsCount());
	            //System.out.println("粉丝数" +status.getUser().getFollowersCount());
	            //System.out.println("微博数" +status.getUser().getStatusesCount());
        	}   
        	ids.deleteCharAt(ids.length() - 1);
        	List<Count> counts = weibo.getCounts(ids.toString());
    		for(Count count : counts) {
    			System.out.println("评论数："+count.getComments()+" - "+"转发数："+count.getRt());
    			count.setComments(count.getComments());
    			count.setRt(count.getRt());
    			
    		}
    		
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
