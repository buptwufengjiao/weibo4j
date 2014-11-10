package weibo4j;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import weibo4j.http.Response;
import weibo4j.org.json.JSONArray;
import weibo4j.org.json.JSONException;
import weibo4j.org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * A data class representing one single status of a user.
 * @author Yusuke Yamamoto - yusuke at mac.com
 */
public class Comment extends WeiboResponse implements java.io.Serializable {

    private Date createdAt;
    private long id;
    private String text;//评论内容
    private String source;
    private boolean isTruncated;
    private long inReplyToStatusId;
    private int inReplyToUserId;
    private boolean isFavorited;
    private String inReplyToScreenName;
    private double latitude = -1;
    private double longitude = -1;
    private String statusId;//被评论的微博id
    private String statusText;//被评论的微博内容
    private String user1Id;//被评论人id
    private String user1Name;
    private String user2Id;//评论人id
    private String user2Name;
    private boolean isReposted;

    private RetweetDetails retweetDetails;
    private static final long serialVersionUID = 1608000492860584608L;
    
    public Comment(){
    	
    }

    /*package*/Comment(Response res, Weibo weibo) throws WeiboException {
        super(res);
        Element elem = res.asDocument().getDocumentElement();
        init(res, elem, weibo);
    }

    /*modify by sycheng add json define */
    /*package*/Comment(Response res) throws WeiboException {
        super(res);
        JSONObject json =res.asJSONObject();
        try {
			id = json.getLong("id");
			text = json.getString("text");
			source = json.getString("source");
			createdAt = parseDate(json.getString("created_at"), "EEE MMM dd HH:mm:ss z yyyy");

			if(!json.isNull("user"))
				user = new User(json.getJSONObject("user"));
		} catch (JSONException je) {
			throw new WeiboException(je.getMessage() + ":" + json.toString(), je);
		}
    }

    /* modify by sina add some field*/
    public Comment(JSONObject json)throws WeiboException, JSONException{
    	id = json.getLong("id");
		text = json.getString("text");
		source = json.getString("source");
		createdAt = parseDate(json.getString("created_at"), "EEE MMM dd HH:mm:ss z yyyy");
		if(!json.isNull("user"))
			user = new User(json.getJSONObject("user"));
    }

    /*package*/Comment(Response res, Element elem, Weibo weibo) throws
            WeiboException {
        super(res);
        init(res, elem, weibo);
    }

    public Comment(String str) throws WeiboException, JSONException {
        // StatusStream uses this constructor
        super();
        JSONObject json = new JSONObject(str);
        id = json.getLong("id");
        text = json.getString("text");
        source = json.getString("source");
        createdAt = parseDate(json.getString("created_at"), "EEE MMM dd HH:mm:ss z yyyy");

        user = new User(json.getJSONObject("user"));
    }

    private void init(Response res, Element elem, Weibo weibo) throws
            WeiboException {
        ensureRootNodeNameIs("comment", elem);
        user = new User(res, (Element) elem.getElementsByTagName("user").item(0)
                , weibo);
        id = getChildLong("id", elem);
        text = getChildText("text", elem);
        source = getChildText("source", elem);
        createdAt = getChildDate("created_at", elem);
    }

    /**
     * Return the created_at
     *
     * @return created_at
     * @since Weibo4J 1.1.0
     */

    public Date getCreatedAt() {
        return this.createdAt;
    }

    /**
     * Returns the id of the status
     *
     * @return the id
     */
    public long getId() {
        return this.id;
    }

    /**
     * Returns the text of the status
     *
     * @return the text
     */
    public String getText() {
        return this.text;
    }

    /**
     * Returns the source
     *
     * @return the source
     * @since Weibo4J 1.0.4
     */
    public String getSource() {
        return this.source;
    }


    public String getStatusId() {
		return statusId;
	}

	public String getStatusText() {
		return statusText;
	}


	public String getUser1Id() {
		return user1Id;
	}

	public String getUser1Name() {
		return user1Name;
	}


	public String getUser2Id() {
		return user2Id;
	}

