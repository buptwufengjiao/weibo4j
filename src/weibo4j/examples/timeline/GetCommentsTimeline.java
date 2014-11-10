package weibo4j.examples.timeline;

import java.util.List;

import weibo4j.Comment;
import weibo4j.Weibo;

 public class GetCommentsTimeline {
 
 	/**
 	 * 获取当前用户发出和收到的评论列表
 	 * @param args
 	 */
 	public static void main(String[] args) {
 	    System.setProperty("weibo4j.oauth.consumerKey", Weibo.CONSUMER_KEY);
     	    System.setProperty("weibo4j.oauth.consumerSecret", Weibo.CONSUMER_SECRET);
            try {
         	Weibo weibo = getWeibo(true,args);
         	List<Comment> comments = weibo.getCommentsTimeline();
		for (Comment comment: comments){
		    System.out.println(comment.getId()+":"+comment.getText()+" by "+comment.getUser().getScreenName());
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
