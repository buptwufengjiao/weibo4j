package weibo4j;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

public class Relation  implements Serializable {
	private long id;
	private String user1Id;
	private String user2Id;
	private String user1Name;
	private String user2Name;
	private int commentsNum;
	private int rtNum;
	private int rtAndCommentNum;
	
	public Relation(){
		
	}
	
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
	public String getUser2Id() {
		return user2Id;
	}
	public void setUser2Id(String user2Id) {
		this.user2Id = user2Id;
	}
	
	public String getUser1Name() {
		return user1Name;
	}
	public void setUser1Name(String user1Name) {
		this.user1Name = user1Name;
	}
	public String getUser2Name() {
		return user2Name;
	}
	public void setUser2Name(String user2Name) {
		this.user2Name = user2Name;
	}

	public int getCommentsNum() {
		return commentsNum;
	}

	public void setCommentsNum(int commentsNum) {
		this.commentsNum = commentsNum;
	}
	

	public int getRtNum() {
		return rtNum;
	}

	public void setRtNum(int rtNum) {
		this.rtNum = rtNum;
	}

	public int getRtAndCommentNum() {
		return rtAndCommentNum;
	}

	public void setRtAndCommentNum(int rtAndCommentNum) {
		this.rtAndCommentNum = rtAndCommentNum;
	}

	/**
	 * @see java.lang.Object#equals(Object)
	 */
	public boolean equals(Object object) {
		if (!(object instanceof Relation)) {
			return false;
		}
		Relation rhs = (Relation) object;
		return new EqualsBuilder().appendSuper(super.equals(object)).append(
				this.user1Id, rhs.user1Id).append(this.rtAndCommentNum,
				rhs.rtAndCommentNum).append(this.rtNum, rhs.rtNum).append(
				this.user2Name, rhs.user2Name).append(this.commentsNum,
				rhs.commentsNum).append(this.user2Id, rhs.user2Id).append(
				this.user1Name, rhs.user1Name).append(this.id, rhs.id)
				.isEquals();
	}

	/**
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode() {
		return new HashCodeBuilder(-895084195, -1040498195).appendSuper(
				super.hashCode()).append(this.user1Id).append(
				this.rtAndCommentNum).append(this.rtNum).append(this.user2Name)
				.append(this.commentsNum).append(this.user2Id).append(
						this.user1Name).append(this.id).toHashCode();
	}

	
}
