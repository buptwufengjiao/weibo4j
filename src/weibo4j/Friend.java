package weibo4j;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.w3c.dom.Element;

import weibo4j.http.Response;
import weibo4j.org.json.JSONException;
import weibo4j.org.json.JSONObject;

public class Friend extends WeiboResponse implements java.io.Serializable{
	private long id;
	private String user1Id;
	private String user1Name;
	private String user2Id;
	private String user2Name;
	
    private static final long serialVersionUID = 1608000492860584608L;
	
	
	public Friend() {
		super();
	}
	
    /*package*/Friend(Response res, Weibo weibo) throws WeiboException {
        super(res);
        Element elem = res.asDocument().getDocumentElement();
        init(res, elem, weibo);
    }

    /*modify by sycheng add json define */
    /*package*/Friend(Response res) throws WeiboException {
        super(res);
        JSONObject json =res.asJSONObject();
        try {
			id = json.getLong("id");
			user1Id = json.getString(user1Id);
			user1Name=json.getString(user1Name);
			user2Id = json.getString(user2Id);
			user2Name=json.getString(user2Name);
		} catch (JSONException je) {
			throw new WeiboException(je.getMessage() + ":" + json.toString(), je);
		}
    }

    /* modify by sina add some field*/
    public Friend(JSONObject json)throws WeiboException, JSONException{
    	id = json.getLong("id");
		user1Id = json.getString(user1Id);
		user1Name=json.getString(user1Name);
		user2Id = json.getString(user2Id);
		user2Name=json.getString(user2Name);
    }

    /*package*/Friend(Response res, Element elem, Weibo weibo) throws
            WeiboException {
        super(res);
        init(res, elem, weibo);
    }

    public Friend(String str) throws WeiboException, JSONException {
        // StatusStream uses this constructor
        super();
        JSONObject json = new JSONObject(str);
        id = json.getLong("id");
		user1Id = json.getString(user1Id);
		user1Name=json.getString(user1Name);
		user2Id = json.getString(user2Id);
		user2Name=json.getString(user2Name);
    }
	
    private void init(Response res, Element elem, Weibo weibo) throws
    WeiboException {
	ensureRootNodeNameIs("friend", elem);
	id = getChildLong("id", elem);
	user1Id = getChildText("user1Id", elem);
	user1Name = getChildText("user1Name", elem);
	user2Id = getChildText("user2Id", elem);
	user2Name = getChildText("user2Name", elem);
}
	
    /**
     * Returns the id of the status
     *
     * @return the id
     */
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getUser1Id() {
		return user1Id;
	}
	public void setUser1Id(String user1Id) {
		this.user1Id = user1Id;
	}
	public String getUser1Name() {
		return user1Name;
	}
	public void setUser1Name(String user1Name) {
		this.user1Name = user1Name;
	}
	public String getUser2Id() {
		return user2Id;
	}
	public void setUser2Id(String user2Id) {
		this.user2Id = user2Id;
	}
	public String getUser2Name() {
		return user2Name;
	}
	public void setUser2Name(String user2Name) {
		this.user2Name = user2Name;
	}
	
	
	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return new ToStringBuilder(this).append("id", this.id).append(
				"user2Id", this.user2Id).append("user2Name", this.user2Name)
				.append("user1Id", this.user1Id).append("user1Name",
						this.user1Name).toString();
	}
	
	
	/**
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode() {
		return new HashCodeBuilder(-1458278145, 1172259603).appendSuper(
				super.hashCode()).append(this.user1Id).append(this.user2Name)
				.append(this.user2Id).append(this.user1Name).append(this.id)
				.toHashCode();
	}
	/**
	 * @see java.lang.Object#equals(Object)
	 */
	public boolean equals(Object object) {
		if (!(object instanceof Friend)) {
			return false;
		}
		Friend rhs = (Friend) object;
		return new EqualsBuilder().appendSuper(super.equals(object)).append(
				this.user1Id, rhs.user1Id)
				.append(this.user2Name, rhs.user2Name).append(this.user2Id,
						rhs.user2Id).append(this.user1Name, rhs.user1Name)
				.append(this.id, rhs.id).isEquals();
	}
	
	

}
