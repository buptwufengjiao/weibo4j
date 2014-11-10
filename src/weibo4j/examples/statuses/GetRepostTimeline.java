/**
 * 
 */
package weibo4j.examples.statuses;

import java.util.List;

import weibo4j.Status;
import weibo4j.Tweet;
import weibo4j.Weibo;

/**
 * @author haidong
 *
 */
public class GetRepostTimeline {

	/**
	 * 返回一条原创微博消息的最新n条转发微博消息。本接口无法对非原创微博进行查询。 
     * @param args
	 */
	public static void main(String[] args) {
		System.setProperty("weibo4j.oauth.consumerKey", Weibo.CONSUMER_KEY);
    	System.setProperty("weibo4j.oauth.consumerSecret", Weibo.CONSUMER_SECRET);
        try {
        	Weibo weibo = getWeibo(true,args);
        	List <Status> list =  weibo.getreposttimeline(args[2]);
        	for(Status status:list){
        	System.out.println(status.toString());
        	System.out.println(status.getRetweeted_status().getCreatedAt());
        	
        	System.out.println(status.getCreatedAt());
        	}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static Weibo getWeibo(boolean isOauth,String ... args) {
		Weibo weibo = new Weibo();
		if(isOauth) {
			weibo.setToken(args[0], args[1]);
			
		}else {
    		weibo.setUserId(args[0]);
    		weibo.setPassword(args[1]);
		}
		return weibo;
	}

}