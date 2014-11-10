package weibo4j.examples.messages;

import java.util.List;
import weibo4j.DirectMessage;
import weibo4j.Weibo;
import weibo4j.WeiboException;

/**
 * Example application that gets recent direct messages from specified account.<br>
 * Usage: java Weibo4j.examples.GetDirectMessages ID Password
 * @author Yusuke Yamamoto - yusuke at mac.com
 */
public class GetSentDirect {
	
	 public static void main(String[] args) {
		    /**
		     * Usage: java Weibo4j.examples.GetDirectMessages ID Password
		     * @param args String[]
		     */
	try {
    	System.setProperty("weibo4j.oauth.consumerKey", Weibo.CONSUMER_KEY);
    	System.setProperty("weibo4j.oauth.consumerSecret", Weibo.CONSUMER_SECRET);
    	
        Weibo weibo = new Weibo();
        
        weibo.setToken(args[0], args[1]);
	
	
	
	try {
        List<DirectMessage> messages = weibo.getSentDirectMessages();
        for (DirectMessage message : messages) {
            System.out.println("Sender:" + message.getSenderScreenName());
            System.out.println("Text:" + message.getText());
            System.out.println("CreatedAt:" + message.getCreatedAt());
            System.out.println("Sender_id:" + message.getSenderId() + "\n");
        }
        System.exit(0);}
     catch (WeiboException te) {
        System.out.println("Failed to get messages: " + te.getMessage());
        System.exit( -1);
    }
	}
	 catch (Exception ioe) {
         System.out.println("Failed to read the system input.");
         System.exit( -1);//try1
	 }//main
}}