package weibo4j.examples;

import weibo4j.DirectMessage;
import weibo4j.Weibo;
import weibo4j.WeiboException;

public class SendDirectMessage {
   /**
    * Usage: java Weibo4j.examples.DirectMessage senderID senderPassword message recipientId
    * @param args String[]
    */
   public static void main(String[] args) {
   	System.setProperty("Weibo4j.oauth.consumerKey", Weibo.CONSUMER_KEY);
   	System.setProperty("Weibo4j.oauth.consumerSecret", Weibo.CONSUMER_SECRET);
   	
       if (args.length < 4) {
           System.out.println("No WeiboID/Password specified.");
           System.out.println("Usage: java Weibo4j.examples.DirectMessage senderID senderPassword  recipientId message");
           System.exit( -1);
       }
       Weibo weibo = new Weibo(args[0], args[1]);
       try {
          DirectMessage message = weibo.sendDirectMessage(args[2], args[3]);
           System.out.println("Direct message successfully sent to " +
                              message.getRecipientScreenName());
           System.exit(0);
       } catch (WeiboException te) {
           System.out.println("Failed to send message: " + te.getMessage());
           System.exit( -1);
       }
   }
}
