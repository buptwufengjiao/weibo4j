package weibo4j.examples;

import weibo4j.DirectMessage;
import weibo4j.Weibo;
import weibo4j.WeiboException;
import java.util.List;
/**
 * Example application that gets recent direct messages from specified account.<br>
 * Usage: java Weibo4j.examples.GetDirectMessages ID Password
 * @author wanzi
 */
public class GetDirectMessages {
	/**
     * Usage: java Weibo4j.examples.GetDirectMessages ID Password
     * @param args String[]
     */
    public static void main(String[] args) {
    	
    	try {
        	System.setProperty("weibo4j.oauth.consumerKey", Weibo.CONSUMER_KEY);
        	System.setProperty("weibo4j.oauth.consumerSecret", Weibo.CONSUMER_SECRET);
        	
            Weibo weibo = new Weibo();
            
            weibo.setToken(args[0], args[1]);
            try {
                List<DirectMessage> messages = weibo.getDirectMessages();
                for (DirectMessage message : messages) {
                    System.out.println("Sender:" + message.getSenderScreenName());
                    System.out.println("Text:" + message.getText() + "\n");
                }
                System.exit(0);
            } catch (WeiboException te) {
                System.out.println("Failed to get messages: " + te.getMessage());
                System.exit( -1);
            }
            }
            
            
            
            catch (Exception ioe) {
                System.out.println("Failed to read the system input.");
                System.exit( -1);//try1
            }
       	 }//main
    
	
}