	public String getUser2Name() {
		return user2Name;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public void setId(long id) {
		this.id = id;
	}

	public void setText(String text) {
		this.text = text;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public void setStatusId(String statusId) {
		this.statusId = statusId;
	}

	public void setStatusText(String statusText) {
		this.statusText = statusText;
	}


	public void setUser1Id(String user1Id) {
		this.user1Id = user1Id;
	}

	public void setUser1Name(String user1Name) {
		this.user1Name = user1Name;
	}


	public void setUser2Id(String user2Id) {
		this.user2Id = user2Id;
	}

	public void setUser2Name(String user2Name) {
		this.user2Name = user2Name;
	}

	/**
     * Test if the status is truncated
     *
     * @return true if truncated
     * @since Weibo4J 1.0.4
     */
    public boolean isTruncated() {
        return isTruncated;
    }

    /**
     * Returns the in_reply_tostatus_id
     *
     * @return the in_reply_tostatus_id
     * @since Weibo4J 1.0.4
     */
    public long getInReplyToStatusId() {
        return inReplyToStatusId;
    }

    /**
     * Returns the in_reply_user_id
     *
     * @return the in_reply_tostatus_id
     * @since Weibo4J 1.0.4
     */
    public int getInReplyToUserId() {
        return inReplyToUserId;
    }

    /**
     * Returns the in_reply_to_screen_name
     *
     * @return the in_in_reply_to_screen_name
     * @since Weibo4J 2.0.4
     */
    public String getInReplyToScreenName() {
        return inReplyToScreenName;
    }

    /**
     * returns The location's latitude that this tweet refers to.
     *
     * @since Weibo4J 2.0.10
     */
    public double getLatitude() {
        return latitude;
    }

    /**
     * returns The location's longitude that this tweet refers to.
     *
     * @since Weibo4J 2.0.10
     */
    public double getLongitude() {
        return longitude;
    }

    /**
     * Test if the status is favorited
     *
     * @return true if favorited
     * @since Weibo4J 1.0.4
     */
    public boolean isFavorited() {
        return isFavorited;
    }


    private User user = null;

    /**
     * Return the user
     *
     * @return the user
     */
    public User getUser() {
        return user;
    }

    /**
     *
     * @since Weibo4J 2.0.10
     */
    public boolean isRetweet(){
        return null != retweetDetails;
    }

    /**
     *
     * @since Weibo4J 2.0.10
     */
    public RetweetDetails getRetweetDetails() {
        return retweetDetails;
    }



	/*package*/
    static List<Comment> constructComments(Response res,Weibo weibo) throws WeiboException {
        Document doc = res.asDocument();
        if (isRootNodeNilClasses(doc)) {
            return new ArrayList<Comment>(0);
        } else {
            try {
                ensureRootNodeNameIs("comments", doc);
                NodeList list = doc.getDocumentElement().getElementsByTagName(
                        "comment");
                int size = list.getLength();
                List<Comment> statuses = new ArrayList<Comment>(size);
                for (int i = 0; i < size; i++) {
                    Element status = (Element) list.item(i);
                    statuses.add(new Comment(res, status, weibo));
                }
                return statuses;
            } catch (WeiboException te) {
                ensureRootNodeNameIs("nil-classes", doc);
                return new ArrayList<Comment>(0);
            }
        }
    }

    static List<Comment> constructComments(Response res) throws WeiboException {
   	 try {
            JSONArray list = res.asJSONArray();
            int size = list.length();
            List<Comment> comments = new ArrayList<Comment>(size);
            for (int i = 0; i < size; i++) {
            	comments.add(new Comment(list.getJSONObject(i)));
            }
            return comments;
        } catch (JSONException jsone) {
            throw new WeiboException(jsone);
        } catch (WeiboException te) {
            throw te;
        }

   }

    @Override
    public int hashCode() {
        return (int) id;
    }

    @Override
    public boolean equals(Object obj) {
        if (null == obj) {
            return false;
        }
        if (this == obj) {
            return true;
        }
        return obj instanceof Comment && ((Comment) obj).id == this.id;
    }

    @Override
    public String toString() {
        return "Comment{" +
                "createdAt=" + createdAt +
                ", id=" + id +
                ", text='" + text + '\'' +
                ", source='" + source + '\'' +
                ", isTruncated=" + isTruncated +
                ", inReplyToStatusId=" + inReplyToStatusId +
                ", inReplyToUserId=" + inReplyToUserId +
                ", isFavorited=" + isFavorited +
                ", inReplyToScreenName='" + inReplyToScreenName + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", retweetDetails=" + retweetDetails +
                ", user=" + user +
                '}';
    }

	public void setUser(User user) {
		this.user = user;
	}

	public boolean isReposted() {
		return isReposted;
	}

	public void setReposted(boolean isReposted) {
		this.isReposted = isReposted;
	}


}
