package com.jungstudy;

import java.util.Date;
import java.util.Vector;
import org.apache.commons.collections15.Factory;

import com.jungstudy.RelationLink;

/**
 *结点类的定义
 * 
 * @author Qingfeng
 * 
 */

public class UserVertex {
	static int sid = -1; //全局变量，用来控制自增长的id号
	int id;  //自增长的id号
	String userId;	//
	String username;  //
	Vector<String> keywords;// keywords(tags)
	Vector<RelationLink> links;// pointer to other nodes
	Date date;
	public boolean isSchedule = false;
	int importance = 0;
	
	public UserVertex(String userId,String username){
		this.id = ++sid;
		this.userId=userId;
		this.username = username;
		
	}

	public UserVertex(String userId,String name,Vector<String> keywords) {
		this.id = ++sid;
		this.userId=userId;
		this.username = name;
		if (keywords == null)
			this.keywords = keywords;
		else
			this.keywords = new Vector<String>();
		this.date = new Date();
		this.links = new Vector<RelationLink>();
		isSchedule = false;
		importance = 0;
	}

	public UserVertex(int i) {
		// TODO Auto-generated constructor stub
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void addLink(RelationLink link) {
		this.links.add(link);
	}
	public String toString() {
		return username + " - " + id;
	}
	
}

class UserVertexFactory implements Factory<UserVertex> {
	static UserVertexFactory instance = new UserVertexFactory();

	public UserVertex create() {
		return new UserVertex("", "", new Vector<String>());
	}

	public static UserVertexFactory getInstance() {
		return instance;
	}
}

