package weibo4j;

import java.util.Date;
import org.apache.commons.lang.builder.ToStringBuilder;

public class Repost extends WeiboResponse implements java.io.Serializable {
	private long id;
    private String user1Id;//被转发人id
    private String user1Name;
    private String user2Id;//转发人id
    private String user2Name;
    private Date repost_time;
    private String commentsText;
    private String statusText;
    private String statusId;
    private boolean isCommented;
    
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
	public Date getRepost_time() {
		return repost_time;
	}
	public void setRepost_time(Date repost_time) {
		this.repost_time = repost_time;
	}
	public String getCommentsText() {
		return commentsText;
	}
	public void setCommentsText(String commentsText) {
		this.commentsText = commentsText;
	}
	public String getStatusText() {
		return statusText;
	}
	public void setStatusText(String statusText) {
		this.statusText = statusText;
	}
	public String getStatusId() {
		return statusId;
	}
	public void setStatusId(String statusId) {
		this.statusId = statusId;
	}
	
	public boolean isCommented() {
		return isCommented;
	}
	public void setCommented(boolean isCommented) {
		this.isCommented = isCommented;
	}
	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return new ToStringBuilder(this).append("id", this.id).append(
				"statusId", this.statusId).append("rateLimitLimit",
				this.getRateLimitLimit()).append("commentsText",
				this.commentsText).append("rateLimitReset",
				this.getRateLimitReset()).append("user2Name", this.user2Name)
				.append("rateLimitRemaining", this.getRateLimitRemaining())
				.append("user2Id", this.user2Id).append("repost_time",
						this.repost_time).append("statusText", this.statusText)
				.append("user1Id", this.user1Id).append("user1Name",
						this.user1Name).toString();
	}
    
    
    

}
